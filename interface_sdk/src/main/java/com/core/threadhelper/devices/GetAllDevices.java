package com.core.threadhelper.devices;

import android.content.Context;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private Context mContext;
    private boolean isNewDevice = false;

    public GetAllDevices(Context context, boolean isNewDevice) {
        this.mContext = context;
        this.isNewDevice = isNewDevice;
    }

    @Override
    public void run() {
        try {
            if (!isNewDevice) {
                bt_send = DeviceCmdData.GetAllDeviceListCmd();
            } else {
                bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
            }

            if (!NetworkUtil.NetWorkType(mContext)) {
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("设备信息byte = " + Arrays.toString(recbuf));

                    String str = new String(packet.getData()).trim();
                    /**
                     * 接受传感器数据并解析
                     */
                    ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
                    sensorData.parseBytes(recbuf);
                    if (sensorData.mZigbeeType.contains("8401")) {
                        System.out.println("传感器返回数据stateSDK = " + sensorData.mDevMac);
                        String time = String.valueOf(System.currentTimeMillis());
                        DataSources.getInstance().getReceiveSensor(sensorData.mDevMac, sensorData.state, Utils.getFormatTellDate(time));
                    }

                    /**
                     * 解析设备属性数据
                     */
                    ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                    attribute.parseBytes(recbuf);
                    if (attribute.zigbee_type.contains("8100")) {
                        System.out.println("返回设备属性Value" + attribute.attribValue);
                        //如果设备属性簇ID等于5则发送保存zonetypecmd
                        if (attribute.clusterID == 5) {
                            byte[] saveZoneType = DeviceCmdData.SaveZoneTypeCmd(attribute.zigbee_type,
                                    attribute.shortaddr, attribute.endpoint + "",(short)attribute.attribValue);
                            Constants.sendMessage(saveZoneType);
                        }
                    }

                    if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbuf[11]) {
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, false);
                    } else if ((int) MessageType.A.UPLOAD_TXT.value() == recbuf[11]) {//新入网设备
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


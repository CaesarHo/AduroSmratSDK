package com.core.threadhelper.devices;

import android.content.Context;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.AES;
import com.core.utils.AESUtils;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
//                bt_send = AESUtils.encode(bt);
            } else {
                bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
//                bt_send = AESUtils.encode(bt);
            }

            if (Constants.isRemote) {//!NetworkUtil.NetWorkType(mContext)
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
                    socket.setBroadcast(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据GetAllDevices = " + Arrays.toString(recbuf));

                    /**
                     * 设备返回的状态
                     */
                    ParseDeviceData.ParseDeviceStateOrLevel pDevStateOrLevel = new ParseDeviceData.ParseDeviceStateOrLevel();
                    pDevStateOrLevel.parseBytes(recbuf);
                    if (pDevStateOrLevel.message_type.contains("8101") & pDevStateOrLevel.clusterID == 6){
                        DataSources.getInstance().getDeviceState(pDevStateOrLevel.short_address, pDevStateOrLevel.state);
                    }

                    /**
                     * 接受传感器数据并解析
                     */
                    ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
                    sensorData.parseBytes(recbuf);
                    if (sensorData.message_type.contains("8401")) {
                        //当有传感器数据上传时读取ZONETYPE
                        byte[] bt = DeviceCmdData.ReadZoneTypeCmd(sensorData.sensor_mac,sensorData.shortaddr_str);
                        Constants.sendMessage(bt);
                        DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
                    }

                    /**
                     * 解析设备属性数据
                     */
                    ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                    attribute.parseBytes(recbuf);

                    /**
                     * 传感器电量返回值
                     */
                    if (attribute.message_type.contains("8102")){
                        DataSources.getInstance().responseBatteryValue(attribute.device_mac,attribute.attribValue);
                    }

                    if (attribute.message_type.contains("8100")) {
                        //如果设备属性簇ID等于5则发送保存zonetypecmd
                        if (attribute.clusterID == 5) {
                            byte[] zt_bt = DeviceCmdData.SaveZoneTypeCmd(
                                    attribute.message_type,
                                    attribute.short_address,
                                    attribute.endpoint + "",
                                    (short)attribute.attribValue);
                            Constants.sendMessage(zt_bt);
                        }
                    }

                    if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbuf[11]) {
//                        byte[] bt = AESUtils.decode(recbuf);
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, false);
                    } else if ((int) MessageType.A.UPLOAD_TXT.value() == recbuf[11]) {//新入网设备
//                        byte[] bt = AESUtils.decode(recbuf);
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, true);
                    }
                    recbuf = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


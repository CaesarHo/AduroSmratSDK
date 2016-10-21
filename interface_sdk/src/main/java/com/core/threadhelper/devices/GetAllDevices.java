package com.core.threadhelper.devices;

import android.content.Context;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
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

    public GetAllDevices(Context context){
        this.mContext = context;
    }
    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                byte[] bt_send = DeviceCmdData.GetAllDeviceListCmd();
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }
                //发送获取设备命令

                bt_send = DeviceCmdData.GetAllDeviceListCmd();
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("SendGetDeviceCmd十六进制 = " + Utils.binary(bt_send, 16));

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
                        System.out.println("传感器返回数据stateSDK = " + sensorData.state);
                        String time = String.valueOf(System.currentTimeMillis());
                        DataSources.getInstance().getReceiveSensor(sensorData.mDevMac, sensorData.state, Utils.getFormatTellDate(time));
                    }

                    /**
                     * 解析设备属性数据
                     */
                    ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                    attribute.parseBytes(recbuf);
                    if (attribute.mZigbeeType.contains("8100")) {
                        System.out.println("返回设备属性Value" + attribute.attribValue);
                        //如果设备属性簇ID等于5则发送保存zonetypecmd
                        if (attribute.clusterID == 5) {
                            SetDeviceAttribute.SendSaveZoneTypeCmd(attribute.mDevMac,
                                    attribute.shortaddr_str, attribute.srcEndpoint + "",
                                    (short) attribute.attribValue);
                        }
                    }

                    //如果str长度大于46则解析设备信息
                    if (str.length() > 46) {
                        System.out.println("str.length = " + str.length());
                        ParseDeviceData.ParseGetDeviceInfo(str, false);
                    }
//                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


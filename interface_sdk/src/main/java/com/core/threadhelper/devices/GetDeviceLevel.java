package com.core.threadhelper.devices;

import android.content.Context;
import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/13.
 */
public class GetDeviceLevel implements Runnable {
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private byte[] bt_send = null;
    private Context mContext;

    public GetDeviceLevel(Context context, AppDevice appDevice) {
        this.appDevice = appDevice;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0008");
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("当前为远程通讯 = " + "getDeviceLevel");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                // 接收数据
                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    System.out.println("当前接收的数据GetDeviceLevel = " + Arrays.toString(recbuf));
                    if (recbuf[11] == MessageType.A.UPLOAD_DEVICE_INFO.value()) {
                        ParseDeviceData.ParseAttributeData parseAttributeData = new ParseDeviceData.ParseAttributeData();
                        parseAttributeData.parseBytes(recbuf);
                        if (parseAttributeData.message_type.equals("8100")) {
                            if (parseAttributeData.clusterID == 8){
                                DataSources.getInstance().getDeviceLevel(parseAttributeData.device_mac, parseAttributeData.attribValue);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

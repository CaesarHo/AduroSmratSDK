package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/12.
 */
public class GetDeviceSwitchState implements Runnable {
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private byte[] bt_send = null;
    private Context mContext;

    public GetDeviceSwitchState(Context context, AppDevice appDevice) {
        this.mContext = context;
        this.appDevice = appDevice;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("当前为远程通讯 = " + "getDeviceSwitchState");
                byte[] bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0006");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                Log.i("网关IP地址 = ", Constants.GW_IP_ADDRESS);
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0006");
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("SendReadAttrbuteCmd十六进制 = " + Utils.binary(bt_send, 16));

                // 接收数据
                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);

                    String str = new String(packet.getData()).trim();
                    if (str.contains("GW") && !str.contains("K64")) {
                        /**
                         * 解析设备属性数据
                         */
                        ParseDeviceData.ParseAttributeData parseAttributeData = new ParseDeviceData.ParseAttributeData();
                        parseAttributeData.parseBytes(recbuf);

                        if (parseAttributeData.zigbee_type.contains("8100") & parseAttributeData.clusterID == 6) {
                            DataSources.getInstance().getDeviceState(parseAttributeData.dev_mac, parseAttributeData.attribValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

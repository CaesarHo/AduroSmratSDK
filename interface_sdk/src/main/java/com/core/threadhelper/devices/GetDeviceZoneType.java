package com.core.threadhelper.devices;

import android.util.Log;
import android.view.WindowManager;

import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/9/25.
 */

public class GetDeviceZoneType implements Runnable{

    private DatagramSocket socket = null;
    private byte[] bt_send = null;
    private String device_mac,shortaddr,main_point;

    public GetDeviceZoneType(String device_mac,String shortaddr,String main_point) {
        this.device_mac = device_mac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
        this.device_mac = device_mac;
    }

    @Override
    public void run() {
        try {
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
//            bt_send = DeviceCmdData.ReadAttrbuteCmd(device_mac, shortaddr, main_point, "0500","0006");
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress,Constants.UDP_PORT);
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
                    if (parseAttributeData.zigbee_type.contains("8100")) {
                        System.out.println("ParseAttributeDatamDevMac" + parseAttributeData.dev_mac);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

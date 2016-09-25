package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.db.AppDeviceInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.ParseData;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/9/25.
 */

public class GetDeviceZoneType implements Runnable{

    private int PORT = 8888;
    private DatagramSocket socket = null;
    private byte[] bt_send = null;
    private String device_mac;
    private String shortaddr;
    private String main_point;

    public GetDeviceZoneType(String device_mac,String shortaddr,String main_point) {
        this.device_mac = device_mac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
        this.device_mac = device_mac;
    }

    @Override
    public void run() {
        try {
            if (UDPHelper.localip == null && Constants.ipaddress == null) {
                DataSources.getInstance().SendExceptionResult(0);
                return;
            }

            Log.i("网关IP地址 = ", Constants.ipaddress);
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }
            bt_send = DeviceCmdData.ReadAttrbuteCmd(device_mac, shortaddr, main_point, "0500","0006");
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("SendReadAttrbuteCmd十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

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
                    ParseData.ParseAttributeData parseAttributeData = new ParseData.ParseAttributeData();
                    parseAttributeData.parseBytes(recbuf);
                    if (parseAttributeData.mZigbeeType.contains("8100")) {
                        System.out.println("ParseAttributeDatamDevMac" + parseAttributeData.mDevMac);
                        System.out.println("ParseAttributeDataclusterID" + parseAttributeData.clusterID);
                        System.out.println("ParseAttributeDatastate" + parseAttributeData.state);
                        System.out.println("ParseAttributeDataattributeID" + parseAttributeData.attributeID);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

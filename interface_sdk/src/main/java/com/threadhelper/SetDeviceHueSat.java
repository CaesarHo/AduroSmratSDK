package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.utils.NewCmdData;
import com.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/8/3.
 */
public class SetDeviceHueSat implements Runnable {
    private static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String  devicemac;
    private String shortaddr;
    private String main_point;
    private DatagramSocket m_CMDSocket = null;
    private int hue = -1;
    private int sat = -1;

    public SetDeviceHueSat(String devicemac , String shortaddr , String main_point, int hue, int sat){
        this.devicemac = devicemac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
        this.hue = hue;
        this.sat = sat;
    }

    @Override
    public void run() {
        try {
            if (m_CMDSocket == null) {
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.bind(new InetSocketAddress(PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);

            byte[]  bt_send = NewCmdData.setDeviceHueSatCmd(devicemac,shortaddr,main_point,hue,sat);
            System.out.println("十六进制SAT = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, PORT);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
            Log.i("receive_huesat = " ,packet_receive.getData().toString().trim());
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

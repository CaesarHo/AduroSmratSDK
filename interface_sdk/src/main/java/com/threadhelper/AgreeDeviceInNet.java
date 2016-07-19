package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;
import com.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/18.
 */
public class AgreeDeviceInNet implements  Runnable{
    private String ipaddress;
    private DatagramSocket m_CMDSocket = null;
    private int port = 8888;

    public AgreeDeviceInNet(String ipaddress){
        this.ipaddress = ipaddress;
    }

    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(ipaddress);

            byte[] bt_send = new byte[16];
            bt_send[0] = 0x41;
            bt_send[1] = 0x50;
            bt_send[2] = 0x50;
            bt_send[3] = (byte)0xC0;
            bt_send[4] = (byte)0xA8;
            bt_send[5] = 0x01;
            bt_send[6] = 0x67;
            bt_send[7] = 0x01;
            bt_send[8] = 0x01;
            bt_send[9] = (byte)0xB9;
            //消息体
            bt_send[10] = 0x01;
            bt_send[11] = 0x00;
            bt_send[12] = 0x00;
            bt_send[13] = 0x00;
            bt_send[14] = 0x00;
            bt_send[15] = 0x62;

            Utils.hexStringToByteArray(Utils.binary(bt_send,16));
            System.out.println("十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));
            System.out.println("十进制DATA =" + Arrays.toString(bt_send));
            DatagramPacket packet_send = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),Utils.hexStringToByteArray(Utils.binary(bt_send,16)).length,serverAddr, port);
            m_CMDSocket.send(packet_send);
            Log.e("UDP_SEND = ", "-----------------------------------------");

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
            System.out.println("out_bt_send =" + Arrays.toString(buf));

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().AgreeDeviceInNet(1);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

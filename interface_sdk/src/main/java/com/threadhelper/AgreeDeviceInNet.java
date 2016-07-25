package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;
import com.utils.NewCmdData;
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
            m_CMDSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(ipaddress);
            byte[] bt_send = NewCmdData.Allow_DevicesAccesstoBytes();
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

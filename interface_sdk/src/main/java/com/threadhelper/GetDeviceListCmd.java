package com.threadhelper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.GatewayInfo;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/20.
 */
public class GetDeviceListCmd implements Runnable{
    private Context context;
    private String ipaddress;
    private DatagramSocket m_CMDSocket = null;
    public Boolean IsThreadDisable = false;//指示监听线程是否终止
    private static WifiManager.MulticastLock lock;
    private int port = 8888;
    public String ip;
    WifiManager wifiManager;
    public GetDeviceListCmd(Context context,WifiManager manager){
        this.context = context;
        this.ipaddress = GatewayInfo.getInstance().getInetAddress(context);
        this.lock = manager.createMulticastLock("localWifi");
        this.wifiManager = manager;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        ip = Utils.intToIp(ipAddress);
        Utils.SplitToIp(ip);
    }

    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket(null);
            InetAddress serverAddr = InetAddress.getByName(ipaddress);

            byte[] bt_send = new byte[34];
            bt_send[0] = 0x41;
            bt_send[1] = 0x50;
            bt_send[2] = 0x50;
            bt_send[3] = (byte)Constants.IpAddress.int_1;
            bt_send[4] = (byte)Constants.IpAddress.int_2;
            bt_send[5] = (byte)Constants.IpAddress.int_3;
            bt_send[6] = (byte)Constants.IpAddress.int_4;
            bt_send[7] = 0x01;
            bt_send[8] = 0x01;
            bt_send[9] = (byte)0xD2;
            //消息体
            bt_send[10] = 0x01;
            bt_send[11] = 0x00;
            bt_send[12] = 0x0B;
            bt_send[13] = 0x00;
            bt_send[14] = 0x12;
//            bt_send[15] = (byte)0xF0;
            //数据体-----头
            bt_send[15] = 0x41;
            bt_send[16] = 0x5F;
            bt_send[17] = 0x5A;
            bt_send[18] = 0x49;
            bt_send[19] = 0x47;
            //数据体序号
            bt_send[20] = 0x01;
            bt_send[21] = (byte)0xFF;
            bt_send[22] = (byte)0xFF;
            bt_send[23] = (byte)0x00;
            bt_send[24] = (byte)0x12;
            bt_send[25] = (byte)0x4b;
            bt_send[26] = (byte)0x00;
            bt_send[27] = (byte)0x07;
            bt_send[28] = (byte)0x6a;
            bt_send[29] = (byte)0xfe;
            bt_send[30] = (byte)0x09;
            bt_send[31] = (byte)0x00;
            bt_send[32] = (byte)0x00;
            bt_send[33] = (byte)0xA9;
//CRC8.calcCrc8(FtFormatTransfer.byteMerger1(boby_type1,boby_type2))
            Utils.hexStringToByteArray(Utils.binary(bt_send,16));
            System.out.println("十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));
            System.out.println("十进制DATA =" + Arrays.toString(bt_send));
            DatagramPacket packet_send = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),Utils.hexStringToByteArray(Utils.binary(bt_send,16)).length,serverAddr, port);

            m_CMDSocket.send(packet_send);
            Log.e("UDP_SEND = ", "-----------------------------------------");

            byte[] message = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
            try {
                while (!IsThreadDisable) {
                    // 准备接收数据
                    Log.d("UDP = ", "接受数据:");
                    lock.acquire();
                    m_CMDSocket.receive(datagramPacket);
                    String strMsg = new String(datagramPacket.getData()).trim();
                    String ipstr = datagramPacket.getAddress().getHostAddress().toString();
                    int port_int = datagramPacket.getPort();
                    System.out.println("data6 =" + Arrays.toString(message));

                    Log.e("UDPHelper2", "UDPHelper2=" + strMsg);
                    Log.d("UDP Demo", datagramPacket.getAddress().getHostAddress().toString() + ":" + strMsg);

                    if (lock.isHeld()){
                        lock.release();
                    }
                }
            } catch (IOException e) {//IOException
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

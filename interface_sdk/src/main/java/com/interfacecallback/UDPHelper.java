package com.interfacecallback;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by best on 2016/6/27.
 */
public class UDPHelper implements Runnable {
    Context context;
    public Boolean IsThreadDisable = false;//指示监听线程是否终止
    private static WifiManager.MulticastLock lock;
    InetAddress mInetAddress;
    // UDP服务器监听的端口
    int port = 8888;
    public static String ip;

    public UDPHelper(Context context, WifiManager manager) {
        this.context = context;
        this.lock = manager.createMulticastLock("localWifi");
        //获取本地IP地址
        WifiInfo wifiInfo = manager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        ip = Utils.intToIp(ipAddress);
    }

    @Override
    public void run() {
        StartListen();
    }

    public void StartListen() {
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] message = new byte[1024];
        int num = 0;
        try {
            // 建立Socket连接
            DatagramSocket datagramSocket = new DatagramSocket(port);
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
            try {
                while (!IsThreadDisable && num < 10) {
                    num++;
                    // 准备接收数据
                    Log.d("UDP = ", "接受数据:");
                    lock.acquire();
                    datagramSocket.receive(datagramPacket);
                    String strMsg = new String(datagramPacket.getData()).trim();
                    String ipstr = datagramPacket.getAddress().getHostAddress().toString();
                    int port_int = datagramPacket.getPort();
                    long time = System.currentTimeMillis();
   
                    DataSources.getInstance().GatewayInfo("GatewatName",strMsg,"SoftwareVersion",
                            "HarddwareVersion",ipstr,Utils.ConvertTimeByLong(time));

                    GatewayInfo.getInstance().setInetAddress(context,ipstr);
                    GatewayInfo.getInstance().setPort(context,port_int);
                    GatewayInfo.getInstance().setGatewayNo(context,strMsg);

                    Log.d("UDP = ", datagramPacket.getAddress().getHostAddress().toString() + ":" + strMsg);
                    if (lock.isHeld()){
                        lock.release();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}

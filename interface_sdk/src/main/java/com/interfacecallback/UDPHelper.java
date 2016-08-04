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
import java.net.InetSocketAddress;
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
    DatagramSocket socket = null;
    public static String localip;

    public UDPHelper(Context context, WifiManager manager) {
        this.context = context;
        this.lock = manager.createMulticastLock("localWifi");
        //获取本地IP地址
        WifiInfo wifiInfo = manager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        localip = Utils.intToIp(ipAddress);
        Utils.SplitToIp(localip);
    }

    @Override
    public void run() {
        if (localip == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        StartListen();
    }

    public void StartListen() {
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] message = new byte[1024];
        int num = 0;
        try {
            // 建立Socket连接
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(port));
            }
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
            try {
                while (!IsThreadDisable && num < 10) {
                    num++;
                    // 准备接收数据
                    Log.d("UDP = ", "接受数据:");
                    lock.acquire();
                    socket.receive(datagramPacket);
                    String strMsg = new String(datagramPacket.getData()).trim();
                    String ipstr = datagramPacket.getAddress().getHostAddress().toString();
                    int port_int = datagramPacket.getPort();
                    long time = System.currentTimeMillis();

                    Constants.ipaddress = ipstr;

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

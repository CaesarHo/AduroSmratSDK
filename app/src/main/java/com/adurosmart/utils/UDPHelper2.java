package com.adurosmart.utils;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.adurosmart.bean.GatewayInfo;
import com.adurosmart.global.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by best on 2016/6/27.
 */
public class UDPHelper2 implements Runnable {
    Context context;
    public Boolean IsThreadDisable = false;//指示监听线程是否终止
    private static WifiManager.MulticastLock lock;
    InetAddress mInetAddress;

    public UDPHelper2(Context context,WifiManager manager) {
        this.lock = manager.createMulticastLock("localWifi");
        this.context = context;
    }

    public void StartListen() {
        // UDP服务器监听的端口
        int port = 8888;
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] message = new byte[1024];
        int num = 0;
        try {
            // 建立Socket连接
            DatagramSocket datagramSocket = new DatagramSocket(port);
//            datagramSocket.setBroadcast(true);
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

                    GatewayInfo.getInstance().setPort(port_int);
                    GatewayInfo.getInstance().setInetAddress(datagramPacket.getAddress());
                    GatewayInfo.getInstance().setGatewayUniqueId(strMsg);

                    Intent intent = new Intent();
                    intent.setAction(Constants.Action.ACTION_LISTEN_UDP_DATA);
                    intent.putExtra("data",strMsg);
                    intent.putExtra("ipaddrss",ipstr);
                    intent.putExtra("port",port_int);
                    context.sendBroadcast(intent);

                    Log.e("UDPHelper2", "UDPHelper2=" + strMsg);
                    Log.d("UDP Demo", datagramPacket.getAddress().getHostAddress().toString() + ":" + strMsg);

                    if (lock.isHeld()){
                        lock.release();
                    }
                }
            } catch (IOException e) {//IOException
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        StartListen();
    }
}

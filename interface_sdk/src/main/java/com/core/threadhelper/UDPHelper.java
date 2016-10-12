package com.core.threadhelper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/6/27.
 */
public class UDPHelper implements Runnable {
    private Context context;
    private Boolean IsThreadDisable = false;//指示监听线程是否终止
    private WifiManager.MulticastLock lock;
    // UDP服务器监听的端口
    private DatagramSocket socket = null;

    public UDPHelper(Context context, WifiManager manager) {
        this.context = context;
        this.lock = manager.createMulticastLock("localWifi");
        //获取本地IP地址
        WifiInfo wifiInfo = manager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Constants.APP_IP_ADDRESS = Utils.intToIp(ipAddress);
        Utils.SplitToIp(Constants.APP_IP_ADDRESS);
    }

    @Override
    public void run() {
        if (Constants.APP_IP_ADDRESS == null) {
            DataSources.getInstance().SendExceptionResult(0);
            return;
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
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length);

            while (!IsThreadDisable && num < 10) {
                num++;
                // 准备接收数据
                lock.acquire();
                socket.receive(datagramPacket);
                String strMsg = new String(datagramPacket.getData()).trim();
                String ipstr = datagramPacket.getAddress().getHostAddress().toString();
                int port_int = datagramPacket.getPort();
                long time = System.currentTimeMillis();

                Constants.GW_IP_ADDRESS = ipstr;

                if (strMsg.contains("K64_SEARCH_GW")) {
                    DataSources.getInstance().GatewayInfo("GatewatName", strMsg,
                            "SoftwareVersion", "HarddwareVersion", ipstr, Utils.ConvertTimeByLong(time));

                    String[] gw_no_arr = strMsg.split(":");
                    String gw_no = gw_no_arr[1];
                    System.out.println("gw_no = " + gw_no);
                    System.out.println("strMsg = " + strMsg);
                    GatewayInfo.getInstance().setInetAddress(context, ipstr);
                    GatewayInfo.getInstance().setPort(context, port_int);
                    GatewayInfo.getInstance().setGatewayNo(context, gw_no);
                }

                if (lock.isHeld()) {
                    lock.release();
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

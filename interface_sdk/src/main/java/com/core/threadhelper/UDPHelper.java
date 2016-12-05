package com.core.threadhelper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;

import static com.core.global.Constants.isScanGwNodeVer;

/**
 * Created by best on 2016/6/27.
 */
public class UDPHelper implements Runnable {
    private Context context;
    private WifiManager.MulticastLock lock;
    private DatagramSocket socket = null;

    public UDPHelper(Context context, WifiManager wifiManager) {
        this.context = context;
        this.lock = wifiManager.createMulticastLock("localWifi");
        //获取本地IP地址
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Constants.APP_IP_ADDRESS = Utils.intToIp(ipAddress);
        Utils.SplitToIp(Constants.APP_IP_ADDRESS);
    }

    @Override
    public void run() {
        if (Constants.APP_IP_ADDRESS.equals("")) {
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

            while (num < 10) {
                num++;
                // 准备接收数据
                lock.acquire();
                socket.receive(datagramPacket);
                String strMsg = new String(datagramPacket.getData()).trim();
                String ip_str = datagramPacket.getAddress().getHostAddress().toString();
                int port_int = datagramPacket.getPort();
                System.out.println("接收的网关信息 = " + Arrays.toString(message));

                Constants.GW_IP_ADDRESS = ip_str;

                if (strMsg.contains("K64_SEARCH_GW")) {
                    String[] gw_no_arr = strMsg.split(":");
                    String gw_no = gw_no_arr[1];
                    System.out.println("gw_no = " + gw_no);
                    System.out.println("strMsg = " + strMsg);
                    GatewayInfo.getInstance().setInetAddress(context, ip_str);
                    GatewayInfo.getInstance().setPort(context, port_int);
                    GatewayInfo.getInstance().setGatewayNo(context, gw_no);

                    if (!isScanGwNodeVer){
                        Thread.sleep(2000);
                        SerialHandler.getInstance().GetGwInfo();
                        Thread.sleep(2000);
                        SerialHandler.getInstance().GetNodeVer();
                    }
                }

                if (lock.isHeld()) {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

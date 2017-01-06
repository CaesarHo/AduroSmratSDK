package com.core.threadhelper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GatewayCmdData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.utils.TransformUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import static com.core.global.Constants.isScanGwNodeVer;

/**
 * Created by best on 2016/6/27.
 */
public class UDPHelper implements Runnable {
    private static final String TAG = "UDPHelper";
    private Context context;
    private WifiManager.MulticastLock lock;
    private DatagramSocket socket = null;

    public UDPHelper(Context context, WifiManager wifiManager) {
        this.context = context;
        this.lock = wifiManager.createMulticastLock("localWifi");
        //获取本地IP地址
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Constants.APP_IP_ADDRESS = TransformUtils.intToIp(ipAddress);
        TransformUtils.SplitToIp(Constants.APP_IP_ADDRESS);
        isScanGwNodeVer = false;
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
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            while (num < 10) {
                num++;
                Thread.sleep(1000);
                // 准备接收数据
                lock.acquire();
                DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
                socket.receive(datagramPacket);
                String str_message = new String(datagramPacket.getData()).trim();
                String ip_address = datagramPacket.getAddress().getHostAddress().toString();
                int port_int = datagramPacket.getPort();

                Constants.GW_IP_ADDRESS = ip_address;

                DataPacket.getInstance().BytesDataPacket(context,message);
                if (str_message.contains("K64_SEARCH_GW") & num < 4) {
                    String[] gw_no_arr = str_message.split(":");
                    String gw_no = gw_no_arr[1];
                    GatewayInfo.getInstance().setInetAddress(context, ip_address);
                    GatewayInfo.getInstance().setPort(context, port_int);
                    GatewayInfo.getInstance().setGatewayNo(context, gw_no);
//                    /**
//                     * 休眠500毫秒后初始化MQTT连接
//                     */
//                    Thread.sleep(500);
//                    if (NetworkUtil.isNetworkAvailable(context)) {
//                        SerialHandler.getInstance().setMqttCommunication();
//                        System.out.println(TAG + " = " + NetworkUtil.isNetworkAvailable(context));
//                    }

                    if (!isScanGwNodeVer) {
                        Thread.sleep(1500);
                        SerialHandler.getInstance().GetGatewayVersionInfo();
                    }

                    Thread.sleep(1500);
                    byte[] bt_send = GatewayCmdData.Get_IEEEAddr_CMD();
                    new Thread(new UdpClient(context, bt_send)).start();
                    System.out.println("K64_SEARCH_GW = " + num);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "Exception");
            lock.release();
            killThread();
        }
    }

    public void killThread() {
        try {
            if (socket.isConnected() & socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

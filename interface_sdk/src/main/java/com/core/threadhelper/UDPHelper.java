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

import java.io.InterruptedIOException;
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
                DatagramPacket packet = new DatagramPacket(message, message.length);
                try {
                    socket.receive(packet);
                } catch (InterruptedIOException e) {
                    System.out.println("continue....................");
                    continue;  //非阻塞循环Operation not permitted
                }
                String str_message = new String(packet.getData()).trim();
                String ip_address = packet.getAddress().getHostAddress().toString();
                int port_int = packet.getPort();

                Constants.GW_IP_ADDRESS = ip_address;

                DataPacket.getInstance().BytesDataPacket(context,message);
                if (str_message.contains("K64_SEARCH_GW") & num < 4) {
                    String[] gw_no_arr = str_message.split(":");
                    String gw_no = gw_no_arr[1];
                    if (context == null){
                        System.out.println("上下文Context为空");
                        return;
                    }
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

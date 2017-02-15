package com.core.connectivity;

import android.content.Context;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GatewayCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.GatewayInfo.GATEWAY_UPDATE_FILE_NEXT;
import static com.core.global.Constants.GatewayInfo.PACKETS;
import static com.core.global.Constants.GatewayInfo.SEND_SIZE;
import static com.core.global.Constants.GatewayInfo.UPDATE_FILE_BT;
import static com.core.global.Constants.isConn;

/**
 * Created by best on 2016/11/3.
 */
public class UdpClient implements Runnable {
    public static final String TAG = "UdpClient";
    private byte[] bt_send = null;
    private DatagramSocket socket = null;
    private Context mContext;
    public static final int DEFAULT_TIMEOUT_DURATION = 3000;
    public static int MAX_BUSY_COUNT = 20;

    public UdpClient(Context context, byte[] bt_send) {
        this.mContext = context;
        this.bt_send = bt_send;
        this.MAX_BUSY_COUNT = 20;
        Constants.context = mContext;
    }

    @Override
    public void run() {
        try {
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.MQTT_CLIENT_ID);
                if (!isConnect) {
                    return;
                }
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket send_packet = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(send_packet);
                System.out.println("当前发送的数据 = " + TransformUtils.binary(bt_send, 16));

                while (MAX_BUSY_COUNT <= 20) {
                    MAX_BUSY_COUNT--;
                    final byte[] recbytes = new byte[1024];
                    final DatagramPacket receive_packet = new DatagramPacket(recbytes, recbytes.length);

                    try {
                        socket.receive(receive_packet);
                    } catch (InterruptedIOException e) {
                        System.out.println("continue....................");
                        continue;  //非阻塞循环Operation not permitted
                    }
                    if (!Utils.isK6(recbytes)) {
                        System.out.println("设备信息UdpClient = " + Arrays.toString(recbytes));
                        DataPacket.getInstance().BytesDataPacket(mContext, recbytes);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + " = " + e.getMessage());
        } finally {
            System.out.println(TAG + " = " + "finally");
        }
    }
}

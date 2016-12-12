package com.core.connectivity;

import android.content.Context;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.DEVICE_GLOBAL.sdkappDevice;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;

/**
 * Created by best on 2016/11/3.
 */

public class UdpClient implements Runnable{

    private byte[] bt_send = null;
    private DatagramSocket socket = null;
    private Context mContext;
    public static final int DEFAULT_TIMEOUT_DURATION = 6000;
    public static int MAX_BUSY_COUNT = 20;

    public UdpClient(Context context, byte[] bt_send){
        this.mContext = context;
        this.bt_send = bt_send;
        this.MAX_BUSY_COUNT = 20;
    }

    @Override
    public void run() {
        try {
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    socket.setSoTimeout(DEFAULT_TIMEOUT_DURATION);
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (MAX_BUSY_COUNT <= 20) {
                    MAX_BUSY_COUNT--;
                    final byte[] recbytes = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbytes, recbytes.length);
                    socket.receive(packet);
                    String isK64 = new String(recbytes).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    System.out.println("设备信息UdpClient = " + Arrays.toString(recbytes));
                    DataPacket.getInstance().BytesDataPacket(mContext,recbytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.isConn;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
    public final String TAG = "GetAllDevices";
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private static Context mContext;
    private boolean isNewDevice = false;

    public GetAllDevices(Context context, boolean isNewDevice) {
        this.mContext = context;
        this.isNewDevice = isNewDevice;
    }

    @Override
    public void run() {
        try {
            if (!isNewDevice) {
                bt_send = DeviceCmdData.GetAllDeviceListCmd();
//                bt_send = AESUtils.encode(bt);
            } else {
                bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
//                bt_send = AESUtils.encode(bt);
            }

            if (GW_IP_ADDRESS.equals("")) {//NetworkUtil.isNetworkAvailable(mContext)  !NetworkUtil.NetWorkType(mContext)
//                boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.MQTT_CLIENT_ID);
//                if (!isConnect){
//                    return;
//                }
//                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                if (!isConn){
                    MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                    System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
                }
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                Log.i(TAG + "当前发送的数据 = " , TransformUtils.binary(bt_send, 16));

                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    try {
                        socket.receive(packet);
                    } catch (InterruptedIOException e) {
                        System.out.println("continue....................");
                        continue;  //非阻塞循环Operation not permitted
                    }

                    if (!Utils.isK6(recbuf)) {
                        Log.i(TAG + "当前接收的数据 = " , Arrays.toString(recbuf));
                        DataPacket.getInstance().BytesDataPacket(mContext, recbuf);
                        System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
                    }
                }


            }
            Log.e("my", "shake thread broadcast end.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GetAllDevices" + " =  " + e.getMessage());
        }
//        finally {
//            System.out.println("finally");
//        }
//        try {
//            if (!isNewDevice) {
//                bt_send = DeviceCmdData.GetAllDeviceListCmd();
////                bt_send = AESUtils.encode(bt);
//            } else {
//                bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
////                bt_send = AESUtils.encode(bt);
//            }
//
//            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
//                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
//                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
//                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
//            } else {
//                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
//                if (socket == null) {
//                    socket = new DatagramSocket(null);
//                    socket.setReuseAddress(true);
//                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
//                }
//                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
//                socket.send(datagramPacket);
//                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));
//
//                while (true) {
//                    byte[] recbuf = new byte[1024];
//                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
//                    socket.receive(packet);
//
//                    String isK64 = new String(recbuf).trim();
//                    if (isK64.contains("K64")) {
//                        return;
//                    }
//
//                    System.out.println("当前接收的数据GetAllDevices = " + Arrays.toString(recbuf));
//                    DataPacket.getInstance().BytesDataPacket(mContext, recbuf);
//                    /**
//                     * 获取网关所有设备及其新入网设备
//                     */
//                    if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbuf[11]) {
////                        byte[] bt = AESUtils.decode(recbuf);
//                        ParseDeviceData.ParseGetDeviceInfo(mContext,recbuf, false);
//                    } else if ((int) MessageType.A.UPLOAD_TXT.value() == recbuf[11]) {//新入网设备
////                        byte[] bt = AESUtils.decode(recbuf);
//                        ParseDeviceData.ParseGetDeviceInfo(mContext,recbuf, true);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Handler2 = ", "Exception");
//        }
    }
}


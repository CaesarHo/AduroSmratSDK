package com.core.threadhelper.devices;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.DEVICE_GLOBAL.appDeviceList;
import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private static Context mContext;
    private boolean isNewDevice = false;
//    private boolean isRun;
    private int SEND_TIMES = 20;

    public GetAllDevices(Context context, boolean isNewDevice) {
        this.mContext = context;
        this.isNewDevice = isNewDevice;
    }

    @Override
    public void run() {
//        isRun = true;
        appDeviceList.clear();
        try {
            if (!isNewDevice) {
                bt_send = DeviceCmdData.GetAllDeviceListCmd();
//                bt_send = AESUtils.encode(bt);
            } else {
                bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
//                bt_send = AESUtils.encode(bt);
            }
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
//                int times = 0;
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);

                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
//                    if (!isRun) {
//                        return;
//                    }
//                    times++;
                    Log.e("my", "shake thread send broadcast.");//第三
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);

                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }

                    System.out.println("当前接收的数据GetAllDevices = " + Arrays.toString(recbuf));
                    DataPacket.getInstance().BytesDataPacket(mContext, recbuf);
                    /**
                     * 获取网关所有设备及其新入网设备
                     */
                    if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbuf[11]) {
//                        byte[] bt = AESUtils.decode(recbuf);
                        ParseDeviceData.ParseGetDeviceInfo(mContext, recbuf, false);
                    } else if ((int) MessageType.A.UPLOAD_TXT.value() == recbuf[11]) {//新入网设备
//                        byte[] bt = AESUtils.decode(recbuf);
                        ParseDeviceData.ParseGetDeviceInfo(mContext, recbuf, true);
                    }
                }
            }
            Log.e("my", "shake thread broadcast end.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mHandler.sendEmptyMessageDelayed(1, 4500);
//                            socket.close();
                Log.e("my", "shake thread finally1.");//第四
            } catch (Exception e) {
                e.printStackTrace();
            }
//                        killThread();
            Log.e("my", "shake thread finally2.");//第五
        }
        Log.e("my", "shake thread end.");//第一
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
//        } finally {
//            for (int i = 0; i < appDeviceList.size(); i++) {
//                if (appDeviceList.get(i).getDeviceid().contains("0051")) {
//                    Log.e("Handler3 = ", "Exception");
//                    String IEEE = GatewayInfo.getInstance().getGwIEEEAddress(mContext);
//                    byte[] bt_bind = DeviceCmdData.BindDeviceCmd(appDeviceList.get(i), IEEE);
//                    new Thread(new UdpClient(mContext, bt_bind)).start();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    public static Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    for (int i = 0; i < appDeviceList.size(); i++) {
                        if (appDeviceList.get(i).getDeviceid().contains("0051")) {
                            Log.e("Handler = ", appDeviceList.get(i).getDeviceMac());
                            String IEEE = GatewayInfo.getInstance().getGwIEEEAddress(mContext);
                            byte[] bt_bind = DeviceCmdData.BindDeviceCmd(appDeviceList.get(i), IEEE);
                            new Thread(new UdpClient(mContext, bt_bind)).start();
                        }
                    }
                }
                break;
            }
            return false;
        }
    });

    public void killThread() {
//        if (isRun) {
//            isRun = false;
//        }
    }
}


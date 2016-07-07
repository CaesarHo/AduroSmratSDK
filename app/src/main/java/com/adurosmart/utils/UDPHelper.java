package com.adurosmart.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UDPHelper {
    Context context;
    public Boolean IsThreadDisable = false;
    public int port;
    InetAddress mInetAddress;
    public Handler mHandler;
    DatagramSocket datagramSocket = null;
    public static final int HANDLER_MESSAGE_BIND_ERROR = 0x01;
    public static final int HANDLER_MESSAGE_RECEIVE_MSG = 0x02;

    public UDPHelper(Context context,int port) {
        this.port = port;
        this.context = context;
    }

    public void setCallBack(Handler handler) {
        this.mHandler = handler;
    }

    public void StartListen() {
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] message = new byte[1024];
                try {
                    // 建立Socket连接
                    try {
                        datagramSocket = new DatagramSocket(port);
                        Log.e("port", "port=" + port);
                    } catch (Exception e) {
                        port = 8888;
                        datagramSocket = new DatagramSocket(port);
                        Log.e("port", "port=" + port);
                    }

                    datagramSocket.setBroadcast(true);
                    DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
                    while (!IsThreadDisable) {
                        // 准备接收数据
                        datagramSocket.receive(datagramPacket);
                        mInetAddress = datagramPacket.getAddress();
                        Log.e("ip_address", "mInetAddress=" + mInetAddress);
                        byte[] data = datagramPacket.getData();

                        String datastr = new String(data);

                        Intent intent = new Intent();
                        intent.setAction("action_upddata");
                        intent.putExtra("data",datastr);
                        context.sendBroadcast(intent);

                        Log.e("setwifi", Arrays.toString(data));
                        int contactId = bytesToInt(data, 16);
                        int frag = bytesToInt(data, 24);
                        Log.e("setwifi", "contactId=" + contactId + "--" + "frag=" + frag);
                        if (null != mHandler) {
                            Message msg = new Message();
                            msg.what = HANDLER_MESSAGE_RECEIVE_MSG;
                            Bundle bundler = new Bundle();
                            bundler.putString("contactId", String.valueOf(contactId));
                            bundler.putString("frag", String.valueOf(frag));
                            String ip_address = String.valueOf(mInetAddress);
                            bundler.putString("ipFlag", ip_address.substring(ip_address.lastIndexOf(".") + 1, ip_address.length()));
                            msg.setData(bundler);
                            mHandler.sendMessage(msg);
                            break;
                        }
                    }
                } catch (SocketException e) {

                } catch (Exception e) {
                    e.printStackTrace();
                    IsThreadDisable = true;
                    if (null != mHandler) {
                        mHandler.sendEmptyMessage(HANDLER_MESSAGE_BIND_ERROR);
                    }
                }
//                finally {
//                    if (null != datagramSocket) {
//                        datagramSocket.close();
//                        datagramSocket = null;
//                    }
//                }
            }
        }).start();
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public void StopListen() {
        this.IsThreadDisable = true;
        if (null != datagramSocket) {
            datagramSocket.close();
            datagramSocket = null;
        }
    }

    public boolean isListening = true;
    DatagramSocket socket = null;
    private String backMsg;
    //监听服务器发来信息的方法
    public void listenThread() {
        try {
            if (socket == null) {
                socket = new DatagramSocket();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] backBuf = new byte[1024];
                InetAddress ip = null;
                try {
                    ip = InetAddress.getByName("192.168.0.60");
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
                DatagramPacket backDp = new DatagramPacket(backBuf, backBuf.length, ip, 9999);
                Log.i("info", "接收之前。。。");
                while (isListening) {
                    try {
                        socket.receive(backDp);//操作阻塞
                        backMsg = new String(backBuf, 0, backDp.getLength());
                        Log.i("info", "接收之后。。。backMsg==" + backMsg);
                        if (backMsg == null || "".equals(backMsg)) {
                            continue;
                        }
//                        dispatchMessage(backMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

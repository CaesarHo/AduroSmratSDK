package com.shake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.utils.NewCmdData;
import com.utils.SearchUtils;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ShakeThread extends Thread {
    public static final int DEFAULT_PORT = 8888;
    public static final int RECEIVE_IPC_INFO = 0;
    public static final int CLOSE_SERVER = 999;
    public int SEND_TIMES;

    private int port;
    private boolean isRun;

    private DatagramSocket socket;
    private Handler handler;

    private InetAddress inetAddress;

    //设备信息
    private String profile_id,device_id,device_mac,device_shortaddr,main_endpoint;

    public ShakeThread(Handler handler){
        this.port = DEFAULT_PORT;
        this.SEND_TIMES = 10;
    }

    public void setSearchTime(long time) {
        SEND_TIMES = (int) (time / 1000);
    }

    public void setInetAddress(InetAddress host) {
        this.inetAddress = host;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        isRun = true;
        try {
            socket = new DatagramSocket();
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(port));
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        int times = 0;
                        if (socket == null) {
                            socket = new DatagramSocket(null);
                            socket.setReuseAddress(true);
                            socket.bind(new InetSocketAddress(port));
                        }
                        while (times < SEND_TIMES) {
                            if (!isRun) {
                                return;
                            }
                            times++;
                            Log.e("myshake", "shake thread send broadcast.");

                            byte[] bt_send = NewCmdData.GetAllDeviceList();
                            bt_send = NewCmdData.GetAllDeviceList();
                            Log.i("GATEWATEIPADDRESS = " ,Constants.ipaddress);
                            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                            DatagramPacket datagramPacket = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), Utils.hexStringToByteArray(Utils.binary(bt_send, 16)).length, inetAddress, port);
                            socket.send(datagramPacket);
                            System.out.println("send " + Utils.hexStringToByteArray(Utils.binary(bt_send, 16)));
                            System.out.println("十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

                            Thread.sleep(1000);
                        }

                        Log.e("myshake", "shake thread broadcast end.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        try {
//                            socket.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        ShakeManager.getInstance().stopShaking();
                    }
                }
            }.start();

            while (isRun) {
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                try {
                    socket.receive(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String rec_str = packet.getData().toString();
                Log.i("myshake = ", rec_str);

                if(rec_str.contains("GW")&&!rec_str.contains("K64")){
                    int profile_id_int = SearchUtils.searchString(rec_str, "PROFILE_ID:");
                    int device_id_int = SearchUtils.searchString(rec_str, "DEVICE_ID:");
                    int device_mac_int = SearchUtils.searchString(rec_str, "DEVICE_MAC:");
                    int device_shortaddr_int = SearchUtils.searchString(rec_str, "DEVICE_SHORTADDR:");
                    int main_endpoint_int = SearchUtils.searchString(rec_str, "MAIN_ENDPOINT:");
                    profile_id = new String(rec_str).substring(profile_id_int, profile_id_int + 6);
                    device_id = new String(rec_str).substring(device_id_int, device_id_int + 6);
                    device_mac = new String(rec_str).substring(device_mac_int, device_mac_int + 18);
                    device_shortaddr = new String(rec_str).substring(device_shortaddr_int, device_shortaddr_int + 6);
                    main_endpoint = new String(rec_str).substring(main_endpoint_int,device_shortaddr_int+4);

                    Log.i("device_mac = ", device_mac);
                    DataSources.getInstance().ScanDeviceResult("Device",profile_id ,device_mac ,device_shortaddr ,device_id,main_endpoint);
                }

                if (null != handler) {
                    Message msg = new Message();
                    msg.what = ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO;
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_id", profile_id);
                    bundle.putString("device_id", device_id);
                    bundle.putString("device_mac", device_mac);
                    bundle.putString("device_shortaddr",device_shortaddr);
                    bundle.putString("main_endpoint",main_endpoint);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
            Log.e("my", "shake thread end.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ShakeManager.getInstance().stopShaking();

            if (null != handler) {
                Message msg = new Message();
                msg.what = ShakeManager.HANDLE_ID_SEARCH_END;
                handler.sendMessage(msg);
            }

            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void killThread() {
        if (isRun) {
            isRun = false;
        }
    }
}

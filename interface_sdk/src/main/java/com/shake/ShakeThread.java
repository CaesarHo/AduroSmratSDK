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
    private String profile_id,device_id,device_mac,device_shortaddr,main_endpoint,device_name,device_zone_type,in_cluster_count,out_cluster_count;

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

                            byte[] bt_send = NewCmdData.GetAllDeviceListCmd();
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

                    int device_name_int = SearchUtils.searchString(rec_str,"DEVICE_NAME:0X");
                    int zone_type_int = SearchUtils.searchString(rec_str, "ZONE_TYPE:0X");
                    int in_cluster_count_int = SearchUtils.searchString(rec_str,"IN_CLUSTER_COUNT:0X");
                    int out_cluster_count_int = SearchUtils.searchString(rec_str,"OUT_CLUSTER_COUNT:0X");


                    String isMac = new String(rec_str).substring(device_mac_int - 13, device_mac_int);
                    if (!isMac.equals("DEVICE_MAC:0X")){
                        return;
                    }

                    profile_id = rec_str.substring(profile_id_int, profile_id_int + 4);
                    device_id = rec_str.substring(device_id_int, device_id_int + 4);
                    device_name = rec_str.substring(device_name_int,device_name_int+4);
                    device_mac = rec_str.substring(device_mac_int, device_mac_int + 16);
                    device_shortaddr = rec_str.substring(device_shortaddr_int, device_shortaddr_int + 4);
                    device_zone_type = rec_str.substring(zone_type_int, zone_type_int + 4);
                    main_endpoint = rec_str.substring(main_endpoint_int,main_endpoint_int+2);
                    in_cluster_count = rec_str.substring(in_cluster_count_int,in_cluster_count_int+2);
                    out_cluster_count = rec_str.substring(out_cluster_count_int,out_cluster_count_int+2);

                    Log.i("device_mac = ", device_mac);
                }

                if (null != handler) {
                    Message msg = new Message();
                    msg.what = ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO;
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_id", profile_id);
                    bundle.putString("device_id", device_id);
                    bundle.putString("device_name",device_name);
                    bundle.putString("device_mac", device_mac);
                    bundle.putString("device_shortaddr",device_shortaddr);
                    bundle.putString("device_zone_type",device_zone_type);
                    bundle.putString("main_endpoint",main_endpoint);
                    bundle.putString("in_cluster_count",in_cluster_count);
                    bundle.putString("out_cluster_count",out_cluster_count);
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

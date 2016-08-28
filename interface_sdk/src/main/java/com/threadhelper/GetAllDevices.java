package com.threadhelper;

import android.content.Context;
import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.interfacecallback.UDPHelper;
import com.utils.NewCmdData;
import com.utils.SearchUtils;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
    private Context context;
    byte[] bt_send;
    public static final int PORT = 8888;
    public DatagramSocket socket = null;
    private boolean ready = true;
    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    bt_send = NewCmdData.GetAllDeviceListCmd();
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), Utils.hexStringToByteArray(Utils.binary(bt_send, 16)).length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("send " + Utils.hexStringToByteArray(Utils.binary(bt_send, 16)));
                    System.out.println("十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(PORT));
                }
                socket.receive(packet);
                System.out.println("收到的数据: ‘" + new String(packet.getData()).trim() + "’\n");
                String str = new String(packet.getData()).trim();

                if(str.contains("GW")&&ready&&!str.contains("K64")){
                    System.out.println("isGroupScene = " + !Constants.isGroupScene);

                    ready = true;
                    int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:0X");
                    int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:0X");
                    int device_name_int = SearchUtils.searchString(str,"DEVICE_NAME:0X");
                    int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:0X");
                    int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:0X");
                    int zone_type_int = SearchUtils.searchString(str, "ZONE_TYPE:0X");
                    int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:0X");
                    int in_cluster_count_int = SearchUtils.searchString(str,"IN_CLUSTER_COUNT:0X");
                    int out_cluster_count_int = SearchUtils.searchString(str,"OUT_CLUSTER_COUNT:0X");

                    String isMac = new String(str).substring(device_mac_int - 13, device_mac_int);

                    System.out.println("isMac = " + isMac);
                    if (!isMac.contains("DEVICE_MAC:0X") && isMac.length() < 13){
                        return;
                    }
                    if (!isMac.equals("DEVICE_MAC:0X")){
                        return;
                    }
                    String profile_id = new String(str).substring(profile_id_int, profile_id_int + 4);
                    String device_id = new String(str).substring(device_id_int, device_id_int + 4);
                    String device_name = new String(str).substring(device_name_int,device_name_int+4);
                    String device_mac = new String(str).substring(device_mac_int, device_mac_int + 16);
                    String device_shortaddr = new String(str).substring(device_shortaddr_int, device_shortaddr_int + 4);
                    String device_zone_type = new String(str).substring(zone_type_int, zone_type_int + 4);
                    String main_endpoint = new String(str).substring(main_endpoint_int,main_endpoint_int+2);
                    String in_cluster_count = new String(str).substring(in_cluster_count_int,in_cluster_count_int+2);
                    String out_cluster_count = new String(str).substring(out_cluster_count_int,out_cluster_count_int+2);

                    switch (device_id) {
                        case "0105":
                            device_name = "DimSwitch";
                            break;
                        case "0102":
                            device_name = "ColorLamp";
                            break;
                        case "0110":
                            device_name = "ColorTemperatureLamp";
                            break;
                        case "0210":
                            device_name = "HueColorLamp";
                            break;
                        case "0200":
                            device_name = "ColorLight";
                            break;
                        case "0220":
                            device_name = "ColorTemperatureLampJZGD";
                            break;
                        case "0100":
                            device_name = "DimmableLight";
                            break;
                        case "0101":
                            device_name = "DimLamp";
                            break;

                        case "0402":
                            device_name = "HumanSensor";
                            break;
                        case "0202":
                            device_name = "WindowCurtain";
                            break;
                        case "0309":
                            device_name = "PM2dot5Sensor";
                            break;

                        case "0310":
                            device_name = "SmokingSensor";
                            break;

                    }
                    DataSources.getInstance().ScanDeviceResult(device_name,profile_id ,device_mac ,device_shortaddr ,device_id,main_endpoint,
                            in_cluster_count,out_cluster_count,device_zone_type);
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


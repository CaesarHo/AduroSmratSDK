package com.threadhelper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.interfacecallback.UDPHelper;
import com.utils.NewCmdData;
import com.utils.ParseData;
import com.utils.SearchUtils;
import com.utils.Utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
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



//        for(int k=0; k<50; k++)//-----500ms 接收一次……。
//        {
//            try {
//                byte[] recbuf = new byte[1024];// 一次接一个结构体
//                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);// 创建UDP接收包
//
//                // 接收UDP包
//                socket.receive(packet);
//
//                // 解析接收到的UDP包1，并赋值给FD_SVR_PACKET类型对象RecvCleint
//                String str = new String(packet.getData()).trim();
//
//                if(str.contains("GW")&&ready&&!str.contains("K64")){
//
//                    ParseData.ParseSensorData parseSensorData = new ParseData.ParseSensorData();
//                    parseSensorData.parseBytes(recbuf);
//                    System.out.println("传感器数据" + parseSensorData.mSensorState);
//                    System.out.println("传感器数据" + parseSensorData.mDevMac);
//                    if (parseSensorData.mZigbeeType == 0x8401){
//                        System.out.println("传感器数据" + parseSensorData.mSensorState);
//                        System.out.println("传感器数据" + parseSensorData.mDevMac);
//                    }
//
//                    System.out.println("isGroupScene = " + !Constants.isGroupScene);
//
//                    ready = true;
//                    int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:0X");
//                    int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:0X");
//                    int device_name_int = SearchUtils.searchString(str,"DEVICE_NAME:0X");
//                    int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:0X");
//                    int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:0X");
//                    int zone_type_int = SearchUtils.searchString(str, "ZONE_TYPE:0X");
//                    int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:0X");
//                    int in_cluster_count_int = SearchUtils.searchString(str,"IN_CLUSTER_COUNT:0X");
//                    int out_cluster_count_int = SearchUtils.searchString(str,"OUT_CLUSTER_COUNT:0X");
//
//                    String isMac = new String(str).substring(device_mac_int - 13, device_mac_int);
//
//                    System.out.println("isMac = " + isMac);
//                    if (!isMac.contains("DEVICE_MAC:0X") && isMac.length() < 13){
//                        return;
//                    }
//                    if (!isMac.equals("DEVICE_MAC:0X")){
//                        return;
//                    }
//                    String profile_id = new String(str).substring(profile_id_int, profile_id_int + 4);
//                    String device_id = new String(str).substring(device_id_int, device_id_int + 4);
//                    String device_name = new String(str).substring(device_name_int,device_name_int+4);
//                    String device_mac = new String(str).substring(device_mac_int, device_mac_int + 16);
//                    String device_shortaddr = new String(str).substring(device_shortaddr_int, device_shortaddr_int + 4);
//                    String device_zone_type = new String(str).substring(zone_type_int, zone_type_int + 4);
//                    String main_endpoint = new String(str).substring(main_endpoint_int,main_endpoint_int+2);
//                    String in_cluster_count = new String(str).substring(in_cluster_count_int,in_cluster_count_int+2);
//                    String out_cluster_count = new String(str).substring(out_cluster_count_int,out_cluster_count_int+2);
//
//                    switch (device_id) {
//                        case "0105":
//                            device_name = "DimSwitch";
//                            break;
//                        case "0102":
//                            device_name = "ColorLamp";
//                            break;
//                        case "0110":
//                            device_name = "ColorTemperatureLamp";
//                            break;
//                        case "0210":
//                            device_name = "HueColorLamp";
//                            break;
//                        case "0200":
//                            device_name = "ColorLight";
//                            break;
//                        case "0220":
//                            device_name = "ColorTemperatureLampJZGD";
//                            break;
//                        case "0100":
//                            device_name = "DimmableLight";
//                            break;
//                        case "0101":
//                            device_name = "DimLamp";
//                            break;
//
//                        case "0402":
//                            device_name = "HumanSensor";
//                            break;
//                        case "0202":
//                            device_name = "WindowCurtain";
//                            break;
//                        case "0309":
//                            device_name = "PM2dot5Sensor";
//                            break;
//
//                        case "0310":
//                            device_name = "SmokingSensor";
//                            break;
//                    }
//                    DataSources.getInstance().ScanDeviceResult(device_name,profile_id ,device_mac ,device_shortaddr ,device_id,main_endpoint,
//                            in_cluster_count,out_cluster_count,device_zone_type);
//                }
//            }catch (Exception e){
//                continue;  //非阻塞循环
//            }
//        }//k=60

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                socket.receive(packet);

                System.out.println("GetAllDevices: ‘" + new String(packet.getData()).trim() + "’\n");
                System.out.println("GetAllDevices: ‘" + Arrays.toString(recbuf) + "’\n");
                String str = new String(packet.getData()).trim();
                System.out.println("str长度 = " + str.length());

                if(str.length() > 46){

                    ParseData.ParseSensorData parseSensorData = new ParseData.ParseSensorData();
                    parseSensorData.parseBytes(recbuf);
                    //判断是否是传感器数据
                    if (parseSensorData.mZigbeeType.contains("8401")){
                        System.out.println("传感器返回数据state" + parseSensorData.mSensorState);
                        String time_str = String.valueOf(System.currentTimeMillis());
                        DataSources.getInstance().getReceiveSensor(parseSensorData.mDevMac,parseSensorData.state,Utils.getFormatTellDate(time_str));
                    }

                    System.out.println("isGroupScene = " + !Constants.isGroupScene);

                    ready = true;
                    int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:");
                    int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:");
                    int device_name_int = SearchUtils.searchString(str,"DEVICE_NAME:");
                    int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:");
                    int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:");
                    int zone_type_int = SearchUtils.searchString(str, "ZONE_TYPE:");
                    int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:");
                    int in_cluster_count_int = SearchUtils.searchString(str,"IN_CLUSTER_COUNT:");
                    int out_cluster_count_int = SearchUtils.searchString(str,"OUT_CLUSTER_COUNT:");

                    String isMac = new String(str).substring(device_mac_int - 11, device_mac_int);

                    System.out.println("isMac = " + isMac);
                    if (!isMac.contains("DEVICE_MAC:") && isMac.length() < 11){
                        return;
                    }
                    if (!isMac.equals("DEVICE_MAC:")){
                        return;
                    }
                    String profile_id = str.substring(profile_id_int + 2, profile_id_int + 6);
                    String device_id = str.substring(device_id_int + 2, device_id_int + 6);
                    String device_name = str.substring(device_name_int + 2,device_name_int+6);
                    String device_mac = str.substring(device_mac_int + 2, device_mac_int + 18);
                    String device_shortaddr = str.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
                    String device_zone_type = str.substring(zone_type_int + 2, zone_type_int + 6);
                    String main_endpoint = str.substring(main_endpoint_int + 2,main_endpoint_int+4);
                    String in_cluster_count = str.substring(in_cluster_count_int + 2,in_cluster_count_int+4);
                    String out_cluster_count = str.substring(out_cluster_count_int + 2,out_cluster_count_int+4);

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


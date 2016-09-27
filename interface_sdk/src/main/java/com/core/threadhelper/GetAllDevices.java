package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.db.AppDeviceInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.FtFormatTransfer;
import com.core.utils.ParseData;
import com.core.utils.SearchUtils;
import com.core.utils.Utils;

import java.io.IOException;
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
    private String device_mac;
    private String device_shortaddr;
    private String main_endpoint;
    private String profile_id;
    private String device_id;
    private String device_name;
    private String device_zone_type;
    private String in_cluster_count;
    private String out_cluster_count;
    private short clusterId = -1;
    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        //发送获取设备命令
        SendGetDeviceCmd();

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                socket.receive(packet);
                String str = new String(packet.getData()).trim();
                /**
                 * 接受传感器数据并解析
                 */
                ParseData.ParseSensorData parseSensorData = new ParseData.ParseSensorData();
                parseSensorData.parseBytes(recbuf);
                if (parseSensorData.mZigbeeType.contains("8401")){
                    System.out.println("传感器返回数据stateSDK = " + parseSensorData.state);
                    String time_str = String.valueOf(System.currentTimeMillis());
                    DataSources.getInstance().getReceiveSensor(parseSensorData.mDevMac,parseSensorData.state,Utils.getFormatTellDate(time_str));
                }

                /**
                 * 解析设备属性数据
                 */
                ParseData.ParseAttributeData parseAttributeData = new ParseData.ParseAttributeData();
                parseAttributeData.parseBytes(recbuf);
                if (parseAttributeData.mZigbeeType.contains("8100")) {
                    System.out.println("ParseAttributeDataattribValue" + parseAttributeData.attribValue);
                    if (parseAttributeData.clusterID == 5){
                        SendSaveZoneTypeCmd((short)parseAttributeData.attribValue);
                    }
                }

                if(str.length() > 46){
                    int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:");
                    int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:");
                    int device_name_int = SearchUtils.searchString(str,"DEVICE_NAME:");
                    int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:");
                    int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:");
                    int zone_type_int = SearchUtils.searchString(str, "ZONE_TYPE:");
                    int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:");
                    int in_cluster_count_int = SearchUtils.searchString(str,"IN_CLUSTER_COUNT:");
                    int out_cluster_count_int = SearchUtils.searchString(str,"OUT_CLUSTER_COUNT:");

                    //判断是否是设备
                    String isMac = new String(str).substring(device_mac_int - 11, device_mac_int);
                    System.out.println("isMac = " + isMac);
                    if (!isMac.contains("DEVICE_MAC:") | isMac.length() < 11 | !isMac.equals("DEVICE_MAC:")){
                        return;
                    }

                    profile_id = str.substring(profile_id_int + 2, profile_id_int + 6);
                    device_id = str.substring(device_id_int + 2, device_id_int + 6);
                    device_name = str.substring(device_name_int ,device_name_int + 6);
                    device_mac = str.substring(device_mac_int + 2, device_mac_int + 18);
                    device_shortaddr = str.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
                    device_zone_type = str.substring(zone_type_int + 2, zone_type_int + 6);
                    main_endpoint = str.substring(main_endpoint_int + 2,main_endpoint_int+4);
                    in_cluster_count = str.substring(in_cluster_count_int + 2,in_cluster_count_int+4);
                    out_cluster_count = str.substring(out_cluster_count_int + 2,out_cluster_count_int+4);

                    //根据cluster_id读取相应属性
                    String[] cluster_id_array = str.split("CLUSTER_ID:");
                    for (int i = 0;i<cluster_id_array.length;i++){
                        String CLUSTER_ID = cluster_id_array[i].substring(2);
                        System.out.println("CLUSTER_ID = " + CLUSTER_ID);

                        if (CLUSTER_ID == "0500"){
                            byte[] bt_clu = new byte[2];
                            bt_clu[0] = Utils.HexString2Bytes(CLUSTER_ID)[0];
                            bt_clu[1] = Utils.HexString2Bytes(CLUSTER_ID)[1];
                            System.out.println("bt_clu = " + Arrays.toString(bt_clu));
                            short clusterId_short = FtFormatTransfer.getShort(bt_clu,0);
                            System.out.println("clusterId_short = " + clusterId_short);
                            clusterId = clusterId_short;
                        }
                    }

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
                            if (device_zone_type.equalsIgnoreCase("ffff")){
                                SendReadZoneTypeCmd();
                            }
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
                        case "ffff":
                            //当deviceid为ffff时发送此命令，识别设备id,然后回调给UI
                            SendActiveReqCmd();
                            break;
                    }

                    if (!device_id.equalsIgnoreCase("ffff")){
                        AppDeviceInfo appDeviceInfo = new AppDeviceInfo();
                        appDeviceInfo.setProfileid(profile_id);
                        appDeviceInfo.setDeviceName(device_name);
                        appDeviceInfo.setDeviceMac(device_mac);
                        appDeviceInfo.setDeviceid(device_id);
                        appDeviceInfo.setShortaddr(device_shortaddr);
                        appDeviceInfo.setEndpoint(main_endpoint);
                        appDeviceInfo.setZonetype(device_zone_type);

                        System.out.println("AppDeviceInfo = " + device_mac);

                        DataSources.getInstance().ScanDeviceResult(appDeviceInfo);
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //发送获取设备命令
    public void SendGetDeviceCmd(){
        new Thread(){
            @Override
            public void run() {
                try {
                    bt_send = DeviceCmdData.GetAllDeviceListCmd();
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendGetDeviceCmd十六进制 = " + Utils.binary(bt_send, 16));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //当device id 位FFFF时发送此命令，识别设备id,以及设备其他属性
    public void SendActiveReqCmd(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }
                    bt_send = DeviceCmdData.ActiveReqDeviceCmd(device_mac,device_shortaddr,main_endpoint);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendActiveReqCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    SendGetDeviceCmd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //当device id 位0402且device_zone_type为ffff时发送此命令读取属性
    public void SendReadZoneTypeCmd(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }
                    bt_send = DeviceCmdData.ReadZoneTypeCmd(device_mac,device_shortaddr,main_endpoint);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendReadZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    SendGetDeviceCmd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //当有傳感器上傳時保存device_zone_type为时发送此命令读取属性
    public void SendSaveZoneTypeCmd(final short zonetype){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }
                    bt_send = DeviceCmdData.SaveZoneTypeCmd(device_mac,device_shortaddr,main_endpoint,zonetype);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendSaveZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    SendGetDeviceCmd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}


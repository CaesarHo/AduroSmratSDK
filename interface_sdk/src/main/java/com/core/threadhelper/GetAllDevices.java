package com.core.threadhelper;

import android.util.Log;

import com.core.data.DeviceCmdData;
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
    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }

        SendGetDeviceCmd();

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                socket.receive(packet);

                System.out.println("GetAllDevices: ‘" + new String(packet.getData()).trim() + "’\n");
                System.out.println("GetAllDevices: ‘" + Arrays.toString(recbuf) + "’\n");
                String str = new String(packet.getData()).trim();
                System.out.println("str长度 = " + str.length());
                ParseData.ParseSensorData parseSensorData = new ParseData.ParseSensorData();
                parseSensorData.parseBytes(recbuf);
                //判断是否是传感器数据
                if (parseSensorData.mZigbeeType.contains("8401")){
                    System.out.println("传感器返回数据state" + parseSensorData.mSensorState);
                    String time_str = String.valueOf(System.currentTimeMillis());
                    DataSources.getInstance().getReceiveSensor(parseSensorData.mDevMac,parseSensorData.state,Utils.getFormatTellDate(time_str));
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
                    String device_name = str.substring(device_name_int ,device_name_int + 6);
                    device_mac = str.substring(device_mac_int + 2, device_mac_int + 18);
                    device_shortaddr = str.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
                    String device_zone_type = str.substring(zone_type_int + 2, zone_type_int + 6);
                    main_endpoint = str.substring(main_endpoint_int + 2,main_endpoint_int+4);
                    String in_cluster_count = str.substring(in_cluster_count_int + 2,in_cluster_count_int+4);
                    String out_cluster_count = str.substring(out_cluster_count_int + 2,out_cluster_count_int+4);

                    String[] cluster_id_array = str.split("CLUSTER_ID:");
                    for (int i = 0;i<cluster_id_array.length;i++){
                        String CLUSTER_ID = cluster_id_array[i].substring(2);
                        if (CLUSTER_ID == "0500"){

                        }
                    }

                    Log.i("sdkdevice_id = " , device_id);
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

                        case "ffff":
                            //如果设备ID为ffff
                            SendActiveReqCmd();
                            break;
                    }
                    if (!device_id.equalsIgnoreCase("ffff")){
                        DataSources.getInstance().ScanDeviceResult(device_name,profile_id ,device_mac ,device_shortaddr ,device_id,main_endpoint,
                                in_cluster_count,out_cluster_count,device_zone_type);
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
    }

    public void SendActiveReqCmd(){
        new Thread(){
            @Override
            public void run() {
                try {
                    byte[] bt = new byte[2];
                    bt[0] = Utils.HexString2Bytes(device_shortaddr)[1];
                    bt[1] = Utils.HexString2Bytes(device_shortaddr)[1];
                    short btToshort = FtFormatTransfer.hBytesToShort(bt);
                    System.out.println("btToshort = " + btToshort);
                    bt_send = DeviceCmdData.ActiveReqDeviceCmd(device_mac,device_shortaddr,main_endpoint);
                    Log.i("网关IP地址 = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), Utils.hexStringToByteArray(Utils.binary(bt_send, 16)).length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendActiveReqCmd十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
                    Thread.sleep(500);
                    SendGetDeviceCmd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}


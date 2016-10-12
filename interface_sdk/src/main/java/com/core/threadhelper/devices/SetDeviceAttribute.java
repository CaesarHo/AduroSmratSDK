package com.core.threadhelper.devices;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/10/10.
 */

public class SetDeviceAttribute {

    public static byte[] bt_send;
    public static DatagramSocket socket = null;
    //当device id 位0402且device_zone_type为ffff时发送此命令读取属性
    public static void SendReadZoneTypeCmd(final String device_mac,final String device_shortaddr,final String main_endpoint){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.GW_IP_ADDRESS);
                    InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    }
                    bt_send = DeviceCmdData.ReadZoneTypeCmd(device_mac,device_shortaddr,main_endpoint);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendReadZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    //发送读取zonetype后重新获取设备列表
                    SerialHandler.getInstance().GetAllDeviceListen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //当有傳感器上傳時保存device_zone_type为时发送此命令读取属性
    public static void SendSaveZoneTypeCmd(final String mac,final String shortaddr,final String endpoint,final short zonetype){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.GW_IP_ADDRESS);
                    InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    }
                    bt_send = DeviceCmdData.SaveZoneTypeCmd(mac,shortaddr,endpoint,zonetype);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendSaveZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    //发送保存zonetype后重新获取设备列表
                    SerialHandler.getInstance().GetAllDeviceListen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //当device id 位FFFF时发送此命令，识别设备id,以及设备其他属性
    public static void SendActiveReqCmd(final String mac,final String shortaddr,final String endpoint){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("网关IP地址 = " , Constants.GW_IP_ADDRESS);
                    InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    }
                    bt_send = DeviceCmdData.ActiveReqDeviceCmd(mac,shortaddr,endpoint);
                    DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length, inetAddress, Constants.UDP_PORT);
                    socket.send(datagramPacket);
                    System.out.println("SendActiveReqCmd十六进制 = " + Utils.binary(bt_send, 16));
                    Thread.sleep(500);
                    //发送识别设备id命令后，重新获取设备
                    SerialHandler.getInstance().GetAllDeviceListen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

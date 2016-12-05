package com.core.global;

import com.core.entity.AppDevice;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/14.
 */
public class Constants {
    public static boolean isRemote = false;
    public static boolean isScanGwNodeVer = false;
    public static int UDP_PORT = 8888;
    public static String MQTT_SERVER = "120.24.242.83";//"192.168.0.80";//"data.adurosmart.com";
    public static String CLIENT_ID;
    public static int MQTT_PORT = 1883;
    public static String URI = "tcp://" + MQTT_SERVER + ":" + MQTT_PORT;

    public static String GW_IP_ADDRESS = "";
    public static String APP_IP_ADDRESS = "";
    public static class IpAddress{
        public static int int_1 = -1;
        public static int int_2 = -1;
        public static int int_3 = -1;
        public static int int_4 = -1;
    }

    public static class MessageType{
        public static final long MAX_VALUE_U_INT_32 = (long) Math.pow(2, 32) - 1;
        public static final int MAX_VALUE_U_INT_16 = (int) (Math.pow(2, 16) - 1);
        public static final int MAX_VALUE_U_INT_8 = (int) (Math.pow(2, 8) - 1);
    }

    public static class GatewayInfo{
        public static  String GatewayNo = "";
    }

    public static class DEVICE_GLOBAL{
        public static AppDevice sdkappDevice;
    }

    public static class GROUP_GLOBAL{
        public static String ADD_GROUP_NAME = "";
        public static String NEW_GROUP_NAME = "";
    }

    public static class SCENE_GLOBAL{
        public static String ADD_SCENE_NAME = "";
        public static short ADD_SCENE_GROUP_ID = -1;
        public static String NEW_SCENE_NAME = "";
    }

    public static void sendMessage(byte[] bt_send) throws Exception {
        DatagramSocket socket = null;
        InetAddress address = InetAddress.getByName(Constants.GW_IP_ADDRESS);
        if (socket == null) {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(Constants.UDP_PORT));
        }
        DatagramPacket dp = new DatagramPacket(bt_send, bt_send.length, address, Constants.UDP_PORT);
        socket.send(dp);
        System.out.println("SendMessage = " + Utils.binary(bt_send, 16));
        byte[] bs = new byte[1024];
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        socket.receive(packet);
        System.out.println("getMessage = " + Arrays.toString(bs));
    }


    public static String DeviceName(String device_id,String zone_type){
        String device_name = "";
        switch (device_id) {
            case "0105":
                device_name = "DimSwitch";
                break;
            case "0102":
                device_name = "Color lamp";
                break;
            case "0110":
                device_name = "Color temperature lamp";
                break;
            case "0210":
                device_name = "Color lamp";
                break;
            case "0200":
                device_name = "Color lamp";
                break;
            case "0220":
                device_name = "ColorTempJZGD";
                break;
            case "0100":
                device_name = "Dim lamp";
                break;
            case "0101":
                device_name = "Dim lamp";
                break;
            case "0402":
                device_name = DeviceZoneType(zone_type);
                break;
            case "0202":
                device_name = "Window Curtain";
                break;
            case "0309":
                device_name = "PM2dot5Sensor";
                break;
            case "0310":
                device_name = "Smoking Sensor";
                break;
            case "0820":
                device_name = "Lighting Remotes";
                break;
            case "0051":
                device_name = "Smart Socket";
                break;
            case "ffff":
                device_name = "unKnown";
                break;
        }
        return device_name;
    }

    public static String DeviceZoneType(String zoneType){
        String device_name = "";
        switch (zoneType) {
            case "0000":
                device_name = "StandardCIE";
                break;
            case "000d":
                device_name = "Motion Sensor";
                break;
            case "0015":
                device_name = "Door Contact Sensor";
                break;
            case "0028":
                device_name = "FireSensor";
                break;
            case "002a":
                device_name = "WaterSensor";
                break;
            case "002b":
                device_name = "GasSensor";
                break;
            case "010f":
                device_name = "Remote Control";
                break;
            case "021d":
                device_name = "Keypad";
                break;
            case "002d":
                device_name = "VibrationMovementSensor";
                break;
            case "ffff":
                device_name = "Unidentified";
                break;
        }

        return device_name;
    }
}

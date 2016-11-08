package com.core.global;

import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/14.
 */
public class Constants {
    public static int UDP_PORT = 8888;
    public static String MQTT_SERVER = "data.adurosmart.com";
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

    public static class GROUP_GLOBAL{
        public static String ADD_GROUP_NAME = "";
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
}

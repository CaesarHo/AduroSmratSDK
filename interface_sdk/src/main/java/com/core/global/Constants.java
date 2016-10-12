package com.core.global;

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
    }
}

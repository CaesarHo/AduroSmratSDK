package com.core.global;

/**
 * Created by best on 2016/7/14.
 */
public class Constants {
    public static String mMqttServer = "www.adurosmart.com";
    public static String ipaddress = "";
    public static boolean isGroupScene = false;
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
}

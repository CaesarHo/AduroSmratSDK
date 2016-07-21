package com.utils;

import android.util.Log;

import com.interfacecallback.Constants;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by best on 2016/7/14.
 */
public class Utils {
    public static byte ip_1[] = new byte[1];
    public static byte ip_2[] = new byte[1];
    public static byte ip_3[] = new byte[1];
    public static byte ip_4[] = new byte[1];

    public static String ConvertTimeByLong(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String intToIp(int i) {
        return (i & 0xFF ) + "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ( i >> 24 & 0xFF) ;
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * 十六进制字符串转换byte数组
     * @param str
     * @return
     */
    public static byte[] hexStringToByteArray(String str) {
        byte[] byteArray = new byte[str.length()/2];
        int len = byteArray.length;
        int j = 0;
        for (int i = 0; i<len; i++) {
            j = (i<<1);
            byteArray[i] = 0;
            char c = str.charAt(j);
            if ('0'<=c && c<='9') {
                byteArray[i] |= ((c-'0')<<4);
            } else if ('A'<=c && c<='F') {
                byteArray[i] |= ((c-'A'+10)<<4);
            } else if ('a'<=c && c<='f') {
                byteArray[i] |= ((c-'a'+10)<<4);
            } else {

            }
            j++;
            c = str.charAt(j);
            if ('0'<=c && c<='9') {
                byteArray[i] |= (c-'0');
            } else if ('A'<=c && c<='F') {
                byteArray[i] |= (c-'A'+10);
            } else if ('a'<=c && c<='f') {
                byteArray[i] |= (c-'a'+10);
            } else {

            }
        }
        return byteArray;
    }

    //截取分段IP地址
    public static void SplitToIp(String ipaddress){
        System.out.println(ipaddress);
        String[] s=ipaddress.split("\\.");
        for (String ss:s) {
            Log.i("out_ip_split", s[0]+s[1]+s[2]+s[3]);
            int s1 = Integer.parseInt(s[0]);
            int s2 = Integer.parseInt(s[1]);
            int s3 = Integer.parseInt(s[2]);
            int s4 = Integer.parseInt(s[3]);
            Constants.IpAddress.int_1 = s1;
            Constants.IpAddress.int_2 = s2;
            Constants.IpAddress.int_3 = s3;
            Constants.IpAddress.int_4 = s4;

            String str = s[0];
            byte[] bt = str.getBytes();
            String outStr = "";
            for (int k=0;k<str.length();k++){
                outStr = Integer.toHexString(bt[k]);
            }
            System.out.println("outStr"+Integer.parseInt(String.valueOf(s[0]),16));
        }
    }
}

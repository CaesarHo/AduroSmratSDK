package com.utils;

import android.util.Log;

import com.interfacecallback.Constants;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
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

    /**
     *  ByteBuffer 转换 String：
     */
    public static String ByteBufferToString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            //用这个的话，只能输出来一次结果，第二次显示为空
            // charBuffer = decoder.decode(buffer);
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF, 0xD9}
     * @param src
     *            String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"–> 0xEF
     * @param src0
     *            byte
     * @param src1
     *            byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String CrcToString(byte[] bt_send,int len){
        byte bt_crc82 = (byte) (CRC8.calc(bt_send,len)&0xFF);
        String hex = Integer.toHexString(bt_crc82 & 0xFF);
        return hex;
    }
}

package com.core.utils;

import android.graphics.Color;
import android.util.Log;

import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.global.MessageType;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.regex.Pattern;

/**
 * Created by best on 2016/7/14.
 */
public class Utils {
    public static final int INT = 100;
    public static byte ip_1[] = new byte[1];
    public static byte ip_2[] = new byte[1];
    public static byte ip_3[] = new byte[1];
    public static byte ip_4[] = new byte[1];

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String ConvertTimeByLong(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String getFormatTellDate(String time) {

        String year = "-";

        SimpleDateFormat sd = new SimpleDateFormat("yyyy" + year + "MM" + year + "dd" + "" + " HH:mm:ss");
        Date dt = null;
        try {
            dt = new Date(Long.parseLong(time));
        } catch (Exception e) {
        }

        String s = "";
        if (dt != null) {
            s = sd.format(dt);
        }

        return s;
    }

    public static int hsvToColor(int hue, int sat) {
        float[] call_ft = new float[3];
        call_ft[0] = hue / 255.0f * 360.0f;
        call_ft[1] = sat / 255.0f;
        call_ft[2] = 1.0f;

        int color = Color.HSVToColor(call_ft);
        System.out.println("call_color = " + color + "," + "call_ft[0] = " + call_ft[0] + "," + "call_ft[1] = " + call_ft[1]);
        return color;
    }

    /**
     * 计算命令长度
     *
     * @param serial_type
     * @return
     */
    public static int cDataByteLen(String serial_type) {
        int commandDataByte_Len = 0;
        if (serial_type != null) {
            switch (serial_type) {
                case "0092"://开关
                    //10 + 40
                    commandDataByte_Len = 50;
                    break;
                case "0081"://亮度
                    //10 + 43
                    commandDataByte_Len = 53;
                    break;
                case "00B6"://颜色
                    commandDataByte_Len = 54;
                    //10 + 44
                    break;
                case "00C0"://色温
                    commandDataByte_Len = 53;
                    //10 + 43
                    break;
                case "00A5"://调用场景
                    //10 + 42
                    commandDataByte_Len = 52;
                    break;
            }
        }

        return commandDataByte_Len;
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }


    /**
     * b >> 7 将原第8位的bit值移到了第1位上，& 0x1的作用是只保留第一位的值，其余7位与0与将为0
     *
     * @param b
     * @return
     */
    public static String byteToBit(byte b) {

        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)

                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)

                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)

                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);

    }

    /**
     * 十六进制字符串转换byte数组
     *
     * @param str
     * @return
     */
    public static byte[] hexStringToByteArray(String str) {
        byte[] byteArray = new byte[str.length() / 2];
        int len = byteArray.length;
        int j = 0;
        for (int i = 0; i < len; i++) {
            j = (i << 1);
            byteArray[i] = 0;
            char c = str.charAt(j);
            if ('0' <= c && c <= '9') {
                byteArray[i] |= ((c - '0') << 4);
            } else if ('A' <= c && c <= 'F') {
                byteArray[i] |= ((c - 'A' + 10) << 4);
            } else if ('a' <= c && c <= 'f') {
                byteArray[i] |= ((c - 'a' + 10) << 4);
            } else {

            }
            j++;
            c = str.charAt(j);
            if ('0' <= c && c <= '9') {
                byteArray[i] |= (c - '0');
            } else if ('A' <= c && c <= 'F') {
                byteArray[i] |= (c - 'A' + 10);
            } else if ('a' <= c && c <= 'f') {
                byteArray[i] |= (c - 'a' + 10);
            } else {

            }
        }
        return byteArray;
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    //截取分段IP地址
    public static void SplitToIp(String ipaddress) {
        System.out.println(ipaddress);
        String[] s = ipaddress.split("\\.");
        for (String ss : s) {
            Log.i("out_ip_split", s[0] + s[1] + s[2] + s[3]);
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
            for (int k = 0; k < str.length(); k++) {
                outStr = Integer.toHexString(bt[k]);
            }
            System.out.println("outStr" + Integer.parseInt(String.valueOf(s[0]), 16));
        }
    }

    /**
     * ByteBuffer 转换 String：
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
     *
     * @param src String
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
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将字节数组转换为String
     *
     * @param b byte[]
     * @return String
     */
    public static String bytesToString(byte[] b) {
        StringBuffer result = new StringBuffer("");
        int length = b.length;
        for (int i = 0; i < length; i++) {
            result.append((char) (b[i] & 0xff));
        }
        return result.toString();
    }

    public static String CrcToString(byte[] bt_send, int len) {
        byte bt_crc82 = (byte) (CRC8.calc(bt_send, len) & 0xFF);
        String hex = Integer.toHexString(bt_crc82 & 0xFF);
        return hex;
    }

    //判断CRC8结果长度是否为1
    public static boolean isCRC8Value(String crc8value) {

        if (crc8value.length() != 2) {
            return false;
        } else {
            return true;
        }
    }

    public static String StringToHexString(String string) {
        String result = "";
        switch (string) {
            case "a":
            case "A":
                result = "0a";
                break;

            case "b":
            case "B":
                result = "0b";
                break;

            case "c":
            case "C":
                result = "0c";
                break;

            case "d":
            case "D":
                result = "0d";
                break;

            case "e":
            case "E":
                result = "0e";
                break;

            case "f":
            case "F":
                result = "0f";
                break;
        }
        if (result.length() == 2) {
            return result;
        } else {
            return "0" + string;
        }
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        System.out.println("device_name_s = " + s.length());
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("device_name_s = " + baKeyword.length);
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("device_name_hex = " + Arrays.toString(baKeyword));
        return s.replaceAll("�","");
    }
}

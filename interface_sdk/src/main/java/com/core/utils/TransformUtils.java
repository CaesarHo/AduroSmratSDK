package com.core.utils;

import android.graphics.Color;

import com.core.global.Constants;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by best on 2016/6/24.
 */
public class TransformUtils {
    public static long byteArray2Long(byte[] a) {
        long res = 0L;
        int[] t = new int[8];
        for (int i = 0; i < 8; i++) {
            t[i] = a[7 - i];
        }
        res = t[0] & 0x0ff;
        for (int i = 1; i < 8; i++) {
            res <<= 8;
            res += (t[i] & 0x0ff);
        }
        return res;
    }

    /**
     * String 数字转为int
     *
     * @param string
     * @return
     */
    public static int string2Int(String string) {
        int a = -1;
        try {
            a = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * int数组转long
     *
     * @param a
     * @return
     */
    public static long intArray2Long(int[] a) {
        long res = 0L;
        int[] t = new int[8];
        for (int i = 0; i < 8; i++) {
            t[i] = a[7 - i];
        }
        res = t[0] & 0x0ff;
        for (int i = 1; i < 8; i++) {
            res <<= 8;
            res += (t[i] & 0x0ff);
        }
        return res;
    }

    /**
     * long型IP地址转换string IP地址
     *
     * @param longip
     * @return
     */
    public static String longtoip(long longip) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(longip >>> 24));// 直接右移24位
        sb.append(".");
        sb.append(String.valueOf((longip & 0x00ffffff) >>> 16)); // 将高8位置0，然后右移16位
        sb.append(".");
        sb.append(String.valueOf((longip & 0x0000ffff) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longip & 0x000000ff));
        return sb.toString();
    }

    /**
     * String IP 地址转为long型
     *
     * @param strip
     * @return
     */
    public static long iptolong(String strip) {
        // int j = 0;
        // int i = 0;
        long[] ip = new long[4];
        int position1 = strip.indexOf(".");
        int position2 = strip.indexOf(".", position1 + 1);
        int position3 = strip.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strip.substring(0, position1));
        ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strip.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]; // ip1*256*256*256+ip2*256*256+ip3*256+ip4
    }

    /**
     * int 型IP地址转String ip地址
     *
     * @param i
     * @return
     */
    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toHH(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将float转为低字节在前，高字节在后的byte数组
     */
    public static byte[] toLH(float f) {
        return toLH(Float.floatToRawIntBits(f));
    }

    /**
     * 将float转为高字节在前，低字节在后的byte数组
     */
    public static byte[] toHH(float f) {
        return toHH(Float.floatToRawIntBits(f));
    }

    /**
     * 将String转为byte数组
     */
    public static byte[] stringToBytes(String s, int length) {
        while (s.getBytes().length < length) {
            s += " ";
        }
        return s.getBytes();
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

    /**
     * 将字符串转换为byte数组
     *
     * @param s String
     * @return byte[]
     */
    public static byte[] stringToBytes(String s)
            throws UnsupportedEncodingException {
        return s.getBytes("UTF-8");
    }

    /**
     * 将高字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int hBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] >= 0) {
                s = s + b[i];
            } else {
                s = s + 256 + b[i];
            }
            s = s * 256;
        }
        if (b[3] >= 0) {
            s = s + b[3];
        } else {
            s = s + 256 + b[3];
        }
        return s;
    }

    /**
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }

    /**
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(int[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }

    /**
     * 高字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short hBytesToShort(byte[] b) {
        int s = 0;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        s = s * 256;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 低字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short lBytesToShort(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 低字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short lBytesToShort(int[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 高字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    @SuppressWarnings("static-access")
    public static float hBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8
                | (b[3] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    @SuppressWarnings("static-access")
    public static float lBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8
                | (b[0] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    @SuppressWarnings("static-access")
    public static float lBytesToFloat(int[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8
                | (b[0] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 将byte数组中的元素倒序排列
     */
    public static byte[] bytesReverseOrder(byte[] b) {
        int length = b.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[length - i - 1] = b[i];
        }
        return result;
    }

    /**
     * 打印byte数组
     */
    public static void printBytes(byte[] bb) {
        int length = bb.length;
        for (int i = 0; i < length; i++) {
            System.out.print(bb + " ");
        }
        System.out.println("");
    }

    public static void logBytes(byte[] bb) {
        int length = bb.length;
        String out = "";
        for (int i = 0; i < length; i++) {
            out = out + bb + " ";
        }

    }

    /**
     * 将int类型的值转换为字节序颠倒过来对应的int值
     *
     * @param i int
     * @return int
     */
    public static int reverseInt(int i) {
        int result = TransformUtils.hBytesToInt(TransformUtils.toLH(i));
        return result;
    }

    /**
     * 将short类型的值转换为字节序颠倒过来对应的short值
     *
     * @param s short
     * @return short
     */
    public static short reverseShort(short s) {
        short result = TransformUtils.hBytesToShort(TransformUtils.toLH(s));
        return result;
    }

    /**
     * 将float类型的值转换为字节序颠倒过来对应的float值
     *
     * @param f float
     * @return float
     */
    public static float reverseFloat(float f) {
        float result = TransformUtils.hBytesToFloat(TransformUtils.toLH(f));
        return result;
    }

    /**
     * 此方法将参数i 转换为 num bytes的byte 数组 (小端模式)
     *
     * @param i
     * @param num
     * @return
     */
    public static byte[] int2Array(Long i, int num) {
        byte[] a = new byte[num];
        for (int j = 0; j < a.length; j++) {
            a[j] = (byte) (i & 0xff);
            i >>= 8;
        }
        byte[] cc = new byte[a.length];
        for (int x = 0; x < a.length; x++) {
            cc[x] = a[x];
        }
        return cc;
    }

    /**
     * 将16位的short转换成byte数组
     *
     * @param s short
     * @return byte[] 长度为2
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] floatTobyte_LH(float f) {
        return intTobyte_LH(Float.floatToRawIntBits(f));
    }

    public static byte[] intTobyte_LH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static byte[] shortTobyte_LH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    public static String bytesToUTF8String(byte[] b) {
        String xmlUTF8 = "";
        try {
            xmlUTF8 = new String(b, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        xmlUTF8 = xmlUTF8.trim();
        return xmlUTF8;
    }

    public static String byteToASCIIString(byte[] bt) {
        String ascii_str = "";
        try {
            ascii_str = new String(bt, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ascii_str = ascii_str.trim();
        return ascii_str;
    }

    public static int byteToint_LH(byte[] b) {
        int ret = b[3];
        ret = (ret << 8) | ((int) (b[2]) & 0xff);
        ret = (ret << 8) | ((int) (b[1]) & 0xff);
        ret = (ret << 8) | ((int) (b[0]) & 0xff);
        return ret;
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    /**
     * 十六进制byte转int
     * byte[]->int
     */
    public final static int getInt(byte[] buf, boolean asc) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (asc)
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        else
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        return r;
    }

    /**
     * HSV转至可直接用颜色值
     *
     * @param hue
     * @param sat
     * @return
     */
    public static int hsvToColor(int hue, int sat) {
        float[] call_ft = new float[3];
        call_ft[0] = hue / 255.0f * 360.0f;
        call_ft[1] = sat / 255.0f;
        call_ft[2] = 1.0f;

        int color = Color.HSVToColor(call_ft);
        return color;
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
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
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

    /**
     * 分割IP地址
     *
     * @param ipaddress
     */
    public static void SplitToIp(String ipaddress) {
        System.out.println(ipaddress);
        String[] s = ipaddress.split("\\.");
        for (String ss : s) {
            int s1 = Integer.parseInt(s[0]);
            int s2 = Integer.parseInt(s[1]);
            int s3 = Integer.parseInt(s[2]);
            int s4 = Integer.parseInt(s[3]);
            Constants.IpAddress.int_1 = s1;
            Constants.IpAddress.int_2 = s2;
            Constants.IpAddress.int_3 = s3;
            Constants.IpAddress.int_4 = s4;
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
     * 转化十六进制编码为字符串
     *
     * @param s
     * @return
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s.replaceAll("�", "");
    }

    /**
     * CRC8转String
     * @param bt_send
     * @param len
     * @return
     */
    public static String CrcToString(byte[] bt_send, int len) {
        byte bt_crc82 = (byte) (CRC8Utils.calc(bt_send, len) & 0xFF);
        String hex = Integer.toHexString(bt_crc82 & 0xFF);
        return hex;
    }

    /**
     * 判断CRC8结果长度是否为1
     * @param crc8value
     * @return
     */
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

            case "1":
                result = "01";
                break;

            case "2":
                result = "02";
                break;

            case "3":
                result = "03";
                break;

            case "4":
                result = "04";
                break;

            case "5":
                result = "05";
                break;

            case "6":
                result = "06";
                break;
            case "7":
                result = "07";
                break;
            case "8":
                result = "08";
                break;
            case "9":
                result = "09";
                break;

        }
        if (result.length() == 2) {
            return result;
        } else {
            return "0" + string;
        }
    }
}

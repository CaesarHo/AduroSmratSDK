package com.adurosmart.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by best on 2016/6/24.
 */
public class FtFormatTransfer {
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

    public static String getLocalhostip() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

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
     * 将int转为低字节在前，高字节在后的byte数组
     *
     * @param n
     *            int
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
     * @param n
     *            int
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
     * @param n
     *            short
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
     * @param n
     *            short
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
     * @param b
     *            byte[]
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
     * @param s
     *            String
     * @return byte[]
     */
    public static byte[] stringToBytes(String s)
            throws UnsupportedEncodingException {
        return s.getBytes("GB2312");
    }

    /**
     * 将高字节数组转换为int
     *
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param b
     *            byte[]
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
     * @param i
     *            int
     * @return int
     */
    public static int reverseInt(int i) {
        int result = FtFormatTransfer.hBytesToInt(FtFormatTransfer.toLH(i));
        return result;
    }

    /**
     * 将short类型的值转换为字节序颠倒过来对应的short值
     *
     * @param s
     *            short
     * @return short
     */
    public static short reverseShort(short s) {
        short result = FtFormatTransfer.hBytesToShort(FtFormatTransfer.toLH(s));
        return result;
    }

    /**
     * 将float类型的值转换为字节序颠倒过来对应的float值
     *
     * @param f
     *            float
     * @return float
     */
    public static float reverseFloat(float f) {
        float result = FtFormatTransfer.hBytesToFloat(FtFormatTransfer.toLH(f));
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
     * @param s
     *            short
     * @return byte[] 长度为2
     * */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger1(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger2(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger3(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
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

    public static int byteToint_LH(byte[] b) {
        int ret = b[3];
        ret = (ret << 8) | ((int) (b[2]) & 0xff);
        ret = (ret << 8) | ((int) (b[1]) & 0xff);
        ret = (ret << 8) | ((int) (b[0]) & 0xff);
        return ret;
    }
}

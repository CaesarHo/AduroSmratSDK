package com.core.utils;

import java.io.IOException;
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
        int result = FtFormatTransfer.hBytesToInt(FtFormatTransfer.toLH(i));
        return result;
    }

    /**
     * 将short类型的值转换为字节序颠倒过来对应的short值
     *
     * @param s short
     * @return short
     */
    public static short reverseShort(short s) {
        short result = FtFormatTransfer.hBytesToShort(FtFormatTransfer.toLH(s));
        return result;
    }

    /**
     * 将float类型的值转换为字节序颠倒过来对应的float值
     *
     * @param f float
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
     * 将Ascii转换成中文字符串
     */
//    public static String AsciiToChineseString(String s) {
//        if (s == null)
//            return s;
//        char[] orig = s.toCharArray();
//        byte[] dest = new byte[orig.length];
//        for (int i = 0; i < orig.length; i++)
//            dest[i] = (byte) (orig[i] & 0xFF);
//        try {
//            ByteToCharConverter toChar = ByteToCharConverter.getConverter("gb2312");
//            return new String(toChar.convertAll(dest));
//        } catch (Exception e) {
//            System.out.println(e);
//            return s;
//        }
//    }

    /**
     * 将中文字符串转换成Ascii
     */
//    public static String ChineseStringToAscii(String s) {
//        if (s == null)
//            return s;
//        try {
//            CharToByteConverter toByte = CharToByteConverter.getConverter("gb2312");
//            byte[] orig = toByte.convertAll(s.toCharArray());
//            char[] dest = new char[orig.length];
//            for (int i = 0; i < orig.length; i++)
//                dest[i] = (char) (orig[i] & 0xFF);
//            return new String(dest);
//        } catch (Exception e) {
//            System.out.println(e);
//            return s;
//        }
//    }

    /**
     * 中文转ascii
     *
     * @param s  要进行转换的字符串
     * @param bl 是否进行转换,一个开关控制 , true代表需要转换。
     * @return 转换后的字符串
     */
//    public static String ChineseStringToAscii(String s, boolean bl) {
//        if (!bl)
//            return s;
//        else
//            return ChineseStringToAscii(s);
//    }

    /**
     * ascii转字符串
     *
     * @param s
     * @param bl
     * @return
     */
//    public static String AsciiToChineseString(String s, boolean bl) {
//        if (!bl)
//            return s;
//        else
//            return AsciiToChineseString(s);
//    }

    /**
     * 根据输入的源串(中文或中西文混合)返回其拼音首字母,以小写返回,如果首字符非拼音字母,则统一返回*号
     *
     * @param str 源串(中文或中西文混合)
     * @return 返回str的拼音首字母, 以小写返回, 如果首字符非拼音字母, 则统一返回*号
     */
    public static String getFirstCharOfString(String str) {
        String firstChar = "*";
        if (str == null || str.length() <= 0)
            return firstChar;
        try {
            byte firstCharBytes[] = new byte[2];
            int gbcode;
            firstCharBytes[0] = str.getBytes("gb2312")[0];
            gbcode = firstCharBytes[0] & 0x000000ff;
            if (str.length() > 1 || gbcode >= 0xb0) {
                firstCharBytes[1] = str.getBytes("gb2312")[1];
                gbcode = (firstCharBytes[0] & 0x000000ff) * 0x100 + (firstCharBytes[1] & 0x000000ff);
            }
            if (gbcode >= 0xb0a1 && gbcode <= 0xb0c4)
                firstChar = "a";
            else if (gbcode >= 0xb0c5 && gbcode <= 0xb2c0)
                firstChar = "b";
            else if (gbcode >= 0xb2c1 && gbcode <= 0xb4ed)
                firstChar = "c";
            else if (gbcode >= 0xb4ee && gbcode <= 0xb6e9)
                firstChar = "d";
            else if (gbcode >= 0xb6ea && gbcode <= 0xb7a1)
                firstChar = "e";
            else if (gbcode >= 0xb7a2 && gbcode <= 0xb8c0)
                firstChar = "f";
            else if (gbcode >= 0xb8c1 && gbcode <= 0xb9fd)
                firstChar = "g";
            else if (gbcode >= 0xb9fe && gbcode <= 0xbbf6)
                firstChar = "h";
            else if (gbcode >= 0xbbf7 && gbcode <= 0xbfa5)
                firstChar = "j";
            else if (gbcode >= 0xbfa6 && gbcode <= 0xc0ab)
                firstChar = "k";
            else if (gbcode >= 0xc0ac && gbcode <= 0xc2e7)
                firstChar = "l";
            else if (gbcode >= 0xc2e8 && gbcode <= 0xc4c2)
                firstChar = "m";
            else if (gbcode >= 0xc4c3 && gbcode <= 0xc5b5)
                firstChar = "n";
            else if (gbcode >= 0xc5b6 && gbcode <= 0xc5bd)
                firstChar = "o";
            else if (gbcode >= 0xc5be && gbcode <= 0xc6d9)
                firstChar = "p";
            else if (gbcode >= 0xc6da && gbcode <= 0xc8ba)
                firstChar = "q";
            else if (gbcode >= 0xc8bb && gbcode <= 0xc8f5)
                firstChar = "r";
            else if (gbcode >= 0xc8f6 && gbcode <= 0xcbf9)
                firstChar = "s";
            else if (gbcode >= 0xcbfa && gbcode <= 0xcdd9)
                firstChar = "t";
            else if (gbcode >= 0xcdda && gbcode <= 0xcef3)
                firstChar = "w";
            else if (gbcode >= 0xcef4 && gbcode <= 0xd1b8)
                firstChar = "x";
            else if (gbcode >= 0xd1b9 && gbcode <= 0xd4d0)
                firstChar = "y";
            else if (gbcode >= 0xd4d1 && gbcode <= 0xd7f9)
                firstChar = "z";
            else
                gbcode = firstCharBytes[0];
            if (gbcode >= 'A' && gbcode <= 'Z')
                gbcode += 32;
            if (gbcode >= 'a' && gbcode <= 'z')
                firstChar = String.valueOf((char) gbcode);
        } catch (Exception e) {
            System.out.println("getFirstCharOfString Exception: " + e.getMessage());
        }
        return firstChar;
    }
}

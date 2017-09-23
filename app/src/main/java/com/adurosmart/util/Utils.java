package com.adurosmart.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.adurosmart.MyApp;
import com.core.gatewayinterface.SerialHandler;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by best on 2016/7/14.
 */
public class Utils {
    private static Utils manager = null;
    public static byte ip_1[] = new byte[1];
    public static byte ip_2[] = new byte[1];
    public static byte ip_3[] = new byte[1];
    public static byte ip_4[] = new byte[1];

    private Utils() {

    }

    public synchronized static Utils getInstance() {
        if (null == manager) {
            synchronized (Utils.class) {
                manager = new Utils();
            }
        }
        return manager;
    }

    public static String getFormatTellDate(String time) {

        String year = "-";

        SimpleDateFormat sd = new SimpleDateFormat("yyyy" + year + "MM" + year + "dd" + year + " HH_mm_ss");
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

    public static String ConvertTimeByLong(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }


    public static String convertDeviceTime(int iYear, int iMonth, int iDay, int iHour, int iMinute, int iSecond) {
        System.out.println("当前时间 = " + iYear);
        System.out.println("当前时间 = " + iMonth);
        System.out.println("当前时间 = " + iDay);
        System.out.println("当前时间 = " + iHour);
        System.out.println("当前时间 = " + iMinute);
        System.out.println("当前时间 = " + iSecond);

        int year = (2000 + iYear);
        int month = iMonth;
        int day = iDay;
        int hour = iHour;
        int minute = iMinute;
        int second = iSecond;

        StringBuilder sb = new StringBuilder();
        sb.append(year + "-");
        System.out.println("year = " + year);

        if (month < 10) {
            sb.append("0" + month + "-");
        } else {
            sb.append(month + "-");
        }
        System.out.println("month = " + month);

        if (day < 10) {
            sb.append("0" + day + " ");
        } else {
            sb.append(day + " ");
        }
        System.out.println("day = " + day);

        if (hour < 10) {
            sb.append("0" + hour + ":");
        } else {
            sb.append(hour + ":");
        }
        System.out.println("hour = " + hour);

        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append("" + minute);
        }

        if (second < 10) {
            sb.append(":" + "0" + second);
        } else {
            sb.append(":" + second);
        }
        SerialHandler.getInstance().setGateWayTime(year, month, day, hour, minute, second);
        System.out.println("minute = " + minute);
        return sb.toString();
    }

    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String convertPlanTime(int hour_from, int minute_from, int hour_to, int minute_to) {

        StringBuilder sb = new StringBuilder();

        if (hour_from < 10) {
            sb.append("0" + hour_from + ":");
        } else {
            sb.append(hour_from + ":");
        }

        if (minute_from < 10) {
            sb.append("0" + minute_from + "-");
        } else {
            sb.append(minute_from + "-");
        }

        if (hour_to < 10) {
            sb.append("0" + hour_to + ":");
        } else {
            sb.append(hour_to + ":");
        }

        if (minute_to < 10) {
            sb.append("0" + minute_to);
        } else {
            sb.append("" + minute_to);
        }

        return sb.toString();
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
     * int i=Utils.parse(str);
     * System.out.println("int:"+i);
     * String hex=Integer.toHexString(i);
     * System.out.println("Hex:"+hex);
     *
     * @param s
     * @return
     * @throws NumberFormatException
     */
    public static int parse(String s) throws NumberFormatException {
        if (!s.startsWith("0x"))
            throw new NumberFormatException();
        int number = 0, n = 0;
        for (int i = 2; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '1':
                    n = 1;
                    break;
                case '2':
                    n = 2;
                    break;
                case '3':
                    n = 3;
                    break;
                case '4':
                    n = 4;
                    break;
                case '5':
                    n = 5;
                    break;
                case '6':
                    n = 6;
                    break;
                case '7':
                    n = 7;
                    break;
                case '8':
                    n = 8;
                    break;
                case '9':
                    n = 9;
                    break;
                case '0':
                    n = 0;
                    break;
                case 'a':
                case 'A':
                    n = 10;
                    break;
                case 'b':
                case 'B':
                    n = 11;
                    break;
                case 'c':
                case 'C':
                    n = 12;
                    break;
                case 'd':
                case 'D':
                    n = 13;
                    break;
                case 'e':
                case 'E':
                    n = 14;
                    break;
                case 'f':
                case 'F':
                    n = 15;
                    break;
                default:
                    throw new NumberFormatException();
            }
            number = number * 16 + n;
        }
        return number;
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

    public static int byteArr2Int(byte[] arrB) {
        if (arrB == null || arrB.length != 4) {
            return 0;
        }
        int i = (arrB[0] << 24) + (arrB[1] << 16) + (arrB[2] << 8) + arrB[3];
        return i;
    }


    //	 转化十六进制编码为字符串
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
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    // 转化十六进制编码为字符串
    public static String toStringHex2(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public void Timer(Handler handler, long time, boolean isRun) {
        Timer timer = new Timer();
        TimerTask timerTask = new timerTask(handler, isRun);
        timer.schedule(timerTask, time, time);
        if (!isRun) {
            timer.cancel();
            Log.i("message.what = 2", "计时取消");
        }
    }

    public class timerTask extends TimerTask {
        private Handler handler;
        private boolean isRun;

        public timerTask(Handler handler, boolean isRun) {
            this.handler = handler;
            this.isRun = isRun;
        }

        @Override
        public void run() {
            Message message = new Message();
            if (isRun) {
                message.what = 1;
                Log.i("message.what = 1", "");
            } else {
                Log.i("message.what = 2", "");
                message.what = 2;
            }

            handler.sendMessage(message);
        }
    }

    ;

    /**
     * 十六进制转换字符串
     *
     * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    public static int byte2int(byte[] res) {
// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    public static void hindKeyBoard(View btnKey) {
        InputMethodManager imm = (InputMethodManager) MyApp.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(btnKey.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

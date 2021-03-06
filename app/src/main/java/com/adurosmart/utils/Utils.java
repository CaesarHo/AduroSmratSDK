package com.adurosmart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Utils {

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
    }

    // yyyy-MM-dd HH:mm:ss ->>>>>>> yyyy-MM-dd HH:mm
    public static String ConvertTimeByString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
            sdf.applyPattern("yyyy-MM-dd HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf.format(date);
    }

    public static String ConvertTimeByLong(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static Bitmap montageBitmap(Bitmap frame, Bitmap src, int x, int y) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap sizeFrame = Bitmap.createScaledBitmap(frame, w, h, true);

        Bitmap newBM = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBM);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(sizeFrame, 0, 0, null);
        return newBM;
    }

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    public static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xff) + "." + ((ip >> 16) & 0xff) + "." + ((ip >> 24) & 0xff);
    }

    public static HashMap getHash(String string) {
        try {
            HashMap map = new HashMap<String, String>();
            String[] item = string.split(",");
            for (int i = 0, len = item.length; i < len; i++) {
                String[] info = item[i].split(":");
                map.put("" + info[0], info[1]);
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String[] getDeleteAlarmIdArray(String[] data, int position) {
        if (data.length == 1) {
            return new String[]{"0"};
        }

        String[] array = new String[data.length - 1];
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (position != i) {
                array[count] = data[i];
                count++;
            }
        }
        return array;
    }

    public static String convertDeviceTime(int iYear, int iMonth, int iDay, int iHour, int iMinute) {
        int year = (2000 + iYear);
        int month = iMonth;
        int day = iDay;
        int hour = iHour;
        int minute = iMinute;

        StringBuilder sb = new StringBuilder();
        sb.append(year + "-");

        if (month < 10) {
            sb.append("0" + month + "-");
        } else {
            sb.append(month + "-");
        }

        if (day < 10) {
            sb.append("0" + day + " ");
        } else {
            sb.append(day + " ");
        }

        if (hour < 10) {
            sb.append("0" + hour + ":");
        } else {
            sb.append(hour + ":");
        }

        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append("" + minute);
        }
        return sb.toString();
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

    public static boolean isNumeric(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static boolean isWeakPassword(String passWord) {
        if (getPassWordStatus(passWord) < 2) {
            return true;
        }
        return false;
    }

    /**
     * 获取密码强弱标记
     *
     * @param password
     * @return
     */
    private static String WEAK = "^[1-9]\\d*$|^[A-Z]+$|^[a-z]+$|^(.)\\1+$";
    private static String MID = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$";
    private static String STRONG = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,100}$";

    public static int getPassWordStatus(String password) {
        if (password.length() == 0) {
            return 0;
        } else if (password.length() < 6 || isRuo(password)) {
            return 1;
        }
        if (password.matches(MID)) {
            if (password.matches(STRONG)) {
                return 3;
            }
            return 2;
        }
        return -1;
    }

    /**
     * 是否弱密码
     *
     * @param password
     * @return
     */
    private static boolean isRuo(String password) {
        if (password.matches(WEAK)) {
            return true;
        }
        return false;
    }

    public static InetAddress getIntentAddress(Context mContext) throws IOException {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public static String getIntentAddress(Context mContext, String deviceID) throws IOException {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int address = wifiInfo.getIpAddress();
        String ip = intToIp(address);
        String ipAddress = ip.substring(0, ip.lastIndexOf(".") + 1) + deviceID;
        Log.e("dxsipaddress", "ip=" + ip + "--" + "ipAddress=" + ipAddress);
        return ipAddress;
    }

    /**
     * byte[]以16进制字符串输出
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("[");
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
            if (i != src.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * 检测邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmial(String email) {
        // String str="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String str =
                // "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
                "^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})";
        Pattern pattern = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        // Matcher matcher = Pattern.compile(regEx).matcher(email);
        return matcher.matches();
    }

    /**
     * WiFi是否加密
     *
     * @param result
     * @return
     */
    public static boolean isWifiOpen(ScanResult result) {
        if (result.capabilities.toLowerCase().indexOf("wep") != -1 || result.capabilities.toLowerCase().indexOf("wpa") != -1) {
            return false;
        }
        return true;
    }

    /**
     * 创建录像根目录 ~/videorecode
     */
    public static File createRecodeFile() {
        if (!isSD()) {
            return null;
        }
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/videorecode";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (file == null) {
            Log.e("Utils", "create Recoding file failed");
        }
        return file;
    }

    /**
     * 判断手机SD卡是否插入
     *
     * @return
     */
    public static boolean isSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置录像文件名（~/videorecoder/callId/callId_yyyy_MM_dd_HH_mm_ss.MP4）
     *
     * @param callId
     * @return
     */
    public static String getVideoRecodeName(String callId) {
        if (!isSD()) {
            return "noSD";
        }
        long time = System.currentTimeMillis();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/videorecode/" + callId;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");// 制定日期的显示格式
        String name = callId + "_" + sdf.format(new Date(time));
        String filename = file.getPath() + "/" + name + ".MP4";
        return filename;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
     */
    public static int[] intToBytes2(int value) {
        int[] src = new int[4];
        src[3] = value & 0xFF;
        src[2] = (value >> 8) & 0xFF;
        src[1] = (value >> 16) & 0xFF;
        src[0] = (value >> 24) & 0xFF;

        return src;
    }

    /**
     * DES加密
     *
     * @param datasource
     * @param password
     * @return
     */
    public static byte[] desCrypto(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param src
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return new String(cipher.doFinal(src));
    }

    public static byte[] gainWifiMode() {
        byte[] data = new byte[20];
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
        return data;
    }

    public static byte[] setDeviceApWifiPwd(String pwd) {
        byte[] data = new byte[272];
        for (int i = 0; i < data.length; i++) {
            data[0] = 0;
        }
        data[0] = 2;
        data[8] = 1;
        byte[] password = pwd.getBytes();
        for (int j = 0; j < password.length; j++) {
            data[j + 144] = password[j];
        }
        return data;

    }

    public static class FileInfo {
        long LastModified;
        String path;
    }

    public static class FileComparatorDown implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo file1, FileInfo file2) {
            if (file1.LastModified < file2.LastModified) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static class FileComparatorUp implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo file1, FileInfo file2) {
            if (file1.LastModified < file2.LastModified) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static byte[] btop = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01, 0x00,
            0x08, 0x00, (byte) 0xff, 0x08};// 上
    public static byte[] bbottom = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x00, 0x10, 0x00, (byte) 0xff, 0x10};// 下
    public static byte[] bleft = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x00, 0x04, (byte) 0xff, 0x00, 0x04};// 左
    public static byte[] bright = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x00, 0x02, (byte) 0xff, 0x00, 0x02,};// 右
    public static byte[] zoom_small = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x00, 0x20, 0x00, 0x00, 0x21};// 变倍短
    public static byte[] zoom_big = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x00, 0x40, 0x00, 0x00, 0x41};// 变倍长
    public static byte[] focus_small = {(byte) 130, 1, 7, 0, (byte) 0xff,
            0x01, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0x81};// 聚焦近
    public static byte[] focus_big = {(byte) 130, 1, 7, 0, (byte) 0xff, 0x01,
            0x01, 0x00, 0x00, 0x00, 0x02};// 聚焦远
    public static byte[] aperture_smal = {(byte) 130, 1, 7, 0, (byte) 0xff,
            0x01, 0x02, 0x00, 0x00, 0x00, 0x03};// 光圈小
    public static byte[] aperture_big = {(byte) 130, 1, 7, 0, (byte) 0xff,
            0x01, 0x04, 0x00, 0x00, 0x00, 0x05};// 光圈大

    public static boolean checkPassword(String password) {
        if (password.length() < 10 && Utils.isNumeric(password) && password.charAt(0) == '0') {
            return false;
        } else {
            return true;
        }
    }

    public static String getWifiName(String wifiname) {
        if (wifiname != null && wifiname.length() > 2) {
            int first = wifiname.charAt(0);
            if (first == 34) {
                wifiname = wifiname.substring(1, wifiname.length() - 1);
            }
        }
        return wifiname;
    }
}

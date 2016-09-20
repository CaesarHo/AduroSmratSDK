package com.utils;

import android.content.Context;
import android.graphics.Color;
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
    public static final int INT = 100;
    public static byte ip_1[] = new byte[1];
    public static byte ip_2[] = new byte[1];
    public static byte ip_3[] = new byte[1];
    public static byte ip_4[] = new byte[1];

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
    public static String toStringHex2(String s) {
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
        return s;
    }

    public static byte[] tCmdData(String device_mac, String device_shortaddr, String device_main_point,
                                  String dev_switch, int switch_state, String dev_level, int level_value,
                                  String dev_hue, int hue_value, int sat_value, String dev_temp, int temp_value,
                                  String recall_scene, int group_id, int scene_id) {
        int switch_cmd_len = 0;
        if (dev_switch != null) {
            switch_cmd_len = 50;
        }
        if (dev_switch != null & dev_level == null & dev_hue == null & dev_temp == null & recall_scene == null) {
            switch_cmd_len = 57;
        }
        System.out.println("switch_cmd_len = " + switch_cmd_len);

        byte[] switch_cmd = new byte[switch_cmd_len];
        if (dev_switch != null) {
            switch_cmd[0] = 0x63;
            switch_cmd[1] = 0x6f;
            switch_cmd[2] = 0x6d;
            switch_cmd[3] = 0x6d;
            switch_cmd[4] = 0x61;
            switch_cmd[5] = 0x6e;
            switch_cmd[6] = 0x64;

            switch_cmd[7] = 0x00;
            byte[] cmdNo_1 = NewCmdData.DevSwitchCmd(device_mac, device_shortaddr, device_main_point, switch_state);
            int cmd_switch_len = cmdNo_1.length;
            switch_cmd[8] = ((byte) (cmd_switch_len << 8));
            switch_cmd[9] = ((byte) cmd_switch_len);

            for (int i = 0; i < cmd_switch_len; i++) {
                switch_cmd[(10 + i)] = cmdNo_1[i];
            }

            if (dev_level == null & dev_hue == null & dev_temp == null & recall_scene == null) {
                switch_cmd[(10 + cmdNo_1.length)] = 0x63;
                switch_cmd[(11 + cmdNo_1.length)] = 0x6f;
                switch_cmd[(12 + cmdNo_1.length)] = 0x6d;
                switch_cmd[(13 + cmdNo_1.length)] = 0x6d;
                switch_cmd[(14 + cmdNo_1.length)] = 0x61;
                switch_cmd[(15 + cmdNo_1.length)] = 0x6e;
                switch_cmd[(16 + cmdNo_1.length)] = 0x64;
            }

        }

        int level_cmd_len = 0;
        if (dev_level != null) {
            level_cmd_len = 53;
        }
        if (dev_level != null & dev_hue == null & dev_temp == null & recall_scene == null) {
            level_cmd_len = 60;
        }
        System.out.println("level_cmd_len = " + level_cmd_len);
        byte[] level_cmd = new byte[level_cmd_len];
        if (dev_level != null) {
            level_cmd[0] = 0x63;
            level_cmd[1] = 0x6f;
            level_cmd[2] = 0x6d;
            level_cmd[3] = 0x6d;
            level_cmd[4] = 0x61;
            level_cmd[5] = 0x6e;
            level_cmd[6] = 0x64;

            level_cmd[7] = 0x01;

            byte[] cmdNo_2 = NewCmdData.setDeviceLevelCmd(device_mac, device_shortaddr, device_main_point, level_value);

            int cmd_level_len = cmdNo_2.length;
            System.out.println("cmd_level_len2 = " + cmd_level_len);
            level_cmd[8] = ((byte) (cmd_level_len << 8));
            level_cmd[9] = ((byte) cmd_level_len);
            for (int i = 0; i < cmd_level_len; i++) {
                level_cmd[(10 + i)] = cmdNo_2[i];
            }

            if (dev_hue == null & dev_temp == null & recall_scene == null) {
                level_cmd[(10 + cmdNo_2.length)] = 0x63;
                level_cmd[(11 + cmdNo_2.length)] = 0x6f;
                level_cmd[(12 + cmdNo_2.length)] = 0x6d;
                level_cmd[(13 + cmdNo_2.length)] = 0x6d;
                level_cmd[(14 + cmdNo_2.length)] = 0x61;
                level_cmd[(15 + cmdNo_2.length)] = 0x6e;
                level_cmd[(16 + cmdNo_2.length)] = 0x64;
            }

        }

        int hue_cmd_len = 0;
        if (dev_hue != null) {
            hue_cmd_len = 54;
        }
        if (dev_hue != null & dev_temp == null & recall_scene == null) {
            hue_cmd_len = 61;
        }
        System.out.println("hue_cmd_len = " + hue_cmd_len);
        byte[] hue_cmd = new byte[hue_cmd_len];
        if (dev_hue != null) {
            hue_cmd[0] = 0x63;
            hue_cmd[1] = 0x6f;
            hue_cmd[2] = 0x6d;
            hue_cmd[3] = 0x6d;
            hue_cmd[4] = 0x61;
            hue_cmd[5] = 0x6e;
            hue_cmd[6] = 0x64;

            hue_cmd[7] = 0x02;

            byte[] cmdNo_3 = NewCmdData.setDeviceHueSatCmd(device_mac, device_shortaddr, device_main_point, hue_value, sat_value);

            int cmd_hue_sat_len = cmdNo_3.length;
            System.out.println("cmd_hue_sat_len3 = " + cmd_hue_sat_len);
            hue_cmd[8] = ((byte) (cmd_hue_sat_len << 8));
            hue_cmd[9] = ((byte) cmd_hue_sat_len);
            for (int i = 0; i < cmd_hue_sat_len; i++) {
                hue_cmd[(10 + i)] = cmdNo_3[i];
            }

            if (dev_temp == null & recall_scene == null) {
                hue_cmd[(10 + cmdNo_3.length)] = 0x63;
                hue_cmd[(11 + cmdNo_3.length)] = 0x6f;
                hue_cmd[(12 + cmdNo_3.length)] = 0x6d;
                hue_cmd[(13 + cmdNo_3.length)] = 0x6d;
                hue_cmd[(14 + cmdNo_3.length)] = 0x61;
                hue_cmd[(15 + cmdNo_3.length)] = 0x6e;
                hue_cmd[(16 + cmdNo_3.length)] = 0x64;
            }

        }

        int temp_cmd_len = 0;
        if (dev_temp != null) {
            temp_cmd_len = 53;
        }
        if (dev_temp != null & recall_scene == null) {
            temp_cmd_len = 60;
        }
        System.out.println("temp_cmd_len = " + temp_cmd_len);
        byte[] temp_cmd = new byte[temp_cmd_len];
        if (dev_temp != null) {
            temp_cmd[0] = 0x63;
            temp_cmd[1] = 0x6f;
            temp_cmd[2] = 0x6d;
            temp_cmd[3] = 0x6d;
            temp_cmd[4] = 0x61;
            temp_cmd[5] = 0x6e;
            temp_cmd[6] = 0x64;

            temp_cmd[7] = 0x03;

            byte[] cmdNo_4 = NewCmdData.setDeviceColorsTemp(device_mac, device_shortaddr, device_main_point, temp_value);

            int cmd_colors_temp_len = cmdNo_4.length;
            System.out.println("cmdNo_len = " + cmd_colors_temp_len);
            temp_cmd[8] = ((byte) (cmd_colors_temp_len << 8));
            temp_cmd[9] = ((byte) cmd_colors_temp_len);
            for (int i = 0; i < cmd_colors_temp_len; i++) {
                temp_cmd[(10 + i)] = cmdNo_4[i];
            }

            if (recall_scene == null) {
                temp_cmd[(10 + cmdNo_4.length)] = 0x63;
                temp_cmd[(11 + cmdNo_4.length)] = 0x6f;
                temp_cmd[(12 + cmdNo_4.length)] = 0x6d;
                temp_cmd[(13 + cmdNo_4.length)] = 0x6d;
                temp_cmd[(14 + cmdNo_4.length)] = 0x61;
                temp_cmd[(15 + cmdNo_4.length)] = 0x6e;
                temp_cmd[(16 + cmdNo_4.length)] = 0x64;
            }

        }

        int recall_cmd_len = 0;
        if (recall_scene != null) {
            recall_cmd_len = 52;
        }
        if (recall_scene != null & recall_scene != null) {
            recall_cmd_len = 59;
        }
        System.out.println("recall_cmd_len = " + recall_cmd_len);
        byte[] recall_cmd = new byte[recall_cmd_len];
        if (recall_scene != null) {
            recall_cmd[0] = 0x63;
            recall_cmd[1] = 0x6f;
            recall_cmd[2] = 0x6d;
            recall_cmd[3] = 0x6d;
            recall_cmd[4] = 0x61;
            recall_cmd[5] = 0x6e;
            recall_cmd[6] = 0x64;

            recall_cmd[7] = 0x04;

            byte[] cmdNo_5 = NewCmdData.RecallScene(group_id, scene_id);

            int cmd_recall_scene_len = cmdNo_5.length;
            recall_cmd[8] = ((byte) (cmd_recall_scene_len << 8));
            recall_cmd[9] = ((byte) cmd_recall_scene_len);
            for (int i = 0; i < cmd_recall_scene_len; i++) {
                recall_cmd[(10 + i)] = cmdNo_5[i];
            }

            recall_cmd[(10 + cmdNo_5.length)] = 0x63;
            recall_cmd[(11 + cmdNo_5.length)] = 0x6f;
            recall_cmd[(12 + cmdNo_5.length)] = 0x6d;
            recall_cmd[(13 + cmdNo_5.length)] = 0x6d;
            recall_cmd[(14 + cmdNo_5.length)] = 0x61;
            recall_cmd[(15 + cmdNo_5.length)] = 0x6e;
            recall_cmd[(16 + cmdNo_5.length)] = 0x64;
        }
        byte[] bt_1 = FtFormatTransfer.byteMerger(switch_cmd, level_cmd);
        byte[] bt_2 = FtFormatTransfer.byteMerger(hue_cmd, temp_cmd);
        byte[] bt_3 = FtFormatTransfer.byteMerger(bt_1, bt_2);

        byte[] cmddata = FtFormatTransfer.byteMerger(bt_3, recall_cmd);
        System.out.println("cmddata = " + cmddata.length);
        String task_str = "";
        for (int i = 0; i < cmddata.length; i++) {
            task_str = task_str + Integer.toHexString(cmddata[i] & 0xFF);
        }
        System.out.println("task_str = " + task_str);
        return cmddata;
    }
}

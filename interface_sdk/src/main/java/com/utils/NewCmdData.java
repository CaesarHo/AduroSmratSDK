package com.utils;


import android.util.Log;

import com.interfacecallback.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by best on 2016/6/24.
 */
public class NewCmdData {

    /*
    *设置网关时间
    */
    public static byte[] setGateWayTimeCmd(int year,int month ,int day,int hour,int minute,int second){
        byte[] bt_send = new byte[41];
        //415050c0a8016b010143
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018  0100100016
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x17;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) 0x19;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00060001
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;

        bt_send[33] = (byte)( year>>8);
        bt_send[34] = (byte) year;
        bt_send[35] = (byte) month;
        bt_send[36] = (byte) day;
        bt_send[37] = (byte) hour;
        bt_send[38] = (byte) minute;
        bt_send[39] = (byte) second;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    // ------------------------------------- 设备相关start--------------------------------------
    //Allows devices to access
    public static byte[] Allow_DevicesAccesstoBytes() {
        byte[] bt_send = new byte[16];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x00;
        bt_send[13] = 0x00;
        bt_send[14] = 0x00;
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[15] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[15] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    //获取设备列表命令
    public static byte[] GetAllDeviceListCmd() {
        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x0B;
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 选择设备提示
     *
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param second
     * @return
     */
    public static byte[] IdentifyDeviceCmd(String mac, String shortaddr, String main_point, int second) {
        byte[] bt_send = new byte[41];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, 9))) {
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, 9));
            bt_send[9] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x70;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = com.utils.Utils.HexString2Bytes(mac)[0];
        bt_send[24] = com.utils.Utils.HexString2Bytes(mac)[1];
        bt_send[25] = com.utils.Utils.HexString2Bytes(mac)[2];
        bt_send[26] = com.utils.Utils.HexString2Bytes(mac)[3];
        bt_send[27] = com.utils.Utils.HexString2Bytes(mac)[4];
        bt_send[28] = com.utils.Utils.HexString2Bytes(mac)[5];
        bt_send[29] = com.utils.Utils.HexString2Bytes(mac)[6];
        bt_send[30] = com.utils.Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = com.utils.Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = com.utils.Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = com.utils.Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = 0x00;
        bt_send[39] = (byte) second;//持续时间


        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] DevSwitchCmd(String mac, String shortaddr, String main_endpoint, int value) {
        byte[] bt_send = new byte[40];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {

            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            System.out.println("打印crc8结果false = " + ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x18;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x06;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) value;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] setDeviceLevelCmd(String mac, String shortaddr, String main_point, int value) {
        byte[] bt_send = new byte[43];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x1b;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x81;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = 0x00;//不带有开关
        bt_send[39] = (byte) value;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * 改变设备颜色
     *
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param hue
     * @return
     */
    public static byte[] setDeviceHueCmd(String mac, String shortaddr, String main_point, int hue) {
        byte[] bt_send = new byte[42];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x1b;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xB0;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte) hue;
        bt_send[39] = 0x00;//不带有开关
        bt_send[40] = 0x00;
        bt_send[41] = 0x08;

        byte bt_crc8 = (CRC8.calc(bt_send, bt_send.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = ", hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);
        System.out.println("bt_crcdata = " + bt_crcdata.length);

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, bt_crcdata);
        System.out.println("kkk = " + Arrays.toString(Utils.HexString2Bytes(hex)));
        return bt_send_data;
    }

    /**
     * 改变设备饱和度
     *
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param hue
     * @param sat
     * @return
     */
    public static byte[] setDeviceHueSatCmd(String mac, String shortaddr, String main_point, int hue, int sat) {
        byte[] bt_send = new byte[44];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x1c;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xB6;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x10;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte) hue;
        bt_send[39] = (byte) sat;
        bt_send[40] = 0x00;//不带有开关
        bt_send[41] = 0x00;
        bt_send[42] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[43] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[43] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 改变设备色温值
     *
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param value
     * @return
     */
    public static byte[] setDeviceColorsTemp(String mac, String shortaddr, String main_point, int value) {
        byte[] bt_send = new byte[43];
        //415050c0a801030101d7
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        //消息体 010002001b
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x1b;
        //数据体头  415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  0100c0
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xC0;
        //mac地址    00158d0001310dfe
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   000902
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        //d206010100f40000ea
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte) (value << 8);//不带有开关
        bt_send[39] = (byte) value;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 修改设备名称cmd
     *
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param device_name
     * @return
     */
    public static byte[] sendUpdateDeviceCmd(String device_name, String mac, String shortaddr, String main_point) throws UnsupportedEncodingException {

        byte[] strTobt = device_name.getBytes("utf-8");
        int device_name_len = strTobt.length;
        int data_style_len = 25 + device_name_len;
        int data_len = 7 + device_name_len;

        //组名称 726f6f6d  31
        String dev_name = "";
        byte[] dev_name_bt = null;
        for (int i = 0; i < strTobt.length; i++) {
            dev_name += Integer.toHexString(strTobt[i] & 0xFF);
            dev_name_bt = Utils.HexString2Bytes(dev_name);
        }
        System.out.println("dev_name_bt = " + Arrays.toString(dev_name_bt));
        System.out.println("dev_name = " + dev_name);

        byte[] bt_send = new byte[40];
        //415050c0a801040101c1
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 010015001d
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x15;//枚举A
        bt_send[13] = (byte) (data_style_len << 8);
        bt_send[14] = (byte) data_style_len;//以下数据长度
        //数据体头 415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号 01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;//枚举B
        bt_send[22] = (byte) 0xFF;//枚举B
        //mac地址    00124b0001de5be4
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   000b02f796010c
        bt_send[31] = (byte) (data_len << 8);
        bt_send[32] = (byte) data_len;//以下数据长度
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte) (device_name_len << 8);//00046d6d6d6db3
        bt_send[39] = (byte) device_name_len;

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, dev_name_bt);

        //将前面数据CRC8校验  7d
        byte bt_crc8 = (CRC8.calc(bt_send_data, bt_send_data.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

//        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send,bt_send.length-1))){
//            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send,bt_send.length-1));
//            bt_send[42] = Utils.HexString2Bytes(ss)[0];
//        }else{
//            bt_send[42] = Utils.HexString2Bytes(Utils.CrcToString(bt_send,bt_send.length-1))[0];
//        }

        byte[] bt_data = FtFormatTransfer.byteMerger(bt_send_data, bt_crcdata);
        return bt_data;
    }

    /**
     * 删除设备命令
     *
     * @param mac
     * @return
     */
    public static byte[] DeleteDeviceCmd(String mac) {
        byte[] bt_send = new byte[34];
        //41 50 50 C0 A8 00 58 01 01 09
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   01 00 01 00 12
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x01;
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体头  41 5F 5A 49 47
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 00
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x00;
        //mac地址    00 12 4b 00 01 de 5c 9c
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 00
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    // ------------------------------------- 设备相关end--------------------------------------


    // ------------------------------------- 房间相关start--------------------------------------

    /**
     * 获取Groups
     *
     * @return
     */
    public static byte[] GetAllGroupListCmd() {
        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x11;
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 添加Group
     *
     * @param groupname
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sendAddGroupCmd(String groupname) throws UnsupportedEncodingException {
        int data_style_len = 20 + groupname.length();
        byte[] strTobt = groupname.getBytes("UTF-8");
        int group_name_len = groupname.length();
        int len = 2 + group_name_len;

        byte[] bt_send = new byte[35];
        //415050c0a8016d0101 3e
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x0f;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) data_style_len;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   0006
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) len;

        //组名称长度   0004
        bt_send[33] = 0x00;
        bt_send[34] = (byte) group_name_len;

        //组名称 59756b69
        String group_str = "";
        byte[] group_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            group_str += Integer.toHexString(strTobt[i] & 0xFF);
            group_data = Utils.HexString2Bytes(group_str);
        }
        //固定数据与group数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, group_data);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(bt_send_data, bt_send_data.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = ", hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(bt_send_data, bt_crcdata);

        return bt_send_cmd;
    }

    /**
     * 添加一个设备到组里面
     *
     * @param group_id
     * @param mac
     * @param shortaddr
     * @param main_endpoint
     * @return
     */
    public static byte[] Add_DeviceToGroup(int group_id, String mac, String shortaddr, String main_endpoint) {
        byte[] bt_send = new byte[41];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x0c;
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x60;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] setGroupState(int group_id, int value) {
        byte[] bt_send = new byte[40];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, 9))) {

            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, 9));
            System.out.println("打印crc8结果false = " + ss);
            bt_send[9] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, 9));
            bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x18;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x06;
        bt_send[33] = (byte) 0x01;
        bt_send[34] = (byte) (group_id >> 8);
        bt_send[35] = (byte) group_id;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = (byte) 0xff;//目标端点
        bt_send[38] = (byte) value;//状态值

        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] setGroupLevel(int group_id, int value) {
        byte[] bt_send = new byte[41];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, 9))) {

            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, 9));
            System.out.println("打印crc8结果false = " + ss);
            bt_send[9] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, 9));
            bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x02;
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x81;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x01;
        bt_send[34] = (byte) (group_id >> 8);
        bt_send[35] = (byte) group_id;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = (byte) 0xff;//目标端点
        bt_send[38] = 0x00;
        bt_send[39] = (byte) value;//状态值

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * 从组里删除Device
     *
     * @param group_id
     * @return
     */
    public static byte[] DeleteDeviceFromGroup(int group_id, String mac, String shortaddr, String main_endpoint) {
        byte[] bt_send = new byte[41];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x0e;
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0x63;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;

        //CRC校验吗
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 删除组
     *
     * @param group_id
     * @return
     */
    public static byte[] sendDeleteGroupCmd(int group_id) {

        byte[] bt_send = new byte[38];
        //415050c0a8016b010143
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018  0100100016
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x10;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) 0x16;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00060001
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x04;

        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        //组名称长度   00000a
        bt_send[35] = 0x00;
        bt_send[36] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[37] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[37] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * change
     *
     * @param groupname
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sendUpdateGroupCmd(int group_id, String groupname) throws UnsupportedEncodingException {

        byte[] strTobt = groupname.getBytes("utf-8");
        int group_name_len = strTobt.length;
        int data_style_len = 22 + group_name_len;
        int data_len = 4 + group_name_len;

        //组名称 726f6f6d  31
        String group_str = "";
        byte[] group_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            group_str += Integer.toHexString(strTobt[i] & 0xFF);
            System.out.println("group_str = " + group_str);
            group_data = Utils.HexString2Bytes(group_str);
        }

        byte[] bt_send = new byte[37];
        //4415050c0a801040101c1    415050c0a801040101c1
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   01001000    010010001b
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x10;//数据类型 枚举A
        bt_send[13] = 0x00;
        bt_send[14] = (byte) data_style_len;//数据体长度  1b = 27
        //数据体头   415f5a4947   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff     01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09   00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   0009
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) data_len;
        // group_id  00a2
        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        //组名称长度   0005
        bt_send[35] = 0x00;
        bt_send[36] = (byte) group_name_len;

        //固定数据与group数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, group_data);

        //将前面数据CRC8校验  7d
        byte bt_crc8 = (CRC8.calc(bt_send_data, bt_send_data.length));

        //crc8 转成需要的类型
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = ", hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(bt_send_data, bt_crcdata);

        return bt_send_cmd;
    }

    // ------------------------------------- 设备相关end--------------------------------------


    //=======================================场景相关start===================================================
    //获取网关所有场景
    public static byte[] GetAllScenesListCmd() {

        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + com.utils.Utils.CrcToString(bt_send, 9));
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, 9));
            bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x14;
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;
        bt_send[33] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        return bt_send;
    }

    /**
     * 添加添加名称
     *
     * @param scenename
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sendAddSceneCmd(String scenename, int groupid) throws UnsupportedEncodingException {
        int data_style_len = 22 + scenename.length();//数据体长度
        byte[] strTobt = scenename.getBytes("UTF-8");
        int scene_name_len = scenename.length();//场景名称长度
        int data_len = 4 + scene_name_len;

        byte[] bt_send = new byte[35];
        //415050c0a801040101c
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   1010012001a
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x12;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) data_style_len;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  01ffff00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   0008
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;

        //场景名称长度  00046b616936000d4c
        bt_send[33] = (byte) (scene_name_len >> 8);
        bt_send[34] = (byte) scene_name_len;

        //场景名称  74657374
        String scene_name = "";
        byte[] scene_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            scene_name += Integer.toHexString(strTobt[i] & 0xFF);
            scene_data = Utils.HexString2Bytes(scene_name);
        }
        //固定数据与Scene数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, scene_data);

        //固定数据与Scene数据相加的结果在与组ID相加  0001
        byte[] group_id_bt = new byte[2];
        group_id_bt[0] = (byte) (groupid >> 8);
        group_id_bt[1] = (byte) groupid;

        byte[] final_data = FtFormatTransfer.byteMerger(bt_send_data, group_id_bt);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(final_data, final_data.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(final_data, bt_crcdata);

        return bt_send_cmd;
    }


    /**
     * 删除场景
     *
     * @param scene_id
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sendDeleteSceneCmd(int scene_id) {

        byte[] bt_send = new byte[37];
        //     415050c0a801040101  c1
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体    0100130015
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x13;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) 0x15;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度      0004001d000087
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x03;

        bt_send[33] = (byte) scene_id;

        //组名称长度   00000a
        bt_send[34] = 0x00;
        bt_send[35] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[36] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[36] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 添加设备到Scene
     *
     * @param group_id
     * @param mac
     * @param shortaddr
     * @param main_endpoint
     * @return
     */
    public static byte[] Add_DeviceToScene(String mac, String shortaddr, String main_endpoint, int group_id, int scene_id) {
        byte[] bt_send = new byte[46];
        //415050c0a801030101d7
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  010003001e
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x03;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x1E;
        //数据体头  415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  0100a1
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xA1;
        //mac地址    00158d0001310e4e
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00120255130101
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x0c;
        bt_send[33] = (byte) 0x02;//短地址模式
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;
        bt_send[40] = (byte) scene_id;//000218000000006e
        bt_send[41] = 0x00;
        bt_send[42] = 0x00;
        bt_send[43] = 0x00;
        bt_send[44] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = com.utils.Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[45] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[45] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 存储场景至设备
     */
    public static byte[] StoreScene(String mac, String shortaddr, String main_endpoint, int group_id, int scene_id) {
        byte[] bt_send = new byte[42];
        //415050c0a801030101d7
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  010003001e
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x03;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x1A;
        //数据体头  415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  0100a1
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xA4;
        //mac地址    00158d0001310e4e
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00120255130101
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x08;
        bt_send[33] = (byte) 0x02;//短地址模式
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;
        bt_send[40] = (byte) scene_id;//000218000000006e

        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[41] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[41] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 从场景里删除Device
     *
     * @param scene_id
     * @return
     */
    public static byte[] DeleteDeviceFromScene(int scene_id, String mac, String shortaddr, String main_endpoint) {
        byte[] bt_send = new byte[41];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x04;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xA2;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(mac)[0];
        bt_send[24] = Utils.HexString2Bytes(mac)[1];
        bt_send[25] = Utils.HexString2Bytes(mac)[2];
        bt_send[26] = Utils.HexString2Bytes(mac)[3];
        bt_send[27] = Utils.HexString2Bytes(mac)[4];
        bt_send[28] = Utils.HexString2Bytes(mac)[5];
        bt_send[29] = Utils.HexString2Bytes(mac)[6];
        bt_send[30] = Utils.HexString2Bytes(mac)[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = (byte) (scene_id >> 8);
        bt_send[39] = (byte) scene_id;

        //CRC校验吗
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 修改场景CMD
     *
     * @param scene_id
     * @param scenename
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sendUpdateSceneCmd(int scene_id, String scenename) throws UnsupportedEncodingException {
        byte[] strTobt = scenename.getBytes("utf-8");
        int group_name_len = strTobt.length;
        int data_style_len = 21 + group_name_len;
        int data_len = 3 + group_name_len;

        //组名称 726f6f6d  31
        String group_str = "";
        byte[] group_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            group_str += Integer.toHexString(strTobt[i] & 0xFF);
            System.out.println("group_str = " + group_str);
            group_data = Utils.HexString2Bytes(group_str);
        }

        byte[] bt_send = new byte[36];
        //4415050c0a801040101c1    415050c0a801040101c1
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!com.utils.Utils.isCRC8Value(com.utils.Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + com.utils.Utils.CrcToString(bt_send, 9));
            String ss = com.utils.Utils.StringToHexString(com.utils.Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = com.utils.Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + com.utils.Utils.CrcToString(bt_send, 9));
            bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   01001000    010010001b
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x13;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) data_style_len;//数据体长度  1b = 27
        //数据体头   415f5a4947   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff     01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09   00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   0009      0009
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) data_len;
        // group_id  00a2   00a1
        bt_send[33] = (byte) scene_id;

        //组名称长度   0005      0005
        bt_send[34] = 0x00;
        bt_send[35] = (byte) group_name_len;

        //固定数据与group数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, group_data);

        //将前面数据CRC8校验  7d
        byte bt_crc8 = (CRC8.calc(bt_send_data, bt_send_data.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = ", hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(bt_send_data, bt_crcdata);
        return bt_send_cmd;
    }

    /**
     * 控制场景
     *
     * @param GroupId
     * @param SceneId
     * @return
     */
    public static byte[] RecallScene(int GroupId, int SceneId) {
        byte[] bt_send = new byte[42];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x05;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x1A;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0x00;
        bt_send[22] = (byte) 0xA5;
        //
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x08;
        bt_send[33] = (byte) 0x01;//组控模式
        bt_send[34] = (byte) (GroupId << 8);
        bt_send[35] = (byte) GroupId;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = (byte) 0xFF;//目标端点
        bt_send[38] = (byte) (GroupId << 8);
        bt_send[39] = (byte) GroupId;
        bt_send[40] = (byte) SceneId;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[41] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[41] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    // ------------------------------------- 场景相关end--------------------------------------



    // ------------------------------------- 任务相关start--------------------------------------
    /**
     * 创建任务
     * @throws UnsupportedEncodingException
     */
    public static byte[] CreateTask(String taskname, byte isRun, byte task_type, byte task_cycle, int hour, int minute, int device_action,
                                    String action_mac, String device_mac, String device_shortaddr, String device_main_point, int cmd_size,
                                    String dev_switch, int switch_state, String dev_level, int level_value, String dev_hue, int hue_value,
                                    int sat_value, String dev_temp, int temp_value, String recall_scene, int group_id, int scene_id) throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = Utils.tCmdData(device_mac, device_shortaddr, device_main_point, dev_switch,
                switch_state, dev_level, level_value, dev_hue, hue_value, sat_value, dev_temp, temp_value, recall_scene, group_id, scene_id);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = task_type;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = task_cycle;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = (byte) hour;//任务小时-15点
        commandDataByte[6] = (byte) minute;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001
        if (task_type == 1 | task_type == 2) {
            commandDataByte[7] = (byte) 0x80;//串口数据类型
            commandDataByte[8] = 0x01;//串口数据类型
            //0001
            commandDataByte[9] = 0x00;//设备动作
            commandDataByte[10] = 0x00;//设备动作
            //产生动作的设备mac开始   00158d0001310e1b
            commandDataByte[11] = 0x11;
            commandDataByte[12] = 0x22;
            commandDataByte[13] = 0x33;
            commandDataByte[14] = 0x44;
            commandDataByte[15] = 0x55;
            commandDataByte[16] = 0x66;
            commandDataByte[17] = 0x77;
            commandDataByte[18] = (byte) 0x88;
        } else {
            commandDataByte[7] = (byte) 0x84;//串口数据类型
            commandDataByte[8] = 0x01;//串口数据类型
            //0001
            commandDataByte[9] = (byte) (device_action << 8);//设备动作
            commandDataByte[10] = (byte) device_action;//设备动作
            //产生动作的设备mac开始   00158d0001310e1b
            commandDataByte[11] = Utils.HexString2Bytes(action_mac)[0];
            commandDataByte[12] = Utils.HexString2Bytes(action_mac)[1];
            commandDataByte[13] = Utils.HexString2Bytes(action_mac)[2];
            commandDataByte[14] = Utils.HexString2Bytes(action_mac)[3];
            commandDataByte[15] = Utils.HexString2Bytes(action_mac)[4];
            commandDataByte[16] = Utils.HexString2Bytes(action_mac)[5];
            commandDataByte[17] = Utils.HexString2Bytes(action_mac)[6];
            commandDataByte[18] = Utils.HexString2Bytes(action_mac)[7];
        }

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        System.out.println("commandDataByte.length = " + dataByte_Len);
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;//枚举B
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        if (task_type == 1 | task_type == 2) {
            bt_send[23] = Utils.HexString2Bytes(device_mac)[0];
            bt_send[24] = Utils.HexString2Bytes(device_mac)[1];
            bt_send[25] = Utils.HexString2Bytes(device_mac)[2];
            bt_send[26] = Utils.HexString2Bytes(device_mac)[3];
            bt_send[27] = Utils.HexString2Bytes(device_mac)[4];
            bt_send[28] = Utils.HexString2Bytes(device_mac)[5];
            bt_send[29] = Utils.HexString2Bytes(device_mac)[6];
            bt_send[30] = Utils.HexString2Bytes(device_mac)[7];
        } else {
            bt_send[23] = Utils.HexString2Bytes(action_mac)[0];
            bt_send[24] = Utils.HexString2Bytes(action_mac)[1];
            bt_send[25] = Utils.HexString2Bytes(action_mac)[2];
            bt_send[26] = Utils.HexString2Bytes(action_mac)[3];
            bt_send[27] = Utils.HexString2Bytes(action_mac)[4];
            bt_send[28] = Utils.HexString2Bytes(action_mac)[5];
            bt_send[29] = Utils.HexString2Bytes(action_mac)[6];
            bt_send[30] = Utils.HexString2Bytes(action_mac)[7];
        }
        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }


    /**
     * 获取网关所有任务
     *
     * @return
     */
    public static byte[] GetAllTasks() {

        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x1b;//数据类型   枚举A
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /*
       *删除task
       */
    public static byte[] DeleteTaskCmd(int task_id){
        byte[] bt_send = new byte[36];
        //415050c0a8016b010143
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018  0100100016
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x19;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) 0x16;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) 0xFF;
        bt_send[22] = (byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00060001
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x04;

        bt_send[33] = (byte) (task_id >> 8);
        bt_send[34] = (byte) task_id;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[35] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[35] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }
}

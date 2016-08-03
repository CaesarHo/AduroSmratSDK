package com.utils;


import android.util.Log;

import com.interfacecallback.Constants;

import java.util.Arrays;

/**
 * Created by best on 2016/6/24.
 */
public class NewCmdData {
    //Allows devices to access
    public static byte[] Allow_DevicesAccesstoBytes() {
        //消息byte数组
        byte[] bt_msg = new byte[10];
        bt_msg[0] = 0x41;
        bt_msg[1] = 0x50;
        bt_msg[2] = 0x50;
        bt_msg[3] = (byte)0xC0;
        bt_msg[4] = (byte)0xA8;
        bt_msg[5] = 0x01;
        bt_msg[6] = 0x6B;
        bt_msg[7] = 0x01;
        bt_msg[8] = 0x01;
        bt_msg[9] = (byte)0x43;//消息校验码
        //消息体数组
        byte[] by_body = new byte[6];
        by_body[0] = 0x01;
        by_body[1] = 0x00;
        by_body[2] = 0x00;
        by_body[3] = 0x00;
        by_body[4] = 0x00;
        by_body[5] = (byte)0x62;//消息体校验码
        return FtFormatTransfer.byteMerger(bt_msg,by_body);
    }

    public static byte[] AgreeToTheNet() {
        byte[] bt_send = new byte[16];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)0xC0;
        bt_send[4] = (byte)0xA8;
        bt_send[5] = 0x01;
        bt_send[6] = 0x67;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;
        bt_send[9] = (byte)0xB9;
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = 0x00;
        bt_send[13] = 0x00;
        bt_send[14] = 0x00;
        bt_send[15] = 0x62;
        return bt_send;
    }

    //获取设备列表命令
    public static byte[] GetAllDeviceListCmd(){
        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;
        bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send,9))[0];
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
        bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send,bt_send.length - 1))[0];
        return bt_send;
    }

    public static byte[] DevSwitchCmd(String mac,String shortaddr,String main_endpoint){
        byte[] bt_send = new byte[40];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        byte bt_crc8 = (byte) (CRC8.calc(bt_send, 9)&0xFF);
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send,9))[0];
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
        bt_send[21] = (byte)0x00;
        bt_send[22] = (byte)0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = (byte)0x00;
        bt_send[24] = Utils.HexString2Bytes(mac)[0];
        bt_send[25] = Utils.HexString2Bytes(mac)[1];
        bt_send[26] = Utils.HexString2Bytes(mac)[2];
        bt_send[27] = Utils.HexString2Bytes(mac)[3];
        bt_send[28] = Utils.HexString2Bytes(mac)[4];
        bt_send[29] = Utils.HexString2Bytes(mac)[5];
        bt_send[30] = Utils.HexString2Bytes(mac)[6];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x06;
        bt_send[33] = (byte)0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_endpoint)[0];//目标端点
        bt_send[38] = 0x02;
        bt_send[39] = Utils.HexString2Bytes(Utils.CrcToString(bt_send,bt_send.length - 1))[0];
        return bt_send;
    }

    public static byte[] setDeviceLevelCmd(String mac,String shortaddr , String main_point ,int value){
        byte[] bt_send = new byte[42];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        bt_send[9] = Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send,9))[0];
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
        bt_send[21] = (byte)0x00;
        bt_send[22] = (byte)0x81;
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
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x09;
        bt_send[33] = (byte)0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = 0x00;//不带有开关
        bt_send[39] = (byte)value;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;

        byte bt_crc8 = (CRC8.calc(bt_send,bt_send.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("LevelCrcHex = " ,hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);
        System.out.println("bt_crcdata = " + bt_crcdata.length);

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send,bt_crcdata);
        System.out.println("kkk = " + Arrays.toString(com.utils.Utils.HexString2Bytes(hex)));
        return bt_send_data;
    }


    /**
     * 改变设备颜色
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param hue
     * @return
     */
    public static byte[] setDeviceHueCmd(String mac,String shortaddr , String main_point ,int hue){
        byte[] bt_send = new byte[42];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        bt_send[9] = Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send,9))[0];
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
        bt_send[21] = (byte)0x00;
        bt_send[22] = (byte)0xB0;
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
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x09;
        bt_send[33] = (byte)0x02;
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte)hue;
        bt_send[39] = 0x00;//不带有开关
        bt_send[40] = 0x00;
        bt_send[41] = 0x08;

        byte bt_crc8 = (CRC8.calc(bt_send,bt_send.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = " ,hex);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);
        System.out.println("bt_crcdata = " + bt_crcdata.length);

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send,bt_crcdata);
        System.out.println("kkk = " + Arrays.toString(Utils.HexString2Bytes(hex)));
        return bt_send_data;
    }


    /**
     * 改变设备饱和度
     * @param mac
     * @param shortaddr
     * @param main_point
     * @param hue
     * @param sat
     * @return
     */
    public static byte[] setDeviceHueSatCmd(String mac,String shortaddr , String main_point ,int hue ,int sat){
        byte[] bt_send = new byte[43];
        //415050C0A8016B 0101 43
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send,9))[0];
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
        bt_send[21] = (byte)0x00;
        bt_send[22] = (byte)0xB6;
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
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x09;
        bt_send[33] = (byte)0x02;
        bt_send[34] = com.utils.Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = com.utils.Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = com.utils.Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte)hue;
        bt_send[39] = (byte)sat;
        bt_send[40] = 0x00;//不带有开关
        bt_send[41] = 0x00;
        bt_send[42] = 0x08;

        byte bt_crc8 = (com.utils.CRC8.calc(bt_send,bt_send.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = " ,hex);
        byte[] bt_crcdata = com.utils.Utils.HexString2Bytes(hex);
        System.out.println("bt_crcdata = " + bt_crcdata.length);

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send,bt_crcdata);
        System.out.println("kkk = " + Arrays.toString(com.utils.Utils.HexString2Bytes(hex)));
        return bt_send_data;
    }

    /**
     * 删除设备命令
     * @param mac
     * @return
     */
    public static byte[] DeleteDeviceCmd(String mac){
        byte[] bt_send = new byte[33];
        //41 50 50 C0 A8 00 58 01 01 09
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)Constants.IpAddress.int_1;
        bt_send[4] = (byte)Constants.IpAddress.int_2;
        bt_send[5] = (byte)Constants.IpAddress.int_3;
        bt_send[6] = (byte)Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        bt_send[9] = com.utils.Utils.HexString2Bytes(com.utils.Utils.CrcToString(bt_send,9))[0];
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
        bt_send[21] = (byte)0x00;
        bt_send[22] = (byte)0x00;
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
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x00;

        byte bt_crc8 = (com.utils.CRC8.calc(bt_send,bt_send.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        Log.i("bt_crc8ToHex = " ,hex);
        byte[] bt_crcdata = com.utils.Utils.HexString2Bytes(hex);
        System.out.println("bt_crcdata = " + bt_crcdata.length);

        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send,bt_crcdata);

        return bt_send_data;
    }
}

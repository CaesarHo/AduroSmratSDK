package com.core.cmddata;

import android.util.Log;

import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.CRC8;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;
import java.util.Arrays;

/**
 * Created by best on 2016/9/23.
 */

public class DeviceCmdData {
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
        bt_send[12] = MessageType.A.SET_GETEWAY_TIME.value();//数据类型
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
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
        bt_send[12] = MessageType.A.CHECK_NEW_DEVICE.value();
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
        bt_send[12] = MessageType.A.UPLOAD_ALL_TXT.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
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
     * 发送Active Req命令
     * @param mac
     * @param shortaddr
     * @param main_point
     * @return
     */
    public static byte[] ActiveReqDeviceCmd(String mac, String shortaddr, String main_point) {
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
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_ACTIVE_ENDPOINT_REQUEST.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ACTIVE_ENDPOINT_REQUEST.value();      //(byte) 0x45;
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
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[39] = Utils.HexString2Bytes(shortaddr)[1];

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 选择设备提示
     *
     * @param second
     * @return
     */
    public static byte[] IdentifyDeviceCmd(AppDevice appDevice, int second) {
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
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_IDENTIFY_SEND.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_IDENTIFY_SEND.value();      //(byte) 0x70;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = 0x00;
        bt_send[39] = (byte) second;//持续时间

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] DevSwitchCmd(AppDevice appDevice, int value) {
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
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value();      //(byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x06;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
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

    public static byte[] setDeviceLevelCmd(AppDevice appDevice, int value) {
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
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value();      //(byte) 0x81;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
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
     * @param hue
     * @return
     */
    public static byte[] setDeviceHueCmd(AppDevice appDevice, int hue) {
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
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_MOVE_TO_HUE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_HUE.value();      //(byte) 0xB0;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
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
     * @param hue
     * @param sat
     * @return
     */
    public static byte[] setDeviceHueSatCmd(AppDevice appDevice, int hue, int sat) {
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
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_MOVE_TO_HUE_SATURATION.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_HUE_SATURATION.value();      //(byte) 0xB6;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x10;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
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
     * @param value
     * @return
     */
    public static byte[] setDeviceColorsTemp(AppDevice appDevice, int value) {
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
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_MOVE_TO_COLOUR_TEMPERATURE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_COLOUR_TEMPERATURE.value();      //(byte) 0xC0;
        //mac地址    00158d0001310dfe
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   000902
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        //d206010100f40000ea
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
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
     * @return
     */
    public static byte[] sendUpdateDeviceCmd(AppDevice appDevice,String device_name){
        byte[] strTobt = null;
        try {
            strTobt = device_name.getBytes("utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

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
        bt_send[12] = MessageType.A.CHANGE_DEVICE_NAME.value();//枚举A
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
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;//枚举B
        //mac地址    00124b0001de5be4
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   000b02f796010c
        bt_send[31] = (byte) (data_len << 8);
        bt_send[32] = (byte) data_len;//以下数据长度
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];//目标端点
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
        bt_send[12] = MessageType.A.DELETE_DEVICE.value();
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


    /**
     * 读属性
     * @param appDevice
     * @return
     */
    public static byte[] ReadAttrbuteCmd(AppDevice appDevice, String SerialType, String value) {
        //415050c0a801040101c1
        byte[] bt_send = new byte[48];
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
        //消息体   0100080020
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.UPLOAD_DEVICE_INFO.value();//数据体类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x20;//数据体长度
        //数据体-----头   415f5a4947010100
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = Utils.HexString2Bytes(SerialType)[0];//枚举B
        bt_send[22] = Utils.HexString2Bytes(SerialType)[1];//枚举B

        //mac地址    00158d0001310e1b
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x0E;//下面数据长度

        bt_send[33] = (byte) 0x02;//段地址模式   02f1bc
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//0101
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = Utils.HexString2Bytes(value)[0];//(byte) (value << 8);//ClusterID    0006000000000100019a
        bt_send[39] = Utils.HexString2Bytes(value)[1];//(byte) value;//ClusterID

        bt_send[40] = 0x00;//Direction  填0
        bt_send[41] = 0x00;//ManuSpecific  填0
        bt_send[42] = 0x00;//ManuID    填0
        bt_send[43] = 0x00;//ManuID    填0

        bt_send[44] = 0x01;//AttribCount   00

        bt_send[45] = 0x00;
        bt_send[46] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[47] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[47] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 读取ZoneType属性
     * @param devicemac
     * @return
     */
    public static byte[] ReadZoneTypeCmd(String devicemac, String shortaddr, String main_point) {
        //415050c0 a8010a0101ed
        byte[] bt_send = new byte[48];
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
        //消息体   0100080020
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.UPLOAD_DEVICE_INFO.value();//数据体类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x20;//数据体长度
        //数据体-----头   415f5a4947010100
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value() >> 8);//(byte) 0x01;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value();      //(byte) 0x00;//枚举B

        //mac地址    00158d0000ecc9a7
        bt_send[23] = Utils.HexString2Bytes(devicemac)[0];
        bt_send[24] = Utils.HexString2Bytes(devicemac)[1];
        bt_send[25] = Utils.HexString2Bytes(devicemac)[2];
        bt_send[26] = Utils.HexString2Bytes(devicemac)[3];
        bt_send[27] = Utils.HexString2Bytes(devicemac)[4];
        bt_send[28] = Utils.HexString2Bytes(devicemac)[5];
        bt_send[29] = Utils.HexString2Bytes(devicemac)[6];
        bt_send[30] = Utils.HexString2Bytes(devicemac)[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x0E;//下面数据长度

        bt_send[33] = (byte) 0x02;//段地址模式   02d008
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//01ff
        bt_send[37] = (byte)0xFF; //Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = 0x05;//ClusterID  0500
        bt_send[39] = 0x00;//ClusterID

        //00000000
        bt_send[40] = 0x00;//Direction  填0
        bt_send[41] = 0x00;//ManuSpecific  填0
        bt_send[42] = 0x00;//ManuID    填0
        bt_send[43] = 0x00;//ManuID    填0

        bt_send[44] = 0x01;//AttribCount   010001   00
        bt_send[45] = 0x00;
        bt_send[46] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[47] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[47] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 保存設備ZoneType
     * @param devicemac
     * @param shortaddr
     * @param main_point
     * @return
     */
    public static byte[] SaveZoneTypeCmd(String devicemac, String shortaddr, String main_point,short zonetype){
        //415050c0 a8010a0101ed
        byte[] bt_send = new byte[41];
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
        //消息体   0100080020
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.SAVE_ZONE_TYPE.value();//数据体类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x19;//数据体长度
        //数据体-----头   415f5a4947010100
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;//枚举B

        //mac地址    00158d0000ecc9a7
        bt_send[23] = Utils.HexString2Bytes(devicemac)[0];
        bt_send[24] = Utils.HexString2Bytes(devicemac)[1];
        bt_send[25] = Utils.HexString2Bytes(devicemac)[2];
        bt_send[26] = Utils.HexString2Bytes(devicemac)[3];
        bt_send[27] = Utils.HexString2Bytes(devicemac)[4];
        bt_send[28] = Utils.HexString2Bytes(devicemac)[5];
        bt_send[29] = Utils.HexString2Bytes(devicemac)[6];
        bt_send[30] = Utils.HexString2Bytes(devicemac)[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x07;//下面数据长度

        bt_send[33] = (byte) 0x02;//段地址模式   02d008
        bt_send[34] = Utils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = Utils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//01ff
        bt_send[37] = Utils.HexString2Bytes(main_point)[0];//目标端点
        bt_send[38] = (byte)(zonetype << 8);//zonetype  0500
        bt_send[39] = (byte)zonetype;//zonetype

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }


    public static byte[] Get_IEEEAddr_CMD() {
        byte[] bt_send = new byte[45];
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
            String ss =  Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(com.core.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_GATEWAY_IEEE.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x1D;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_IEEE_ADDRESS_REQUEST.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_IEEE_ADDRESS_REQUEST.value();      //(byte) 0x70;
        //mac地址    00124b0001dd7ac1
        bt_send[23] = 0x00;
        bt_send[24] = 0x00;
        bt_send[25] = 0x00;
        bt_send[26] = 0x00;
        bt_send[27] = 0x00;
        bt_send[28] = 0x00;
        bt_send[29] = 0x00;
        bt_send[30] = 0x00;
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x0B;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = 0x00;
        bt_send[35] = 0x00;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = 0x00;//目标端点
        bt_send[38] = 0x00;
        bt_send[39] = 0x00;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;
        bt_send[42] = 0x00;
        bt_send[43] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[44] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[44] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] BindDeviceCmd(AppDevice appDevice,String IeeeAddr) {
        byte[] bt_send = new byte[60];
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
        bt_send[12] = MessageType.A.BIND_DEVICE.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x2C;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_BIND.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte)  MessageType.B.E_SL_MSG_BIND.value();      //(byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x1A;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[39] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[40] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[41] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[42] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[43] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[44] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[45] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        bt_send[46] = 0x01;
        bt_send[47] = 0x0B;
        bt_send[48] = 0x04;
        bt_send[49] = 0x03;
        bt_send[50] = Utils.HexString2Bytes(IeeeAddr)[0];
        bt_send[51] = Utils.HexString2Bytes(IeeeAddr)[1];
        bt_send[52] = Utils.HexString2Bytes(IeeeAddr)[2];
        bt_send[53] = Utils.HexString2Bytes(IeeeAddr)[3];
        bt_send[54] = Utils.HexString2Bytes(IeeeAddr)[4];
        bt_send[55] = Utils.HexString2Bytes(IeeeAddr)[5];
        bt_send[56] = Utils.HexString2Bytes(IeeeAddr)[6];
        bt_send[57] = Utils.HexString2Bytes(IeeeAddr)[7];
        bt_send[58] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[59] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[59] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    // ------------------------------------- 设备相关end--------------------------------------
}

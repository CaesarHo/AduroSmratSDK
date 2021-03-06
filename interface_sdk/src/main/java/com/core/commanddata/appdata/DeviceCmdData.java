package com.core.commanddata.appdata;

import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.CRC8Utils;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

/**
 * Created by best on 2016/9/23.
 */

public class DeviceCmdData {

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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CHECK_NEW_DEVICE.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x00;
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[15] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[15] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 发送Active Req命令
     *
     * @param mac
     * @param shortaddr
     * @return
     */
    public static byte[] ActiveReqDeviceCmd(String mac, String shortaddr) {
        byte[] bt_send = new byte[36];
        //415050c0a8031101018f
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体 0100020019
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x14;
        //数据体头415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号010045
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_ACTIVE_ENDPOINT_REQUEST.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ACTIVE_ENDPOINT_REQUEST.value();      //(byte) 0x45;
        //mac地址 00158d0000ecc969
        bt_send[23] = TransformUtils.HexString2Bytes(mac)[0];
        bt_send[24] = TransformUtils.HexString2Bytes(mac)[1];
        bt_send[25] = TransformUtils.HexString2Bytes(mac)[2];
        bt_send[26] = TransformUtils.HexString2Bytes(mac)[3];
        bt_send[27] = TransformUtils.HexString2Bytes(mac)[4];
        bt_send[28] = TransformUtils.HexString2Bytes(mac)[5];
        bt_send[29] = TransformUtils.HexString2Bytes(mac)[6];
        bt_send[30] = TransformUtils.HexString2Bytes(mac)[7];
        //数据长度
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x02;
        bt_send[33] = TransformUtils.HexString2Bytes(shortaddr)[0];
        bt_send[34] = TransformUtils.HexString2Bytes(shortaddr)[1];

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[35] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[35] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_IDENTIFY_SEND.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_IDENTIFY_SEND.value();      //(byte) 0x70;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = 0x00;
        bt_send[39] = (byte) second;//持续时间

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value();      //(byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x06;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) value;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[39] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value();      //(byte) 0x81;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = 0x00;//不带有开关
        bt_send[39] = (byte) value;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[42] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 改变设备颜色
     *
     * @param hue
     * @return
     */
    public static byte[] setDeviceHueCmd(AppDevice appDevice, int hue) {
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_HUE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_HUE.value();      //(byte) 0xB0;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) hue;
        bt_send[39] = 0x00;//不带有开关
        bt_send[40] = 0x00;
        bt_send[41] = 0x08;
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[42] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
//        byte bt_crc8 = (CRC8Utils.calc(bt_send, bt_send.length));
//        String hex = Integer.toHexString(bt_crc8 & 0xFF);
//        byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);
//
//        byte[] bt_send_data = TransformUtils.byteMerger(bt_send, bt_crcdata);
        return bt_send;
    }

    /**
     * 改变设备饱和度
     *
     * @param hue
     * @param sat
     * @return
     */
    public static byte[] setDeviceHueSatCmd(AppDevice appDevice, int hue, int sat) {
        byte[] bt_send = new byte[41];
        //415050c0a80169010195 010002 0019415f5a494701007000158d0000aed8460007023e7e0102000483
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_HUE_SATURATION.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte)  MessageType.B.E_SL_MSG_MOVE_TO_HUE_SATURATION.value();      //(byte) 0xB6;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x07;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) hue;
        bt_send[39] = (byte) sat;
//        bt_send[40] = 0x00;//不带有开关
//        bt_send[41] = 0x00;
//        bt_send[42] = 0x00;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 改变设备色温值
     *
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_COLOUR_TEMPERATURE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_COLOUR_TEMPERATURE.value();      //(byte) 0xC0;
        //mac地址    00158d0001310dfe
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   000902
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x02;
        //d206010100f40000ea
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) (value >> 8);//不带有开关
        bt_send[39] = (byte) value;
        bt_send[40] = 0x00;
        bt_send[41] = 0x00;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[42] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[42] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 修改设备名称cmd
     *
     * @return
     */
    public static byte[] sendUpdateDeviceCmd(AppDevice appDevice, String device_name) {
        byte[] strTobt = null;
        try {
            strTobt = device_name.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int device_name_len = strTobt.length;
        int data_style_len = 20 + device_name_len;
        int data_len = 2 + device_name_len;

        //组名称 726f6f6d  31
        String dev_name = "";
        byte[] dev_name_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            dev_name += Integer.toHexString(strTobt[i] & 0xFF);
            dev_name_data = TransformUtils.HexString2Bytes(dev_name);
        }

        byte[] bt_send = new byte[35];
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体 010015001d
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CHANGE_DEVICE_NAME.value();//枚举A
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//以下数据长度
        //数据体头415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;//枚举B
        //mac地址 00158d0000737221
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度000b02dcd101dc0004667566662a
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;//以下数据长度
        bt_send[33] = (byte) (device_name_len >> 8);
        bt_send[34] = (byte) device_name_len;

        byte[] bt_send_data = TransformUtils.byteMerger(bt_send, dev_name_data);

        //将前面数据CRC8校验  7d
        byte bt_crc8 = (CRC8Utils.calc(bt_send_data, bt_send_data.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);

        byte[] bt_data = TransformUtils.byteMerger(bt_send_data, bt_crcdata);
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[23] = TransformUtils.HexString2Bytes(mac)[0];
        bt_send[24] = TransformUtils.HexString2Bytes(mac)[1];
        bt_send[25] = TransformUtils.HexString2Bytes(mac)[2];
        bt_send[26] = TransformUtils.HexString2Bytes(mac)[3];
        bt_send[27] = TransformUtils.HexString2Bytes(mac)[4];
        bt_send[28] = TransformUtils.HexString2Bytes(mac)[5];
        bt_send[29] = TransformUtils.HexString2Bytes(mac)[6];
        bt_send[30] = TransformUtils.HexString2Bytes(mac)[7];
        //数据长度   00 00
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * 读属性
     *
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value() >> 8);//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value();//枚举B

        //mac地址    00158d0001310e1b
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x0E;//下面数据长度

        bt_send[33] = (byte) 0x02;//段地址模式   02f1bc
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//0101
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = TransformUtils.HexString2Bytes(value)[0];//(byte) (value << 8);//ClusterID    0006000000000100019a
        bt_send[39] = TransformUtils.HexString2Bytes(value)[1];//(byte) value;//ClusterID

        bt_send[40] = 0x00;//Direction  填0
        bt_send[41] = 0x00;//ManuSpecific  填0
        bt_send[42] = 0x00;//ManuID    填0
        bt_send[43] = 0x00;//ManuID    填0

        bt_send[44] = 0x01;//AttribCount   00

        bt_send[45] = 0x00;
        bt_send[46] = 0x00;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[47] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[47] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 读取ZoneType属性
     *
     * @param devicemac
     * @return
     */
    public static byte[] ReadZoneTypeCmd(String devicemac, String shortaddr) {
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体0100 08 0020
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.UPLOAD_DEVICE_INFO.value();//数据体类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x20;//数据体长度
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value() >> 8);//(byte) 0x01;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_READ_ATTRIBUTE_REQUEST.value();      //(byte) 0x00;//枚举B

        //mac地址    00158d0000ecc9a7
        bt_send[23] = TransformUtils.HexString2Bytes(devicemac)[0];
        bt_send[24] = TransformUtils.HexString2Bytes(devicemac)[1];
        bt_send[25] = TransformUtils.HexString2Bytes(devicemac)[2];
        bt_send[26] = TransformUtils.HexString2Bytes(devicemac)[3];
        bt_send[27] = TransformUtils.HexString2Bytes(devicemac)[4];
        bt_send[28] = TransformUtils.HexString2Bytes(devicemac)[5];
        bt_send[29] = TransformUtils.HexString2Bytes(devicemac)[6];
        bt_send[30] = TransformUtils.HexString2Bytes(devicemac)[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x0E;//下面数据长度
        bt_send[33] = (byte) 0x02;//段地址模式   02d008
        bt_send[34] = TransformUtils.HexString2Bytes(shortaddr)[0];
        bt_send[35] = TransformUtils.HexString2Bytes(shortaddr)[1];
        bt_send[36] = 0x01;//01ff
        bt_send[37] = (byte) 0xFF; //Utils.HexString2Bytes(main_point)[0];//目标端点
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[47] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[47] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 保存設備ZoneType
     *
     * @param devicemac
     * @param shortaddr
     * @param main_point
     * @return
     */
    public static byte[] SaveZoneTypeCmd(String devicemac, String shortaddr, int main_point, short zonetype) {
        //415050c0 a8010a0101ed
        byte[] bt_send = new byte[36];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100080020
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.SAVE_ZONE_TYPE.value();//数据体类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x14;//数据体长度
        //数据体-----头   415f5a4947010100
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;//枚举B

        //mac地址    00158d0000ecc9a7
        bt_send[23] = TransformUtils.HexString2Bytes(devicemac)[0];
        bt_send[24] = TransformUtils.HexString2Bytes(devicemac)[1];
        bt_send[25] = TransformUtils.HexString2Bytes(devicemac)[2];
        bt_send[26] = TransformUtils.HexString2Bytes(devicemac)[3];
        bt_send[27] = TransformUtils.HexString2Bytes(devicemac)[4];
        bt_send[28] = TransformUtils.HexString2Bytes(devicemac)[5];
        bt_send[29] = TransformUtils.HexString2Bytes(devicemac)[6];
        bt_send[30] = TransformUtils.HexString2Bytes(devicemac)[7];
        bt_send[31] = (byte) 0x00;//下面数据长度    000e
        bt_send[32] = (byte) 0x02;//下面数据长度

//        bt_send[33] = (byte) 0x02;//段地址模式   02d008
//        bt_send[34] = TransformUtils.HexString2Bytes(shortaddr)[0];
//        bt_send[35] = TransformUtils.HexString2Bytes(shortaddr)[1];
//        bt_send[36] = 0x01;   //01ff
//        bt_send[37] = (byte) main_point;//目标端点
        bt_send[33] = (byte) (zonetype >> 8); //zonetype  0500
        bt_send[34] = (byte) zonetype;        //zonetype

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[35] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[35] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    public static byte[] BindDeviceCmd(AppDevice appDevice, String IeeeAddr) {
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
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
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
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_BIND.value();      //(byte) 0x92;
        //mac地址    00124b0001dd7ac1   124b0001dd7ac1
        bt_send[23] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00 06  02   f767 01 0C 02 4c
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x1A;
        bt_send[33] = (byte) 0x02;
        bt_send[34] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = TransformUtils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = TransformUtils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[39] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[40] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[41] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[42] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[43] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[44] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[45] = TransformUtils.HexString2Bytes(appDevice.getDeviceMac())[7];
        bt_send[46] = 0x01;
        bt_send[47] = 0x0B;
        bt_send[48] = 0x04;
        bt_send[49] = 0x03;
        bt_send[50] = TransformUtils.HexString2Bytes(IeeeAddr)[0];
        bt_send[51] = TransformUtils.HexString2Bytes(IeeeAddr)[1];
        bt_send[52] = TransformUtils.HexString2Bytes(IeeeAddr)[2];
        bt_send[53] = TransformUtils.HexString2Bytes(IeeeAddr)[3];
        bt_send[54] = TransformUtils.HexString2Bytes(IeeeAddr)[4];
        bt_send[55] = TransformUtils.HexString2Bytes(IeeeAddr)[5];
        bt_send[56] = TransformUtils.HexString2Bytes(IeeeAddr)[6];
        bt_send[57] = TransformUtils.HexString2Bytes(IeeeAddr)[7];
        bt_send[58] = 0x01;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[59] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[59] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }
}

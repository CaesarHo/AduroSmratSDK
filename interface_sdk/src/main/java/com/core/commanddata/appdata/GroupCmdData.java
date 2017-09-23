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

public class GroupCmdData {
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_ALL_GROUP.value();
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;;//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();        //0xFF;;//(byte) 0xFF;
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
     * 添加Group
     *
     * @param groupname
     * @return
     */
    public static byte[] sendAddGroupCmd(String groupname, int dev_count, String merge_mac) {
        byte[] strTobt = null;
        try {
            strTobt = groupname.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设备个数及其mac地址数据
        byte[] bt_dev_count = new byte[1];
        bt_dev_count[0] = (byte) dev_count;
        byte[] bt_dev = TransformUtils.HexString2Bytes(merge_mac);

        int group_name_len = strTobt.length;
        int data_style_len;
        int data_len;
        if (dev_count == 0) {
            data_style_len = 20 + groupname.length();
            data_len = 2 + group_name_len;
        } else {
            data_style_len = 20 + group_name_len + bt_dev_count.length + bt_dev.length;
            data_len = 2 + group_name_len + bt_dev_count.length + bt_dev.length;
        }

        //组名称 726f6f6d  31
        String group_str = "";
        byte[] group_name_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            group_str += Integer.toHexString(strTobt[i] & 0xFF);
            group_name_data = TransformUtils.HexString2Bytes(group_str);
        }

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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.ADD_GROUP_NAME.value();//数据类型
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();       //(byte) 0xFF;
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
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;

        //组名称长度   0004
        bt_send[33] = (byte) (group_name_len >> 8);
        bt_send[34] = (byte) group_name_len;
        if (dev_count == 0) {
            //固定数据与group数据相加
            byte[] bt_send_data = TransformUtils.byteMerger(bt_send, group_name_data);
            //将前面数据CRC8校验
            byte bt_crc8 = (CRC8Utils.calc(bt_send_data, bt_send_data.length));
            String hex = Integer.toHexString(bt_crc8 & 0xFF);
            byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);
            //Cmd 数据与CRC8相加
            byte[] bt_send_cmd = TransformUtils.byteMerger(bt_send_data, bt_crcdata);
            return bt_send_cmd;
        } else {
            //固定数据与group数据相加
            byte[] bt_send_data = TransformUtils.byteMerger(bt_send, group_name_data);
            //设备个数及其mac地址数据
            byte[] count_mac = TransformUtils.byteMerger(bt_dev_count, bt_dev);
            byte[] bt_merge = TransformUtils.byteMerger(bt_send_data, count_mac);
            //将前面数据CRC8校验
            byte bt_crc8 = (CRC8Utils.calc(bt_merge, bt_merge.length));
            String hex = Integer.toHexString(bt_crc8 & 0xFF);
            byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);
            //Cmd 数据与CRC8相加
            byte[] bt_send_cmd = TransformUtils.byteMerger(bt_merge, bt_crcdata);
            return bt_send_cmd;
        }
    }

    /**
     * 添加一个设备到组里面
     *
     * @param group_id
     * @return
     */
    public static byte[] Add_DeviceToGroup(AppDevice appDevice, int group_id) {
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
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.ADD_DEVICE_TO_GROUP.value();
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_ADD_GROUP.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ADD_GROUP.value();       //(byte) 0x60;
        //mac地址    00124b0001dd7ac1
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
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value();       //(byte) 0x92;
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[39] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] setGroupLevel(int group_id, int value) {
        byte[] bt_send = new byte[41];
        //415050c0a8010801013b
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
        //消息体0100020019415f5a494701008100124b00076afe09000701000201ff00fc91
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value();       //(byte) 0x81;
        //mac地址    00124b0001dd7ac1
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
        bt_send[37] = (byte) 0xFF;//目标端点
        bt_send[38] = 0x01;
        bt_send[39] = (byte) value;//状态值

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * 从组里删除Device
     *
     * @return
     */
    public static byte[] DeleteDeviceFromGroup(AppDevice appDevice, int group_id) {
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
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.REMOVE_GROUP.value();
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_REMOVE_GROUP.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_REMOVE_GROUP.value();       //(byte) 0x63;
        //mac地址    00124b0001dd7ac1
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
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;

        //CRC校验吗
        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[40] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[40] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018  0100100016
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CHANGE_GROUP_NAME.value();//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = 0x14;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();        //(byte) 0xFF;
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
        bt_send[32] = (byte) 0x02;

        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[35] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[35] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }


    /**
     * change名称
     *
     * @param groupname
     * @return
     */
    public static byte[] sendUpdateGroupCmd(int group_id, String groupname) {
        byte[] strTobt = null;
        try {
            strTobt = groupname.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int group_name_len = strTobt.length;
        int data_style_len = 22 + group_name_len;
        int data_len = 4 + group_name_len;

        //组名称 726f6f6d  31
        String group_str = "";
        byte[] group_name_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            group_str += Integer.toHexString(strTobt[i] & 0xFF);
            group_name_data = TransformUtils.HexString2Bytes(group_str);
        }

        byte[] bt_send = new byte[37];
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
        //消息体010010001d
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CHANGE_GROUP_NAME.value();//数据类型 枚举A
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//数据体长度  1b = 27
        //数据体头415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();        //(byte) 0xFF;
        //macaddr 00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度000b
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;
        // group_id 0002
        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        //组名称长度 0007 6b69746368656e60
        bt_send[35] = (byte) (group_name_len >> 8);
        bt_send[36] = (byte) group_name_len;

        //固定数据与group数据相加
        byte[] bt_send_data = TransformUtils.byteMerger(bt_send, group_name_data);

        //将前面数据CRC8校验  7d
        byte bt_crc8 = (CRC8Utils.calc(bt_send_data, bt_send_data.length));

        //crc8 转成需要的类型
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = TransformUtils.byteMerger(bt_send_data, bt_crcdata);

        return bt_send_cmd;
    }

    public static byte[] AddDeviceFromFileCmd(short group_id, int dev_count, String merge_mac) {

        //设备个数及其mac地址数据
        byte[] bt_dev_count = new byte[1];
        bt_dev_count[0] = (byte) dev_count;
        byte[] bt_dev = TransformUtils.HexString2Bytes(merge_mac);

        int data_style_len = 20 + bt_dev_count.length + bt_dev.length;
        int data_len = 2 + bt_dev_count.length + bt_dev.length;

        byte[] bt_send = new byte[35];
        //415050c0a8010801013b
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
        //消息体010020001d
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.ADD_DEVICE_FORM_GROUP_FILE.value();//数据类型
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//数据体长度
        //数据体头415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();       //(byte) 0xFF;
        //macaddr 00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度000b0006
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;
        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        //设备个数及其mac地址数据 01 00158d0000737221 f5
        byte[] count_mac = TransformUtils.byteMerger(bt_dev_count, bt_dev);
        byte[] bt_merge = TransformUtils.byteMerger(bt_send, count_mac);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8Utils.calc(bt_merge, bt_merge.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = TransformUtils.byteMerger(bt_merge, bt_crcdata);

        return bt_send_cmd;
    }

    public static byte[] DeleteDeviceFormFileCmd(short group_id, int dev_count, String merge_mac) {

        //设备个数及其mac地址数据
        byte[] bt_dev_count = new byte[1];
        bt_dev_count[0] = (byte) dev_count;
        byte[] bt_dev = TransformUtils.HexString2Bytes(merge_mac);

        int data_style_len = 20 + bt_dev_count.length + bt_dev.length;
        int data_len = 2 + bt_dev_count.length + bt_dev.length;

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

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, 9))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, 9));
            bt_send[9] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.DELETE_DEVICE_FORM_GROUP_FILE.value();//数据类型
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();       //(byte) 0xFF;
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
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;
        bt_send[33] = (byte) (group_id >> 8);
        bt_send[34] = (byte) group_id;

        //设备个数及其mac地址数据
        byte[] count_mac = TransformUtils.byteMerger(bt_dev_count, bt_dev);
        byte[] bt_merge = TransformUtils.byteMerger(bt_send, count_mac);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8Utils.calc(bt_merge, bt_merge.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = TransformUtils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = TransformUtils.byteMerger(bt_merge, bt_crcdata);

        return bt_send_cmd;
    }

    public static byte[] AllDeviceSwitchCmd(int state) {
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value() >> 8);
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ONOFF_NOEFFECTS.value();
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
        bt_send[32] = (byte) 0x06;
        bt_send[33] = (byte) 0x04;
        bt_send[34] = 0x00;
        bt_send[35] = (byte) 0xff;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = (byte) 0xff;//目标端点
        bt_send[38] = (byte) state;

        if (!TransformUtils.isCRC8Value(TransformUtils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = TransformUtils.StringToHexString(TransformUtils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = TransformUtils.HexString2Bytes(ss)[0];
        } else {
            bt_send[39] = TransformUtils.HexString2Bytes(TransformUtils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    public static byte[] AllDeviceLevelCmd(int levelValue) {
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
        //消息体  01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CONTROL_DEVICE.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x1B;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value() >> 8);
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_MOVE_TO_LEVEL_ONOFF.value();
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
        bt_send[32] = (byte) 0x09;
        bt_send[33] = (byte) 0x04;
        bt_send[34] = 0x00;
        bt_send[35] = (byte) 0xfc;
        bt_send[36] = 0x01;//源端点
        bt_send[37] = (byte) 0xff;//目标端点
        bt_send[38] = 0x01;
        bt_send[39] = (byte) levelValue;
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
}

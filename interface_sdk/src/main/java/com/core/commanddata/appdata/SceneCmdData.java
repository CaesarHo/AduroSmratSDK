package com.core.commanddata.appdata;

import android.util.Log;

import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.CRC8;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

/**
 * Created by best on 2016/9/23.
 */

public class SceneCmdData {
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

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_ALL_SCENE.value();
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
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    /**
     * 添加添加名称
     *
     * @param scenename
     * @return
     */
    public static byte[] sendAddSceneCmd(String scenename, int group_id,int dev_count,String merge_mac) {
        byte[] strTobt = null;
        try {
            strTobt = scenename.getBytes("gb2312");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设备个数及其mac地址数据
        byte[] bt_dev_count = new byte[1];
        bt_dev_count[0] = (byte) dev_count;
        byte[] bt_dev = Utils.HexString2Bytes(merge_mac);

        int scene_name_len = strTobt.length;//场景名称长度
        int data_style_len = 22 + scene_name_len + bt_dev_count.length + bt_dev.length;
        int data_len = 4 + scene_name_len + bt_dev_count.length + bt_dev.length;

        byte[] bt_send = new byte[35];
        //415050c0a8010701017c
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
        //消息体0100120023
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.ADD_SCENE_NAME.value();//数据类型
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
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度0011
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;

        //场景名称长度000f
        bt_send[33] = (byte) (scene_name_len >> 8);
        bt_send[34] = (byte) scene_name_len;

        //场景名称e68e8fe7a9bae4ba86e5bfabe4b990 0002 7e
        String scene_name = "";
        byte[] scene_name_data = null;
        for (int i = 0; i < scene_name_len; i++) {
            scene_name += Integer.toHexString(strTobt[i] & 0xFF);
            scene_name_data = Utils.HexString2Bytes(scene_name);
        }
        //固定数据与Scene数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, scene_name_data);

        //设备个数及其mac地址数据
        byte[] count_mac = FtFormatTransfer.byteMerger(bt_dev_count,bt_dev);
        byte[] bt_merge = FtFormatTransfer.byteMerger(bt_send_data,count_mac);

        //固定数据与Scene数据相加的结果在与组ID相加  0001
        byte[] group_id_bt = new byte[2];
        group_id_bt[0] = (byte) (group_id >> 8);
        group_id_bt[1] = (byte)  group_id;

        byte[] final_data = FtFormatTransfer.byteMerger(bt_merge, group_id_bt);

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
        bt_send[12] = MessageType.A.CHANGE_SCENE_NAME.value();//数据类型
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
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
     * @return
     */
    public static byte[] Add_DeviceToScene(AppDevice appDevice, int group_id, int scene_id) {
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
        bt_send[12] = MessageType.A.ADD_SCENE.value();//数据类型
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_ADD_SCENE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_ADD_SCENE.value();      //(byte) 0xA1;
        //mac地址    00158d0001310e4e
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00120255130101
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x0c;
        bt_send[33] = (byte) 0x02;//短地址模式
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;
        bt_send[40] = (byte) scene_id;//000218000000006e
        bt_send[41] = 0x00;
        bt_send[42] = 0x00;
        bt_send[43] = 0x00;
        bt_send[44] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[45] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[45] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 存储场景至设备
     */
    public static byte[] StoreScene(AppDevice appDevice, int group_id, int scene_id) {
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
        bt_send[12] = MessageType.A.ADD_SCENE.value();//数据类型
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_STORE_SCENE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_STORE_SCENE.value();      //(byte) 0xA4;
        //mac地址    00158d0001310e4e
        bt_send[23] = Utils.HexString2Bytes(appDevice.getDeviceMac())[0];
        bt_send[24] = Utils.HexString2Bytes(appDevice.getDeviceMac())[1];
        bt_send[25] = Utils.HexString2Bytes(appDevice.getDeviceMac())[2];
        bt_send[26] = Utils.HexString2Bytes(appDevice.getDeviceMac())[3];
        bt_send[27] = Utils.HexString2Bytes(appDevice.getDeviceMac())[4];
        bt_send[28] = Utils.HexString2Bytes(appDevice.getDeviceMac())[5];
        bt_send[29] = Utils.HexString2Bytes(appDevice.getDeviceMac())[6];
        bt_send[30] = Utils.HexString2Bytes(appDevice.getDeviceMac())[7];
        //数据长度   00120255130101
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x08;
        bt_send[33] = (byte) 0x02;//短地址模式
        bt_send[34] = Utils.HexString2Bytes(appDevice.getShortaddr())[0];
        bt_send[35] = Utils.HexString2Bytes(appDevice.getShortaddr())[1];
        bt_send[36] = 0x01;//源端点
        bt_send[37] = Utils.HexString2Bytes(appDevice.getEndpoint())[0];//目标端点
        bt_send[38] = (byte) (group_id >> 8);
        bt_send[39] = (byte) group_id;
        bt_send[40] = (byte) scene_id;//000218000000006e

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[41] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[41] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /**
     * 从场景里删除Device
     *
     * @param scene_id
     * @return
     */
    public static byte[] DeleteDeviceFromScene(AppDevice appDevice, int scene_id) {
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
        bt_send[12] = MessageType.A.DELETE_SCENE.value();//数据类型
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_REMOVE_SCENE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_REMOVE_SCENE.value();      //(byte) 0xA2;
        //mac地址    00124b0001dd7ac1
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
     */
    public static byte[] sendUpdateSceneCmd(int scene_id, String scenename) {
        byte[] strTobt = null;
        try {
            strTobt = scenename.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int scene_name_len = strTobt.length;
        int data_style_len = 21 + scene_name_len;
        int data_len = 3 + scene_name_len;

        //组名称 726f6f6d  31
        String scene_str = "";
        byte[] scene_name_data = null;
        for (int i = 0; i < strTobt.length; i++) {
            scene_str += Integer.toHexString(strTobt[i] & 0xFF);
            System.out.println("scene_str = " + scene_str);
            scene_name_data = Utils.HexString2Bytes(scene_str);
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

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            System.out.println("打印crc8结果false = " + Utils.CrcToString(bt_send, 9));
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            Log.i("ss = ", ss);
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            System.out.println("打印crc8结果true = " + Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   01001000    010010001b
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CHANGE_SCENE_NAME.value();//数据类型
        bt_send[13] = (byte) (data_style_len >> 8);
        bt_send[14] = (byte) data_style_len;//数据体长度  1b = 27
        //数据体头   415f5a4947   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff     01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
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
        bt_send[31] = (byte) (data_len >> 8);
        bt_send[32] = (byte) data_len;
        // group_id  00a2
        bt_send[33] = (byte) scene_id;

        //组名称长度   0005
        bt_send[34] = (byte) (scene_name_len >> 8);
        bt_send[35] = (byte) scene_name_len;

        //固定数据与group数据相加
        byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, scene_name_data);

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
        bt_send[12] = MessageType.A.CONTROL_SCENE.value();//数据类型
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_RECALL_SCENE.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_RECALL_SCENE.value();      //(byte) 0xA5;
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
}

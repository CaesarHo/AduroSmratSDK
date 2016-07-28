package com.utils;


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

    public static byte[] GetAllDeviceList(){
        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte)192;
        bt_send[4] = (byte)168;
        bt_send[5] = (byte)1;
        bt_send[6] = (byte)103;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;
        bt_send[9] = (byte)0xB9;
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
        bt_send[21] = (byte)0xFF;
        bt_send[22] = (byte)0xFF;
        bt_send[23] = (byte)0x00;
        bt_send[24] = (byte)0x12;
        bt_send[25] = (byte)0x4b;
        bt_send[26] = (byte)0x00;
        bt_send[27] = (byte)0x07;
        bt_send[28] = (byte)0x6a;
        bt_send[29] = (byte)0xfe;
        bt_send[30] = (byte)0x09;
        bt_send[31] = (byte)0x00;
        bt_send[32] = (byte)0x00;
        bt_send[33] = (byte)0xA9;
        return bt_send;
    }
}

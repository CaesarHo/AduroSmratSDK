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
        bt_msg[6] = 0x67;
        bt_msg[7] = 0x01;
        bt_msg[8] = 0x01;
        bt_msg[9] = (byte)0xB9;//消息校验码
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
}

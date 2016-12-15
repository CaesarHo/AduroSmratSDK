package com.core.commanddata.appdata;

import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.CRC8;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by best on 2016/12/9.
 */

public class GatewayCmdData {
    //获取网关MAC地址和固件版本信息
    public static byte[] GetGwInfoCmd() {
        byte[] bt_send = new byte[34];
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
            bt_send[9] = Utils.HexString2Bytes(com.core.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_GW_INFO.value();
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体头  415F5A4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01 00 92
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0x70;
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
        bt_send[31] = 0x00;
        bt_send[32] = 0x00;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
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
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
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
        bt_send[22] = (byte)  MessageType.B.E_SL_MSG_IEEE_ADDRESS_REQUEST.value();      //(byte) 0x70;
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

    /**
     * 获取网关协调器的软件版本
     *
     * @return
     */
    public static byte[] GetNodeVerCmd() {
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
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(com.core.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_NODE_VER.value();
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_GET_NODE_VER.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_GET_NODE_VER.value();      //(byte) 0x70;
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

    public static byte[] FactoryResetCmd(String pwd) {
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
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(com.core.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0002 0018
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.FACTORY_RESET.value();
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
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0x70;
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
        bt_send[31] = 0x00;
        bt_send[32] = 0x06;

        byte[] bt = null;
        try {
            bt = FtFormatTransfer.stringToBytes(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bt_send[33] = bt[0];
        bt_send[34] = bt[1];
        bt_send[35] = bt[2];
        bt_send[36] = bt[3];
        bt_send[37] = bt[4];
        bt_send[38] = bt[5];

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[39] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[39] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }

    //获取网关MAC地址和固件版本信息
    public static byte[] GetSetGWServerAddress(String address) {
        byte[] address_bt = null;
        try {
            address_bt = address.getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int address_len = address_bt.length;
        int bt_send_len = 18 + address_len;
        byte[] bt_send = new byte[33];
        //415050c0a8005d0101 c9
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
            bt_send[9] = Utils.HexString2Bytes(com.core.utils.Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体 01 0083 0023
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_SET_SERVER_ADDRESS.value();
        bt_send[13] = (byte) (bt_send_len >> 8);
        bt_send[14] = (byte) bt_send_len;
        //数据体头 415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte) (MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0x00;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0x70;
        //mac地址 00000000 00000000
        bt_send[23] = 0x00;
        bt_send[24] = 0x00;
        bt_send[25] = 0x00;
        bt_send[26] = 0x00;
        bt_send[27] = 0x00;
        bt_send[28] = 0x00;
        bt_send[29] = 0x00;
        bt_send[30] = 0x00;
        //数据长度 0013 64 61 74 61 2e 61 64 75 72 6f 73 6d 61 72 74 2e 63 6f 6d
        bt_send[31] = (byte) (address_len >> 8);
        bt_send[32] = (byte) address_len;
        if (address_len == 0) {
            //将前面数据CRC8校验  7d
            byte bt_crc8 = (CRC8.calc(bt_send, bt_send.length));
            String hex = Integer.toHexString(bt_crc8 & 0xFF);
            byte[] bt_crcdata = Utils.HexString2Bytes(hex);
            byte[] bt_data = FtFormatTransfer.byteMerger(bt_send, bt_crcdata);
            return bt_data;
        } else {
            byte[] bt = null;
            try {
                bt = FtFormatTransfer.stringToBytes(address);
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] bt_send_data = FtFormatTransfer.byteMerger(bt_send, bt);

            //将前面数据CRC8校验  7d
            byte bt_crc8 = (CRC8.calc(bt_send_data, bt_send_data.length));
            String hex = Integer.toHexString(bt_crc8 & 0xFF);
            byte[] bt_crcdata = Utils.HexString2Bytes(hex);

            byte[] bt_data = FtFormatTransfer.byteMerger(bt_send_data, bt_crcdata);
            return bt_data;
        }
    }
}

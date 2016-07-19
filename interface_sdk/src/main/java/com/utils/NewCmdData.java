package com.utils;


import android.util.Log;

import com.interfacecallback.UDPHelper;

/**
 * Created by best on 2016/6/24.
 */
public class NewCmdData {

    //Allows devices to access
    public static class allow_devices_access{
        //第一段
        public String herder = "APP";
        public String local_Ip;
        public byte[] msg_herder_no = new byte[0];
        public byte[] msg_segments_body = new byte[0];

        //第二段

        public short Length;

        public byte[] sendContent = new byte[20];

        public allow_devices_access(){
            this.Length = 0;
            this.local_Ip = UDPHelper.ip;
            this.msg_herder_no[0] = 0x01;
            this.msg_segments_body[0] = 0x01;
        }

        public byte[] toBytes() {
            byte[] temp = null;
            //数据体头"APP"
            System.arraycopy(herder.getBytes(), 0, sendContent, 0, herder.length());

            //本地IP地址
            temp = local_Ip.getBytes();
            System.arraycopy(temp, 0, sendContent, herder.length(), temp.length);

            //消息头序号
            temp = msg_herder_no;
            System.arraycopy(temp, 0, sendContent, herder.length()
                    + local_Ip.getBytes().length + msg_herder_no.length, temp.length);
            //消息段数segments
            temp = msg_segments_body;
            System.arraycopy(temp, 0, sendContent,herder.length() + local_Ip.getBytes().length
                    + msg_herder_no.length + msg_segments_body.length, temp.length);

            //校验码
            byte crc = CRC8.calcCrc8(temp);
            temp = Byte.toString(crc).getBytes();
            System.out.println("TAG" + Integer.toHexString(0x00ff & crc));
            System.arraycopy(temp, 0, sendContent,herder.length() + local_Ip.getBytes().length
                    + msg_herder_no.length + msg_segments_body.length + Byte.toString(crc).getBytes().length, temp.length);








            int data_len = herder.length() + local_Ip.getBytes().length
                    + msg_herder_no.length + msg_segments_body.length + Byte.toString(crc).getBytes().length;


            Log.i("DATA_LEN = " ,String.valueOf(data_len));

            return sendContent;
        }
    }



    //rep添加回复
    public static class add_cmd_add_Rep {
        public String herder = "besbell.";

        public int id;
        public short Cmd;
        public short Length;
        public int app_id;
        public int error_type;
        public byte[] sendContent = new byte[20];

        public add_cmd_add_Rep() {
            this.Length = 0;
            this.Cmd = 0;
            this.id = 0;
            this.app_id = 0;
        }

        public void parseBytes(byte[] data) {
            byte[] tempInt = new byte[4];
            byte[] tempShort = new byte[2];
            byte[] tempString = new byte[8];

            System.arraycopy(data, 0, tempString, 0, 8);
            herder = FtFormatTransfer.bytesToUTF8String(tempString);

            System.arraycopy(data, 8, tempInt, 0, 4);
            id = FtFormatTransfer.byteToint_LH(tempInt);

            System.arraycopy(data, 12, tempShort, 0, 2);
            Cmd = FtFormatTransfer.lBytesToShort(tempShort);

            System.arraycopy(data, 14, tempShort, 0, 2);
            Length = FtFormatTransfer.lBytesToShort(tempShort);

            System.arraycopy(data, 16, tempInt, 0, 4);
            app_id = FtFormatTransfer.byteToint_LH(tempInt);

            System.arraycopy(data, 20, tempInt, 0, 4);
            error_type = FtFormatTransfer.byteToint_LH(tempInt);
        }
    }

    //req发现
    public static class add_cmd_detect {
        public String herder = "besbell.";

        public int id;
        public short Cmd;
        public short Length;
        public int app_id;

        public byte[] sendContent = new byte[20];

        public add_cmd_detect() {
            this.Length = 0;
            this.Cmd = 0;
            this.id = 0;
            this.app_id = 0;
        }

        public byte[] toBytes() {
            byte[] temp = null;
            System.arraycopy(herder.getBytes(), 0, sendContent, 0, herder.length());

            temp = FtFormatTransfer.intTobyte_LH(id);
            System.arraycopy(temp, 0, sendContent, 8, temp.length);

            temp = FtFormatTransfer.shortTobyte_LH(Cmd);
            System.arraycopy(temp, 0, sendContent, 12, temp.length);

            temp = FtFormatTransfer.shortTobyte_LH(Length);
            System.arraycopy(temp, 0, sendContent, 14, temp.length);

            temp = FtFormatTransfer.intTobyte_LH(app_id);
            System.arraycopy(temp, 0, sendContent, 16, temp.length);

            return sendContent;
        }
    }

    //rep发现回复
    public static class add_cmd_detect_Rep {
        public String herder = "besbell.";

        public int id;
        public short Cmd;
        public short Length;
        public int app_id;
        public int error_type;
        public byte[] sendContent = new byte[20];

        public add_cmd_detect_Rep() {
            this.Length = 0;
            this.Cmd = 0;
            this.id = 0;
            this.app_id = 0;
        }

        public void parseBytes(byte[] data) {
            byte[] tempInt = new byte[4];
            byte[] tempShort = new byte[2];
            byte[] tempString = new byte[8];

            System.arraycopy(data, 0, tempString, 0, 8);
            herder = FtFormatTransfer.bytesToUTF8String(tempString);

            System.arraycopy(data, 8, tempInt, 0, 4);
            id = FtFormatTransfer.byteToint_LH(tempInt);

            System.arraycopy(data, 12, tempShort, 0, 2);
            Cmd = FtFormatTransfer.lBytesToShort(tempShort);

            System.arraycopy(data, 14, tempShort, 0, 2);
            Length = FtFormatTransfer.lBytesToShort(tempShort);

            System.arraycopy(data, 16, tempInt, 0, 4);
            app_id = FtFormatTransfer.byteToint_LH(tempInt);

            System.arraycopy(data, 20, tempInt, 0, 4);
            error_type = FtFormatTransfer.byteToint_LH(tempInt);
        }
    }
}

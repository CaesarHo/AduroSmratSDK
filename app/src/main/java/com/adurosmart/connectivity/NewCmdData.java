package com.adurosmart.connectivity;

import com.adurosmart.utils.FtFormatTransfer;

/**
 * Created by best on 2016/6/24.
 */
public class NewCmdData {
    //req添加
    public static class add_cmd_add {
        public String herder = "besbell.";

        public int id;
        public short Cmd;
        public short Length;
        public int app_id;

        public byte[] sendContent = new byte[20];

        public add_cmd_add() {
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

package com.utils;

/**
 * Created by best on 2016/8/8.
 */
public class ParseData {
    //return data
    public static class ParseGroupInfo {
        public short mGroupID;
        public String mGroupName;

        public ParseGroupInfo(){
            mGroupID = 0;
            mGroupName = "";
        }

        public void parseBytes(byte[] data ,int len) {
            byte[] mId_Bt = new byte[2];
            byte[] mName_Bt = new byte[len];

            System.arraycopy(data, 32, mId_Bt, 0, 2);
            mGroupID = FtFormatTransfer.hBytesToShort(mId_Bt);

            System.arraycopy(data, 36, mName_Bt, 0, len);
            mGroupName = FtFormatTransfer.bytesToString(mName_Bt);
        }
    }
}

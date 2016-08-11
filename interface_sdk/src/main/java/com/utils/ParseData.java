package com.utils;

import java.util.Arrays;

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

    //解析delete group 返回的数据
    public static class ParseDeleteGroupResult{
        public short mGroupID;

        public ParseDeleteGroupResult(){
            mGroupID = 0;
        }

        public void parseBytes(byte[] data) {
            System.out.println("解析byte数组 = " + Arrays.toString(data));
            byte[] mId_Bt = new byte[2];

            System.arraycopy(data, 32, mId_Bt, 0, 2);
            mGroupID = FtFormatTransfer.hBytesToShort(mId_Bt);
        }
    }
}

package com.core.utils;

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


    public static class ParseAddSceneInfo {
        public short mSceneID;
        public String mSceneName;
        public short mGroupId;

        public ParseAddSceneInfo(){
            mSceneID = 0;
            mSceneName = "";
            mGroupId = 0;
        }

        public void parseBytes(byte[] data ,int len) {
            byte[] mGroupId_Bt = new byte[2];
            byte[] mName_Bt = new byte[len];

            byte mSceneId = data[32];
            int i = mSceneId & 0xff;
            mSceneID = (short)i;

            System.arraycopy(data, 35, mName_Bt, 0, len);
            mSceneName = FtFormatTransfer.bytesToString(mName_Bt);

            System.arraycopy(data,35 + len,mGroupId_Bt,0,2);
            mGroupId = FtFormatTransfer.hBytesToShort(mGroupId_Bt);
        }
    }


    /**
     * 解析修改场景名称
     */
    public static class ParseModifySceneInfo {
        public short mSceneID;
        public String mSceneName;

        public ParseModifySceneInfo(){
            mSceneID = 0;
            mSceneName = "";
        }

        public void parseBytes(byte[] data ,int len) {
            byte[] mName_Bt = new byte[len];

            byte mSceneId = data[32];
            int i = mSceneId & 0xff;
            mSceneID = (short)i;

            System.arraycopy(data, 35, mName_Bt, 0, len);
            mSceneName = FtFormatTransfer.bytesToString(mName_Bt);
        }
    }

    /**
     * 解析传感器上传数据
     * <415f5a4947 01 8401(Zigbee消息类型) 00158d0001479820(设备MAC)
     * 000c(数据长度) 01(设备的端点) 0500(簇ID) 02(短地址模式) ae4d(短地址)
     * 0000(传感器状态) 00(扩展状态) 00(ZoneID) 0000(Delay-触发到上报的时间)>
     */
    public static class ParseSensorData{
        public String mDevMac;
        public Short mSensorState;
        public String  mZigbeeType;
        public int state = -1;

        public ParseSensorData(){
            mDevMac = "";
            mSensorState = 0;
            mZigbeeType = "";
        }

        public void parseBytes(byte[] data) {
            byte[] mZigbee_bt = new byte[2];
            byte[] mSensorState_bt = new byte[2];
            byte[] mDevMac_bt = new byte[8];

            System.arraycopy(data,20, mZigbee_bt, 0, 2);
            for (int i = 0;i < mZigbee_bt.length;i++){
                String str_zero = "";
                if ( mZigbee_bt[i] >= 0 &&  mZigbee_bt[i] <= 16){
                    str_zero = "0" + (mZigbee_bt[i] & 0xFF);
                    mZigbeeType = mZigbeeType + str_zero;
                }else{
                    mZigbeeType = mZigbeeType + Integer.toHexString(mZigbee_bt[i] & 0xFF);
                }
            }

            if (!mZigbeeType.contains("8401")){
                return;
            }

            System.arraycopy(data,22,mDevMac_bt,0,8);
            String str_zero = "";
            for(int i = 0;i < mDevMac_bt.length; i++){
                if ( mDevMac_bt[i] >= 0 &&  mDevMac_bt[i] <= 16){
                    str_zero = "0" + (mDevMac_bt[i] & 0xFF);
                    mDevMac = mDevMac + str_zero;
                }else{
                    mDevMac = mDevMac + Integer.toHexString(mDevMac_bt[i] & 0xFF);
                }
            }

            System.arraycopy(data,38,mSensorState_bt,0,2);
            mSensorState = FtFormatTransfer.hBytesToShort(mSensorState_bt);
            state = (int)mSensorState & 0x1;
            System.out.println("state = " + state);
        }
    }
}

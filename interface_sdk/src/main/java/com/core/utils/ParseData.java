package com.core.utils;

import android.util.Log;

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
        public String mDevMac = "";
        public Short mSensorState;
        public String  mZigbeeType = "";
        public short data_len_s;
        public String srcEndpoint;
        public String clusterID = "";
        public int srcEndpoint_mode;
        public String shortaddr_str = "";
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

            //数据长度
            byte[] data_len = new byte[2];
            System.arraycopy(data,30,data_len,0,2);
            data_len_s = FtFormatTransfer.hBytesToShort(data_len);
            System.out.println("ParseSensorData dataLen =" + data_len_s);

            //源端点
            srcEndpoint = Integer.toHexString(data[32] & 0xFF);//data[32] & 0xFF;
            System.out.println("ParseSensorData srcpoint =" + srcEndpoint);

            //簇ID
            byte[] clusterid_bt = new byte[2];
            System.arraycopy(data,33,clusterid_bt,0,2);
            for (int i = 0;i < clusterid_bt.length;i++){
                String clusterID_zero = "";
                if ( clusterid_bt[i] >= 0 &&  clusterid_bt[i] <= 16){
                    clusterID_zero = "0" + (clusterid_bt[i] & 0xFF);
                    clusterID = clusterID + clusterID_zero;
                }else{
                    clusterID = clusterID + Integer.toHexString(clusterid_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseSensorData clusterID =" + clusterID);


            //短地址模式
            srcEndpoint_mode = data[35] & 0xFF;
            System.out.println("ParseSensorData point_mode =" + srcEndpoint_mode);

            //短地址
            byte[] shortaddress = new byte[2];
            System.arraycopy(data,36,shortaddress,0,2);
            for (int i = 0;i < shortaddress.length;i++){
                String shortaddr_zero = "";
                if ( shortaddress[i] >= 0 &&  shortaddress[i] <= 16){
                    shortaddr_zero = "0" + (shortaddress[i] & 0xFF);
                    shortaddr_str = shortaddr_str + shortaddr_zero;
                }else{
                    shortaddr_str = shortaddr_str + Integer.toHexString(shortaddress[i] & 0xFF);
                }
            }
            System.out.println("ParseSensorData shortAddr =" + shortaddr_str);

            //传感器状态
            System.arraycopy(data,38,mSensorState_bt,0,2);
            mSensorState = FtFormatTransfer.hBytesToShort(mSensorState_bt);
            state = (int)mSensorState & 0x1;
            System.out.println("ParseSensorData state = " + state);

            //当有传感器数据上传时读取属性，成功后保存zonetype

        }
    }


    /**
     * 解析设备属性
     */
    public static class ParseAttributeData{
        public String  mZigbeeType;
        public String mDevMac;
        public short data_len_s;
        public String shortaddr_str = "";
        public int srcEndpoint;
        public short attributeID;
        public int state = -1;
        public int attribValue = -1;
        public short clusterID;

        public ParseAttributeData(){
            mDevMac = "";
            mZigbeeType = "";
            shortaddr_str = "";
        }

        public void parseBytes(byte[] data) {
            byte[] mZigbee_bt = new byte[2];
            byte[] mDevMac_bt = new byte[8];

            //Zigbee串口类型
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
            if (!mZigbeeType.contains("8100")){
                return;
            }
            System.out.println("ParseAttributeData mZigbeeType = " + mZigbeeType);

            //设备MAC地址
            System.arraycopy(data,22,mDevMac_bt,0,8);
            String str_zero = "";
            for(int i = 0;i < mDevMac_bt.length; i++){
                if ( mDevMac_bt[i] >= 0 &&  mDevMac_bt[i] <= 16){
                    String hexTostr = Integer.toHexString(mDevMac_bt[i]);
                    str_zero = "0" + hexTostr;
                    mDevMac = mDevMac + str_zero;
                }else{
                    mDevMac = mDevMac + Integer.toHexString(mDevMac_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData mDevMac = " + mDevMac);

            //数据长度
            byte[] data_len = new byte[2];
            System.arraycopy(data,30,data_len,0,2);
            data_len_s = FtFormatTransfer.hBytesToShort(data_len);
            Log.i("parseBytes dataLen =","" + data_len_s);

            //短地址
            byte[] shortaddress = new byte[2];
            System.arraycopy(data,32,shortaddress,0,2);
            for (int i = 0;i < shortaddress.length;i++){
                String shortaddr_zero = "";
                if ( shortaddress[i] >= 0 &&  shortaddress[i] <= 16){
                    shortaddr_zero = "0" + (shortaddress[i] & 0xFF);
                    shortaddr_str = shortaddr_str + shortaddr_zero;
                }else{
                    shortaddr_str = shortaddr_str + Integer.toHexString(shortaddress[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData shortAddr = " + shortaddr_str);

            //源端点
            srcEndpoint = data[34] & 0xFF;
            System.out.println("ParseAttributeData srcpoint = " + srcEndpoint);

            //簇ID
            byte[] clusterid_bt = new byte[2];
            System.arraycopy(data,35,clusterid_bt,0,2);
            clusterID = FtFormatTransfer.hBytesToShort(clusterid_bt);
            System.out.println("ParseAttributeData clusterID =" + clusterID);

            //属性ID
            byte[] attributeid_bt = new byte[2];
            System.arraycopy(data,37,attributeid_bt,0,2);
            attributeID = FtFormatTransfer.hBytesToShort(attributeid_bt);
            System.out.println("ParseAttributeData attribID =" + attributeID);

            //属性状态
            int u8AttribType = data[39] & 0xFF;
            System.out.println("ParseAttributeData AttrType =" + u8AttribType);


            switch ((byte)u8AttribType)
            {
                case 0x10:
                {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x10= " + attribValue);
                }
                break;
                case 0x18:
                {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x18= " + attribValue);
                }
                break;
                case 0x20:
                {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x20= " + attribValue);
                }
                break;
                case 0x21:
                {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data,40,attribValue_bt,0,2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("clusterID 0x21= ","" + clusterID);
                }
                break;
                case 0x23:
                {
                    byte[] attribValue_bt = new byte[4];
                    System.arraycopy(data,40,attribValue_bt,0,4);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x23= ","" + attribValue);
                }
                break;
                case 0x29:
                {}
                break;
                case 0x30:
                {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data,40,attribValue_bt,0,2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                }
                break;
                case 0x31:
                {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data,40,attribValue_bt,0,2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                }
                break;
                case 0x42:
                {
//                    NSInteger u8StrLen = attriByte[18+8];
//                    for (int i = 0; i < u8StrLen; i++)
//                    {
//                        char c = (char)attriByte[18+8 + i + 1];
//                        Log.i("attribValue = %c",c);
//                    }
                }
                break;
                case (byte) 0xF0:
                {
//                    for (int i=0; i<8; i++) {
//                        Byte attribValue = attriByte[18+8+i];
//                        Log.i("attribValue i=%d = %x", "" +i + attribValue);
//                    }
                }
                break;
            }
        }
    }
}

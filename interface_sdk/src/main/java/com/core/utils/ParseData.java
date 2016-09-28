package com.core.utils;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.entity.AppDeviceInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.threadhelper.GetAllDevices;

import java.lang.ref.PhantomReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/8/8.
 */
public class ParseData {




    public static class ParseDeviceInfo{
        private String device_mac;
        private String device_shortaddr;
        private String main_endpoint;
        private String profile_id;
        private String device_id;
        private String device_name;
        private String device_zone_type;
        private String in_cluster_count;
        private String out_cluster_count;
        private short clusterId = -1;

        public DatagramSocket socket = null;
        private int PORT = 8888;
        private byte[] bt_send = null;

        public ParseDeviceInfo(){
            device_mac = "";
            device_shortaddr = "";
            main_endpoint = "";
            profile_id = "";
            device_id = "";
            device_name = "";
            device_zone_type = "";
            in_cluster_count = "";
            out_cluster_count = "";
            clusterId = -1;
        }

        public void parseData(String str) {
            if (str.length() > 46) {
                int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:");
                int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:");
                int device_name_int = SearchUtils.searchString(str, "DEVICE_NAME:");
                int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:");
                int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:");
                int zone_type_int = SearchUtils.searchString(str, "ZONE_TYPE:");
                int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:");
                int in_cluster_count_int = SearchUtils.searchString(str, "IN_CLUSTER_COUNT:");
                int out_cluster_count_int = SearchUtils.searchString(str, "OUT_CLUSTER_COUNT:");

                //判断是否是设备
                String isMac = new String(str).substring(device_mac_int - 11, device_mac_int);
                System.out.println("isMac = " + isMac);
                if (!isMac.contains("DEVICE_MAC:") | isMac.length() < 11 | !isMac.equals("DEVICE_MAC:")) {
                    return;
                }

                profile_id = str.substring(profile_id_int + 2, profile_id_int + 6);
                device_id = str.substring(device_id_int + 2, device_id_int + 6);
                device_name = str.substring(device_name_int, device_name_int + 6);
                device_mac = str.substring(device_mac_int + 2, device_mac_int + 18);
                device_shortaddr = str.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
                device_zone_type = str.substring(zone_type_int + 2, zone_type_int + 6);
                main_endpoint = str.substring(main_endpoint_int + 2, main_endpoint_int + 4);
                in_cluster_count = str.substring(in_cluster_count_int + 2, in_cluster_count_int + 4);
                out_cluster_count = str.substring(out_cluster_count_int + 2, out_cluster_count_int + 4);

                //根据cluster_id读取相应属性
                String[] cluster_id_array = str.split("CLUSTER_ID:");
                for (int i = 0; i < cluster_id_array.length; i++) {
                    String CLUSTER_ID = cluster_id_array[i].substring(2);
                    System.out.println("CLUSTER_ID = " + CLUSTER_ID);

                    if (CLUSTER_ID == "0500") {
                        byte[] bt_clu = new byte[2];
                        bt_clu[0] = Utils.HexString2Bytes(CLUSTER_ID)[0];
                        bt_clu[1] = Utils.HexString2Bytes(CLUSTER_ID)[1];
                        System.out.println("bt_clu = " + Arrays.toString(bt_clu));
                        short clusterId_short = FtFormatTransfer.getShort(bt_clu, 0);
                        System.out.println("clusterId_short = " + clusterId_short);
                        clusterId = clusterId_short;
                    }
                }

                switch (device_id) {
                    case "0105":
                        device_name = "DimSwitch";
                        break;
                    case "0102":
                        device_name = "ColorLamp";
                        break;
                    case "0110":
                        device_name = "ColorTemperatureLamp";
                        break;
                    case "0210":
                        device_name = "HueColorLamp";
                        break;
                    case "0200":
                        device_name = "ColorLight";
                        break;
                    case "0220":
                        device_name = "ColorTemperatureLampJZGD";
                        break;
                    case "0100":
                        device_name = "DimmableLight";
                        break;
                    case "0101":
                        device_name = "DimLamp";
                        break;
                    case "0402":
                        device_name = "HumanSensor";
                        if (device_zone_type.equalsIgnoreCase("ffff")) {
                            SendReadZoneTypeCmd();
                        }
                        break;
                    case "0202":
                        device_name = "WindowCurtain";
                        break;
                    case "0309":
                        device_name = "PM2dot5Sensor";
                        break;
                    case "0310":
                        device_name = "SmokingSensor";
                        break;
                    case "ffff":
                        //当deviceid为ffff时发送此命令，识别设备id,然后回调给UI
                        SendActiveReqCmd();
                        break;
                }

                if (!device_id.equalsIgnoreCase("ffff")) {
                    AppDeviceInfo appDeviceInfo = new AppDeviceInfo();
                    appDeviceInfo.setProfileid(profile_id);
                    appDeviceInfo.setDeviceName(device_name);
                    appDeviceInfo.setDeviceMac(device_mac);
                    appDeviceInfo.setDeviceid(device_id);
                    appDeviceInfo.setShortaddr(device_shortaddr);
                    appDeviceInfo.setEndpoint(main_endpoint);
                    appDeviceInfo.setZonetype(device_zone_type);

                    System.out.println("AppDeviceInfo = " + device_mac);

                    DataSources.getInstance().ScanDeviceResult(appDeviceInfo);
                }
            }
        }

        //当device id 位FFFF时发送此命令，识别设备id,以及设备其他属性
        public void SendActiveReqCmd(){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Log.i("网关IP地址 = " , Constants.ipaddress);
                        InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                        if (socket == null) {
                            socket = new DatagramSocket(null);
                            socket.setReuseAddress(true);
                            socket.bind(new InetSocketAddress(PORT));
                        }
                        bt_send = DeviceCmdData.ActiveReqDeviceCmd(device_mac,device_shortaddr,main_endpoint);
                        DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length, inetAddress, PORT);
                        socket.send(datagramPacket);
                        System.out.println("SendActiveReqCmd十六进制 = " + Utils.binary(bt_send, 16));
                        Thread.sleep(500);
                        new Thread(new GetAllDevices()).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        //当device id 位0402且device_zone_type为ffff时发送此命令读取属性
        public void SendReadZoneTypeCmd(){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Log.i("网关IP地址 = " , Constants.ipaddress);
                        InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                        if (socket == null) {
                            socket = new DatagramSocket(null);
                            socket.setReuseAddress(true);
                            socket.bind(new InetSocketAddress(PORT));
                        }
                        bt_send = DeviceCmdData.ReadZoneTypeCmd(device_mac,device_shortaddr,main_endpoint);
                        DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
                        socket.send(datagramPacket);
                        System.out.println("SendReadZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                        Thread.sleep(500);
                        new Thread(new GetAllDevices()).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


        //当有傳感器上傳時保存device_zone_type为时发送此命令读取属性
        public void SendSaveZoneTypeCmd(final short zonetype){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Log.i("网关IP地址 = " , Constants.ipaddress);
                        InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);

                        if (socket == null) {
                            socket = new DatagramSocket(null);
                            socket.setReuseAddress(true);
                            socket.bind(new InetSocketAddress(PORT));
                        }
                        bt_send = DeviceCmdData.SaveZoneTypeCmd(device_mac,device_shortaddr,main_endpoint,zonetype);
                        DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
                        socket.send(datagramPacket);
                        System.out.println("SendSaveZoneTypeCmd十六进制 = " + Utils.binary(bt_send, 16));
                        Thread.sleep(500);
                        new Thread(new GetAllDevices()).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


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
        public short clusterID = -1;
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
            clusterID = FtFormatTransfer.lBytesToShort(clusterid_bt);
//            for (int i = 0;i < clusterid_bt.length;i++){
//                String clusterID_zero = "";
//                if ( clusterid_bt[i] >= 0 &&  clusterid_bt[i] <= 16){
//                    clusterID_zero = "0" + (clusterid_bt[i] & 0xFF);
//                    clusterID = clusterID + clusterID_zero;
//                }else{
//                    clusterID = clusterID + Integer.toHexString(clusterid_bt[i] & 0xFF);
//                }
//            }
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
                    Log.i("attribValue 0x21= ","" + clusterID);
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
                    Log.i("attribValue 0x30= ","" + attribValue);
                }
                break;
                case 0x31:
                {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data,40,attribValue_bt,0,2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x31= ","" + attribValue);
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

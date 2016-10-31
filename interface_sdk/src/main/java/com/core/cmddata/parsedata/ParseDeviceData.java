package com.core.cmddata.parsedata;

import android.util.Log;

import com.core.entity.AppDevice;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.devices.SetDeviceAttribute;
import com.core.utils.FtFormatTransfer;
import com.core.utils.SearchUtils;
import com.core.utils.Utils;

import java.util.Arrays;

/**
 * Created by best on 2016/10/10.
 */

public class ParseDeviceData {

    public static void ParseGetDeviceInfo(String deviceinfo,boolean isNew_Device){
        String device_mac,device_shortaddr,main_endpoint,profile_id,device_id,device_name,device_zone_type;
        String in_cluster_count;
        String out_cluster_count;
        short clusterId = -1;
        System.out.println("ParseGetDeviceInfo = " + "ParseGetDeviceInfo1");
        if (deviceinfo.length() > 46) {
            System.out.println("ParseGetDeviceInfo = " + "ParseGetDeviceInfo2");
            int profile_id_int = SearchUtils.searchString(deviceinfo, "PROFILE_ID:");
            int device_id_int = SearchUtils.searchString(deviceinfo, "DEVICE_ID:");
            int device_name_int = SearchUtils.searchString(deviceinfo, "DEVICE_NAME:");
            int device_mac_int = SearchUtils.searchString(deviceinfo, "DEVICE_MAC:");
            int device_shortaddr_int = SearchUtils.searchString(deviceinfo, "DEVICE_SHORTADDR:");
            int zone_type_int = SearchUtils.searchString(deviceinfo, "ZONE_TYPE:");
            int main_endpoint_int = SearchUtils.searchString(deviceinfo, "MAIN_ENDPOINT:");
            int in_cluster_count_int = SearchUtils.searchString(deviceinfo, "IN_CLUSTER_COUNT:");
            int out_cluster_count_int = SearchUtils.searchString(deviceinfo, "OUT_CLUSTER_COUNT:");

            //判断是否是设备
            String isMac = new String(deviceinfo).substring(device_mac_int - 11, device_mac_int);
            System.out.println("isMac = " + isMac);
            if (!isMac.contains("DEVICE_MAC:") | isMac.length() < 11 | !isMac.equals("DEVICE_MAC:")) {
                return;
            }

            profile_id = deviceinfo.substring(profile_id_int + 2, profile_id_int + 6);
            device_id = deviceinfo.substring(device_id_int + 2, device_id_int + 6);
            device_name = deviceinfo.substring(device_name_int, device_name_int + 6);
            device_mac = deviceinfo.substring(device_mac_int + 2, device_mac_int + 18);
            device_shortaddr = deviceinfo.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
            device_zone_type = deviceinfo.substring(zone_type_int + 2, zone_type_int + 6);
            main_endpoint = deviceinfo.substring(main_endpoint_int + 2, main_endpoint_int + 4);
            in_cluster_count = deviceinfo.substring(in_cluster_count_int + 2, in_cluster_count_int + 4);
            out_cluster_count = deviceinfo.substring(out_cluster_count_int + 2, out_cluster_count_int + 4);

            //根据cluster_id读取相应属性
            String[] cluster_id_array = deviceinfo.split("CLUSTER_ID:");
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
                    device_name = "ColorTemp";
                    break;
                case "0210":
                    device_name = "HueColorLamp";
                    break;
                case "0200":
                    device_name = "ColorLight";
                    break;
                case "0220":
                    device_name = "ColorTempJZGD";
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
                        SetDeviceAttribute.SendReadZoneTypeCmd(device_mac,device_shortaddr,main_endpoint);
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
                    SetDeviceAttribute.SendActiveReqCmd(device_mac,device_shortaddr,main_endpoint);
                    break;
            }

            if (!device_id.equalsIgnoreCase("ffff")) {

                AppDevice appDevice = new AppDevice();
                appDevice.setProfileid(profile_id);
                appDevice.setDeviceName(device_name);
                appDevice.setDeviceMac(device_mac);
                appDevice.setDeviceid(device_id);
                appDevice.setShortaddr(device_shortaddr);
                appDevice.setEndpoint(main_endpoint);
                appDevice.setZonetype(device_zone_type);

                System.out.println("AppDeviceInfo = " + device_mac);

                if (isNew_Device){
                    DataSources.getInstance().AddDeviceResult(appDevice);
                }else{
                    DataSources.getInstance().ScanDeviceResult(appDevice);
                }
            }
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
        public String  zigbee_type;
        public String dev_mac;
        public short data_len_s;
        public String shortaddr_str = "";
        public int srcEndpoint;
        public short attributeID;
        public int state = -1;
        public int attribValue = -1;
        public short clusterID;

        public ParseAttributeData(){
            dev_mac = "";
            zigbee_type = "";
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
                    zigbee_type = zigbee_type + str_zero;
                }else{
                    zigbee_type = zigbee_type + Integer.toHexString(mZigbee_bt[i] & 0xFF);
                }
            }
            if (!zigbee_type.contains("8100")){
                return;
            }
            System.out.println("ParseAttributeData mZigbeeType = " + zigbee_type);

            //设备MAC地址
            System.arraycopy(data,22,mDevMac_bt,0,8);
            String str_zero = "";
            for(int i = 0;i < mDevMac_bt.length; i++){
                if ( mDevMac_bt[i] >= 0 &&  mDevMac_bt[i] <= 16){
                    String hexTostr = Integer.toHexString(mDevMac_bt[i]);
                    str_zero = "0" + hexTostr;
                    dev_mac = dev_mac + str_zero;
                }else{
                    dev_mac = dev_mac + Integer.toHexString(mDevMac_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData mDevMac = " + dev_mac);

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

    public static class ParseIEEEData {
        public String gateway_mac = "";

        public ParseIEEEData() {
            gateway_mac = "";
        }

        public void parseBytes(byte[] data) {
            byte[] gateway_bt = new byte[8];

            if (data[31] == 8 || data[31] == 10) {
                System.arraycopy(data, 32, gateway_bt, 0, 8);
                String str_zero = "";
                for (int i = 0; i < gateway_bt.length; i++) {
                    if (gateway_bt[i] >= 0 && gateway_bt[i] <= 16) {
                        if (gateway_bt[i] >= 10 && gateway_bt[i] <= 16){
                            str_zero = "0" + Integer.toHexString(gateway_bt[i] & 0xFF) ;
                        }else{
                            str_zero = "0" + (gateway_bt[i] & 0xFF);
                        }
                        gateway_mac = gateway_mac + str_zero;
                        System.out.println("str_zero = " + str_zero);
                    } else {
                        gateway_mac = gateway_mac + Integer.toHexString(gateway_bt[i] & 0xFF);
                    }

                    System.out.println("gateway_mac = " + gateway_mac);
                }
            }
        }
    }



    public static class ParseBindVCPFPData {

        public short  frequency = -1;
        public double  voltage = -1;
        public double  current = -1;
        public double  power = -1;
        public double  power_factor = -1;

        public ParseBindVCPFPData() {
            frequency = -1;
        }

        public void parseBytes(byte[] data) {
            byte[] type_bt = new byte[2];

            byte[] frequency_bt = new byte[2];
            byte[] voltage_bt = new byte[2];
            byte[] current_bt = new byte[2];
            byte[] power_bt = new byte[2];
            byte[] power_factor_bt = new byte[2];

            //Zigbee串口类型
            System.arraycopy(data,20, type_bt, 0, 2);
            String type = "";
            for (int i = 0; i < type_bt.length; i++) {
                type += Integer.toHexString(type_bt[i] & 0xFF);
            }
            System.out.println("BytesToShort = " + type);
            if (!type.contains("802")){
                return;
            }

            System.arraycopy(data, 58, frequency_bt, 0, 2);//frequency_bt
            frequency = FtFormatTransfer.lBytesToShort(frequency_bt);
            System.out.println("parseBytes_frequency = " + frequency);

            System.arraycopy(data, 63, voltage_bt, 0, 2);//voltage_bt
            short voltage_s = FtFormatTransfer.lBytesToShort(voltage_bt);
            System.out.println("parseBytes_voltage = " + voltage_s);
            voltage = voltage_s / 100.00;
            System.out.println("parseBytes_voltage_1 = " + voltage);

            System.arraycopy(data, 68, current_bt, 0, 2);//current_bt
            short current_s = FtFormatTransfer.lBytesToShort(current_bt);
            System.out.println("parseBytes_current = " + current_s);
            current = current_s /1000.0000;
            System.out.println("parseBytes_current_1 = " + current);

            System.arraycopy(data, 73, power_bt, 0, 2);//power_bt
            short power_s = FtFormatTransfer.lBytesToShort(power_bt);
            System.out.println("parseBytes_power = " + power);
            power = power_s /1000.0000;
            System.out.println("parseBytes_power_1 = " + power);

            System.arraycopy(data, 78, power_factor_bt, 0, 2);//power_factor_bt
            short power_factor_s = FtFormatTransfer.lBytesToShort(power_factor_bt);
            power_factor = power_factor_s /100.00;
            System.out.println("parseBytes_power_factor_1 = " + power_factor);

            DataSources.getInstance().BindDevice(frequency,voltage,current,power,power_factor);
        }
    }
}

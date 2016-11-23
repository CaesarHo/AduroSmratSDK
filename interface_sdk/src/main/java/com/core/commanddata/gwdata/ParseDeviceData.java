package com.core.commanddata.gwdata;

import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.utils.FtFormatTransfer;
import com.core.utils.SearchUtils;
import com.core.utils.Utils;

/**
 * Created by best on 2016/10/10.
 */

public class ParseDeviceData {

    /**
     * 解析接受到的数据
     *
     * @param bt
     * @param isNew_Device
     */
    public static void ParseGetDeviceInfo(byte[] bt, boolean isNew_Device) throws Exception {
        String deviceinfo = FtFormatTransfer.bytesToUTF8String(bt);
        String device_mac, device_shortaddr, main_endpoint, profile_id, device_id, device_name, device_zone_type;
        String in_cluster_count, out_cluster_count;
        int profile_id_int = SearchUtils.searchString(deviceinfo, "PROFILE_ID:");
        int device_id_int = SearchUtils.searchString(deviceinfo, "DEVICE_ID:");
        int device_name_int = SearchUtils.searchString(deviceinfo, "DEVICE_NAME:");
        int device_mac_int = SearchUtils.searchString(deviceinfo, "DEVICE_MAC:");
        int device_shortaddr_int = SearchUtils.searchString(deviceinfo, "DEVICE_SHORTADDR:");
        int zone_type_int = SearchUtils.searchString(deviceinfo, "ZONE_TYPE:");
        int main_endpoint_int = SearchUtils.searchString(deviceinfo, "MAIN_ENDPOINT:");
        int in_cluster_count_int = SearchUtils.searchString(deviceinfo, "IN_CLUSTER_COUNT:");
        int out_cluster_count_int = SearchUtils.searchString(deviceinfo, "OUT_CLUSTER_COUNT:");

        profile_id = deviceinfo.substring(profile_id_int + 2, profile_id_int + 6);
        device_id = deviceinfo.substring(device_id_int + 2, device_id_int + 6);
        String dev_name = deviceinfo.substring(device_name_int, device_name_int + 6);

        device_mac = deviceinfo.substring(device_mac_int + 2, device_mac_int + 18);
        device_shortaddr = deviceinfo.substring(device_shortaddr_int + 2, device_shortaddr_int + 6);
        device_zone_type = deviceinfo.substring(zone_type_int + 2, zone_type_int + 6);
        main_endpoint = deviceinfo.substring(main_endpoint_int + 2, main_endpoint_int + 4);
        in_cluster_count = deviceinfo.substring(in_cluster_count_int + 2, in_cluster_count_int + 4);
        out_cluster_count = deviceinfo.substring(out_cluster_count_int + 2, out_cluster_count_int + 4);

        if (!dev_name.equalsIgnoreCase("Device")) {
            int device_name_end = deviceinfo.indexOf(":", device_name_int) - 9;
            String device_name_hex = deviceinfo.substring(device_name_int, device_name_end);
            device_name = Utils.toStringHex(device_name_hex);
        } else {
            device_name = Constants.DeviceName(device_id, device_zone_type);
//            for (int i = 0; i < a.length(); i++) {
//                //与'0'和'9'比较，不是0,9.
//                if (a.charAt(i) >= '0' && a.charAt(i) <= '9' || a.charAt(i) >= 'a' && a.charAt(i) <= 'f' || a.charAt(i) >= 'A' && a.charAt(i) <= 'F') {
//                    continue;
//                } else {
//                    System.out.println("MAC地址必须为不带任何标点符号的十六进制数");
//                    return;
//                }
//            }
//            System.out.println(a + " 是一个mac地址！");
        }

        if (device_id.equalsIgnoreCase("ffff")) {
            //当deviceid为ffff时发送此命令，识别设备id,然后回调给UI
            byte[] bt_send = DeviceCmdData.ActiveReqDeviceCmd(device_mac, device_shortaddr);
            Constants.sendMessage(bt_send);
        }

        if (device_id.contains("0402") & device_zone_type.equalsIgnoreCase("ffff")) {
            byte[] bt_send = DeviceCmdData.ReadZoneTypeCmd(device_mac, device_shortaddr, main_endpoint);
            Constants.sendMessage(bt_send);
        }

        AppDevice appDevice = new AppDevice();
        appDevice.setProfileid(profile_id);
        appDevice.setDeviceName(device_name);
        appDevice.setDeviceMac(device_mac);
        appDevice.setDeviceid(device_id);
        appDevice.setShortaddr(device_shortaddr);
        appDevice.setEndpoint(main_endpoint);
        appDevice.setZonetype(device_zone_type);

        if (isNew_Device) {
            DataSources.getInstance().AddDeviceResult(appDevice);
        } else {
            DataSources.getInstance().ScanDeviceResult(appDevice);
        }
    }

    /**
     * 解析传感器上传数据
     * <415f5a4947 01 8401(Zigbee消息类型) 00158d0001479820(设备MAC)
     * 000c(数据长度) 01(设备的端点) 0500(簇ID) 02(短地址模式) ae4d(短地址)
     * 0000(传感器状态) 00(扩展状态) 00(ZoneID) 0000(Delay-触发到上报的时间)>
     */
    public static class ParseSensorData {
        public String sensor_mac = "";
        public Short sensor_state;
        public String message_type = "";
        public short data_len_s;
        public String srcEndpoint;
        public short clusterID = -1;
        public int srcEndpoint_mode;
        public String shortaddr_str = "";
        public int state = -1;

        public ParseSensorData() {
            sensor_mac = "";
            sensor_state = 0;
            message_type = "";
        }

        public void parseBytes(byte[] data) {
            byte[] message_type_bt = new byte[2];
            byte[] sensor_state_bt = new byte[2];
            byte[] device_mac_bt = new byte[8];

            System.arraycopy(data, 20, message_type_bt, 0, 2);
            for (int i = 0; i < message_type_bt.length; i++) {
                String str_zero = "";
                if (message_type_bt[i] >= 0 && message_type_bt[i] <= 16) {
                    str_zero = "0" + (message_type_bt[i] & 0xFF);
                    message_type = message_type + str_zero;
                } else {
                    message_type = message_type + Integer.toHexString(message_type_bt[i] & 0xFF);
                }
            }

            if (!message_type.contains("8401")) {
                return;
            }

            System.arraycopy(data, 22, device_mac_bt, 0, 8);
            String str_zero = "";
            for (int i = 0; i < device_mac_bt.length; i++) {
                if (device_mac_bt[i] >= 0 && device_mac_bt[i] <= 16) {
                    if (device_mac_bt[i] >= 10 && device_mac_bt[i] <= 16) {
                        str_zero = "0" + Integer.toHexString(device_mac_bt[i] & 0xFF);
                    } else {
                        str_zero = "0" + (device_mac_bt[i] & 0xFF);
                    }
                    sensor_mac = sensor_mac + str_zero;
                } else {
                    sensor_mac = sensor_mac + Integer.toHexString(device_mac_bt[i] & 0xFF);
                }
            }

            //数据长度
            byte[] data_len = new byte[2];
            System.arraycopy(data, 30, data_len, 0, 2);
            data_len_s = FtFormatTransfer.hBytesToShort(data_len);
            System.out.println("ParseSensorData dataLen =" + data_len_s);

            //源端点
            srcEndpoint = Integer.toHexString(data[32] & 0xFF);//data[32] & 0xFF;
            System.out.println("ParseSensorData srcpoint =" + srcEndpoint);

            //簇ID
            byte[] clusterid_bt = new byte[2];
            System.arraycopy(data, 33, clusterid_bt, 0, 2);
            clusterID = FtFormatTransfer.lBytesToShort(clusterid_bt);
            System.out.println("ParseSensorData clusterID =" + clusterID);

            //短地址模式
            srcEndpoint_mode = data[35] & 0xFF;
            System.out.println("ParseSensorData point_mode =" + srcEndpoint_mode);

            //短地址
            byte[] shortaddress = new byte[2];
            System.arraycopy(data, 36, shortaddress, 0, 2);
            for (int i = 0; i < shortaddress.length; i++) {
                String shortaddr_zero = "";
                if (shortaddress[i] >= 0 && shortaddress[i] <= 16) {
                    shortaddr_zero = "0" + (shortaddress[i] & 0xFF);
                    shortaddr_str = shortaddr_str + shortaddr_zero;
                } else {
                    shortaddr_str = shortaddr_str + Integer.toHexString(shortaddress[i] & 0xFF);
                }
            }
            System.out.println("ParseSensorData shortAddr =" + shortaddr_str);

            //传感器状态
            System.arraycopy(data, 38, sensor_state_bt, 0, 2);
            sensor_state = FtFormatTransfer.hBytesToShort(sensor_state_bt);
            state = (int) sensor_state & 0x1;
            System.out.println("ParseSensorData state = " + state);
        }
    }


    /**
     * 解析设备属性
     */
    public static class ParseAttributeData {
        public String message_type;
        public String device_mac;
        public short data_len_s;
        public String short_address;
        public int endpoint;
        public short attributeID;
        public int state;
        public int attribValue;
        public short clusterID;

        public ParseAttributeData() {
            device_mac = "";
            message_type = "";
            short_address = "";
            state = -1;
            attribValue = -1;
        }

        public void parseBytes(byte[] data) {
            byte[] message_type_bt = new byte[2];
            byte[] device_mac_bt = new byte[8];

            //Zigbee串口类型
            System.arraycopy(data, 20, message_type_bt, 0, 2);
            for (int i = 0; i < message_type_bt.length; i++) {
                String str_zero = "";
                if (message_type_bt[i] >= 0 && message_type_bt[i] <= 16) {
                    str_zero = "0" + (message_type_bt[i] & 0xFF);
                    message_type = message_type + str_zero;
                } else {
                    message_type = message_type + Integer.toHexString(message_type_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData message_type = " + message_type);

            //设备MAC地址
            System.arraycopy(data, 22, device_mac_bt, 0, 8);
            String str_zero = "";
            for (int i = 0; i < device_mac_bt.length; i++) {
                if (device_mac_bt[i] >= 0 && device_mac_bt[i] <= 16) {
                    String hexTostr = Integer.toHexString(device_mac_bt[i]);
                    str_zero = "0" + hexTostr;
                    device_mac = device_mac + str_zero;
                } else {
                    device_mac = device_mac + Integer.toHexString(device_mac_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData mDevMac = " + device_mac);

            //数据长度
            byte[] data_len = new byte[2];
            System.arraycopy(data, 30, data_len, 0, 2);
            data_len_s = FtFormatTransfer.hBytesToShort(data_len);
            Log.i("parseBytes dataLen =", "" + data_len_s);

            //短地址
            byte[] short_address_bt = new byte[2];
            System.arraycopy(data, 32, short_address_bt, 0, 2);
            for (int i = 0; i < short_address_bt.length; i++) {
                String shortaddr_zero = "";
                if (short_address_bt[i] >= 0 && short_address_bt[i] <= 16) {
                    shortaddr_zero = "0" + (short_address_bt[i] & 0xFF);
                    short_address = short_address + shortaddr_zero;
                } else {
                    short_address = short_address + Integer.toHexString(short_address_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseAttributeData shortAddr = " + short_address);

            //源端点
            endpoint = data[34] & 0xFF;
            System.out.println("ParseAttributeData srcpoint = " + endpoint);

            //簇ID
            byte[] clusterid_bt = new byte[2];
            System.arraycopy(data, 35, clusterid_bt, 0, 2);
            clusterID = FtFormatTransfer.hBytesToShort(clusterid_bt);
            System.out.println("ParseAttributeData clusterID =" + clusterID);

            //设备状态
            state = data[37] & 0xFF;

            //属性ID
            byte[] attributeid_bt = new byte[2];
            System.arraycopy(data, 37, attributeid_bt, 0, 2);
            attributeID = FtFormatTransfer.hBytesToShort(attributeid_bt);
            System.out.println("ParseAttributeData attribID =" + attributeID);

            //属性状态
            int u8AttribType = data[39] & 0xFF;
            System.out.println("ParseAttributeData AttrType =" + u8AttribType);

            switch ((byte) u8AttribType) {

                case 0x10: {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x10= " + attribValue);
                }
                break;
                case 0x18: {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x18= " + attribValue);
                }
                break;
                case 0x20: {
                    attribValue = data[40] & 0xFF;
                    System.out.println("attribValue 0x20= " + attribValue);
                }
                break;
                case 0x21: {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data, 40, attribValue_bt, 0, 2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x21= ", "" + clusterID);
                }
                break;
                case 0x23: {
                    byte[] attribValue_bt = new byte[4];
                    System.arraycopy(data, 40, attribValue_bt, 0, 4);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x23= ", "" + attribValue);
                }
                break;
                case 0x29: {
                }
                break;
                case 0x30: {
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data, 40, attribValue_bt, 0, 2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x30= ", "" + attribValue);
                }
                break;
                case 0x31: {//zonetype
                    byte[] attribValue_bt = new byte[2];
                    System.arraycopy(data, 40, attribValue_bt, 0, 2);
                    attribValue = FtFormatTransfer.hBytesToShort(attribValue_bt);
                    Log.i("attribValue 0x31= ", "" + attribValue);
                    String zone_type = "";
                    for (int i = 0; i < attribValue_bt.length; i++) {
                        String zero = "";
                        if (attribValue_bt[i] >= 0 && attribValue_bt[i] <= 16) {
                            zero = "0" + (attribValue_bt[i] & 0xFF);
                            zone_type = zone_type + zero;
                        } else {
                            zone_type = zone_type + Integer.toHexString(attribValue_bt[i] & 0xFF);
                        }
                    }
                    DataSources.getInstance().vDataZoneType(device_mac,zone_type);
                    Log.i("attribValue 0x31= ", "" + attribValue);
                }
                break;
                case 0x42: {
//                    NSInteger u8StrLen = attriByte[18+8];
//                    for (int i = 0; i < u8StrLen; i++)
//                    {
//                        char c = (char)attriByte[18+8 + i + 1];
//                        Log.i("attribValue = %c",c);
//                    }
                }
                break;
                case (byte) 0xF0: {
//                    for (int i=0; i<8; i++) {
//                        Byte attribValue = attriByte[18+8+i];
//                        Log.i("attribValue i=%d = %x", "" +i + attribValue);
//                    }
                }
                break;
            }
        }
    }

    /**
     * 解析接收到的设备状态及其亮度
     * 71, 87, -64, -88, 0, 85, 10, 1, 49, 1,
     * 0, 31, 0, 26, 65, 95, 90, 73, 71, 1,
     * -127, 1(枚举B),(第二十一)
       0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 1, 1,(源端点)  0, 6(clusterID) , 0(命令ID), 0(状态吗), -27, -112(短地址), -115, -1, -1
     */
    public static class ParseDeviceStateOrLevel{
        public String short_address = "";
        public String message_type = "";
        public short clusterID = -1;
        public int state = -1;
        public int level = -1;

        public ParseDeviceStateOrLevel(){

        }

        public void parseBytes(byte[] data){
            byte[] message_type_bt = new byte[2];
            byte[] short_address_bt = new byte[2];

            System.arraycopy(data, 20, message_type_bt, 0, 2);
            for (int i = 0; i < message_type_bt.length; i++) {
                String str_zero;
                if (message_type_bt[i] >= 0 && message_type_bt[i] <= 16) {
                    str_zero = "0" + (message_type_bt[i] & 0xFF);
                    message_type = message_type + str_zero;
                } else {
                    message_type = message_type + Integer.toHexString(message_type_bt[i] & 0xFF);
                }
            }
            System.out.println("ParseDeviceStateOrLevel = "+message_type);
            //簇ID
            byte[] clusterid_bt = new byte[2];
            System.arraycopy(data, 34, clusterid_bt, 0, 2);
            clusterID = FtFormatTransfer.hBytesToShort(clusterid_bt);
            Log.i("ParseDeviceStateOrLevel",clusterID + "");
            //短地址
            System.arraycopy(data, 38, short_address_bt, 0, 2);
            for (int i = 0; i < short_address_bt.length; i++) {
                String shortaddr_zero = "";
                if (short_address_bt[i] >= 0 && short_address_bt[i] <= 16) {
                    shortaddr_zero = "0" + (short_address_bt[i] & 0xFF);
                    short_address = short_address + shortaddr_zero;
                } else {
                    short_address = short_address + Integer.toHexString(short_address_bt[i] & 0xFF);
                }
            }

            //设备状态
            state = data[37] & 0xFF;
            //设备亮度
            level = data[37] & 0xFF;
        }
    }

    /**
     * 解析IEEE地址
     */
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
                        if (gateway_bt[i] >= 10 && gateway_bt[i] <= 16) {
                            str_zero = "0" + Integer.toHexString(gateway_bt[i] & 0xFF);
                        } else {
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

        public short frequency = -1;
        public double voltage = -1;
        public double current = -1;
        public double power = -1;
        public double power_factor = -1;

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
            System.arraycopy(data, 20, type_bt, 0, 2);
            String type = "";
            for (int i = 0; i < type_bt.length; i++) {
                type += Integer.toHexString(type_bt[i] & 0xFF);
            }
            if (!type.contains("802")) {
                return;
            }

            System.arraycopy(data, 58, frequency_bt, 0, 2);//frequency_bt
            frequency = FtFormatTransfer.lBytesToShort(frequency_bt);

            System.arraycopy(data, 63, voltage_bt, 0, 2);//voltage_bt
            short voltage_s = FtFormatTransfer.lBytesToShort(voltage_bt);
            voltage = voltage_s / 100.00;

            System.arraycopy(data, 68, current_bt, 0, 2);//current_bt
            short current_s = FtFormatTransfer.lBytesToShort(current_bt);
            current = current_s / 1000.0000;

            System.arraycopy(data, 73, power_bt, 0, 2);//power_bt
            short power_s = FtFormatTransfer.lBytesToShort(power_bt);
            power = power_s / 1000.0000;

            System.arraycopy(data, 78, power_factor_bt, 0, 2);//power_factor_bt
            short power_factor_s = FtFormatTransfer.lBytesToShort(power_factor_bt);
            power_factor = power_factor_s / 100.00;

            DataSources.getInstance().BindDevice(frequency, voltage, current, power, power_factor);
        }
    }
}

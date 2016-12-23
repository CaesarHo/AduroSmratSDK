package com.core.commanddata.gwdata;

import com.core.utils.FTPUtils;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import org.apache.commons.net.ftp.FTP;

/**
 * Created by best on 2016/12/23.
 */

public class ParseGatewayData {

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
            if (data[31] == 8) {
                System.arraycopy(data, 32, gateway_bt, 0, 8);
                gateway_mac = Utils.bytesToHexString(gateway_bt);
            }
        }
    }

    public  static class ParseStartUpdateData{
        public int update_size;
        public ParseStartUpdateData(){
            update_size = -1;
        }
        public void parseBytes(byte[] data){
            byte[] size_bt = new byte[2];
            System.arraycopy(data,32,size_bt,0,2);
            update_size = FtFormatTransfer.getInt(size_bt,false);
            System.out.println("update_size = " + update_size);


        }
    }

    /**
     * 解析网关信息
     */
    public static class ParseGWInfoData {
        public String gw_mac = "";
        public int gw_version = -1;
        public int gw_bootrodr = -1;

        public ParseGWInfoData() {
            gw_mac = "";
            gw_version = -1;
        }

        public void parseBytes(byte[] data) {
            byte[] gw_mac_bt = new byte[6];
            byte[] gw_version_bt = new byte[4];
            byte[] gw_bootrodr_bt = new byte[4];

            //Zigbee串口类型
            System.arraycopy(data, 32, gw_mac_bt, 0, 6);
            String str_zero = "";
            for (int i = 0; i < gw_mac_bt.length; i++) {
                if (gw_mac_bt[i] >= 0 && gw_mac_bt[i] <= 16) {
                    String hexTostr = Integer.toHexString(gw_mac_bt[i]);
                    str_zero = "0" + hexTostr;
                    gw_mac = gw_mac + str_zero + "-";
                } else {
                    if (i + 1 == gw_mac_bt.length) {
                        gw_mac = gw_mac + Integer.toHexString(gw_mac_bt[i] & 0xFF);
                    } else {
                        gw_mac = gw_mac + Integer.toHexString(gw_mac_bt[i] & 0xFF) + "-";
                    }
                }
            }

            System.arraycopy(data, 38, gw_version_bt, 0, 4);
            gw_version = FtFormatTransfer.hBytesToInt(gw_version_bt) + 10000;

            System.arraycopy(data, 42, gw_bootrodr_bt, 0, 4);
            gw_bootrodr = FtFormatTransfer.hBytesToInt(gw_bootrodr_bt);
            System.out.println("bootrodr = " + gw_bootrodr);
        }
    }

    /**
     * 解析协调器主版本号安装版本号
     */
    public static class ParseNodeVer {
        public int major_ver = -1;
        public int Install_ver = -1;

        public ParseNodeVer() {
            major_ver = -1;
            Install_ver = -1;
        }

        public void parseBytes(byte[] data) {
            byte[] gw_mac_bt = new byte[2];
            byte[] gw_version_bt = new byte[2];

            System.arraycopy(data, 32, gw_mac_bt, 0, 2);
            major_ver = FtFormatTransfer.hBytesToShort(gw_mac_bt);

            System.arraycopy(data, 34, gw_version_bt, 0, 2);
            Install_ver = FtFormatTransfer.hBytesToShort(gw_version_bt);
        }
    }

    /**
     * 解析网关服务器地址
     */
    public static class ParseServerAddress {
        public String server_address;

        public ParseServerAddress() {
            this.server_address = "";
        }

        public void parseBytes(byte[] data) {
            byte[] server_address_bt = new byte[data[31]];
            System.arraycopy(data, 32, server_address_bt, 0, data[31]);
            server_address = Utils.bytesToString(server_address_bt);//Utils.bytesToHexString(server_address_bt);
//            Constants.MQTT_SERVER = server_address;
            System.out.println("server_address = " + server_address);
        }
    }
}

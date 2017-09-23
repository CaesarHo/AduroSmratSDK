package com.core.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by best on 2016/6/30.
 */
public class GatewayInfo {
    public static final String FIRMWARE_VERSION = "firmware_versio";
    public static final String INETADDRESS = "inetaddress";
    public static final String AESKEY = "aeskey";
    public static final String PORT = "port";
    public static final String GATEWAY_NO = "gateway_no";
    public static final String GATEWAY_MAC = "gateway_mac";
    public static final String BOOTRODR = "gateway_bootrodr";
    public static final String GW_IEEE_ADDRESS = "gateway_ieee_address";
    public static final String GW_UPDATE_FILE_ADDRESS = "GW_UPDATE_FILE_ADDRESS";
    public static final String PACKET_SIZE = "UPDATE_PACKET_SIZE";
    public static final String GATEWAY_UPDATE_CRC32 = "gateway_update_crc32";
    public static final String GATEWAY_UPDATE_VERSION = "GATEWAY_UPDATE_VERSION";

    private static GatewayInfo manager = null;

    private GatewayInfo() {

    }

    public synchronized static GatewayInfo getInstance() {
        if (null == manager) {
            synchronized (GatewayInfo.class) {
                if (null == manager) {
                    manager = new GatewayInfo();
                }
            }
        }
        return manager;
    }

    //网关版本固件获取
    public int getFirmwareVersion(Context context) {
        SharedPreferences sf = context.getSharedPreferences(FIRMWARE_VERSION, context.MODE_PRIVATE);
        return sf.getInt("ADURO" + FIRMWARE_VERSION, 0);
    }

    //网关版本固件保存
    public void setFirmwareVersion(Context context,int value) {
        SharedPreferences sf = context.getSharedPreferences(FIRMWARE_VERSION, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt("ADURO" + FIRMWARE_VERSION, value);
        editor.commit();
    }

    //获取网关编号
    public String getGatewayNo(Context context){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_NO,context.MODE_PRIVATE);
        return sf.getString("ADURO" + GATEWAY_NO ,"");
    }

    //保存网关编号
    public void setGatewayNo(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_NO,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + GATEWAY_NO,value);
        editor.commit();
    }

    //获取网关编号
    public String getGatewayMac(Context context){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_MAC,context.MODE_PRIVATE);
        return sf.getString("ADURO" + GATEWAY_MAC ,"");
    }

    //保存网关编号
    public void setGatewayMac(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_MAC,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + GATEWAY_MAC,value);
        editor.commit();
    }

    //获取网关IP地址
    public String getInetAddress(Context context){
        SharedPreferences sf = context.getSharedPreferences(INETADDRESS,context.MODE_PRIVATE);
        return sf.getString("ADURO" + INETADDRESS,"");
    }

    //保存网关IP地址
    public void setInetAddress(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(INETADDRESS,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + INETADDRESS,value);
        editor.commit();
    }

    //获取网关key
    public String getAesKey(Context context){
        SharedPreferences sf = context.getSharedPreferences(AESKEY,context.MODE_PRIVATE);
        return sf.getString("ADURO" + AESKEY,"");
    }

    //保存网关key
    public void setAesKey(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(AESKEY,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + AESKEY,value);
        editor.commit();
    }

    //获取网关端口
    public int getPort(Context context) {
        SharedPreferences sf = context.getSharedPreferences(PORT, context.MODE_PRIVATE);
        return sf.getInt("ADURO"+PORT,0);
    }

    //保存网关端口
    public void setPort(Context context,int value) {
        SharedPreferences sf = context.getSharedPreferences(PORT, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt("ADURO"+PORT, value);
        editor.commit();
    }

//    bootrodr
    //获取网关端口
    public int getBootrodr(Context context) {
        SharedPreferences sf = context.getSharedPreferences(BOOTRODR, context.MODE_PRIVATE);
        return sf.getInt("ADURO"+BOOTRODR,0);
    }

    //保存网关端口
    public void setBootrodr(Context context,int value) {
        SharedPreferences sf = context.getSharedPreferences(BOOTRODR, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt("ADURO"+BOOTRODR, value);
        editor.commit();
    }

    //获取网关IEEE地址
    public String getGwIEEEAddress(Context context){
        SharedPreferences sf = context.getSharedPreferences(GW_IEEE_ADDRESS,context.MODE_PRIVATE);
        return sf.getString("ADURO" + GW_IEEE_ADDRESS,"");
    }

    //保存网关IEEE地址
    public void setGwIEEEAddress(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(GW_IEEE_ADDRESS,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + GW_IEEE_ADDRESS,value);
        editor.commit();
    }


    //获取网关更新文件
    public String getGatewayUpdateFileName(Context context){
        SharedPreferences sf = context.getSharedPreferences(GW_UPDATE_FILE_ADDRESS,context.MODE_PRIVATE);
        return sf.getString("ADURO" + GW_UPDATE_FILE_ADDRESS,"");
    }

    //保存网关更新文件
    public void setGatewayUpdateFileName(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(GW_UPDATE_FILE_ADDRESS,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + GW_UPDATE_FILE_ADDRESS,value);
        editor.commit();
    }

    //获取网关更新每包发送大小
    public int getPacketSize(Context context) {
        SharedPreferences sf = context.getSharedPreferences(PACKET_SIZE, context.MODE_PRIVATE);
        return sf.getInt("ADURO"+PACKET_SIZE,-1);
    }

    //保存网关更新每包发送大小
    public void setPacketSize(Context context,int value) {
        SharedPreferences sf = context.getSharedPreferences(PACKET_SIZE, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt("ADURO"+PACKET_SIZE, value);
        editor.commit();
    }


    //获取网关更新文件
    public String getGatewayUpdateCRC32(Context context){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_UPDATE_CRC32,context.MODE_PRIVATE);
        return sf.getString("ADURO" + GATEWAY_UPDATE_CRC32,"");
    }

    //保存网关更新文件
    public void setGatewayUpdateCRC32(Context context,String value){
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_UPDATE_CRC32,context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putString("ADURO" + GATEWAY_UPDATE_CRC32,value);
        editor.commit();
    }

    //获取网关更新版本号
    public int getGateWayUpdateVserion(Context context) {
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_UPDATE_VERSION, context.MODE_PRIVATE);
        return sf.getInt("ADURO"+GATEWAY_UPDATE_VERSION,0);
    }

    //保存网关更新版本号
    public void setGateWayUpdateVserion(Context context,int value) {
        SharedPreferences sf = context.getSharedPreferences(GATEWAY_UPDATE_VERSION, context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putInt("ADURO"+GATEWAY_UPDATE_VERSION, value);
        editor.commit();
    }
}

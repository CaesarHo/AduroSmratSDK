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
}

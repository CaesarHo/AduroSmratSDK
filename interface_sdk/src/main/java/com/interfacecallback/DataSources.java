package com.interfacecallback;


import android.net.wifi.WifiManager;

import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public class DataSources {
    private static DataSources manager = null;
    private static SdkCallback sdkCallback;
    public Boolean IsThreadDisable = false;
    public static WifiManager.MulticastLock lock;
    public static DataSources getInstance() {
        if (manager == null) {
            manager = new DataSources();
        }
        return manager;
    }

    public void setSettingInterface(SdkCallback settingInterface) {
        this.sdkCallback = settingInterface;
    }

    //============================实现数据接收与发送==============================

    public void GetGateWayInformation(){
        int port = GatewayInfo.getInstance().getPort();
        InetAddress mIpAddress = GatewayInfo.getInstance().getInetAddress();
        String mUniqueId = GatewayInfo.getInstance().getGatewayUniqueId();

        sdkCallback.GatewayInformation(port,mIpAddress,mUniqueId);
    }

    public void setDeviceStateResult(String deviceid,int result){
        sdkCallback.setDeviceStateResult(deviceid,result);
    }

    //添加房间
    public void AddRoomResult(int result){
        sdkCallback.AddRoomCallback(result);
    }

    //删除房间
    public void DeleteRoomResult(int result){
        sdkCallback.DeleteRoomCallback(result);
    }

    //修改房间
    public void ModifyRoomResult(int result){
        sdkCallback.ModifyRoomCallback(result);
    }

    //添加设备
    public void AddDeviceResult(){
        sdkCallback.AddDeviceCallback(1);
    }
}

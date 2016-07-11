package com.interfacecallback;

import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public interface SdkCallback {
    //回调函数ateway information
    public void GatewayInformation(int port, InetAddress mIpAddress, String id);
    public void setDeviceStateResult(String deviceid, int result);
    //房间操作
    public void AddRoomCallback(int result);
    public void DeleteRoomCallback(int rsult);
    public void ModifyRoomCallback(int result);

    //设备操作
    public void AddDeviceCallback(int result);
    public void DeleteDeviceCallback(int result);
    public void ModifyDeviceCallback(int result);
}

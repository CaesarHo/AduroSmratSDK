package com.interfacecallback;

import android.content.Context;

import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnAddDevice;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnAddRoom;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnDeleteRoom;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnModifyRoom;

/**
 * Created by best on 2016/7/11.
 */
public class SerialHandler {
    private static SerialHandler manager = null;
    Context context;

    private SerialHandler(){

    }

    public synchronized static SerialHandler getInstance() {
        if (null == manager) {
            synchronized (SerialHandler.class) {
                manager = new SerialHandler();
            }
        }
        return manager;
    }

    /**
     * 初始化
     */
    public void p2pInit(Context context,SdkCallback sdkCallback) {
        DataSources.getInstance().setSettingInterface(sdkCallback);
        this.context = context;
    }

    //打开app时先调用此方法获取网关信息
    public void GetGateWayInformation(){
        DataSources.getInstance().GetGateWayInformation();
    }

    public void setDeviceState(String deviceid,int state){
        DataSources.getInstance().setDeviceStateResult(deviceid,state);
    }



    /**
     * 添加房间
     * @param roomid
     * @param roomname
     */
    public void CreateRoom(Context context,String ipaddress,int port,String roomname,String roomid){
        SendandReceiveOnAddRoom sendandReceiveOnAddRoom = new SendandReceiveOnAddRoom(context,ipaddress,port,roomname,roomid);
        Thread thread = new Thread(sendandReceiveOnAddRoom);
        thread.start();
    }

    /**
     * 删除房间
     * @param roomid
     * @param roomname
     */
    public void DeleteRoom(Context context,String ipaddress,int port,String roomname,String roomid){
        SendandReceiveOnDeleteRoom sendandReceiveOnDeleteRoom = new SendandReceiveOnDeleteRoom(context,ipaddress,port,roomname,roomid);
        Thread thread = new Thread(sendandReceiveOnDeleteRoom);
        thread.start();
    }

    /**
     * 修改房间
     * @param roomid
     * @param roomname
     */
    public void ModifyRoom(Context context,String ipaddress,int port,String roomname,String roomid){
        SendandReceiveOnModifyRoom sendandReceiveOnModifyRoom = new SendandReceiveOnModifyRoom(context,ipaddress,port,roomname,roomid);
        Thread thread = new Thread(sendandReceiveOnModifyRoom);
        thread.start();
    }

    /**
     * 房间开关
     * @param roomid
     * @param roomname
     * @param type  开关类型(0开,1关)
     */
    public void RoomSwitch(String roomid,String roomname,int type){
        //
    }

    /**
     * 添加设备
     * @param deviceName
     * @param uId
     * @param deviceId
     */
    public void AddDevice(Context context,String ipaddress,int port,String deviceName, int uId, short deviceId,short zoneType){
        SendandReceiveOnAddDevice sendandReceiveOnAddDevice = new SendandReceiveOnAddDevice(context,ipaddress,port,deviceName,uId);
        Thread thread = new Thread(sendandReceiveOnAddDevice);
        thread.start();
    }

    /**
     * 删除设备
     * @param deviceid
     * @param devicename
     * @param type
     */
    public void DeleteDevice(String deviceid,String devicename,int type){
        //
    }

    /**
     * 修改设备
     * @param deviceid
     * @param devicename
     * @param type
     */
    public void ModifyDevice(String deviceid,String devicename,int type){
        //
    }

    /**
     * 改变设备状态(0关1开)
     * @param deviceid
     * @param devicename
     * @param state
     */
    public void setDeviceState(String deviceid,String devicename,int state){
        //
    }

    /**
     * 改变设备值，（亮度）
     * @param deviceInfo
     * @param value
     */
//    public void setDeviceLevel(DeviceInfo deviceInfo,byte value){
//
//    }

    /**
     * 改变设备色调、饱和度
     *
     * @param deviceid 要改变色调、饱和度的设备
     * @param hue  色调
     * @param sat  饱和度
     */
    public void setDeviceHueSat(String deviceid,byte hue,byte sat){
        //
    }
}

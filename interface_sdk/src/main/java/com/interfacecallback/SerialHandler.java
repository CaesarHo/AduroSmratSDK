package com.interfacecallback;

import android.content.Context;

import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnAddDevice;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnAddRoom;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnDeleteDevice;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnDeleteRoom;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnModifyDevice;
import com.com.interfacecallback.receivingandsendthread.SendandReceiveOnModifyRoom;
import com.com.interfacecallback.receivingandsendthread.SendandReciiveOnDeviceSwitch;

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

    //获取网关所有房间
    public void getRooms(){

    }
    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setRoomsState(String ipaddress,int port,short roomId,byte state){

    }
    //设置房间中所有lamp的亮度
    public void setRoomsLevel(String ipaddress,int port,short roomId,byte level){

    }
    //设置房间中所有lamp的色调
    public void setRoomHue(String ipaddress,int port,short roomId,byte level){

    }
    //设置房间中所有lamp的饱和度
    public void setRoomSat(String ipaddress,int port,short roomId,byte sat){

    }
    //设置房间中所有设备的饱和度及色调
    public void setRoomHueSat(String ipaddress,int port,short roomId,byte hue,byte sat){

    }
    //设置房间中所有设备的色温
    public void setRoomColorTemperature(String ipaddress,int port,short groupId, int value){

    }
    //将指定的设备加入到指定的组中
    public void addDeviceToRoom(String ipaddress,int port,String roomName,int uId){

    }
    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(String ipaddress,int port,String roomName,int uId ){

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
     * @param ipaddress
     * @param devicename
     * @param deviceid
     */
    public void DeleteDevice(String ipaddress,int port,String devicename,int deviceid){
        SendandReceiveOnDeleteDevice sendandReceiveOnAddDevice = new SendandReceiveOnDeleteDevice(ipaddress,port,devicename,deviceid);
        Thread thread = new Thread(sendandReceiveOnAddDevice);
        thread.start();
    }

    /**
     * 修改设备
     * @param deviceid
     * @param devicename
     * @param deviceid
     */
    public void ModifyDevice(String ipaddress,int port,String devicename,int deviceid){
        SendandReceiveOnModifyDevice mModifyDevice = new SendandReceiveOnModifyDevice(ipaddress,port,devicename,deviceid);
        Thread thread = new Thread(mModifyDevice);
        thread.start();
    }

    /**
     * 改变设备状态(0关1开)
     * @param deviceid
     * @param devicename
     * @param state
     */
    public void setDeviceState(String ipaddress,int port,Short deviceid,String devicename,int uid ,int state){
        SendandReciiveOnDeviceSwitch mSwitchDevice = new SendandReciiveOnDeviceSwitch(ipaddress,port,deviceid,devicename,uid,state);
        Thread thread = new Thread(mSwitchDevice);
        thread.start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceState(String ipaddress,int port,Short deviceid,String devicename,int uid ,int state){

    }

    //获取设备状态
    public void getDeviceState(String ipaddress,int port,int uid){

    }
    //设置设备亮度回调
    public void setDeviceLevel(String ipaddress,int port,int uid,byte value){

    }
    //获取设备亮度回调
    public void getDeviceLevel(String ipaddress,int port,int uid){

    }
    //改变设备色调,饱和度
    public void setDeviceHueSat(String ipaddress,int port,byte hue,byte sat){

    }
    //获取设备色调
    public void getDeviceHue(String ipaddress,int port,int uid){

    }
    //获取设备饱和度
    public void getDeviceSat(String ipaddress,int port,int uid){

    }
    //改变色温值
    public void setColorTemperature(String ipaddress,int port,int uid){

    }

    //场景相关
    //获取网关所有场景
    public void getSences(String ipaddress,int port,short sencesid,String sencesname){

    }
    //获取指定场景的详细信息，
    public void getSenceDetails(String ipaddress,int port,short senceId, String senceName){

    }
    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(String ipaddress,int port,String senceName, int uid,short deviceId,byte delaytime){

    }
    //删除场景中指定设备成员senceName  场景名 设备uId
    public void deleteSenceMember(String ipaddress,int port,String senceName, int uId){

    }

    //删除指定场景
    public void deleteSence(String ipaddress,int port,short senceId, String senceName){

    }

    //修改指定场景
    public void ChangeSceneName(short sceneId, String newSceneName){

    }
}

package com.interfacecallback;

import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public interface SdkCallback {
    //回调函数ateway information
    void GatewayInformation(int port, InetAddress mIpAddress, String id);
    void setDeviceStateResult(String deviceid, int result);
    //房间操作
    //添加房间
    void AddRoomCallback(int result);
    //删除房间
    void DeleteRoomCallback(int result);
    //修改房间
    void ModifyRoomCallback(int result);
    //获取网关所有房间
    void getRoomsCallback();
    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    void setRoomsStateCallback(short roomId,byte state);
    //设置房间中所有lamp的亮度
    void setRoomsLevelCallback(short roomId,byte level);
    //设置房间中所有lamp的色调
    void setRoomHueCallback(short roomId,byte level);
    //设置房间中所有lamp的饱和度
    void setRoomSatCallback(short roomId,byte sat);
    //设置房间中所有设备的饱和度及色调
    void setRoomHueSatCallback(short roomId,byte hue,byte sat);
    //设置房间中所有设备的色温
    void setRoomColorTemperatureCallback(short groupId, int value);
    //将指定的设备加入到指定的组中
    void addDeviceToRoomCallback(String roomName,int uId);
    //将指定的设备从组中删除
    void deleteDeviceFromGroupCallback(String roomName,int uId );

    //设备操作
    void AddDeviceCallback(int result);
    void DeleteDeviceCallback(int result);
    void ModifyDeviceCallback(int result);
    //设备开关
    void setDeviceStateCallback(int result);
    //获取设备状态
    void getDeviceStateCallback();
    //设置设备亮度回调
    void setDeviceLevelCallback();
    //获取设备亮度回调
    void getDeviceLevelCallback();
    //改变设备色调,饱和度
    void setDeviceHueSatCallback();
    //获取设备色调
    void getDeviceHueCallback();
    //获取设备饱和度
    void getDeviceSatCallback();
    //改变色温值
    void setColorTemperatureCallback();

    //场景相关
    //获取网关所有场景
    void getSencesCallback(short sencesid,String sencesname);
    //获取指定场景的详细信息，
    void getSenceDetailsCallback(short senceId, String senceName);
    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    void addDeviceToSenceCallback(String senceName, int uid,short deviceId,byte delaytime);
    //删除场景中指定设备成员senceName  场景名 设备uId
    void deleteSenceMemberCallback(String senceName, int uId);
    //删除指定场景
    void deleteSence(int result);
    //修改指定场景
    void ChangeSceneName(short sceneId, String newSceneName);

}

package com.interfacecallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/7/11.
 */
public interface InterfaceCallback {
    //-------------------------------网关相关------------------------------
    //网关信息回调
    void GatewatInfoCallback(String gatewayName,String gatewayNo,String gatewaySoftwareVersion,String gatewayHardwareVersion,String gatewayIPv4Address,String gatewayDatetime);
    //===============================房间相关==============================
    //添加房间
    void AddGroupCallback(Short group_id,String group_name);
    //删除房间
    void DeleteGroupCallback(short result);
    //修改房间
    void ModifyGroupCallback(int result);
    //获取网关所有房间
    void getAllGroupsCallback(Short groupId,String groupsName,String groupIconPath,ArrayList<String> mac_data);
    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    void setGroupsStateCallback(short groupId,byte state);
    //设置房间中所有lamp的亮度
    void setGroupsLevelCallback(short groupId,byte level);
    //设置房间中所有lamp的色调
    void setGroupHueCallback(short groupId,byte level);
    //设置房间中所有lamp的饱和度
    void setGroupSatCallback(short groupId,byte sat);
    //设置房间中所有设备的饱和度及色调
    void setGroupHueSatCallback(short groupId,byte hue,byte sat);
    //设置房间中所有设备的色温
    void setGroupColorTemperatureCallback(short groupId, int value);
    //将指定的设备加入到指定的组中
    void addDeviceToGroupCallback(int result);
    //将指定的设备从组中删除
    void deleteDeviceFromGroupCallback(int result);

    //=========================设备操作===========================
    void SendExceptionCallack(int result);
    //允许设备入网
    void AgreeDeviceInNetCallback(int result);
    //扫面设备回调
    void ScanDeviceCallback(String deviceName,String profile_id,String devicemac,String deviceshortaddr,String deviceid,String main_endpoint,String in_cluster_count,String out_cluster_count);

    void AddDeviceCallback(String deviceName,byte deviceNetStatus,byte deviceSwitchState,
                           byte deviceLightLevel,byte deviceLightHue,byte deviceLightSat,
                           byte deviceLightColorTemperature,String deviceID,int deviceTypeID,
                           String deviceType,int Sensordata,Short clusterId,Short attribID,Short zoneType);
    //添加设备
    void DeleteDeviceCallback(int result);
    //删除设备
    void ModifyDeviceCallback(int result);
    //设置设备开关
    void setDeviceStateCallback(int result);
    //获取设备状态开关
    void getDeviceStateCallback(String deviceid,byte state);
    //获取设备在线状态
    void getDeviceOnLinStatus(String deviceName,String deviceId,byte status);
    //设置设备亮度回调
    void setDeviceLevelCallback(String deviceid,byte value);
    //获取设备亮度回调
    void getDeviceLevelCallback(String deviceid,byte value);
    //改变设备色调,饱和度
    void setDeviceHueSatCallback(String deviceid,int result);
    //获取设备色调
    void getDeviceHueCallback(String deviceid,byte hue);
    //获取设备饱和度
    void getDeviceSatCallback(String deviceid,byte sat);
    //改变色温值
    void setColorTemperatureCallback(String deviceid,byte value);

    //==========================场景相关===========================
    //获取网关所有场景
    void getSencesCallback(short sencesId,String sencesName,short groups_id,ArrayList<String> devices_mac);
    //添加场景
    void addSencesCallback(short sencesid ,String sencesName,short group_id);
    //获取指定场景的详细信息，
    void getSenceDetailsCallback(short senceId, String senceName,int uid,Short groupId);
    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    void addDeviceToSenceCallback(String senceName, int uid,short deviceId,int result);
    //删除场景中指定设备成员senceName  场景名 设备uId
    void deleteSenceMemberCallback(int result);
    //删除指定场景
    void deleteSence(int result);
    //修改指定场景
    void ChangeSceneName(short sceneId, String newSceneName,int result);

}

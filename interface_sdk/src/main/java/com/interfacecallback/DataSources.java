package com.interfacecallback;


import android.net.wifi.WifiManager;

/**
 * Created by best on 2016/7/11.
 */
public class DataSources {
    private static DataSources manager = null;
    private static InterfaceCallback sdkCallback;
    public Boolean IsThreadDisable = false;
    public static WifiManager.MulticastLock lock;
    public static DataSources getInstance() {
        if (manager == null) {
            manager = new DataSources();
        }
        return manager;
    }

    public void setSettingInterface(InterfaceCallback settingInterface) {
        this.sdkCallback = settingInterface;
    }

    //============================实现数据接收与发送==============================
    //------------------------------------设备相关--------------------------------
    //允许设备入网
    public void AgreeDeviceInNet(int result){
        sdkCallback.AgreeDeviceInNetCallback(1);
    }

    //网关信息
    public void GatewayInfo(String gatewayName,String gatewayNo,String gatewaySoftwareVersion,
                            String gatewayHardwareVersion,String gatewayIPv4Address,String gatewayDatetime){
        sdkCallback.GatewatInfoCallback(gatewayName,gatewayNo, gatewaySoftwareVersion,
        gatewayHardwareVersion,gatewayIPv4Address,gatewayDatetime);
    }


    public void ScanDeviceResult(String deviceName,String profileid,String devicemac,String deviceshortaddr,String deviceid,String main_endpoint){
        sdkCallback.ScanDeviceCallback(deviceName,profileid,devicemac,deviceshortaddr,deviceid,main_endpoint);
    }

    //添加设备
    public void AddDeviceResult(String deviceName,byte deviceNetStatus,byte deviceSwitchState,
                                byte deviceLightLevel,byte deviceLightHue,byte deviceLightSat,
                                byte deviceLightColorTemperature,String deviceID,int deviceTypeID,
                                String deviceType,int Sensordata,Short clusterId,Short attribID,Short zoneType){
        sdkCallback.AddDeviceCallback(deviceName,deviceNetStatus,deviceSwitchState,deviceLightLevel,deviceLightHue,
        deviceLightSat,deviceLightColorTemperature,deviceID,deviceTypeID,deviceType,Sensordata,clusterId,attribID,zoneType);
    }

    //设备删除回调
    public void DeleteDeviceResult(int result){
        sdkCallback.DeleteDeviceCallback(result);
    }

    //修改设备回调
    public void ModifyDeviceResult(int result){
        sdkCallback.ModifyDeviceCallback(result);
    }

    //设备状态改变
    public void setDeviceStateResule(int state){
        sdkCallback.setDeviceStateCallback(state);
    }

    //获取设备状态
    public void getDeviceState(String deviceid,byte state){
        sdkCallback.getDeviceStateCallback(deviceid,state);
    }

    //获取设备在线状态
    public void getDeviceStatus(String deviceName,String deviceId,byte status){
        sdkCallback.getDeviceOnLinStatus(deviceName,deviceId,status);
    }

    //改变设备值
    public void setDeviceLevel(String deviceId,byte value){
        sdkCallback.setDeviceLevelCallback(deviceId,value);
    }

    //获取设备亮度值
    public void getDeviceLevel(String deviceId,byte value){
        sdkCallback.getDeviceLevelCallback(deviceId,value);
    }

    //设置设备色调,饱和度
    public void setDeviceHueSat(String deviceId,int result){
        sdkCallback.setDeviceHueSatCallback(deviceId,result);
    }

    //获取设备色调
    public void getDeviceHue(String deviceId,byte hue){
        sdkCallback.getDeviceHueCallback(deviceId,hue);
    }

    //获取设备饱和度
    public void getDeviceSat(String deviceId,byte sat){
        sdkCallback.getDeviceSatCallback(deviceId,sat);
    }

    //设置色温值
    public void setDeviceColorTemperature(String deviceid,byte value){
        sdkCallback.setColorTemperatureCallback(deviceid,value);
    }

    //---------------------------------场景相关----------------------------------
    //获取网关所有场景
    public void getAllSences(int sencesId,String sencesName,String sencesIconPath){
        sdkCallback.getSencesCallback(sencesId,sencesName,sencesIconPath);
    }

    //添加场景
    public void AddSences(int sencesid ,String sencesName,String sceneIconPath){
        sdkCallback.addSencesCallback(sencesid,sencesName,sceneIconPath);
    }

    //获取场景的详细信息
    public void getSencesDetails(Short sencesId,String sencesName,int uid,Short roomId){
        sdkCallback.getSenceDetailsCallback(sencesId,sencesName,uid,roomId);
    }

    //添加指定设备至场景
    public void AddDeviceToSences(String senceName, int uid,short deviceId,int result){
        sdkCallback.addDeviceToSenceCallback(senceName,uid,deviceId,result);
    }

    //删除场景中指定设备成员senceName  场景名 设备uId
    public void DeleteSencesMember(int result){
        sdkCallback.deleteSenceMemberCallback(result);
    }

    //删除场景
    public void DeleteSences(int result){
        sdkCallback.deleteSence(result);
    }

    //修改场景
    public void ChangeSencesName(Short sencesId,String sencesName,int result){
        sdkCallback.ChangeSceneName(sencesId,sencesName,result);
    }

    //--------------------------------组相关------------------------------
    //获取网关所有组
    public void GetAllGroups(Short groupId,String groupsName,String groupIconPath){
        sdkCallback.getAllGroupsCallback(groupId,groupsName,groupIconPath);
    }

    //添加组
    public void AddGroupResult(int result){
        sdkCallback.AddGroupCallback(result);
    }

    //删除组
    public void DeleteGroupResult(int result){
        sdkCallback.DeleteGroupCallback(result);
    }

    //修改组
    public void ModifyGroupResult(int result){
        sdkCallback.ModifyGroupCallback(result);
    }

    //设置组状态
    public void setGroupsState(Short groupId,byte state){
        sdkCallback.setGroupsStateCallback(groupId,state);
    }

    //设置组设备的亮度level
    public void setGroupLevel(Short groupId,byte level){
        sdkCallback.setGroupsLevelCallback(groupId,level);
    }

    //设置房间中所有lamp的色调
    public void setGroupHue(Short groupId,byte hue){
        sdkCallback.setGroupHueCallback(groupId,hue);
    }

    //设置房间中所有lamp的饱和度
    public void setGroupSat(Short groupId,byte sat){
        sdkCallback.setGroupSatCallback(groupId,sat);
    }

    //设置房间中所有设备的饱和度及色调
    public void setGroupHueSat(Short grouId,byte hue,byte sat){
        sdkCallback.setGroupHueSatCallback(grouId,hue,sat);
    }

    //设置房间中所有设备的色温
    public void setGroupColorTemperature(Short grouId,int value){
        sdkCallback.setGroupColorTemperatureCallback(grouId,value);
    }

    //将指定设备添加到指定组中
    public void addDeviceToGroup(int result){
        sdkCallback.addDeviceToGroupCallback(result);
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(int result){
        sdkCallback.deleteDeviceFromGroupCallback(result);
    }
}

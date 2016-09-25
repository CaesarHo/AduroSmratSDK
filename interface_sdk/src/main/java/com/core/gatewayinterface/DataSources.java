package com.core.gatewayinterface;

import android.net.wifi.WifiManager;

import com.core.db.AppDeviceInfo;

import java.util.ArrayList;

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
    //发送命令异常回调
    public void SendExceptionResult(int result){
        sdkCallback.SendExceptionCallack(result);
    }
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

//    public void ScanDeviceResult(String deviceName,String profileid,String devicemac,String deviceshortaddr,
//                                 String deviceid,String main_endpoint,String in_cluster_count,
//                                 String out_cluster_count,String device_zone_type){
//        sdkCallback.ScanDeviceCallback(deviceName,profileid,devicemac,deviceshortaddr,
//                deviceid,main_endpoint,in_cluster_count,out_cluster_count,device_zone_type);
//    }
    public void ScanDeviceResult(AppDeviceInfo appDeviceInfo){
        sdkCallback.ScanDeviceCallback(appDeviceInfo);
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
    public void getDeviceState(String device_mac,int state){
        sdkCallback.getDeviceStateCallback(device_mac,state);
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



    //接受传感器数据
    public void getReceiveSensor(String mDevMac,int state,String time){
        sdkCallback.getReceiveSensorDataCallback(mDevMac,state,time);
    }


    //---------------------------------场景相关----------------------------------
    //获取网关所有场景
    public void getAllScenes(short sencesId,String sencesName,short groups_id,ArrayList<String> devices_mac){
        sdkCallback.getScenesCallback(sencesId,sencesName,groups_id,devices_mac);
    }

    //添加场景
    public void AddScene(short sencesid ,String sencesName,short group_id){
        sdkCallback.addSceneCallback(sencesid,sencesName,group_id);
    }

    //获取场景的详细信息说
    public void getScenesDetails(Short sceneId,String sceneName,int uid,Short roomId){
        sdkCallback.getSceneDetailsCallback(sceneId,sceneName,uid,roomId);
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
    public void ChangeSencesName(Short sencesId,String sencesName){
        sdkCallback.ChangeSceneName(sencesId,sencesName);
    }

    //--------------------------------组相关------------------------------
    //获取网关所有组
    public void GetAllGroups(Short groupId,String groupsName,String groupIconPath,ArrayList<String> mac_data){
        sdkCallback.getAllGroupsCallback(groupId,groupsName,groupIconPath,mac_data);
    }

    //添加组
    public void AddGroupResult(short group_id,String group_name){
        sdkCallback.AddGroupCallback(group_id,group_name);
    }

    //删除组
    public void DeleteGroupResult(short result){
        sdkCallback.DeleteGroupCallback(result);
    }

    //修改组
    public void ChangeGroupName(short groupId,String group_name){
        sdkCallback.ChangeGroupNameCallback(groupId,group_name);
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

    //=================================任务相关==================================
    //获取网关所有任务
    public void getAllTasks(int task_no,String task_name,int isEnabled,int task_type,
                           int task_cycle,int task_hour,int task_minute,
                           int sensor_state, String sensor,String device_mac,int cmd_size,
                           String serial_type1, int action_state1,int action_state6,
                           String serial_type2, int action_state2,int action_state7,
                           String serial_type3, int action_state3,int action_state8,
                           String serial_type4, int action_state4,int action_state9,
                           String serial_type5, int action_state5,int action_state10){
        sdkCallback.getAllTasksCallback(task_no,task_name,isEnabled,task_type, task_cycle,task_hour,
                task_minute,sensor_state, sensor,device_mac,cmd_size, serial_type1,action_state1,action_state6,
                serial_type2, action_state2, action_state7, serial_type3,action_state3,action_state8,
                serial_type4,action_state4,action_state9,serial_type5,action_state5,action_state10);
    }
}

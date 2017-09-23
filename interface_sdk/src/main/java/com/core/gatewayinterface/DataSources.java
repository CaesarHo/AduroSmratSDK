package com.core.gatewayinterface;

import com.core.entity.AppDevice;
import com.core.entity.AppGateway;
import com.core.entity.AppGroup;
import com.core.entity.AppScene;
import com.core.entity.AppTask;
import com.core.entity.AppTask2;
import com.core.utils.Utils;

import java.util.ArrayList;

/**
 * Created by best on 2016/7/11.
 */
public class DataSources {
    private static DataSources manager = null;
    private static InterfaceCallback sdkCallback;
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
    public void vExceptionResult(int result){
        sdkCallback.vRetExceptionCallack(result);
    }
    public void ResetGatewayResult(int result){
        sdkCallback.vRetResetGatewayCallback(result);
    }
    //------------------------------------设备相关--------------------------------
    //允许设备入网
    public void AgreeDeviceInNet(int result){
        sdkCallback.AgreeDeviceInNetCallback(1);
    }

    //网关信息
    public void GatewayInfo(AppGateway appGateway){
        sdkCallback.GatewatInfoCallback(appGateway);
    }

    public void ScanDeviceResult(AppDevice appDevice){
        sdkCallback.ScanDeviceCallback(appDevice);
    }

    //添加设备
    public void AddDeviceResult(AppDevice appDevice){
        sdkCallback.AddDeviceCallback(appDevice);
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
    public void getDeviceLevel(String deviceId,int value){
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
    public void getReceiveSensor(String device_address,int state){
        String time = String.valueOf(System.currentTimeMillis());
        sdkCallback.getReceiveSensorDataCallback(device_address,state, Utils.getFormatTellDate(time));
    }

    //传感器电量
    public void responseBatteryValue(String device_mac,int value){
        sdkCallback.vRetResponseBatteryValueCallback(device_mac,value);
    }

    //解析zonetype
    public void vDataZoneType(String device_mac,String zone_type){
        sdkCallback.vRetDeviceZoneType(device_mac,zone_type);
    }

    //绑定设备
    public void BindDevice(String device_short_addr,short frequency,double voltage,double current,double power,double power_factor){
        sdkCallback.bingdevicecallback(device_short_addr,frequency,voltage,current,power,power_factor);
    }

    //---------------------------------场景相关----------------------------------
    //获取网关所有场景
    public void getAllScenes(AppScene appScene){
        sdkCallback.getScenesCallback(appScene);
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
    public void GetAllGroups(AppGroup appGroup){
        sdkCallback.getAllGroupsCallback(appGroup);
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
    public void getAllTasks(AppTask2 appTask2){
        sdkCallback.getAllTasksCallback(appTask2);
    }

    //网关相关
    public void GatewayUpdateVersion(int version){
        sdkCallback.vRetGatewayUpdateVersion(version);
    }
    /**
     * 网关更新结果
     */
    public void GatewayUpdateResult(int result){
        sdkCallback.vRetGatewayUpdateResult(result);
    }

    /**
     * 网关服务器地址
     */
    public void GatewayServerAddress(String address){
        sdkCallback.vRetGatewayServerAddress(address);
    }
}

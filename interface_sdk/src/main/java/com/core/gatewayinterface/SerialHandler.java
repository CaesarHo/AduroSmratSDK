package com.core.gatewayinterface;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.core.db.AppDeviceInfo;
import com.core.db.GatewayInfo;
import com.core.threadhelper.AddDeviceToGroup;
import com.core.threadhelper.AddDeviceToSence;
import com.core.threadhelper.AddGroup;
import com.core.threadhelper.AddSence;
import com.core.threadhelper.AgreeDeviceInNet;
import com.core.threadhelper.CreateTask;
import com.core.threadhelper.CreateTimingDeviceTask;
import com.core.threadhelper.CreateTimingSceneTask;
import com.core.threadhelper.CreateTriggerDeviceTask;
import com.core.threadhelper.CreateTriggerSceneTask;
import com.core.threadhelper.DeleteDeviceFromGroup;
import com.core.threadhelper.DeleteGroup;
import com.core.threadhelper.DeleteScence;
import com.core.threadhelper.DeleteDeviceFromScene;
import com.core.threadhelper.DeleteTask;
import com.core.threadhelper.GetAllDevices;
import com.core.threadhelper.GetAllGroups;
import com.core.threadhelper.GetAllSences;
import com.core.threadhelper.GetAllTasks;
import com.core.threadhelper.GetDeviceHue;
import com.core.threadhelper.GetDeviceLevel;
import com.core.threadhelper.GetDeviceOnLinStatus;
import com.core.threadhelper.GetDeviceSat;
import com.core.threadhelper.GetDeviceSwitchState;
import com.core.threadhelper.GetSenceDetails;
import com.core.threadhelper.IdentifyDevice;
import com.core.threadhelper.RecallScene;
import com.core.threadhelper.SendDeleteDeviceCmd;
import com.core.threadhelper.SetColorTemperature;
import com.core.threadhelper.SetDeviceHue;
import com.core.threadhelper.SetDeviceHueSat;
import com.core.threadhelper.SetDeviceLevel;
import com.core.threadhelper.SetDeviceSwitchState;
import com.core.threadhelper.SetGroupColorTemperature;
import com.core.threadhelper.SetGroupHue;
import com.core.threadhelper.SetGroupHueSat;
import com.core.threadhelper.SetGroupLevel;
import com.core.threadhelper.SetGroupSat;
import com.core.threadhelper.SetGroupState;
import com.core.threadhelper.UDPHelper;
import com.core.threadhelper.UpdateDeviceName;
import com.core.threadhelper.UpdateGroupName;
import com.core.threadhelper.UpdateSceneName;
import com.core.threadhelper.setGateWayTime;

/**
 * Created by best on 2016/7/11.
 */
public class SerialHandler {
    private static SerialHandler manager = null;
    Context context;
    private String ipaddress;
    private int port = -1;
    private String aeskey;

    private SerialHandler() {

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
    public void Init(Context context, String aeskey, InterfaceCallback sdkCallback) {
        DataSources.getInstance().setSettingInterface(sdkCallback);
        this.context = context;
        this.aeskey = aeskey;
        this.ipaddress = GatewayInfo.getInstance().getInetAddress(context);
        this.port = GatewayInfo.getInstance().getPort(context);
        GatewayInfo.getInstance().setAesKey(context, aeskey);
    }

    /**
     * 扫描网关信息
     *
     * @param context
     * @param wifiManager
     */
    public void ScanGatewayInfo(Context context, WifiManager wifiManager) {
        new Thread(new UDPHelper(context, wifiManager)).start();
    }

    /**
     * 设置网关时间
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    public void setGateWayTime(int year, int month, int day, int hour, int minute, int second) {
        new Thread(new setGateWayTime(year, month, day, hour, minute, second)).start();
    }


    //组操作 ===========================start ============================

    /**
     * 添加房间
     *
     * @param group_name
     */
    public void CreateGroup(String group_name) {
        new Thread(new AddGroup(group_name)).start();
    }

    /**
     * 删除房间
     *
     * @param groupid
     */
    public void DeleteGroup(short groupid) {
        new Thread(new DeleteGroup(groupid)).start();
    }

    /**
     * 修改房间
     *
     * @param group_id
     * @param group_name
     */
    public void ChangeGroupName(short group_id, String group_name) {
        UpdateGroupName mUpdateGroup = new UpdateGroupName(group_id, group_name);
        Thread thread = new Thread(mUpdateGroup);
        thread.start();
    }

    //获取网关所有房间
    public void getGroups() {
        GetAllGroups getAllGroups = new GetAllGroups();
        Thread getGroups = new Thread(getAllGroups);
        getGroups.start();
    }

    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setGroupState(short groupId, int state) {
        int group_id_int = (int) groupId;
        SetGroupState mSetGroupState = new SetGroupState(group_id_int, state);
        Thread thread = new Thread(mSetGroupState);
        thread.start();
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId, int level) {
        int group_id_int = (int) groupId;
        SetGroupLevel mSetGroupLevel = new SetGroupLevel(group_id_int, level);
        Thread thread = new Thread(mSetGroupLevel);
        thread.start();
    }

    //设置房间中所有lamp的色调(未实现)
    public void setGroupHue(short groupId, byte hue) {
        SetGroupHue mSetGroupHue = new SetGroupHue(ipaddress, port, groupId, hue);
        Thread thread = new Thread(mSetGroupHue);
        thread.start();
    }

    //设置房间中所有lamp的饱和度(未实现)
    public void setGroupSat(short groupId, byte sat) {
        SetGroupSat setGroupSat = new SetGroupSat(ipaddress, port, groupId, sat);
        Thread thread = new Thread(setGroupSat);
        thread.start();
    }

    //设置房间中所有设备的饱和度及色调(未实现)
    public void setGroupHueSat(short groupId, byte hue, byte sat) {
        SetGroupHueSat mSetGroupHueSat = new SetGroupHueSat(ipaddress, port, groupId, hue, sat);
        Thread thread = new Thread(mSetGroupHueSat);
        thread.start();
    }

    //设置房间中所有设备的色温(未实现)
    public void setGroupColorTemperature(short groupId, int value) {
        SetGroupColorTemperature mSetGroupColorTemperature = new SetGroupColorTemperature(ipaddress, port, groupId, value);
        Thread thread = new Thread(mSetGroupColorTemperature);
        thread.start();
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(short group_id, String device_mac, String device_shortaddr, String main_endpoint) {
        AddDeviceToGroup mAddDeviceToGroup = new AddDeviceToGroup(group_id, device_mac, device_shortaddr, main_endpoint);
        Thread thread = new Thread(mAddDeviceToGroup);
        thread.start();
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(short group_id, String device_mac, String shortaddr, String main_endpoint) {
        DeleteDeviceFromGroup mDeleteDeviceFromGroup = new DeleteDeviceFromGroup(group_id, device_mac, shortaddr, main_endpoint);
        Thread thread = new Thread(mDeleteDeviceFromGroup);
        thread.start();
    }

    //组操作============================end====================================

    //设备操作 ===============================start============================

    /**
     * 允许设备入网
     */
    public void AgreeDeviceInNet() {
        new Thread(new AgreeDeviceInNet()).start();
    }

    /**
     * search device
     */
    public void GetAllDeviceListen() {
        new Thread(new GetAllDevices()).start();
    }

    /**
     * 删除设备
     *
     * @param devicemac
     */
    public void DeleteDevice(String devicemac) {
        new Thread(new SendDeleteDeviceCmd(devicemac)).start();
    }

    /**
     * 选择设备提示
     * @param devicemac
     * @param shortaddr
     * @param main_point
     * @param second
     */
    public void IdentifyDevice(String devicemac , String shortaddr , String main_point,int second){
        new Thread(new IdentifyDevice(devicemac,shortaddr,main_point,second)).start();
    }


    /**
     * 改变设备状态(0关1开)
     *
     * @param devicemac
     * @param deviceshortaddr
     * @param main_endpoint
     */
    public void setDeviceSwitchState(String devicemac, String deviceshortaddr, String main_endpoint, int value) {
        new Thread(new SetDeviceSwitchState(devicemac, deviceshortaddr, main_endpoint, value)).start();
    }

    //设置设备亮度回调
    public void setDeviceLevel(String devicemac, String shortaddr, String main_point, int Level) {
        new Thread(new SetDeviceLevel(devicemac, shortaddr, main_point, Level)).start();
    }

    public void setDeviceHue(String devicemac, String shortaddr, String main_point, int hue) {
        new Thread(new SetDeviceHue(devicemac, shortaddr, main_point, hue)).start();
    }

    //改变设备色调,饱和度
    public void setDeviceHueSat(String devicemac, String shortaddr, String main_point, int hue, int sat) {
        new Thread(new SetDeviceHueSat(devicemac, shortaddr, main_point, hue, sat)).start();
    }

    //改变色温值
    public void setColorTemperature(String devicemac, String shortaddr, String main_point, int value) {
        SetColorTemperature mSetColorTemperature = new SetColorTemperature(devicemac, shortaddr, main_point, value);
        Thread thread = new Thread(mSetColorTemperature);
        thread.start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(AppDeviceInfo appDeviceInfo) {
        GetDeviceSwitchState mDeviceSwitchState = new GetDeviceSwitchState(appDeviceInfo);
        Thread thread = new Thread(mDeviceSwitchState);
        thread.start();
    }

    public void getDeviceOnLinStatus(String devicename, String deviceid) {
        GetDeviceOnLinStatus getDeviceOnLinStatus = new GetDeviceOnLinStatus();
        Thread thread = new Thread(getDeviceOnLinStatus);
        thread.start();
    }

    //获取设备亮度回调
    public void getDeviceLevel(String deviceid) {
        GetDeviceLevel mDeviceLevel = new GetDeviceLevel(ipaddress, port, deviceid);
        Thread thread = new Thread(mDeviceLevel);
        thread.start();
    }

    //获取设备色调
    public void getDeviceHue(String deviceid) {
        GetDeviceHue mDeviceHue = new GetDeviceHue(ipaddress, port, deviceid);
        Thread thread = new Thread(mDeviceHue);
        thread.start();
    }

    //获取设备饱和度
    public void getDeviceSat(String deviceid) {
        GetDeviceSat mDeviceSat = new GetDeviceSat(ipaddress, port, deviceid);
        Thread thread = new Thread(mDeviceSat);
        thread.start();
    }

    //修改设备名称
    public void UpdateDeviceName(String device_name, String mac, String shortaddr, String main_point) {
        UpdateDeviceName updateDeviceName = new UpdateDeviceName(device_name, mac, shortaddr, main_point);
        Thread thread = new Thread(updateDeviceName);
        thread.start();
    }


    //设备 ==========================end============================

    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences() {
        GetAllSences mDeviceSences = new GetAllSences();
        Thread thread = new Thread(mDeviceSences);
        thread.start();
    }

    //添加场景
    public void AddSence(String Out_Scene_Name, short Out_Group_Id) {
        AddSence addSences = new AddSence(Out_Scene_Name, Out_Group_Id);
        Thread thread = new Thread(addSences);
        thread.start();
    }

    //Recall场景
    public void RecallScene(Short Group_Id, Short Scene_Id) {
        RecallScene recallScene = new RecallScene(Group_Id, Scene_Id);
        Thread thread = new Thread(recallScene);
        thread.start();
    }

    //获取指定场景的详细信息，
    public void getSenceDetails(short senceId, String senceName) {
        GetSenceDetails mDeviceSenceDetails = new GetSenceDetails(ipaddress, port, senceId, senceName);
        Thread thread = new Thread(mDeviceSenceDetails);
        thread.start();
    }

    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(String mac, String shortaddr, String main_endpoint, short group_id, short scene_id) {
        AddDeviceToSence mAddDeviceToSence = new AddDeviceToSence(mac, shortaddr, main_endpoint, group_id, scene_id);
        Thread thread = new Thread(mAddDeviceToSence);
        thread.start();
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteDeviceFromScene(short scene_id, String mac, String shortaddr, String main_endpoint) {
        DeleteDeviceFromScene mDeleteSenceMember = new DeleteDeviceFromScene(scene_id, mac, shortaddr, main_endpoint);
        Thread thread = new Thread(mDeleteSenceMember);
        thread.start();
    }

    //删除指定场景
    public void deleteScence(short senceId) {
        DeleteScence mDeleteSence = new DeleteScence(senceId);
        Thread thread = new Thread(mDeleteSence);
        thread.start();
    }

    //修改指定场景
    public void ChangeSceneName(short sceneId, String newSceneName) {
        UpdateSceneName mChangeSceneName = new UpdateSceneName(sceneId, newSceneName);
        Thread thread = new Thread(mChangeSceneName);
        thread.start();
    }
    //场景操作 =========================end============================


    //任务操作============================start==============================

    /**
     * 获取网关所有设备
     */
    public void GetAllTask() {
        new Thread(new GetAllTasks()).start();
    }

    /**
     * 创建定时设备任务
     */
    public void CreateTimingDeviceTask(String task_name, byte is_run,byte task_cycle, int task_hour, int task_minute,
                                       String device_mac, String task_device_shortaddr, String task_device_main_point,
                                       int cmd_size,
                                       String dev_switch, int switch_state,
                                       String dev_level, int level_value,
                                       String dev_hue, int hue_value, int sat_value,
                                       String dev_temp, int temp_value){
        new Thread(new CreateTimingDeviceTask(task_name,is_run,task_cycle,task_hour, task_minute,
                device_mac,task_device_shortaddr,task_device_main_point,
                cmd_size,dev_switch,switch_state,dev_level,level_value,
                dev_hue,hue_value,sat_value,dev_temp,temp_value)).start();
    }

    /**
     *创建定时场景任务
     */
    public void CreateTimingSceneTask(String task_name, byte is_run, byte task_cycle, int task_hour, int task_minute,
                                      int cmd_size, int group_id, int scene_id){
        new Thread(new CreateTimingSceneTask(task_name,is_run,task_cycle,task_hour,
                task_minute,cmd_size,group_id,scene_id)).start();
    }

    /**
     * 创建触发设备任务
     */
    public void CreateTriggerDeviceTask(String task_name, byte is_run,
                                        int sensor_state,String sensor_mac,
                                        String device_mac, String task_device_shortaddr, String task_device_main_point,
                                        int cmd_size,
                                        String dev_switch, int switch_state,
                                        String dev_level, int level_value,
                                        String dev_hue, int hue_value, int sat_value,
                                        String dev_temp, int temp_value){
        new Thread(new CreateTriggerDeviceTask(task_name,is_run,
                sensor_state,sensor_mac,
                device_mac,task_device_shortaddr,task_device_main_point,
                cmd_size,dev_switch,switch_state,dev_level,level_value,
                dev_hue,hue_value,sat_value,dev_temp,temp_value)).start();
    }

    /**
     *创建定时场景任务
     */
    public void CreateTriggerSceneTask(String task_name, byte is_run,
                                       int sensor_state,String sensor_mac,int cmd_size, int group_id, int scene_id){
        new Thread(new CreateTriggerSceneTask(task_name,is_run,sensor_state,sensor_mac,cmd_size,group_id,scene_id)).start();
    }

    /**
     * 删除任务
     */
    public void DeleteTask(int task_id) {
        new Thread(new DeleteTask(task_id)).start();
    }












    /**
     * 创建任务
     */
    public void CreateTask(String task_name, byte is_run, byte task_type, byte task_cycle, int task_hour, int task_minute,
                           int device_action, String action_mac, String device_mac, String task_device_shortaddr,
                           String task_device_main_point, int cmd_size,
                           String dev_switch, int switch_state,
                           String dev_level, int level_value,
                           String dev_hue, int hue_value, int sat_value,
                           String dev_temp, int temp_value,
                           String recall_scene, int group_id, int scene_id) {
        new Thread(new CreateTask(task_name, is_run, task_type, task_cycle, task_hour, task_minute, device_action,
                action_mac, device_mac, task_device_shortaddr, task_device_main_point, cmd_size, dev_switch, switch_state,
                dev_level, level_value, dev_hue, hue_value, sat_value, dev_temp, temp_value, recall_scene, group_id, scene_id))
                .start();
    }
}

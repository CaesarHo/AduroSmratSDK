package com.core.gatewayinterface;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.GroupCmdData;
import com.core.cmddata.SceneCmdData;
import com.core.entity.AppDevice;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.groups.AddDeviceToGroup;
import com.core.threadhelper.scenes.AddDeviceToSence;
import com.core.threadhelper.groups.AddGroup;
import com.core.threadhelper.scenes.AddSence;
import com.core.threadhelper.devices.AgreeDeviceInNet;
import com.core.threadhelper.tasks.EditTask;
import com.core.threadhelper.tasks.CreateTimingDeviceTask;
import com.core.threadhelper.tasks.CreateTimingSceneTask;
import com.core.threadhelper.tasks.CreateTriggerDeviceTask;
import com.core.threadhelper.tasks.CreateTriggerSceneTask;
import com.core.threadhelper.groups.DeleteDeviceFromGroup;
import com.core.threadhelper.groups.DeleteGroup;
import com.core.threadhelper.scenes.DeleteScence;
import com.core.threadhelper.scenes.DeleteDeviceFromScene;
import com.core.threadhelper.tasks.DeleteTask;
import com.core.threadhelper.devices.GetAllDevices;
import com.core.threadhelper.groups.GetAllGroups;
import com.core.threadhelper.scenes.GetAllSences;
import com.core.threadhelper.tasks.GetAllTasks;
import com.core.threadhelper.devices.GetDeviceHue;
import com.core.threadhelper.devices.GetDeviceLevel;
import com.core.threadhelper.devices.GetDeviceOnLinStatus;
import com.core.threadhelper.devices.GetDeviceSat;
import com.core.threadhelper.devices.GetDeviceSwitchState;
import com.core.threadhelper.scenes.GetSenceDetails;
import com.core.threadhelper.devices.IdentifyDevice;
import com.core.threadhelper.scenes.RecallScene;
import com.core.threadhelper.devices.DeleteDevice;
import com.core.threadhelper.devices.SetDeviceColorTemp;
import com.core.threadhelper.devices.SetDeviceHue;
import com.core.threadhelper.devices.SetDeviceHueSat;
import com.core.threadhelper.devices.SetDeviceLevel;
import com.core.threadhelper.devices.SetDeviceSwitchState;
import com.core.threadhelper.groups.SetGroupColorTemp;
import com.core.threadhelper.groups.SetGroupHue;
import com.core.threadhelper.groups.SetGroupHueSat;
import com.core.threadhelper.groups.SetGroupLevel;
import com.core.threadhelper.groups.SetGroupSat;
import com.core.threadhelper.groups.SetGroupState;
import com.core.threadhelper.UDPHelper;
import com.core.threadhelper.devices.UpdateDeviceName;
import com.core.threadhelper.groups.UpdateGroupName;
import com.core.threadhelper.scenes.UpdateSceneName;
import com.core.threadhelper.setGateWayTime;
import com.core.utils.NetworkUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by best on 2016/7/11.
 */
public class SerialHandler {
    private static SerialHandler manager = null;
    public Context context;
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
    public void Init(Context mContext, String aeskey, InterfaceCallback sdkCallback) {
        DataSources.getInstance().setSettingInterface(sdkCallback);
        this.context = mContext;
        this.aeskey = aeskey;
        this.ipaddress = GatewayInfo.getInstance().getInetAddress(context);
        this.port = GatewayInfo.getInstance().getPort(context);
        GatewayInfo.getInstance().setAesKey(context, aeskey);

        Constants.CLIENT_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("clientId: " + Constants.CLIENT_ID);

        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            MqttManager.getInstance().init(context);
            //如果網絡是3G網，則連接mqtt及訂閱mqtt
            setMqttCommunication();
        }
    }

    /**
     * MQTT连接订阅
     */
    public void setMqttCommunication() {
        //連接MQTT服務器
        boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.CLIENT_ID);
        System.out.println("isConnected: " + isConnect);
        if (isConnect) {
            MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context), 2);
            System.out.println("网关编号 = " + GatewayInfo.getInstance().getGatewayNo(context));
        }
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
     * @param year @param month @param day @param hour @param minute @param second
     */
    public void setGateWayTime(int year, int month, int day, int hour, int minute, int second) {
        new Thread(new setGateWayTime(year, month, day, hour, minute, second)).start();
    }

    //===============================设备操作 start============================

    /**
     * 允许设备入网
     */
    public void AgreeDeviceInNet() {
        new Thread(new AgreeDeviceInNet(context)).start();
    }

    /**
     * search device
     */
    public void GetAllDeviceListen() {
        new Thread(new GetAllDevices(context)).start();
    }

    /**
     * 删除设备
     *
     * @param devicemac
     */
    public void DeleteDevice(String devicemac) {
        new Thread(new DeleteDevice(context,devicemac)).start();
    }

    /**
     * 选择设备提示
     */
    public void IdentifyDevice(AppDevice appDevice, int second) {
        new Thread(new IdentifyDevice(context,appDevice, second)).start();
    }

    /**
     * 改变设备状态(0关1开)
     */
    public void setDeviceSwitchState(AppDevice appDevice, int value) {
        new Thread(new SetDeviceSwitchState(context,appDevice, value)).start();
    }

    //设置设备亮度回调
    public void setDeviceLevel(AppDevice appDevice, int Level) {
        new Thread(new SetDeviceLevel(context,appDevice, Level)).start();
    }

    //设置颜色
    public void setDeviceHue(AppDevice appDevice, int hue) {
        new Thread(new SetDeviceHue(context,appDevice, hue)).start();
    }

    //改变设备色调,饱和度
    public void setDeviceHueSat(AppDevice appDevice, int hue, int sat) {
        new Thread(new SetDeviceHueSat(context,appDevice, hue, sat)).start();
    }

    //改变色温值
    public void setColorTemperature(AppDevice appDevice, int value) {
        new Thread(new SetDeviceColorTemp(context,appDevice, value)).start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(AppDevice appDevice) {
        new Thread(new GetDeviceSwitchState(context,appDevice)).start();
    }

    public void getDeviceOnLinStatus(String devicename, String deviceid) {
        GetDeviceOnLinStatus getDeviceOnLinStatus = new GetDeviceOnLinStatus();
        Thread thread = new Thread(getDeviceOnLinStatus);
        thread.start();
    }

    //获取设备亮度回调
    public void getDeviceLevel(AppDevice appDevice) {
        new Thread(new GetDeviceLevel(context,appDevice)).start();
    }

    //获取设备色调
    public void getDeviceHue(String deviceid) {
        new Thread(new GetDeviceHue(context,ipaddress, port, deviceid)).start();
    }

    //获取设备饱和度
    public void getDeviceSat(String deviceid) {
        new Thread(new GetDeviceSat(context,ipaddress, port, deviceid)).start();
    }

    //修改设备名称
    public void UpdateDeviceName(AppDevice appDevice, String device_name){
        new Thread(new UpdateDeviceName(context,appDevice, device_name)).start();
    }
    //设备 ==========================end============================


    //组操作 ===========================start ============================

    /**
     * 添加房间
     * @param group_name
     */
    public void CreateGroup(String group_name){
        new Thread(new AddGroup(context,group_name)).start();
    }

    /**
     * 删除房间
     *
     * @param groupid
     */
    public void DeleteGroup(short groupid) {
        new Thread(new DeleteGroup(context,groupid)).start();
    }

    /**
     * 修改房间
     *
     * @param group_id
     * @param group_name
     */
    public void ChangeGroupName(short group_id, String group_name){
        new Thread(new UpdateGroupName(context,group_id, group_name)).start();
    }

    //获取网关所有房间
    public void getGroups() {
        new Thread(new GetAllGroups(context)).start();
    }

    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setGroupState(short groupId, int state) {
        new Thread(new SetGroupState(context,(int)groupId,state)).start();
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId, int level) {
        new Thread(new SetGroupLevel(context,(int)groupId,level)).start();
    }

    //设置房间中所有lamp的色调(未实现)
    public void setGroupHue(short groupId, byte hue) {
        if (!NetworkUtil.NetWorkType(context)) {
            System.out.println("远程打开 = " + "setGroupHue");
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
        } else {
            new Thread(new SetGroupHue(ipaddress, port, groupId, hue)).start();
        }
    }

    //设置房间中所有lamp的饱和度(未实现)
    public void setGroupSat(short groupId, byte sat) {
        if (!NetworkUtil.NetWorkType(context)) {
            System.out.println("远程打开 = " + "setGroupSat");
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
        } else {
            new Thread(new SetGroupSat(ipaddress, port, groupId, sat)).start();
        }
    }

    //设置房间中所有设备的饱和度及色调(未实现)
    public void setGroupHueSat(short groupId, byte hue, byte sat) {
        if (!NetworkUtil.NetWorkType(context)) {
            System.out.println("远程打开 = " + "setGroupHueSat");
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
        } else {
            new Thread(new SetGroupHueSat(ipaddress, port, groupId, hue, sat)).start();
        }
    }

    //设置房间中所有设备的色温(未实现)
    public void setGroupColorTemperature(short groupId, int value) {
        if (!NetworkUtil.NetWorkType(context)) {
            System.out.println("远程打开 = " + "setGroupColorTemperature");
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
        } else {
            new Thread(new SetGroupColorTemp(ipaddress, port, groupId, value)).start();
        }
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(AppDevice appDevice, short group_id) {
        new Thread(new AddDeviceToGroup(context,appDevice, group_id)).start();
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(AppDevice appDevice, short group_id) {
        new Thread(new DeleteDeviceFromGroup(context,appDevice, group_id)).start();
    }

    //组操作============================end====================================


    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences() {
        new Thread(new GetAllSences(context)).start();
    }

    //添加场景
    public void AddSence(String scene_Name, short group_Id){
        Constants.SCENE_GLOBAL.ADD_SCENE_NAME = scene_Name;
        Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID = group_Id;
        new Thread(new AddSence(context,scene_Name, group_Id)).start();
    }

    //Recall场景
    public void RecallScene(Short group_Id, Short scene_Id) {
        new Thread(new RecallScene(context,group_Id, scene_Id)).start();
    }

    //获取指定场景的详细信息，
    public void getSenceDetails(short senceId, String senceName) {
        if (!NetworkUtil.NetWorkType(context)) {
            System.out.println("远程打开 = " + "getSenceDetails");
            byte[] bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
        } else {
            new Thread(new GetSenceDetails(ipaddress, port, senceId, senceName)).start();
        }
    }

    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(AppDevice appDevice, short group_id, short scene_id) {
        new Thread(new AddDeviceToSence(context,appDevice, group_id, scene_id)).start();
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteDeviceFromScene(AppDevice appDevice, short scene_id) {
        new Thread(new DeleteDeviceFromScene(context,appDevice, scene_id)).start();
    }

    //删除指定场景
    public void deleteScence(short scenceId) {
        new Thread(new DeleteScence(context,scenceId)).start();
    }

    //修改指定场景
    public void ChangeSceneName(short scene_Id, String scene_Name){
        new Thread(new UpdateSceneName(context,scene_Id, scene_Name)).start();
    }
    //场景操作 =========================end============================


    //任务操作============================start==============================

    /**
     * 获取网关所有
     */
    public void GetAllTask() {
        new Thread(new GetAllTasks()).start();
    }

    /**
     * 创建定时设备任务
     */
    public void CreateTimingDeviceTask(String task_name, byte is_run, byte task_cycle, int task_hour, int task_minute,
                                       AppDevice appDevice, int cmd_size, String dev_switch,
                                       String dev_level, String dev_hue, String dev_temp) {
        new Thread(new CreateTimingDeviceTask(task_name, is_run, task_cycle, task_hour, task_minute,
                appDevice, cmd_size, dev_switch, dev_level, dev_hue, dev_temp)).start();
    }

    /**
     * 创建定时场景任务
     */
    public void CreateTimingSceneTask(String task_name, byte is_run, byte task_cycle, int task_hour, int task_minute,
                                      int cmd_size, int group_id, int scene_id) {
        new Thread(new CreateTimingSceneTask(task_name, is_run, task_cycle, task_hour,
                task_minute, cmd_size, group_id, scene_id)).start();
    }

    /**
     * 创建触发设备任务
     */
    public void CreateTriggerDeviceTask(String task_name, byte is_run, int sensor_state, String sensor_mac,
                                        AppDevice appDevice, int cmd_size, String dev_switch, String dev_level,
                                        String dev_hue, String dev_temp) {
        new Thread(new CreateTriggerDeviceTask(task_name, is_run, sensor_state, sensor_mac,
                appDevice, cmd_size, dev_switch, dev_level, dev_hue, dev_temp)).start();
    }

    /**
     * 创建定时场景任务
     */
    public void CreateTriggerSceneTask(String task_name, byte is_run,
                                       int sensor_state, String sensor_mac, int cmd_size, int group_id, int scene_id) {
        new Thread(new CreateTriggerSceneTask(task_name, is_run, sensor_state, sensor_mac, cmd_size, group_id, scene_id)).start();
    }

    /**
     * 删除任务
     */
    public void DeleteTask(int task_id) {
        new Thread(new DeleteTask(task_id)).start();
    }


    /**
     * 編輯任务
     */
    public void EditTask(String task_name, byte is_run, byte task_type, byte task_cycle, int task_hour, int task_minute,
                         int device_action, String action_mac, AppDevice appDevice, int cmd_size,
                         String dev_switch, String dev_level, String dev_hue, String dev_temp,
                         String recall_scene, int group_id, int scene_id) {
        new Thread(new EditTask(task_name, is_run, task_type, task_cycle, task_hour,
                task_minute, device_action, action_mac, appDevice, cmd_size, dev_switch,
                dev_level, dev_hue, dev_temp, recall_scene, group_id, scene_id)).start();
    }
}
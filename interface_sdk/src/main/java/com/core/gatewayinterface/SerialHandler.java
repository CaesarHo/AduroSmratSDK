package com.core.gatewayinterface;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.appdata.GatewayCmdData;
import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.appdata.SceneCmdData;
import com.core.commanddata.appdata.TaskCmdData;
import com.core.connectivity.UdpClient;
import com.core.entity.AppDevice;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.shake.ShakeManager;
import com.core.threadhelper.UDPHelper;
import com.core.threadhelper.UpdateHelper;
import com.core.threadhelper.devices.GetAllDevices;
import com.core.threadhelper.groups.GetAllGroups;
import com.core.threadhelper.scenes.AddDeviceToSence;
import com.core.threadhelper.scenes.GetAllSences;
import com.core.threadhelper.tasks.GetAllTasks;
import com.core.utils.FTPUtils;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static com.core.global.Constants.GROUP_GLOBAL.ADD_GROUP_NAME;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;
import static com.core.global.Constants.isConn;

/**
 * Created by best on 2016/7/11.
 */
public class SerialHandler {
    private static SerialHandler manager = null;
    public Context context;
    private String aeskey;
    private String topicName = null;

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
        GatewayInfo.getInstance().setAesKey(context, aeskey);
//        topicName = GatewayInfo.getInstance().getGatewayNo(context);
//        if (!topicName.equals("") & NetworkUtil.isNetworkAvailable(mContext)){
//            setMqttCommunication(context,topicName);
//        }
    }

    /**
     * 初始化MQTT连接订阅
     */
    public void setMqttCommunication(Context context, String topicName) {
        System.out.println("网关编号 = " + topicName);
        MqttManager.getInstance().init(context);
        //連接MQTT服務器
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Constants.MQTT_CLIENT_ID = Utils.getIMEI(context) + android_id;
        boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.MQTT_CLIENT_ID);
        System.out.println("isConnected: " + isConnect + ", client = " + Constants.MQTT_CLIENT_ID);
        if (isConnect) {
            isConn = true;
            GatewayInfo.getInstance().setGatewayNo(context, topicName);
            MqttManager.getInstance().subscribe(topicName, 2);
        }
    }

    /**
     * 扫描网关信息
     *
     * @param wifiManager
     */
    public void ScanGatewayInfo(WifiManager wifiManager) {
        new Thread(new UDPHelper(context, wifiManager)).start();
    }

    /**
     * 获取网关协调器的软件版本
     */
    public void GetCoordinatorVersion() {
        byte[] bt_send = GatewayCmdData.GetCoordinatorVersionCmd();
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 获取网关MAC地址和固件版本信息
     */
    public void GetGatewayVersionInfo() {
        byte[] bt_send = GatewayCmdData.GetGatewayInfoCmd();
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 设置网关时间
     *
     * @param year @param month @param day @param hour @param minute @param second
     */
    public void setGateWayTime(int year, int month, int day, int hour, int minute, int second) {
        byte[] bt_send = TaskCmdData.setGateWayTimeCmd(year, month, day, hour, minute, second);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    public void vRecoveryFactory(String pwd) {
        byte[] bt = GatewayCmdData.FactoryResetCmd(pwd);
        new Thread(new UdpClient(context, bt)).start();
    }

    public void GetSetGwServerAddress(String server_address) {
        byte[] bt = GatewayCmdData.GetSetGWServerAddress(server_address);
        new Thread(new UdpClient(context, bt)).start();
    }

    public void CheckUpdateGatewayInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (NetworkUtil.isNetworkAvailable(context)){
                        new FTPUtils(context).openConnect(true);
                    }else{
                        System.out.println("网络不可用");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void doUpdateGateway(){
        new Thread(new UpdateHelper(context)).start();
    }

    //===============================设备操作 start============================

    /**
     * 允许设备入网
     */
    public void AgreeDeviceInNet() {
        new Thread(new GetAllDevices(context, true)).start();
    }

    /**
     * search device
     */
    public void GetAllDeviceListen() {
        new Thread(new GetAllDevices(context, false)).start();
    }

    /**
     * 删除设备
     *
     * @param devicemac
     */
    public void DeleteDevice(String devicemac) {
        byte[] bt_send = DeviceCmdData.DeleteDeviceCmd(devicemac);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 选择设备提示
     */
    public void IdentifyDevice(AppDevice appDevice, int second) {
        byte[] bt_send = DeviceCmdData.IdentifyDeviceCmd(appDevice, second);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 改变设备状态(0关1开)
     */
    public void setDeviceSwitchState(AppDevice appDevice, int value) {
        byte[] bt_send = DeviceCmdData.DevSwitchCmd(appDevice, value);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //设置设备亮度回调
    public void setDeviceLevel(AppDevice appDevice, int Level) {
        byte[] bt_send = DeviceCmdData.setDeviceLevelCmd(appDevice, Level);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //设置颜色
    public void setDeviceHue(AppDevice appDevice, int hue) {
        byte[] bt_send = DeviceCmdData.setDeviceHueCmd(appDevice, hue);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //改变设备色调,饱和度
    public void setDeviceHueSat(AppDevice appDevice, int hue, int sat) {
        byte[] bt_send = DeviceCmdData.setDeviceHueSatCmd(appDevice, hue, sat);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //改变色温值
    public void setColorTemperature(AppDevice appDevice, int value) {
        byte[] bt_send = DeviceCmdData.setDeviceColorsTemp(appDevice, value);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(AppDevice appDevice) {
        byte[] bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0006");
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new GetDeviceSwitchState(context,appDevice)).start();
    }

    /**
     * 获取设备亮度回调
     *
     * @param appDevice
     */
    public void getDeviceLevel(AppDevice appDevice) {
        byte[] bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0008");
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new GetDeviceLevel(context,appDevice)).start();
    }

    //修改设备名称
    public void UpdateDeviceName(AppDevice appDevice, String device_name) {
        byte[] bt_send = DeviceCmdData.sendUpdateDeviceCmd(appDevice, device_name);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //所有设备的开关
    public void SwitchAllDevices(int state) {
        byte[] bt_send = GroupCmdData.AllDeviceSwitchCmd(state);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //调整所有设备的亮度
    public void DimmingAllDevices(int levelValue) {
        byte[] bt_send = GroupCmdData.AllDeviceLevelCmd(levelValue);
        new Thread(new UdpClient(context, bt_send)).start();
    }


    //设备 ==========================end============================


    //组操作 ===========================start ============================

    /**
     * 添加房间
     *
     * @param group_name
     */
    public void CreateGroup(String group_name, int count, String dev_mac) {
        ADD_GROUP_NAME = group_name;
        byte[] bt_send = GroupCmdData.sendAddGroupCmd(group_name, count, dev_mac);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 添加设备到文件系统
     *
     * @param group_id
     * @param count
     * @param device_mac
     */
    public void AddDeviceFromGroupFile(short group_id, int count, String device_mac) {
        byte[] bt = GroupCmdData.AddDeviceFromFileCmd(group_id, count, device_mac);
        new Thread(new UdpClient(context, bt)).start();
    }

    public void DeleteDeviceFromGroupFile(short group_id, int count, String device_mac) {
        byte[] bt = GroupCmdData.DeleteDeviceFormFileCmd(group_id, count, device_mac);
        new Thread(new UdpClient(context, bt)).start();
    }

    /**
     * 删除房间
     *
     * @param group_id
     */
    public void DeleteGroup(short group_id) {
        byte[] bt_send = GroupCmdData.sendDeleteGroupCmd(group_id);
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new DeleteGroup(context,group_id)).start();
    }

    /**
     * 修改房间
     *
     * @param group_id
     * @param group_name
     */
    public void ChangeGroupName(short group_id, String group_name) {
        NEW_GROUP_NAME = group_name;
        byte[] bt_send = GroupCmdData.sendUpdateGroupCmd((int) group_id, group_name);
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new UpdateGroupName(context,group_id,group_name)).start();
    }

    //获取网关所有房间
    public void getGroups() {
        new Thread(new GetAllGroups(context)).start();
    }

    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setGroupState(short groupId, int state) {
        byte[] bt_send = GroupCmdData.setGroupState((int) groupId, state);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId, int level) {
        byte[] bt_send = GroupCmdData.setGroupLevel((int) groupId, level);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(AppDevice appDevice, short group_id) {
        byte[] bt_send = GroupCmdData.Add_DeviceToGroup(appDevice, (int) group_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(AppDevice appDevice, short group_id) {
        byte[] bt_send = GroupCmdData.DeleteDeviceFromGroup(appDevice, (int) group_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //组操作============================end====================================


    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences() {
        new Thread(new GetAllSences(context)).start();
    }

    //添加场景
    public void AddSence(String scene_name, short group_Id, int count, String dev_mac) {
        ADD_SCENE_NAME = scene_name;
        ADD_SCENE_GROUP_ID = group_Id;
        byte[] bt_send = SceneCmdData.sendAddSceneCmd(scene_name, group_Id, count, dev_mac);
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new AddSence(context,scene_name,group_Id,count,dev_mac)).start();
    }

    //Recall场景
    public void RecallScene(Short group_Id, Short scene_Id) {
        byte[] bt_send = SceneCmdData.RecallScene((int) group_Id, (int) scene_Id);
        new Thread(new UdpClient(context, bt_send)).start();
    }


    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(AppDevice appDevice, short group_id, short scene_id) {
        new Thread(new AddDeviceToSence(context, appDevice, group_id, scene_id)).start();
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteDeviceFromScene(AppDevice appDevice, short scene_id) {
        byte[] bt_send = SceneCmdData.DeleteDeviceFromScene(appDevice, (int) scene_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    //删除指定场景
    public void deleteScence(short scene_id) {
        byte[] bt_send = SceneCmdData.sendDeleteSceneCmd((int) scene_id);
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new DeleteScence(context,scene_id)).start();
    }

    //修改指定场景
    public void ChangeSceneName(short scene_id, String scene_name) {
        NEW_SCENE_NAME = scene_name;
        byte[] bt_send = SceneCmdData.sendUpdateSceneCmd(scene_id, scene_name);
        new Thread(new UdpClient(context, bt_send)).start();
//        new Thread(new UpdateSceneName(context,scene_id,scene_name)).start();
    }

    /**
     * 添加设备到文件系统
     *
     * @param scene_id
     * @param count
     * @param device_mac
     */
    public void AddDeviceFromSceneFile(short scene_id, int count, String device_mac) {
        byte[] bt = SceneCmdData.AddDeviceFromSceneFile(scene_id, count, device_mac);
        new Thread(new UdpClient(context, bt)).start();
    }

    public void DeleteDeviceFromSceneFile(short scene_id, int count, String device_mac) {
        byte[] bt = SceneCmdData.DeleteDeviceFormSceneFile(scene_id, count, device_mac);
        new Thread(new UdpClient(context, bt)).start();
    }

    //场景操作 =========================end============================

    //任务操作============================start==============================

    /**
     * 获取网关所有
     */
    public void GetAllTask() {
        new Thread(new GetAllTasks(context)).start();
    }

    //----------------------------------------任务第二版-------------------------------
    public void CreateEditLinkTask(AppDevice appDevice, int no, short scene_id, short group_id, int enable, String task_name, int type, int status) {
        byte[] bytes = TaskCmdData.CreateEditLinkTask(appDevice, no, scene_id, group_id, enable, task_name, type, status);
        new Thread(new UdpClient(context, bytes)).start();
    }

    public void CreateEditTimeTask(int no, int task_cycle, int task_hour, int task_minute, short scene_id, short group_id, int enable, String task_name, int type) {
        byte[] bytes = TaskCmdData.CreateEditTimeTask(no, task_cycle, task_hour, task_minute, scene_id, group_id, enable, task_name, type);
        new Thread(new UdpClient(context, bytes)).start();
    }

    /**
     * 删除任务
     */
    public void DeleteTask(int task_id) {
        byte[] bt_send = TaskCmdData.DeleteTaskCmd(task_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }


    //----------------------------------------任务第一版-------------------------------

    /**
     * 创建定时设备任务
     */
    public void CreateTimingDeviceTask(String task_name, byte is_run, byte task_cycle, int task_hour, int task_minute,
                                       AppDevice appDevice, int cmd_size, String dev_switch,
                                       String dev_level, String dev_hue, String dev_temp) {
        byte[] bt_send = TaskCmdData.CreateTimingDeviceTaskCmd(task_name, is_run, task_cycle,
                task_hour, task_minute, appDevice, cmd_size, dev_switch, dev_level, dev_hue, dev_temp);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 创建定时场景任务
     */
    public void CreateTimingSceneTask(String task_name, byte is_run, byte task_cycle,
                                      int task_hour, int task_minute,
                                      int cmd_size, int group_id, int scene_id) {
        byte[] bt_send = TaskCmdData.CreateTimingSceneTaskCmd(
                task_name, is_run, task_cycle, task_hour,
                task_minute, cmd_size, group_id, scene_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 创建触发设备任务
     */
    public void CreateTriggerDeviceTask(String task_name, byte is_run, int sensor_state, String sensor_mac,
                                        AppDevice appDevice, int cmd_size, String dev_switch, String dev_level,
                                        String dev_hue, String dev_temp) {
        byte[] bt_send = TaskCmdData.CreateTriggerDeviceTaskCmd(task_name, is_run,
                sensor_state, sensor_mac, appDevice, cmd_size,
                dev_switch, dev_level, dev_hue, dev_temp);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 创建定时场景任务
     */
    public void CreateTriggerSceneTask(String task_name, byte is_run,
                                       int sensor_state, String sensor_mac,
                                       int cmd_size, int group_id, int scene_id) {
        byte[] bt_send = TaskCmdData.CreateTriggerSceneTaskCmd(task_name, is_run,
                sensor_state, sensor_mac, cmd_size, group_id, scene_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }

    /**
     * 編輯任务
     */
    public void EditTask(String task_name, byte is_run, byte task_type, byte task_cycle, int task_hour, int task_minute,
                         int device_action, String action_mac, AppDevice appDevice, int cmd_size,
                         String dev_switch, String dev_level, String dev_hue, String dev_temp,
                         String recall_scene, int group_id, int scene_id) {
        byte[] bt_send = TaskCmdData.EditTask(task_name, is_run, task_type, task_cycle, task_hour,
                task_minute, device_action, action_mac, appDevice, cmd_size, dev_switch, dev_level,
                dev_hue, dev_temp, recall_scene, group_id, scene_id);
        new Thread(new UdpClient(context, bt_send)).start();
    }
    //------------------------------------任务end-------------------------------------


    /**
     * 释放所引用的资源
     */
    public void release() {
        try {
            if (manager != null) {
                MqttManager.getInstance().release();
                manager = null;
            }
            ShakeManager.getInstance().stopShaking();
        } catch (Exception e) {
        }
    }
}

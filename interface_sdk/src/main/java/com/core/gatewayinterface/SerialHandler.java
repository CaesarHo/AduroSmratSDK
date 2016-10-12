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
import com.core.mqtt.MqttHelper;
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

        Constants.CLIENT_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("clientId: " + Constants.CLIENT_ID);

//        Constants.GatewayInfo.GatewayNo = GatewayInfo.getInstance().getGatewayNo(context);
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            //如果網絡是3G網，則連接mqtt及訂閱mqtt
            setMqttCommunication();
        }
    }

    /**
     * 扫描网关信息
     *
     * @param context
     * @param wifiManager
     */
    public void ScanGatewayInfo(Context context, WifiManager wifiManager) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            //如果網絡是3G網，則連接mqtt及訂閱mqtt
//            setMqttCommunication(Constants.clientId );
        } else {
            new Thread(new UDPHelper(context, wifiManager)).start();
        }
    }

    /**
     * 设置网关时间
     *
     * @param year @param month @param day @param hour @param minute @param second
     */
    public void setGateWayTime(int year, int month, int day, int hour, int minute, int second) {
        new Thread(new setGateWayTime(year, month, day, hour, minute, second)).start();
    }

    /**
     * MQTT连接订阅
     */
    public void setMqttCommunication() {
        new Thread(new MqttHelper()).start();
    }

    //===============================设备操作 start============================

    /**
     * 允许设备入网
     */
    public void AgreeDeviceInNet() {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new AgreeDeviceInNet()).start();
        }
    }

    /**
     * search device
     */
    public void GetAllDeviceListen() {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.GetAllDeviceListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetAllDevices()).start();
        }
    }

    /**
     * 删除设备
     *
     * @param devicemac
     */
    public void DeleteDevice(String devicemac) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.DeleteDeviceCmd(devicemac);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new DeleteDevice(devicemac)).start();
        }
    }

    /**
     * 选择设备提示
     */
    public void IdentifyDevice(AppDevice appDevice, int second) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.IdentifyDeviceCmd(appDevice, second);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new IdentifyDevice(appDevice, second)).start();
        }
    }

    /**
     * 改变设备状态(0关1开)
     */
    public void setDeviceSwitchState(AppDevice appDevice, int value) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.DevSwitchCmd(appDevice, value);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetDeviceSwitchState(appDevice, value)).start();
        }
    }

    //设置设备亮度回调
    public void setDeviceLevel(AppDevice appDevice, int Level) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.setDeviceLevelCmd(appDevice, Level);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetDeviceLevel(appDevice, Level)).start();
        }
    }

    //设置颜色
    public void setDeviceHue(AppDevice appDevice, int hue) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.setDeviceHueCmd(appDevice, hue);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetDeviceHue(appDevice, hue)).start();
        }
    }

    //改变设备色调,饱和度
    public void setDeviceHueSat(AppDevice appDevice, int hue, int sat) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.setDeviceHueSatCmd(appDevice, hue, sat);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetDeviceHueSat(appDevice, hue, sat)).start();
        }
    }

    //改变色温值
    public void setColorTemperature(AppDevice appDevice, int value) {
        //初始化mqtt
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.setDeviceColorsTemp(appDevice, value);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetDeviceColorTemp(appDevice, value)).start();
        }
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(AppDevice appDevice) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0006");
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetDeviceSwitchState(appDevice)).start();
        }
    }

    public void getDeviceOnLinStatus(String devicename, String deviceid) {
        GetDeviceOnLinStatus getDeviceOnLinStatus = new GetDeviceOnLinStatus();
        Thread thread = new Thread(getDeviceOnLinStatus);
        thread.start();
    }

    //获取设备亮度回调
    public void getDeviceLevel(AppDevice appDevice) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.ReadAttrbuteCmd(appDevice, "0100", "0008");
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetDeviceLevel(appDevice)).start();
        }
    }

    //获取设备色调
    public void getDeviceHue(String deviceid) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.GetAllDeviceListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetDeviceHue(ipaddress, port, deviceid)).start();
        }
    }

    //获取设备饱和度
    public void getDeviceSat(String deviceid) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.GetAllDeviceListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetDeviceSat(ipaddress, port, deviceid)).start();
        }
    }

    //修改设备名称
    public void UpdateDeviceName(AppDevice appDevice, String device_name){
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.sendUpdateDeviceCmd(appDevice, device_name);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new UpdateDeviceName(appDevice, device_name)).start();
        }
    }
    //设备 ==========================end============================


    //组操作 ===========================start ============================

    /**
     * 添加房间
     *
     * @param group_name
     */
    public void CreateGroup(String group_name){
        Constants.GROUP_GLOBAL.ADD_GROUP_NAME = group_name;
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.sendAddGroupCmd(group_name);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new AddGroup(group_name)).start();
        }
    }

    /**
     * 删除房间
     *
     * @param groupid
     */
    public void DeleteGroup(short groupid) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.sendDeleteGroupCmd((int) groupid);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new DeleteGroup(groupid)).start();
        }
    }

    /**
     * 修改房间
     *
     * @param group_id
     * @param group_name
     */
    public void ChangeGroupName(short group_id, String group_name){
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.sendUpdateGroupCmd((int) group_id, group_name);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new UpdateGroupName(group_id, group_name)).start();
        }
    }

    //获取网关所有房间
    public void getGroups() {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetAllGroups()).start();
        }
    }

    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setGroupState(short groupId, int state) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.setGroupState((int) groupId, (int) state);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupState((int) groupId, state)).start();
        }
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId, int level) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.setGroupLevel((int) groupId, (int) level);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupLevel((int) groupId, level)).start();
        }
    }

    //设置房间中所有lamp的色调(未实现)
    public void setGroupHue(short groupId, byte hue) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupHue(ipaddress, port, groupId, hue)).start();
        }
    }

    //设置房间中所有lamp的饱和度(未实现)
    public void setGroupSat(short groupId, byte sat) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupSat(ipaddress, port, groupId, sat)).start();
        }
    }

    //设置房间中所有设备的饱和度及色调(未实现)
    public void setGroupHueSat(short groupId, byte hue, byte sat) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupHueSat(ipaddress, port, groupId, hue, sat)).start();
        }
    }

    //设置房间中所有设备的色温(未实现)
    public void setGroupColorTemperature(short groupId, int value) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new SetGroupColorTemp(ipaddress, port, groupId, value)).start();
        }
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(AppDevice appDevice, short group_id) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.Add_DeviceToGroup(appDevice, group_id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new AddDeviceToGroup(appDevice, group_id)).start();
        }
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(AppDevice appDevice, short group_id) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = GroupCmdData.DeleteDeviceFromGroup(appDevice, group_id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new DeleteDeviceFromGroup(appDevice, group_id)).start();
        }
    }

    //组操作============================end====================================


    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences() {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.GetAllScenesListCmd();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetAllSences()).start();
        }
    }

    //添加场景
    public void AddSence(String Out_Scene_Name, short Out_Group_Id){
        Constants.SCENE_GLOBAL.ADD_SCENE_NAME = Out_Scene_Name;
        Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID = Out_Group_Id;
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.sendAddSceneCmd(Out_Scene_Name, Out_Group_Id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new AddSence(Out_Scene_Name, Out_Group_Id)).start();
        }
    }

    //Recall场景
    public void RecallScene(Short Group_Id, Short Scene_Id) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.RecallScene((int) Group_Id, (int) Scene_Id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new RecallScene(Group_Id, Scene_Id)).start();
        }
    }

    //获取指定场景的详细信息，
    public void getSenceDetails(short senceId, String senceName) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new GetSenceDetails(ipaddress, port, senceId, senceName)).start();
        }
    }

    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(AppDevice appDevice, short group_id, short scene_id) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.Add_DeviceToScene(appDevice, (int) group_id, (int) scene_id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new AddDeviceToSence(appDevice, group_id, scene_id)).start();
        }
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteDeviceFromScene(AppDevice appDevice, short scene_id) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.DeleteDeviceFromScene(appDevice, (int) scene_id);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new DeleteDeviceFromScene(appDevice, scene_id)).start();
        }
    }

    //删除指定场景
    public void deleteScence(short scenceId) {
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.sendDeleteSceneCmd((int) scenceId);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new DeleteScence(scenceId)).start();
        }
    }

    //修改指定场景
    public void ChangeSceneName(short sceneId, String newSceneName){
        if (!NetworkUtil.NetWorkType(context)) {
            byte[] bt_send = SceneCmdData.sendUpdateSceneCmd((int) sceneId, newSceneName);
            MqttManager.getInstance().publish("170005203637", 2, bt_send);
        } else {
            new Thread(new UpdateSceneName(sceneId, newSceneName)).start();
        }
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

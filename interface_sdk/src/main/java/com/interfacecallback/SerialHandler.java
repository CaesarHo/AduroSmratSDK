package com.interfacecallback;

import android.content.Context;

import com.threadhelper.AddDeviceToGroup;
import com.threadhelper.AddDeviceToSence;
import com.threadhelper.AddGroup;
import com.threadhelper.AddSences;
import com.threadhelper.AgreeDeviceInNet;
import com.threadhelper.DeleteDeviceFromGroup;
import com.threadhelper.DeleteGroup;
import com.threadhelper.DeleteSence;
import com.threadhelper.DeleteSenceMember;
import com.threadhelper.GetAllDevices;
import com.threadhelper.GetAllGroups;
import com.threadhelper.GetAllSences;
import com.threadhelper.GetDeviceHue;
import com.threadhelper.GetDeviceLevel;
import com.threadhelper.GetDeviceOnLinStatus;
import com.threadhelper.GetDeviceSat;
import com.threadhelper.GetDeviceSwitchState;
import com.threadhelper.GetSenceDetails;
import com.threadhelper.SendDeleteDeviceCmd;
import com.threadhelper.SetColorTemperature;
import com.threadhelper.SetDeviceHue;
import com.threadhelper.SetDeviceHueSat;
import com.threadhelper.SetDeviceLevel;
import com.threadhelper.SetDeviceSwitchState;
import com.threadhelper.SetGroupColorTemperature;
import com.threadhelper.SetGroupHue;
import com.threadhelper.SetGroupHueSat;
import com.threadhelper.SetGroupLevel;
import com.threadhelper.SetGroupSat;
import com.threadhelper.SetGroupState;
import com.threadhelper.UpdateGroup;
import com.threadhelper.UpdateSceneName;

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
     * @param groupname
     */
    public void DeleteGroup(String groupname, String groupid) {
        DeleteGroup mDeleteGroup = new DeleteGroup(ipaddress, port, groupname, groupid);
        Thread thread = new Thread(mDeleteGroup);
        thread.start();
    }

    /**
     * 修改房间
     *
     * @param groupid
     * @param groupname
     */
    public void ModifyGroup(String groupname, String groupid) {
        UpdateGroup mUpdateGroup = new UpdateGroup(ipaddress, port, groupname, groupid);
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
    public void setGroupState(Short groupId, byte state) {
        SetGroupState mSetGroupState = new SetGroupState(ipaddress, port, groupId, state);
        Thread thread = new Thread(mSetGroupState);
        thread.start();
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId, byte level) {
        SetGroupLevel mSetGroupLevel = new SetGroupLevel(ipaddress, port, groupId, level);
        Thread thread = new Thread(mSetGroupLevel);
        thread.start();
    }

    //设置房间中所有lamp的色调
    public void setGroupHue(short groupId, byte hue) {
        SetGroupHue mSetGroupHue = new SetGroupHue(ipaddress, port, groupId, hue);
        Thread thread = new Thread(mSetGroupHue);
        thread.start();
    }

    //设置房间中所有lamp的饱和度
    public void setGroupSat(short groupId, byte sat) {
        SetGroupSat setGroupSat = new SetGroupSat(ipaddress, port, groupId, sat);
        Thread thread = new Thread(setGroupSat);
        thread.start();
    }

    //设置房间中所有设备的饱和度及色调
    public void setGroupHueSat(short groupId, byte hue, byte sat) {
        SetGroupHueSat mSetGroupHueSat = new SetGroupHueSat(ipaddress, port, groupId, hue, sat);
        Thread thread = new Thread(mSetGroupHueSat);
        thread.start();
    }

    //设置房间中所有设备的色温
    public void setGroupColorTemperature(short groupId, int value) {
        SetGroupColorTemperature mSetGroupColorTemperature = new SetGroupColorTemperature(ipaddress, port, groupId, value);
        Thread thread = new Thread(mSetGroupColorTemperature);
        thread.start();
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(short group_id,String device_mac,String device_shortaddr,String main_endpoint) {
        AddDeviceToGroup mAddDeviceToGroup = new AddDeviceToGroup(group_id,device_mac,device_shortaddr,main_endpoint);
        Thread thread = new Thread(mAddDeviceToGroup);
        thread.start();
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(short group_id,String device_mac,String shortaddr,String main_endpoint) {
        DeleteDeviceFromGroup mDeleteDeviceFromGroup = new DeleteDeviceFromGroup(group_id,device_mac,shortaddr,main_endpoint);
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
     * 改变设备状态(0关1开)
     *
     * @param devicemac
     * @param deviceshortaddr
     * @param main_endpoint
     */
    public void setDeviceSwitchState(String devicemac, String deviceshortaddr, String main_endpoint) {
        new Thread(new SetDeviceSwitchState(devicemac, deviceshortaddr, main_endpoint)).start();
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
    public void setColorTemperature(String deviceid, byte value) {
        SetColorTemperature mSetColorTemperature = new SetColorTemperature(ipaddress, port, deviceid, value);
        Thread thread = new Thread(mSetColorTemperature);
        thread.start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(String devicename, String deviceid, int state) {
        GetDeviceSwitchState mDeviceSwitchState = new GetDeviceSwitchState(ipaddress, port, devicename, deviceid, state);
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

    //设备 ==========================end============================

    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences() {
        DataSources.getInstance().getAllSences(125468, "在家", "");//返回假数据

        GetAllSences mDeviceSences = new GetAllSences(ipaddress, port);
        Thread thread = new Thread(mDeviceSences);
        thread.start();
    }

    //添加场景
    public void AddSences(int sencesid, String sencesName, String sceneIconPath) {

        DataSources.getInstance().AddSences(sencesid, sencesName, sceneIconPath);//返回添加场景数据

        AddSences addSences = new AddSences(ipaddress, port, sencesid, sencesName, sceneIconPath);
        Thread thread = new Thread(addSences);
        thread.start();
    }

    //获取指定场景的详细信息，
    public void getSenceDetails(short senceId, String senceName) {
        GetSenceDetails mDeviceSenceDetails = new GetSenceDetails(ipaddress, port, senceId, senceName);
        Thread thread = new Thread(mDeviceSenceDetails);
        thread.start();
    }

    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(Short sencesId, String senceName, String deviceid, byte delaytime) {
        AddDeviceToSence mAddDeviceToSence = new AddDeviceToSence(ipaddress, port, sencesId, senceName, deviceid, delaytime);
        Thread thread = new Thread(mAddDeviceToSence);
        thread.start();
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteSenceMember(String senceName, String deviceid) {
        DeleteSenceMember mDeleteSenceMember = new DeleteSenceMember(ipaddress, port, senceName, deviceid);
        Thread thread = new Thread(mDeleteSenceMember);
        thread.start();
    }

    //删除指定场景
    public void deleteSence(short senceId, String senceName) {
        DataSources.getInstance().DeleteSences(1);//返回添加场景成功(假数据)
        DeleteSence mDeleteSence = new DeleteSence(ipaddress, port, senceId, senceName);
        Thread thread = new Thread(mDeleteSence);
        thread.start();
    }

    //修改指定场景
    public void ChangeSceneName(short sceneId, String newSceneName) {
        UpdateSceneName mChangeSceneName = new UpdateSceneName(ipaddress, port, sceneId, newSceneName);
        Thread thread = new Thread(mChangeSceneName);
        thread.start();
    }
    //场景操作 =========================end============================

}

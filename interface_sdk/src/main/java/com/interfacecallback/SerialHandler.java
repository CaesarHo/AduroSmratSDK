package com.interfacecallback;

import android.content.Context;

import com.threadhelper.AddDevice;
import com.threadhelper.AddDeviceToGroup;
import com.threadhelper.AddDeviceToSence;
import com.threadhelper.AddGroup;
import com.threadhelper.AddSences;
import com.threadhelper.AgreeDeviceInNet;
import com.threadhelper.DeleteDevice;
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
import com.threadhelper.SetColorTemperature;
import com.threadhelper.SetDeviceHueSat;
import com.threadhelper.SetDeviceLevel;
import com.threadhelper.SetDeviceSwitchState;
import com.threadhelper.SetGroupColorTemperature;
import com.threadhelper.SetGroupHue;
import com.threadhelper.SetGroupHueSat;
import com.threadhelper.SetGroupLevel;
import com.threadhelper.SetGroupSat;
import com.threadhelper.SetGroupState;
import com.threadhelper.UpdateDevice;
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

    private SerialHandler(){

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
    public void Init(Context context,String aeskey,InterfaceCallback sdkCallback) {
        DataSources.getInstance().setSettingInterface(sdkCallback);
        this.context = context;
        this.aeskey = aeskey;
        this.ipaddress = GatewayInfo.getInstance().getInetAddress(context);
        this.port = GatewayInfo.getInstance().getPort(context);
        GatewayInfo.getInstance().setAesKey(context,aeskey);
    }

    //组操作 ===========================start ============================
    /**
     * 添加房间
     * @param roomid
     * @param roomname
     */
    public void CreateGroup(String roomname,String roomid){
        AddGroup mAddGroup = new AddGroup(ipaddress,port,roomname,roomid);
        Thread thread = new Thread(mAddGroup);
        thread.start();
    }

    /**
     * 删除房间
     * @param groupid
     * @param groupname
     */
    public void DeleteGroup(String groupname,String groupid){
        DeleteGroup mDeleteGroup = new DeleteGroup(ipaddress,port,groupname,groupid);
        Thread thread = new Thread(mDeleteGroup);
        thread.start();
    }

    /**
     * 修改房间
     * @param groupid
     * @param groupname
     */
    public void ModifyGroup(String groupname,String groupid){
        UpdateGroup mUpdateGroup = new UpdateGroup(ipaddress,port,groupname,groupid);
        Thread thread = new Thread(mUpdateGroup);
        thread.start();
    }

    //获取网关所有房间
    public void getGroups(){
        GetAllGroups mGetGroups = new GetAllGroups(ipaddress,port);
        Thread thread = new Thread(mGetGroups);
        thread.start();
    }

    //设置房间中所有设备的状态roomId(房间ID),state(房间状态)
    public void setGroupState(Short groupId,byte state){
        SetGroupState mSetGroupState = new SetGroupState(ipaddress,port,groupId,state);
        Thread thread = new Thread(mSetGroupState);
        thread.start();
    }

    //设置房间中所有lamp的亮度
    public void setGroupLevel(short groupId,byte level){
        SetGroupLevel mSetGroupLevel = new SetGroupLevel(ipaddress,port,groupId,level);
        Thread thread = new Thread(mSetGroupLevel);
        thread.start();
    }
    //设置房间中所有lamp的色调
    public void setGroupHue(short groupId,byte hue){
        SetGroupHue mSetGroupHue = new SetGroupHue(ipaddress,port,groupId,hue);
        Thread thread = new Thread(mSetGroupHue);
        thread.start();
    }
    //设置房间中所有lamp的饱和度
    public void setGroupSat(short groupId,byte sat){
        SetGroupSat setGroupSat = new SetGroupSat(ipaddress,port,groupId,sat);
        Thread thread = new Thread(setGroupSat);
        thread.start();
    }
    //设置房间中所有设备的饱和度及色调
    public void setGroupHueSat(short groupId,byte hue,byte sat){
        SetGroupHueSat mSetGroupHueSat = new SetGroupHueSat(ipaddress,port,groupId,hue,sat);
        Thread thread = new Thread(mSetGroupHueSat);
        thread.start();
    }

    //设置房间中所有设备的色温
    public void setGroupColorTemperature(short groupId, int value){
        SetGroupColorTemperature mSetGroupColorTemperature = new SetGroupColorTemperature(ipaddress,port,groupId,value);
        Thread thread = new Thread(mSetGroupColorTemperature);
        thread.start();
    }

    //将指定的设备加入到指定的组中
    public void addDeviceToGroup(String groupName,String deviceid){
        AddDeviceToGroup mAddDeviceToGroup = new AddDeviceToGroup(ipaddress,port,groupName,deviceid);
        Thread thread = new Thread(mAddDeviceToGroup);
        thread.start();
    }

    //将指定的设备从组中删除
    public void deleteDeviceFromGroup(String groupName,String deviceid){
        DeleteDeviceFromGroup mDeleteDeviceFromGroup = new DeleteDeviceFromGroup(ipaddress,port,groupName,deviceid);
        Thread thread = new Thread(mDeleteDeviceFromGroup);
        thread.start();
    }
    //组操作============================end====================================

    //设备操作 ===============================start============================

    /**
     * 允许设备入网
     */
    public void AgreeDeviceInNet(){
        AgreeDeviceInNet agreeDeviceInNet = new AgreeDeviceInNet(ipaddress);
        Thread thread = new Thread(agreeDeviceInNet);
        thread.start();
    }

    /**
     * search device
     */
    public void GetAllDeviceListen(){
        //测试假数据
        DataSources.getInstance().ScanDeviceResult("MyDevice","0X0104","0X00124b0001de5c9c","0X03ad" ,"0Xffff","main_endpoint");
        new Thread(new GetAllDevices()).start();
    }

    /**
     * 添加设备
     * @param deviceName
     * @param deviceId
     * @param deviceTypeId
     * @param deviceType
     * @param zoneType
     */
    public void AddDevice(String deviceName,String deviceId,int deviceTypeId,String deviceType,short zoneType){
        DataSources.getInstance().AddDeviceResult(deviceName,(byte)0x01,(byte)0x00,(byte)220,(byte)220,(byte)220,(byte)220,deviceId,
                0x0101,"Unknown Device",0,(short)0,(short)0,(short)0x000d);
        AddDevice mAddDevice = new AddDevice(ipaddress,port,deviceName,deviceId,deviceTypeId,deviceType,zoneType);
        Thread thread = new Thread(mAddDevice);
        thread.start();
    }

    /**
     * 删除设备
     * @param devicename
     * @param deviceid
     */
    public void DeleteDevice(String devicename,String deviceid){
        DataSources.getInstance().DeleteDeviceResult(1);//定义假数据回调1表示Delete成功
        DeleteDevice mDeleteDevice = new DeleteDevice(ipaddress,port,devicename,deviceid);
        Thread thread = new Thread(mDeleteDevice);
        thread.start();
    }

    /**
     * 修改设备
     * @param deviceid
     * @param devicename
     * @param deviceid
     */
    public void ModifyDevice(String devicename,String deviceid){
        UpdateDevice mModifyDevice = new UpdateDevice(ipaddress,port,devicename,deviceid);
        Thread thread = new Thread(mModifyDevice);
        thread.start();
    }

    /**
     * 改变设备状态(0关1开)
     * @param deviceid
     * @param devicename
     * @param state
     */
    public void setDeviceSwitchState(String devicename,String deviceid,int state){
        DataSources.getInstance().setDeviceStateResule(1);
        SetDeviceSwitchState mSwitchDevice = new SetDeviceSwitchState(ipaddress,port,devicename,deviceid,state);
        Thread thread = new Thread(mSwitchDevice);
        thread.start();
    }

    /**
     * 获得设备状态
     */
    public void getDeviceSwitchState(String devicename,String deviceid,int state){
        GetDeviceSwitchState mDeviceSwitchState = new GetDeviceSwitchState(ipaddress,port,devicename,deviceid,state);
        Thread thread = new Thread(mDeviceSwitchState);
        thread.start();
    }

    public void getDeviceOnLinStatus(String devicename,String deviceid){
        //测试数据
        DataSources.getInstance().getDeviceStatus(devicename,deviceid,(byte)1);
        GetDeviceOnLinStatus getDeviceOnLinStatus = new GetDeviceOnLinStatus();
        Thread thread = new Thread(getDeviceOnLinStatus);
        thread.start();
    }

    //设置设备亮度回调
    public void setDeviceLevel(String deviceId,byte value){
        SetDeviceLevel mDeviceLevel = new SetDeviceLevel(ipaddress,port,deviceId,value);
        Thread thread = new Thread(mDeviceLevel);
        thread.start();
    }

    //获取设备亮度回调
    public void getDeviceLevel(String deviceid){
        GetDeviceLevel mDeviceLevel = new GetDeviceLevel(ipaddress,port,deviceid);
        Thread thread = new Thread(mDeviceLevel);
        thread.start();
    }
    //改变设备色调,饱和度
    public void setDeviceHueSat(String deviceid,byte hue,byte sat){
        SetDeviceHueSat mDeviceHueSat = new SetDeviceHueSat(ipaddress,port,deviceid,hue,sat);
        Thread thread = new Thread(mDeviceHueSat);
        thread.start();
    }
    //获取设备色调
    public void getDeviceHue(String deviceid){
        GetDeviceHue mDeviceHue = new GetDeviceHue(ipaddress,port,deviceid);
        Thread thread = new Thread(mDeviceHue);
        thread.start();
    }

    //获取设备饱和度
    public void getDeviceSat(String deviceid){
        GetDeviceSat mDeviceSat = new GetDeviceSat(ipaddress,port,deviceid);
        Thread thread = new Thread(mDeviceSat);
        thread.start();
    }

    //改变色温值
    public void setColorTemperature(String deviceid,byte value){
        SetColorTemperature mSetColorTemperature = new SetColorTemperature(ipaddress,port,deviceid,value);
        Thread thread = new Thread(mSetColorTemperature);
        thread.start();
    }
    //设备 ==========================end============================

    //=======================场景操作 start========================
    //获取网关所有场景
    public void getSences(){
        DataSources.getInstance().getAllSences(125468,"在家","");//返回假数据

        GetAllSences mDeviceSences = new GetAllSences(ipaddress,port);
        Thread thread = new Thread(mDeviceSences);
        thread.start();
    }

    //添加场景
    public void AddSences(int sencesid,String sencesName,String sceneIconPath){

        DataSources.getInstance().AddSences(sencesid,sencesName,sceneIconPath);//返回添加场景数据

        AddSences addSences = new AddSences(ipaddress,port,sencesid,sencesName,sceneIconPath);
        Thread thread = new Thread(addSences);
        thread.start();
    }

    //获取指定场景的详细信息，
    public void getSenceDetails(short senceId, String senceName){
        GetSenceDetails mDeviceSenceDetails = new GetSenceDetails(ipaddress,port,senceId,senceName);
        Thread thread = new Thread(mDeviceSenceDetails);
        thread.start();
    }

    //将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景,uid(设备uID)
    public void addDeviceToSence(Short sencesId,String senceName, String deviceid,byte delaytime){
        AddDeviceToSence mAddDeviceToSence = new AddDeviceToSence(ipaddress,port,sencesId,senceName,deviceid,delaytime);
        Thread thread = new Thread(mAddDeviceToSence);
        thread.start();
    }

    //删除场景中指定设备成员 senceName场景名 设备Id
    public void deleteSenceMember(String senceName,String deviceid){
        DeleteSenceMember mDeleteSenceMember = new DeleteSenceMember(ipaddress,port,senceName,deviceid);
        Thread thread = new Thread(mDeleteSenceMember);
        thread.start();
    }

    //删除指定场景
    public void deleteSence(short senceId, String senceName){
        DataSources.getInstance().DeleteSences(1);//返回添加场景成功(假数据)
        DeleteSence mDeleteSence = new DeleteSence(ipaddress,port,senceId,senceName);
        Thread thread = new Thread(mDeleteSence);
        thread.start();
    }

    //修改指定场景
    public void ChangeSceneName(short sceneId, String newSceneName){
        UpdateSceneName mChangeSceneName = new UpdateSceneName(ipaddress,port,sceneId,newSceneName);
        Thread thread = new Thread(mChangeSceneName);
        thread.start();
    }
    //场景操作 =========================end============================

}

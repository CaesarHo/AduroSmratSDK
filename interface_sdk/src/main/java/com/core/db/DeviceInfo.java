package com.core.db;

/**
 * Created by best on 2016/9/25.
 */

public class DeviceInfo {

    private String deviceName;
    private byte deviceStatus;// 设备是否在线；
    private byte deviceState; // 设备状态（灯:开、关)
    private int uId;
    private String deviceId;
    private String ProfileId;
    public String type = "Unknown Device";

    private int Sensordata = 0;// 用来存储传感器设备上传的数据
    private String clusterId;
    private short attribID;
    // 判断设备类型
    public byte hasColourable = 0;
    public byte hasDimmable = 0;
    public byte hasSwitchable = 0;
    public byte hasThermometer = 0;
    public byte hasPowerUsage = 0;
    public byte hasOutSwitch = 0;
    public byte hasOutLeveL = 0;
    public byte hasOutColor = 0;
    public byte hasOutScene = 0;
    public byte hasOutGroup = 0;
    public byte hasSensor = 0; // 是传感器
    public byte issmartplug = 0; // 是智能开关
    public short zoneType = 0;
    private byte[] IEEE = new byte[8];
    public DeviceInfo() {

    }
    public DeviceInfo(String deviceName, int uId, String deviceId,
                      String profileId, byte hasColourable, byte hasDimmable,
                      byte hasSwitchable, byte hasThermometer, byte hasPowerUsage,
                      byte hasOutSwitch, byte hasOutLeveL, byte hasOutColor,
                      byte hasOutScene, byte hasOutGroup, byte hasSensor, byte issmartplug, short zoneType) {
        super();
        this.deviceName = deviceName;
        this.uId = uId;
        this.deviceId = deviceId;
        this.ProfileId = profileId;
        this.hasColourable = hasColourable;
        this.hasDimmable = hasDimmable;
        this.hasSwitchable = hasSwitchable;
        this.hasThermometer = hasThermometer;
        this.hasPowerUsage = hasPowerUsage;
        this.hasOutSwitch = hasOutSwitch;
        this.hasOutLeveL = hasOutLeveL;
        this.hasOutColor = hasOutColor;
        this.hasOutScene = hasOutScene;
        this.hasOutGroup = hasOutGroup;
        this.hasSensor = hasSensor;
        this.issmartplug = issmartplug;
        this.zoneType = zoneType;
    }

    public DeviceInfo(int uId, int data, String clusterId, short attribID,String deviceName) {
        this.uId = uId;
        this.Sensordata = data;
        this.clusterId = clusterId;
        this.attribID = attribID;
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        if (deviceName == null) {
            return "";
        }
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uid) {
        this.uId = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getProfileId() {
        return ProfileId;
    }

    public void setProfileId(String profileId) {
        ProfileId = profileId;
    }

    public byte getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(byte deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public byte getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(byte deviceState) {
        this.deviceState = deviceState;
    }

    public int getSensordata() {
        return Sensordata;
    }

    public void setSensordata(int sensordata) {
        Sensordata = sensordata;
    }
    public String getClusterId() {
        return clusterId;
    }
    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
    public short getAttribID() {
        return attribID;
    }
    public void setAttribID(short attribID) {
        this.attribID = attribID;
    }
}

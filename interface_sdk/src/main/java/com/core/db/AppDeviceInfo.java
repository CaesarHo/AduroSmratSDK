package com.core.db;

import java.io.Serializable;

/**
 * Created by best on 2016/9/25.
 */

public class AppDeviceInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String deviceName;
    private String profileid;
    private String deivcemac;
    private String shortaddr;
    private String deviceid;
    private String endpoint;
    private String zonetype;
    private String clusterId;
    private String attribID;
    private Boolean selected = false;

    private byte deviceStatus;// 设备是否在线；
    private int deviceState; // 设备状态（灯:开、关)0 开 1 关

    public String type = "Unknown Device";

    private int Sensordata = -1;// 用来存储传感器设备上传的数据

    int bright = -1;  //设备亮度:
    int color = -1;   //设备颜色
    int temp = -1;   //色温
    int colorSat = -1;
    int colorHue = -1;


//    public AppDeviceInfo(String deviceName) {
//        this.deviceName = deviceName;
//    }

    public int getColorSat() {
        return colorSat;
    }

    public void setColorSat(int colorSat) {
        this.colorSat = colorSat;
    }

    public int getColorHue() {
        return colorHue;
    }

    public void setColorHue(int colorHue) {
        this.colorHue = colorHue;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getBright() {
        return bright;
    }

    public void setBright(int bright) {
        this.bright = bright;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getZonetype() {
        return zonetype;
    }

    public void setZonetype(String zonetype) {
        this.zonetype = zonetype;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String name) {
        this.deviceName = name;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getDeviceMac() {
        return deivcemac;
    }

    public void setDeviceMac(String deivcemac) {
        this.deivcemac = deivcemac;
    }

    public String getShortaddr() {
        return shortaddr;
    }

    public void setShortaddr(String shortaddr) {
        this.shortaddr = shortaddr;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}

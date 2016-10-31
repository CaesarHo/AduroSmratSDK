package com.core.entity;

import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by best on 2016/9/25.
 */

public class AppDevice implements Serializable {
    public short frequency = 0;
    public double voltage = 0;
    public double current = 0;
    public double power = 0;
    public double power_factor = 0;

    private static final long serialVersionUID = 1L;
    private String deviceName;
    private String profileid;
    private String deivcemac;
    private String shortaddr;
    private String deviceid;
    private String endpoint;
    private String zonetype;
    private Boolean selected = false;

    private byte deviceStatus;// 设备是否在线；
    private int deviceState; // 设备状态（灯:开、关)0 开 1 关

    public String type = "Unknown Device";

    private int Sensordata = -1;// 用来存储传感器设备上传的数据
    private short clusterId;
    private short attribID;

    int bright = -1;  //设备亮度:
    int color = -1;   //设备颜色
    int temp = -1;   //色温
    int colorSat = -1;
    int colorHue = -1;

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


    public int getSensordata() {
        return Sensordata;
    }

    public void setSensordata(int sensordata) {
        Sensordata = sensordata;
    }

    public short getClusterId() {
        return clusterId;
    }

    public void setClusterId(short clusterId) {
        this.clusterId = clusterId;
    }

    public short getAttribID() {
        return attribID;
    }

    public void setAttribID(short attribID) {
        this.attribID = attribID;
    }


    public short getFrequency() {
        return frequency;
    }

    public double getVoltage() {
        return voltage;
    }


    public double getCurrent() {
        return current;
    }

    public double getPower() {
        return power;
    }

    public double getPower_factor() {
        return power_factor;
    }

    public void setFrequency(short frequency) {
        this.frequency = frequency;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setPower_factor(double power_factor) {
        this.power_factor = power_factor;
    }
}

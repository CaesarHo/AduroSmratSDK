package com.adurosmart.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable{
	private String deviceName;
	private byte deviceStatus;// ???????
	private byte deviceState; // ??????:???)
	private int uId;
	private short deviceId;
	private short ProfileId;
	public String type = "Unknown Device";

	private int Sensordata = 0;// ??????????????
	private short clusterId;
	private short attribID;
	// ??????
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
	public byte hasSensor = 0; // ????
	public byte issmartplug = 0; // ?????
	public short zoneType = 0;
	private byte[] IEEE = new byte[8];
	public DeviceInfo() {

	}
	public DeviceInfo(String deviceName, int uId, short deviceId,
					  short profileId, byte hasColourable, byte hasDimmable,
					  byte hasSwitchable, byte hasThermometer, byte hasPowerUsage,
					  byte hasOutSwitch, byte hasOutLeveL, byte hasOutColor,
					  byte hasOutScene, byte hasOutGroup, byte hasSensor, byte issmartplug, short zoneType) {
		super();
		this.deviceName = deviceName;
		this.uId = uId;
		this.deviceId = deviceId;
		ProfileId = profileId;
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

	public DeviceInfo(int uId, int data, short clusterId, short attribID,String deviceName) {
		// TODO Auto-generated constructor stub
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

	public short getDeviceId() {
		return deviceId;
	}

	public short getProfileId() {
		return ProfileId;
	}

	public void setProfileId(short profileId) {
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
}

package com.adurosmart.global;

import android.content.Intent;

import com.interfacecallback.InterfaceCallback;

import org.eclipse.paho.android.service.sample.MyApp;

/**
 * Created by best on 2016/7/11.
 */
public class ListenCallback implements InterfaceCallback{
    @Override
    public void AddGroupCallback(int i) {

    }

    @Override
    public void DeleteGroupCallback(int i) {

    }

    @Override
    public void ModifyGroupCallback(int i) {

    }

    @Override
    public void getAllGroupsCallback(Short aShort, String s, String s1) {

    }

    @Override
    public void setGroupsStateCallback(short i, byte b) {

    }

    @Override
    public void setGroupsLevelCallback(short i, byte b) {

    }

    @Override
    public void setGroupHueCallback(short i, byte b) {

    }

    @Override
    public void setGroupSatCallback(short i, byte b) {

    }

    @Override
    public void setGroupHueSatCallback(short i, byte b, byte b1) {

    }

    @Override
    public void setGroupColorTemperatureCallback(short i, int i1) {

    }

    @Override
    public void addDeviceToGroupCallback(int i) {

    }

    @Override
    public void deleteDeviceFromGroupCallback(int i) {

    }

    @Override
    public void setDeviceStateCallback(int i) {

    }


    @Override
    public void getSenceDetailsCallback(short i, String s, int i1, Short aShort) {

    }

    @Override
    public void addDeviceToSenceCallback(String s, int i, short i1, int i2) {

    }

    @Override
    public void deleteSenceMemberCallback(int i) {

    }

    @Override
    public void deleteSence(int i) {

    }

    @Override
    public void ChangeSceneName(short i, String s, int i1) {

    }

    @Override
    public void DeleteDeviceCallback(int i) {

    }

    @Override
    public void ModifyDeviceCallback(int i) {

    }

    @Override
    public void GatewatInfoCallback(String gatewayName,String gatewayNo,String gatewaySoftwareVersion, String gatewayHardwareVersion,String gatewayIPv4Address,String gatewayDatetime) {
        Intent intent = new Intent();
        intent.setAction("GatewatInfoCallback");
        intent.putExtra("gatewayName",gatewayName);
        intent.putExtra("gatewayNo",gatewayNo);
        intent.putExtra("gatewaySoftwareVersion",gatewaySoftwareVersion);
        intent.putExtra("gatewayHardwareVersion",gatewayHardwareVersion);
        intent.putExtra("gatewayIPv4Address",gatewayIPv4Address);
        intent.putExtra("gatewayDatetime",gatewayDatetime);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void AgreeDeviceInNetCallback(int i) {

    }

    @Override
    public void ScanDeviceCallback(String s, byte b, byte b1, byte b2, byte b3, byte b4, byte b5, String s1, int i, String s2, int i1, Short aShort, Short aShort1, Short aShort2) {

    }

    @Override
    public void AddDeviceCallback(String s, byte b, byte b1, byte b2, byte b3, byte b4, byte b5, String s1, int i, String s2, int i1, Short aShort, Short aShort1, Short aShort2) {

    }

    @Override
    public void getDeviceStateCallback(String s, byte b) {

    }

    @Override
    public void getDeviceOnLinStatus(String s, String s1, byte b) {

    }

    @Override
    public void setDeviceLevelCallback(String s, byte b) {

    }

    @Override
    public void getDeviceLevelCallback(String s, byte b) {

    }

    @Override
    public void setDeviceHueSatCallback(String s, int i) {

    }

    @Override
    public void getDeviceHueCallback(String s, byte b) {

    }

    @Override
    public void getDeviceSatCallback(String s, byte b) {

    }

    @Override
    public void setColorTemperatureCallback(String s, byte b) {

    }

    @Override
    public void getSencesCallback(int i, String s, String s1) {

    }

    @Override
    public void addSencesCallback(int i, String s, String s1) {

    }
}

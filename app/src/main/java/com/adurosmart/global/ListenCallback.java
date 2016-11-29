package com.adurosmart.global;

import android.content.Intent;

import com.core.entity.AppDevice;
import com.core.entity.AppGroup;
import com.core.entity.AppScene;
import com.core.entity.AppTask;
import com.core.gatewayinterface.InterfaceCallback;

import org.eclipse.paho.android.service.sample.MyApp;

/**
 * Created by best on 2016/7/11.
 */
public class ListenCallback implements InterfaceCallback {
    @Override
    public void DeleteGroupCallback(short i) {

    }

    @Override
    public void ChangeGroupNameCallback(short i, String s) {

    }

    @Override
    public void getAllGroupsCallback(AppGroup appGroup) {

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
    public void SendExceptionCallack(int i) {

    }

    @Override
    public void AgreeDeviceInNetCallback(int i) {

    }

    @Override
    public void ScanDeviceCallback(AppDevice appDevice) {

    }

    @Override
    public void AddDeviceCallback(AppDevice appDevice) {

    }

    @Override
    public void DeleteDeviceCallback(int i) {

    }

    @Override
    public void ModifyDeviceCallback(int i) {

    }

    @Override
    public void setDeviceStateCallback(int i) {

    }

    @Override
    public void getDeviceStateCallback(String s, int i) {

    }

    @Override
    public void getDeviceOnLinStatus(String s, String s1, byte b) {

    }

    @Override
    public void setDeviceLevelCallback(String s, byte b) {

    }

    @Override
    public void getDeviceLevelCallback(String s, int i) {

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
    public void vRetResponseBatteryValueCallback(String s, int i) {

    }

    @Override
    public void vRetDeviceZoneType(String s, String s1) {

    }

    @Override
    public void getReceiveSensorDataCallback(String s, int i, String s1) {

    }

    @Override
    public void bingdevicecallback(short i, double v, double v1, double v2, double v3) {

    }

    @Override
    public void getScenesCallback(AppScene appScene) {

    }

    @Override
    public void addSceneCallback(short i, String s, short i1) {

    }

    @Override
    public void getSceneDetailsCallback(short i, String s, int i1, Short aShort) {

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
    public void ChangeSceneName(short i, String s) {

    }

    @Override
    public void getAllTasksCallback(AppTask appTask) {

    }

    @Override
    public void AddGroupCallback(Short aShort, String s) {

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

}

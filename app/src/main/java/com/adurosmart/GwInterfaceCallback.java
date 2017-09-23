package com.adurosmart;

import android.content.Intent;
import android.util.Log;

import com.adurosmart.global.Constants;
import com.core.entity.AppDevice;
import com.core.entity.AppGateway;
import com.core.entity.AppGroup;
import com.core.entity.AppScene;
import com.core.entity.AppTask2;
import com.core.gatewayinterface.InterfaceCallback;
import com.core.gatewayinterface.SerialHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by best on 2016/10/25.
 */

public class GwInterfaceCallback implements InterfaceCallback {
    @Override
    public void vRetExceptionCallack(int i) {
        System.out.println("Exception = " + i);
        Intent intent = new Intent();
        intent.setAction("SEND_EXCEPTION_CALLBACK");
        intent.putExtra("Callback_Value", i);
        MyApp.app.sendBroadcast(intent);
    }


    @Override
    public void vRetGatewayUpdateResult(int result) {
        System.out.println("UpdateResult = " + result);
    }

    @Override
    public void vRetGatewayServerAddress(String serverAddress) {
        System.out.println("serverAddress = " + serverAddress);
    }

    @Override
    public void vRetGatewayUpdateVersion(int version) {
        System.out.println("version = " + version);
    }

    @Override
    public void vRetResetGatewayCallback(int i) {
        System.out.println("vRetResetGatewayCallback = " + i);
    }

    @Override
    public void vRetDeviceZoneType(String device_mac, String zone_type) {
        Log.i("attribValue 0x31= ", "" + zone_type);
        Intent i = new Intent();
        i.setAction("RetDeviceZoneType");
        i.putExtra("DEVICE_MAC", device_mac);
        i.putExtra("ZONE_TYPE", zone_type);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetResponseBatteryValueCallback(String device_mac, int value) {
        Intent intent = new Intent();
        intent.setAction("SENSORS_BatteryValue");
        intent.putExtra("device_mac", device_mac);
        intent.putExtra("value", value);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void bingdevicecallback(String deivce_short_addr, short frequency, double voltage, double current, double power, double power_factor) {
        Intent intent = new Intent();
        intent.setAction("bingdevicecallback");
        intent.putExtra("short_address", deivce_short_addr);
        intent.putExtra("frequency", frequency);
        intent.putExtra("voltage", voltage);
        intent.putExtra("current", current);
        intent.putExtra("power", power);
        intent.putExtra("power_factor", power_factor);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void ScanDeviceCallback(AppDevice appDeviceInfo) {
        System.out.println("appDeviceInfo = " + appDeviceInfo.getDeviceMac());
        EventBus.getDefault().post(new FirstEvent(appDeviceInfo));
        Intent intent = new Intent();
        intent.setAction("AppDeviceInfo_CallBack");
        intent.putExtra("AppDeviceInfo", appDeviceInfo);
        MyApp.app.sendBroadcast(intent);

        for (AppDevice devInfo : Constants.appDeviceList) {
            if (devInfo.getDeviceMac().endsWith(appDeviceInfo.getDeviceMac())) {
                return;
            }
        }
        Constants.appDeviceList.add(appDeviceInfo);
        //第一版设备数据
//        Intent intent1 = new Intent();
//        intent1.setAction("TaskDeviceInfoAction");
//        intent1.putExtra("DeviceInfo", appDeviceInfo);
//        MyApp.app.sendBroadcast(intent1);
    }

    @Override
    public void getAllTasksCallback(AppTask2 appTask) {
        System.out.println("getAllTasksCallback task_no = " + appTask.getTask_no());
        Intent intent = new Intent();
        intent.setAction("TASK_INFO");
        intent.putExtra("AppTaskInfo", appTask);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void ChangeGroupNameCallback(short groupid, String groupname) {
        System.out.println("GwInterfaceCallback = " + groupid + "," + groupname);
    }

    @Override
    public void AddDeviceCallback(AppDevice appDevice) {
        Intent intent = new Intent();
        intent.setAction("AppDeviceInfo_CallBack");
        intent.putExtra("AppDeviceInfo", appDevice);
        MyApp.app.sendBroadcast(intent);
        System.out.println("AddDeviceCallback" + appDevice.getDeviceMac());
    }

    @Override
    public void GatewatInfoCallback(AppGateway appGateway) {
        System.out.println("GatewatInfoCallbackgatewayName = " + appGateway.getGateway_no());
        System.out.println("GatewatInfoCallbackgatewayNo = " + appGateway.getGateway_mac());
        System.out.println("GatewatInfoCallbacksoftwareVersion = " + appGateway.getIp_address());
        System.out.println("GatewatInfoCallbackhardwareVersion = " + appGateway.getGateway_version());
        System.out.println("GatewatInfoCallbackIPv4Address = " + appGateway.getNode_main_version());
        System.out.println("GatewatInfoCallbackdatetime = " + appGateway.getBootrodr());
        Calendar c = Calendar.getInstance();
        SerialHandler.getInstance().setGateWayTime(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
    }

    @Override
    public void AddGroupCallback(Short group_id, String group_name) {
        Log.i("group_id = ", "" + group_id);
        Log.i("group_name = ", "" + group_name);
        if (group_id < 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("ADD_GROUP_CALLBACK");
        intent.putExtra("Group_Id", group_id);
        intent.putExtra("Group_Name", group_name);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void DeleteGroupCallback(short result) {

    }

    @Override
    public void getAllGroupsCallback(AppGroup appGroup) {
        Log.i("组名称 = ", appGroup.getGroup_name());
        if (appGroup.getGroup_name() == null && appGroup.getGroup_id() == 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("GROUPS_INFO");
        intent.putExtra("AppGroupInfo", appGroup);
        MyApp.app.sendBroadcast(intent);

        //获取grouplist
        for (AppGroup appGroupInfo : Constants.appGroupList) {
            if (appGroupInfo.getGroup_id().equals(appGroup.getGroup_id())) {
                appGroupInfo.setMac_data(appGroup.getMac_data());
                return;
            }
        }
        Constants.appGroupList.add(appGroup);
    }

    @Override
    public void setGroupsStateCallback(short i, byte b) {

    }

    @Override
    public void getReceiveSensorDataCallback(String deviceid, int state, String time) {
        Intent intent = new Intent();
        intent.setAction("SENSORS_INFO");
        intent.putExtra("device_mac", deviceid);
        intent.putExtra("msg_state", state);
        intent.putExtra("msg_time", time);
        MyApp.app.sendBroadcast(intent);
        Log.i("SENSORS_INFO = ", deviceid);
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
    public void AgreeDeviceInNetCallback(int i) {

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
    public void getDeviceStateCallback(String device_mac, int state) {
        System.out.println("getDeviceStateCallback = " + device_mac + "," + state);
        Intent intent = new Intent();
        intent.setAction("DeviceState_CallBack");
        intent.putExtra("Device_Mac", device_mac);
        intent.putExtra("DeviceState", state);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void getDeviceOnLinStatus(String s, String s1, byte b) {

    }

    @Override
    public void setDeviceLevelCallback(String s, byte b) {

    }

    @Override
    public void getDeviceLevelCallback(String device_mac, int value) {
        Log.i("value = ", "" + value);
        Intent intent = new Intent();
        intent.setAction("DeviceLevelCallback");
        intent.putExtra("Device_Mac", device_mac);
        intent.putExtra("DeviceValue", value);
        MyApp.app.sendBroadcast(intent);
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
    public void getScenesCallback(AppScene appScene) {
        Log.i("sencesId", appScene.getSencesId() + "");
        EventBus.getDefault().post(new FirstEvent(appScene));

        Intent intent = new Intent();
        intent.setAction("SECENS_INFO");
        intent.putExtra("AppSceneInfo", appScene);
        MyApp.app.sendBroadcast(intent);

        for (AppScene appSceneInfo : Constants.appSceneList) {
            if (appSceneInfo.getSencesId() == appScene.getSencesId()) {
                return;
            }
        }
        Constants.appSceneList.add(appScene);
    }

    @Override
    public void addSceneCallback(short sencesid, String sencesName, short group_id) {
        Intent intent = new Intent();
        intent.setAction("ADD_SCENE_CALLBACK");
        intent.putExtra("Scene_Id", sencesid);
        intent.putExtra("Scene_Name", sencesName);
        intent.putExtra("Scene_Group_Id", group_id);
        MyApp.app.sendBroadcast(intent);
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
    public void ChangeSceneName(short scene_id, String scene_name) {
        System.out.println("scene_id callback = " + scene_id);
        System.out.println("scene_name callback = " + scene_name);
    }
}

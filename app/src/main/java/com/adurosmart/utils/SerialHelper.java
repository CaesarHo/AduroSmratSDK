package com.adurosmart.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.adurosmart.bean.DeviceInfo;

import org.eclipse.paho.android.service.sample.MyApp;

/**
 * Created by best on 2016/7/7.
 */
public class SerialHelper {
    public static final String TAG = "SDK";
    private static SerialHelper mHelper;
    public Context context;
    private SerialHelper(Context context){
        this.context = context;
    }

    public synchronized static SerialHelper getInstance(Context context) {
        if (null == mHelper) {
            synchronized (SerialHelper.class) {
                mHelper = new SerialHelper(context);
            }
        }
        return mHelper;
    }



    /**
     * 添加房间
     * @param roomid
     * @param roomname
     */
    public void CreateRoom(String roomid,String roomname){
        //
    }

    /**
     * 删除房间
     * @param roomid
     * @param roomname
     */
    public void DeleteRoom(String roomid,String roomname){
        //
    }

    /**
     * 修改房间
     * @param roomid
     * @param roomname
     */
    public void ModifyRoom(String roomid,String roomname){
        //
    }

    /**
     * 房间开关
     * @param roomid
     * @param roomname
     * @param type  开关类型(0开,1关)
     */
    public void RoomSwitch(String roomid,String roomname,int type){
        //
    }

    //获取网关所有设备
    public void GetDevice(){
        Short deviceId = 0x0001;
        DeviceInfo deviceInfo = new DeviceInfo("DeviceNmae",1234,deviceId,deviceId,(byte) 0x02,(byte) 0x02,(byte) 0x020,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x02,(byte)0x01,deviceId);
        Log.d("serial","a new device has added.. name = " + deviceInfo.getDeviceName());
        Log.d("serial", "dinfo.getDeviceState() = " + deviceInfo.getDeviceState());
        Intent i = new Intent();
        i.setAction("getdevice_callback");
        i.putExtra("action", true);
        i.putExtra("dinfo",deviceInfo);
        MyApp.app.sendBroadcast(i);
    }
    /**
     * 添加设备
     * @param deviceid
     * @param devicename
     * @param type
     */
    public void AddDevice(String deviceid,String devicename,int type){
        //
    }

    /**
     * 删除设备
     * @param deviceid
     * @param devicename
     * @param type
     */
    public void DeleteDevice(String deviceid,String devicename,int type){
        //
    }

    /**
     * 修改设备
     * @param deviceid
     * @param devicename
     * @param type
     */
    public void ModifyDevice(String deviceid,String devicename,int type){
        //
    }

    /**
     * 改变设备状态(0关1开)
     * @param deviceid
     * @param devicename
     * @param state
     */
    public void setDeviceState(String deviceid,String devicename,int state){
        //
    }

    /**
     * 改变设备值，（亮度）
     * @param deviceInfo
     * @param value
     */
    public void setDeviceLevel(DeviceInfo deviceInfo,byte value){

    }

    /**
     * 改变设备色调、饱和度
     *
     * @param deviceid 要改变色调、饱和度的设备
     * @param hue  色调
     * @param sat  饱和度
     */
    public void setDeviceHueSat(String deviceid,byte hue,byte sat){
        //
    }


}

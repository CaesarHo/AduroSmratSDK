package com.adurosmart.global;

import android.content.Intent;

import com.interfacecallback.SdkCallback;

import org.eclipse.paho.android.service.sample.MyApp;

import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public class ListenCallback implements SdkCallback{
    @Override
    public void GatewayInformation(int i, InetAddress inetAddress, String s) {

    }

    @Override
    public void setDeviceStateResult(String s, int i) {

    }

    @Override
    public void AddRoomCallback(int i) {

    }

    @Override
    public void DeleteRoomCallback(int i) {

    }

    @Override
    public void ModifyRoomCallback(int i) {

    }

    @Override
    public void AddDeviceCallback(int i) {
        Intent intent = new Intent();
        intent.setAction("adddevicecallback");
        intent.putExtra("i",i);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void DeleteDeviceCallback(int i) {

    }

    @Override
    public void ModifyDeviceCallback(int i) {

    }
}

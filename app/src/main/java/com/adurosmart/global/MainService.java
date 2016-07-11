package com.adurosmart.global;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;

import com.adurosmart.thread.MainThread;
import com.adurosmart.utils.UDPHelper2;

/**
 * Created by best on 2016/6/27.
 */
public class MainService extends Service {
    Context context;
    Thread tReceived;
    UDPHelper2 udpHelper2;
    WifiManager wifiManager;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new MainThread(context).go();

        //获取wifi服务
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //监听2
        udpHelper2 = new UDPHelper2(context,wifiManager);
        tReceived = new Thread(udpHelper2);
        tReceived.start();
    }

    public void MyMethod(){

    }

    @Override
    public void onStart(Intent intent, int startId) {
        //监听2
        udpHelper2 = new UDPHelper2(context,wifiManager);
        tReceived = new Thread(udpHelper2);
        tReceived.start();
        new MainThread(context).go();
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        new MainThread(context).go();
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        tReceived.interrupt();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    public MainService() {
        super();
    }

    public class MyBinder extends Binder {
        public MainService getService(){
            return MainService.this;
        }
    }
    private MyBinder myBinder = new MyBinder();
}

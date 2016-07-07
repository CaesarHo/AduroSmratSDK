package com.adurosmart.global;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.adurosmart.thread.MainThread;

/**
 * Created by best on 2016/6/27.
 */
public class MainService extends Service {
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new MainThread(context).go();
    }

    public void MyMethod(){

    }

    @Override
    public void onStart(Intent intent, int startId) {
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

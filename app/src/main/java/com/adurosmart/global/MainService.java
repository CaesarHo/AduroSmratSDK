package com.adurosmart.global;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by best on 2016/6/27.
 */
public class MainService extends Service {
    Context context;
    public static String mServer = "www.adurosmart.com";
    public static String clientId;
    public static int port = 1883;
    public static String mAndroidId;
    public String clientHandle = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
    }

    public void MyMethod(){

    }

    @Override
    public void onStart(Intent intent, int startId) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
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

    public void connectAction(){

    }

    public class MQTTReconnectThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);// 线程暂停1秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    break;
            }
        }
    };

    /**
     * Reconnect the selected client
     */
    private void reconnect() {

    }
}

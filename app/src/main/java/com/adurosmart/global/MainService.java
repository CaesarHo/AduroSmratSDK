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

import com.mqttglobal.MqttAndroidClient;
import com.mqttglobal.handle.Connection;
import com.mqttglobal.handle.Connections;
import com.mqttglobal.handle.MQTTHelper;
import com.mqttglobal.handle.MqttCallbackHandler;
import com.mqttglobal.handle.MqttTraceCallback;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by best on 2016/6/27.
 */
public class MainService extends Service {
    Context context;
    MQTTHelper mqttHelper;
    public static String mServer = "www.adurosmart.com";
    public static String clientId;
    public static int port = 1883;
    public static String mAndroidId;
    public String clientHandle = null;
    Thread thread;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        new Thread(new MQTTReconnectThread()).start();//创建线程判断如果MQTT掉线能重连
        mqttHelper = new MQTTHelper(context,clientHandle);
        thread = new Thread();
        thread.start();
    }

    public void MyMethod(){

    }

    @Override
    public void onStart(Intent intent, int startId) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        new Thread(new MQTTReconnectThread()).start();//创建线程判断如果MQTT掉线能重连
        mqttHelper = new MQTTHelper(context,clientHandle);
        thread = new Thread();
        thread.start();
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        new Thread(new MQTTReconnectThread()).start();//创建线程判断如果MQTT掉线能重连
        mqttHelper = new MQTTHelper(context,clientHandle);
        thread = new Thread();
        thread.start();
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        clientId = mAndroidId;
        connectAction();
        new Thread(new MQTTReconnectThread()).start();//创建线程判断如果MQTT掉线能重连
        mqttHelper = new MQTTHelper(context,clientHandle);
        thread = new Thread();
        thread.start();
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
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

    public void connectAction(){
        String uri = "tcp://" + mServer + ":" + port;
        MqttConnectOptions conOpt = new MqttConnectOptions();
        MqttAndroidClient client = Connections.getInstance(this).createClient(this, uri, clientId);

        // create a client handle
        clientHandle = uri + clientId;
        Connection connection = new Connection(clientHandle, clientId, mServer, port, this, client, false);
        client.setCallback(new MqttCallbackHandler(this, clientHandle));

        //set traceCallback
        client.setTraceCallback(new MqttTraceCallback());

        connection.addConnectionOptions(conOpt);
        Connections.getInstance(this).addConnection(connection);
        try {
            client.connect(conOpt, null,null);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "MqttException Occured", e);
        }
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
                    if (!Connections.getInstance(context).getConnection(clientHandle).isConnected()){
                        reconnect();
                    }
                    break;
            }
        }
    };

    /**
     * Reconnect the selected client
     */
    private void reconnect() {
//		Connections.getInstance(app).getConnection(clientHandle).changeConnectionStatus(ConnectionStatus.CONNECTING);
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        try {
            c.getClient().connect(c.getConnectionOptions(), null,null);
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle, e);
            c.addAction("Client failed to connect");
        }
    }
}

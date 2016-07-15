package com.mqttglobal.handle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mqttglobal.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Created by best on 2016/7/1.
 */
public class MQTTHelper implements Runnable {
    Context context;

    MQTTHelper mqttHelper;
    public static String mServer = "www.adurosmart.com";
    public static String clientId;
    public static int port = 1883;
    public static String mAndroidId;
    public String clientHandle = null;
    Thread thread;

    public MQTTHelper(Context context, String clientHandle) {
        this.context = context;
        this.clientHandle = clientHandle;
    }
    @Override
    public void run() {
//        mHandler.sendEmptyMessageDelayed(1,5000);
        mAndroidId = "123456789";
        clientId = mAndroidId;
        connectAction();
        new Thread(new MQTTReconnectThread()).start();//创建线程判断如果MQTT掉线能重连
        mqttHelper = new MQTTHelper(context,clientHandle);
        thread = new Thread();
        thread.start();
        mHandler.sendEmptyMessageDelayed(1,2000);
    }

    public Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                {
                    if (Connections.getInstance(context).getConnection(clientHandle).isConnected()){
                        subscribe();
                    }else{
                        reconnect();
                    }
                }
                break;
            }
            return false;
        }
    });

    /**
     * Subscribe to a topic that the user has specified
     */
    private void subscribe() {
        String topic = "200004401331";
        try {
            String[] topics = new String[1];
            topics[0] = topic;
            Connections.getInstance(context).getConnection(clientHandle).getClient().subscribe(
                    topic, 0, context,null);
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
    }

    /**
     * Reconnect the selected client
     */
    private void reconnect() {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        try {
            c.getClient().connect(c.getConnectionOptions(), null,null);
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle, e);
            c.addAction("Client failed to connect");
        }
    }







    public void connectAction(){
        String uri = "tcp://" + mServer + ":" + port;
//        MqttConnectOptions conOpt = new MqttConnectOptions();
        MqttAndroidClient client = Connections.getInstance(context).createClient(context, uri, clientId);

        // create a client handle
        clientHandle = uri + clientId;
        Connection connection = new Connection(clientHandle, clientId, mServer, port, context, client, false);
        client.setCallback(new MqttCallbackHandler(context, clientHandle));

        //set traceCallback
        client.setTraceCallback(new MqttTraceCallback());

//        connection.addConnectionOptions(conOpt);
        Connections.getInstance(context).addConnection(connection);
//        try {
//            client.connect(conOpt, null,null);
//        } catch (MqttException e) {
//            Log.e(this.getClass().getCanonicalName(), "MqttException Occured", e);
//        }
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
}

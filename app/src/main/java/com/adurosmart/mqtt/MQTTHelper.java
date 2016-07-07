package com.adurosmart.mqtt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.android.service.sample.ActionListener;
import org.eclipse.paho.android.service.sample.Connection;
import org.eclipse.paho.android.service.sample.Connections;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Created by best on 2016/7/1.
 */
public class MQTTHelper implements Runnable {
    Context context;
    private String clientHandle;

    public MQTTHelper(Context context, String clientHandle) {
        this.context = context;
        this.clientHandle = clientHandle;
    }
    @Override
    public void run() {
        mHandler.sendEmptyMessageDelayed(1,2000);
//        mHandler.sendEmptyMessageDelayed(1,5000);
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
                    topic, 0, context,new ActionListener(context, ActionListener.Action.SUBSCRIBE,clientHandle,topics));
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
            c.getClient().connect(c.getConnectionOptions(), null, new ActionListener(context, ActionListener.Action.CONNECT, clientHandle, null));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle, e);
            c.addAction("Client failed to connect");
        }
    }
}

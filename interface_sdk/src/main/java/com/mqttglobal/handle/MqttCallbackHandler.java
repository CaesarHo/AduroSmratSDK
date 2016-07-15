/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 * <p/>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p/>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.mqttglobal.handle;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Handles call backs from the MQTT Client
 */
public class MqttCallbackHandler implements MqttCallback {
    private Context context;
    private String clientHandle;

    public MqttCallbackHandler(Context context, String clientHandle) {
        this.context = context;
        this.clientHandle = clientHandle;
    }

    @Override
    public void connectionLost(Throwable cause) {
//	  cause.printStackTrace();
        if (cause != null) {
            Connection c = Connections.getInstance(context).getConnection(clientHandle);
            //当mqtt掉线时重新连接
            if (!c.isConnected()){
                reconnect();
            }
            c.addAction("Connection Lost");
            c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
        }
    }

    /**
     * Reconnect the selected client
     */
    private void reconnect(){
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        try {
            c.getClient().connect(c.getConnectionOptions(), null,null);
            Log.i("MQTT重连连接 = ",clientHandle);
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle, e);
            c.addAction("Client failed to connect");
        }
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        //Get connection object associated with this object
        Connection c = Connections.getInstance(context).getConnection(clientHandle);

        //create arguments to format message arrived notifcation string
        String[] args = new String[2];
        args[0] = new String(message.getPayload());
        args[1] = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();

        //get the string from strings.xml and format
        String messageString = "Received message %1$s &lt;br/&gt; &lt;small&gt;Topic: %2$s &lt;/small&gt; " +  (Object[]) args;//context.getString(R.string.messageRecieved, (Object[]) args);

        Log.i("MQTT接收到消息 = ",messageString);
        //create intent to start activity
//        Intent intent = new Intent();
//        intent.setClassName(context, "org.eclipse.paho.android.service.sample.ConnectionDetails");
//        intent.putExtra("handle", clientHandle);

        //update client history
        c.addAction(messageString);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }
}

package com.core.mqtt;

import android.util.Log;

import com.core.utils.ParseData;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by best on 2016/9/28.
 */

public class MqttCallbackBus implements MqttCallback {
    private static final String TAG = "MqttCallbackBus";
    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG,cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.e(TAG,topic + "====" + message.toString());
        System.out.println(TAG+message.getPayload());
        ParseData.ParseDeviceInfo parseDeviceInfo = new ParseData.ParseDeviceInfo();
        parseDeviceInfo.parseData(message.toString());
//        EventBus.getDefault().post(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

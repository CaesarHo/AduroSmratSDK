package com.core.mqtt;

import android.util.Log;

import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;

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
        Log.e(TAG + "Lost = ",cause.getMessage());
        MqttManager.getInstance().creatConnect(Constants.URI,null,null,Constants.CLIENT_ID);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.e(TAG,topic + "=" + message.toString());
        System.out.println(TAG + "=" +message.getPayload());

        //解析获取设备信息
        ParseDeviceData.ParseGetDeviceInfo(message.toString(),false);
        //解析获取groupinfo
        ParseGroupData.ParseGetGroupsInfo(message.toString());
        //解析获取sceneinfo
        ParseSceneData.ParseGetScenesInfo(message.toString());

        //解析添加组返回数据
        ParseGroupData.ParseAddGroupBack(message.getPayload(),Constants.GROUP_GLOBAL.ADD_GROUP_NAME.length());
        //解析添加场景返回数据

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

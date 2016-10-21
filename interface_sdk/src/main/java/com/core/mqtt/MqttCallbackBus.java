package com.core.mqtt;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.db.GatewayInfo;
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
    private Context mContext;

    public MqttCallbackBus (Context context){
        mContext = context;
        System.out.println("MqttCallbackBus = " + "Context");
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG + "Lost = ",cause.getMessage());
        MqttManager.getInstance().creatConnect(Constants.URI,null,null,Constants.CLIENT_ID);
        MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.e(TAG,topic + "=" + message.toString());
        System.out.println(TAG + "=" +message.getPayload());

        //解析获取设备信息
        ParseDeviceData.ParseGetDeviceInfo(message.toString(),false);
        System.out.println("ParseGetDeviceInfo" + "ParseGetDeviceInfo");
        //解析获取groupinfo
        ParseGroupData.ParseGetGroupsInfo(message.toString());
        System.out.println("ParseGetGroupsInfo" + "ParseGetGroupsInfo");
        //解析获取sceneinfo
        ParseSceneData.ParseGetScenesInfo(message.toString());
        System.out.println("ParseGetScenesInfo" + "ParseGetScenesInfo");

        //解析添加组返回数据
        ParseGroupData.ParseAddGroupBack(message.getPayload(),Constants.GROUP_GLOBAL.ADD_GROUP_NAME.length());
        System.out.println("ParseAddGroupBack" + "ParseAddGroupBack");

        //解析添加场景返回数据
        ParseSceneData.ParseAddSceneBackInfo(message.getPayload(),Constants.SCENE_GLOBAL.ADD_SCENE_NAME.length());
        System.out.println("ParseAddSceneBackInfo" + "ParseAddSceneBackInfo");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

package com.core.mqtt;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;

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

        if ((int) MessageType.A.GET_ALL_GROUP.value() == message.getPayload()[11]){
            //解析接受到的数据
            ParseGroupData.ParseGetGroupsInfo(message.toString());
            return;
        }

        if((int) MessageType.A.UPLOAD_TXT.value() == message.getPayload()[11]){
            //新入网的设备
            ParseDeviceData.ParseGetDeviceInfo(message.toString(),true);
            return;
        }else if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == message.getPayload()[11]){
            //解析获取设备信息
            ParseDeviceData.ParseGetDeviceInfo(message.toString(),false);
            return;
        }

        if ((int) MessageType.A.GET_ALL_SCENE.value() == message.getPayload()[11]){
            //解析获取sceneinfo
            ParseSceneData.ParseGetScenesInfo(message.toString());
            return;
        }

        if((int)MessageType.A.ADD_GROUP_NAME.value() == message.getPayload()[11]){
            //解析添加组返回数据
            ParseGroupData.ParseAddGroupBack(message.getPayload(),Constants.GROUP_GLOBAL.ADD_GROUP_NAME.length());
            System.out.println("ParseAddGroupBack" + "ParseAddGroupBack");
        }

        if ((int) MessageType.A.ADD_SCENE_NAME.value() == message.getPayload()[11]) {
            //解析添加场景返回数据
            ParseSceneData.ParseAddSceneBackInfo(message.getPayload(),Constants.SCENE_GLOBAL.ADD_SCENE_NAME.length());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

package com.core.mqtt;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.Utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import static com.core.global.Constants.isConn;

/**
 * Created by best on 2016/9/28.
 */

public class MqttCallbackBus implements MqttCallback {
    private static final String TAG = "MqttCallbackBus";
    private Context mContext;

    public MqttCallbackBus(Context context) {
        mContext = context;
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG + "Lost = ", cause.getMessage());
        if (!isConn){
            SerialHandler.getInstance().setMqttCommunication();
        }
//        MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.CLIENT_ID);
//        MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
    }

    @Override
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        Log.e(TAG, topic + "=" + message.toString());
        Log.e(TAG, " = " + Utils.bytesToHexString(message.getPayload()));
        System.out.println(TAG + "=" + Arrays.toString(message.getPayload()));

        if (message.toString().contains("K64") | message.toString().contains("APP")) {
            return;
        }

        DataPacket.getInstance().BytesDataPacket(mContext, message.getPayload());
        /**
         * 获取网关所有设备及其新入网设备
         */
        if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == message.getPayload()[11]) {
//                        byte[] bt = AESUtils.decode(recbuf);
            ParseDeviceData.ParseGetDeviceInfo(mContext,message.getPayload(), false);
        } else if ((int) MessageType.A.UPLOAD_TXT.value() == message.getPayload()[11]) {//新入网设备
//                        byte[] bt = AESUtils.decode(recbuf);
            ParseDeviceData.ParseGetDeviceInfo(mContext,message.getPayload(), true);
        }
        /**
         * 枚举A判断是否是房间列表
         */
        if ((int) MessageType.A.GET_ALL_GROUP.value() == message.getPayload()[11]) {
            ParseGroupData.ParseGetGroupsInfo(message.getPayload());
        }
        /**
         * 枚举A判断是否是场景列表
         */
        if ((int) MessageType.A.GET_ALL_SCENE.value() == message.getPayload()[11]) {
            ParseSceneData.ParseGetScenesInfo(message.getPayload());
        }
        /**
         * 枚举A判断是否是任务列表
         */
        if (MessageType.A.GET_ALL_TASK.value() == message.getPayload()[11]) {
            ParseTaskData.ParseGetTaskInfo2 parseGetTaskInfo2 = new ParseTaskData.ParseGetTaskInfo2();
            parseGetTaskInfo2.parseBytes(message.getPayload());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

package com.core.mqtt;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import static com.core.global.Constants.DEVICE_GLOBAL.sdkappDevice;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;

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
        MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.CLIENT_ID);
        MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
    }

    @Override
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        Log.e(TAG, topic + "=" + message.toString());
        System.out.println(TAG + "=" + Arrays.toString(message.getPayload()));
        //-------------------------------------device start-------------------------------------
        /**
         * MQTT获取网关新设备及其老设备
         */
        if ((int) MessageType.A.UPLOAD_TXT.value() == message.getPayload()[11]) {
            //新入网的设备
            ParseDeviceData.ParseGetDeviceInfo(message.getPayload(), true);
            return;
        } else if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == message.getPayload()[11]) {
            //解析获取设备信息
            ParseDeviceData.ParseGetDeviceInfo(message.getPayload(), false);
            return;
        }
        /**
         * MQTT获取网关IEEE地址
         */
        if ((int) MessageType.A.GET_GATEWAY_IEEE.value() == message.getPayload()[11]) {
            ParseDeviceData.ParseIEEEData parseIEEEData = new ParseDeviceData.ParseIEEEData();
            parseIEEEData.parseBytes(message.getPayload());
            byte[] bt = DeviceCmdData.BindDeviceCmd(sdkappDevice,parseIEEEData.gateway_mac);
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt);
        }
        /**
         * MQTT绑定指定设备
         */
        if ((int)MessageType.A.BIND_DEVICE.value() == message.getPayload()[11]){
            ParseDeviceData.ParseBindVCPFPData parseBindVCPFPData = new ParseDeviceData.ParseBindVCPFPData();
            parseBindVCPFPData.parseBytes(message.getPayload());
        }
        /**
         * MQTT接收上传数据包括传感器数据，设备属性
         */
        if (message.getPayload()[11] == MessageType.A.UPLOAD_DEVICE_INFO.value()){
            ParseDeviceData.ParseAttributeData parseAttributeData = new ParseDeviceData.ParseAttributeData();
            parseAttributeData.parseBytes(message.getPayload());
            if (parseAttributeData.message_type.contains("8100") & parseAttributeData.clusterID == 8) {
                DataSources.getInstance().getDeviceLevel(parseAttributeData.device_mac, parseAttributeData.attribValue);
            }
            if (parseAttributeData.message_type.contains("8100") & parseAttributeData.clusterID == 6) {
                DataSources.getInstance().getDeviceState(parseAttributeData.device_mac, parseAttributeData.attribValue);
            }
        }

        /**
         * 解析网关信息
         */
        if ((int) MessageType.A.GET_GW_INFO.value() == message.getPayload()[11]) {
            ParseDeviceData.ParseGWInfoData gwInfoData = new ParseDeviceData.ParseGWInfoData();
            gwInfoData.parseBytes(message.getPayload());
        }

        /**
         * 设备返回的状态
         */
        ParseDeviceData.ParseDeviceStateOrLevel pDevStateOrLevel = new ParseDeviceData.ParseDeviceStateOrLevel();
        pDevStateOrLevel.parseBytes(message.getPayload());
        if (pDevStateOrLevel.message_type.contains("8101") & pDevStateOrLevel.clusterID == 6) {
            DataSources.getInstance().getDeviceState(pDevStateOrLevel.short_address, pDevStateOrLevel.state);
        }

        /**
         * 接受传感器数据并解析
         */
        ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
        sensorData.parseBytes(message.getPayload());
        if (sensorData.message_type.contains("8401")) {
            //当有传感器数据上传时读取ZONETYPE
            byte[] bt = DeviceCmdData.ReadZoneTypeCmd(sensorData.sensor_mac, sensorData.short_address);
            MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt);
            DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
        }

        /**
         * 解析设备属性数据
         */
        ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
        attribute.parseBytes(message.getPayload());

        /**
         * 传感器电量返回值
         */
        if (attribute.message_type.contains("8102")) {
            DataSources.getInstance().responseBatteryValue(attribute.device_mac, attribute.attribValue);
        }

        if (attribute.message_type.contains("8100")) {
            //如果设备属性簇ID等于5则发送保存zonetypecmd
            if (attribute.clusterID == 5) {
                byte[] zt_bt = DeviceCmdData.SaveZoneTypeCmd(
                        attribute.message_type,
                        attribute.short_address,
                        attribute.endpoint + "",
                        (short) attribute.attribValue);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, zt_bt);
            }
        }
        //--------------------------------------device end--------------------------------------

        //--------------------------------------group start-------------------------------------
        /**
         * 创建group返回的id和name
         */
        if ((int) MessageType.A.ADD_GROUP_NAME.value() == message.getPayload()[11]) {
            //解析添加组返回数据
            byte[] group_name_bt = Constants.GROUP_GLOBAL.ADD_GROUP_NAME.getBytes("utf-8");
            ParseGroupData.ParseAddGroupInfo groupInfo = new ParseGroupData.ParseAddGroupInfo();
            groupInfo.parseBytes(message.getPayload(), group_name_bt.length);
        }
        /**
         * MQTT获取groups
         */
        if ((int) MessageType.A.GET_ALL_GROUP.value() == message.getPayload()[11]) {
            //解析接受到的数据
            ParseGroupData.ParseGetGroupsInfo(message.getPayload());
            return;
        }
        /**
         * MQTT删除group返回的结果
         */
        if (message.getPayload()[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
            //解析数据
            ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
            parseData.parseBytes(message.getPayload());
            DataSources.getInstance().DeleteGroupResult(parseData.group_id);
        }
        /**
         * MQTT修改group返回的数据
         */
        if (message.getPayload()[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
            byte[] group_name_bt = NEW_GROUP_NAME.getBytes("utf-8");
            ParseGroupData.ParseModifyGroupInfo groupInfo = new ParseGroupData.ParseModifyGroupInfo();
            groupInfo.parseBytes(message.getPayload(), group_name_bt.length);
            DataSources.getInstance().ChangeGroupName(groupInfo.group_id, groupInfo.group_name);
        }

        //-------------------------------------group end----------------------------------------

        //-------------------------------------scene start--------------------------------------
        /**
         * 创建scene返回的id和name
         */
        if ((int) MessageType.A.ADD_SCENE_NAME.value() == message.getPayload()[11]) {
            //解析添加场景返回数据
            byte[] scene_name = ADD_SCENE_NAME.getBytes("utf-8");
            ParseSceneData.ParseAddSceneInfo parseAddSceneInfo = new ParseSceneData.ParseAddSceneInfo();
            parseAddSceneInfo.parseBytes(message.getPayload(), scene_name.length);
        }

        /**
         * MQTT获取scenes
         */
        if ((int) MessageType.A.GET_ALL_SCENE.value() == message.getPayload()[11]) {
            //解析获取sceneinfo
            ParseSceneData.ParseGetScenesInfo(message.getPayload());
            return;
        }
        /**
         * MQTT删除场景返回结果
         */
        if (message.getPayload()[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
            byte btToint = message.getPayload()[32];
            int i = btToint & 0xFF;
            DataSources.getInstance().DeleteSences(i);
        }
        /**
         * MQTT修改场景返回数据
         */
        if (message.getPayload()[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
            byte[] bt = NEW_SCENE_NAME.getBytes("utf-8");
            ParseSceneData.ParseModifySceneInfo parseData = new ParseSceneData.ParseModifySceneInfo();
            parseData.parseBytes(message.getPayload(), bt.length);
            DataSources.getInstance().ChangeSencesName(parseData.scene_id, parseData.scene_name);
        }
        //------------------------------------scene end-----------------------------------------

        //------------------------------------task start----------------------------------------
        /**
         * MQTT获取tasks
         */
        if ((int) MessageType.A.GET_ALL_TASK.value() == message.getPayload()[11]) {
            ParseTaskData.ParseGetTaskInfo parseGetTaskInfo = new ParseTaskData.ParseGetTaskInfo();
            parseGetTaskInfo.parseBytes(message.getPayload());
        }

        //------------------------------------task end------------------------------------------
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}

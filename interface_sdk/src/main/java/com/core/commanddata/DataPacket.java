package com.core.commanddata;

import android.content.Context;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.connectivity.UdpClient;
import com.core.gatewayinterface.DataSources;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static com.core.global.Constants.DEVICE_GLOBAL.sdkappDevice;
import static com.core.global.Constants.GROUP_GLOBAL.ADD_GROUP_NAME;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;

/**
 * Created by best on 2016/12/8.
 */

public class DataPacket {
    public static DataPacket dataPacket = null;
    private Context context;

    private DataPacket() {

    }

    public synchronized static DataPacket getInstance() {
        if (null == dataPacket) {
            synchronized (DataPacket.class) {
                dataPacket = new DataPacket();
            }
        }
        return dataPacket;
    }

    public void BytesDataPacket(Context context, byte[] bytes) {
        this.context = context;
        try {
            //-----------------------------网关相关--------------------------------32
            if ((int)MessageType.A.FACTORY_RESET.value() == bytes[11]){
                DataSources.getInstance().ResetGatewayResult(bytes[32]);
            }
            //-----------------------------设备start-------------------------------
            /**
             * 解析网关信息
             */
            if ((int) MessageType.A.GET_GW_INFO.value() == bytes[11]) {
                ParseDeviceData.ParseGWInfoData gwInfoData = new ParseDeviceData.ParseGWInfoData();
                gwInfoData.parseBytes(bytes);
            }

            /**
             * 设备返回的状态
             */
            ParseDeviceData.ParseDeviceStateOrLevel pDevStateOrLevel = new ParseDeviceData.ParseDeviceStateOrLevel();
            pDevStateOrLevel.parseBytes(bytes);
            if (pDevStateOrLevel.message_type.contains("8101") & pDevStateOrLevel.clusterID == 6) {
                DataSources.getInstance().getDeviceState(pDevStateOrLevel.short_address, pDevStateOrLevel.state);
            }

            /**
             * 接受传感器数据并解析
             */
            ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
            sensorData.parseBytes(bytes);
            if (sensorData.message_type.contains("8401")) {
                //当有传感器数据上传时读取ZONETYPE
                byte[] bt = DeviceCmdData.ReadZoneTypeCmd(sensorData.sensor_mac, sensorData.short_address);
                Constants.sendMessage(bt);
                DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
                DataSources.getInstance().getReceiveSensor(sensorData.short_address, sensorData.state);
            }

            /**
             * 解析设备属性数据
             */
            ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
            attribute.parseBytes(bytes);

            /**
             * 传感器电量返回值
             */
            if (attribute.message_type.contains("8102")) {
                DataSources.getInstance().responseBatteryValue(attribute.device_mac, attribute.attribValue);
            }

            if (attribute.message_type.contains("8100")) {
                //如果设备属性簇ID等于5则发送保存zonetypecmd
                if (attribute.clusterID == 5) {
                    byte[] zt_bt = DeviceCmdData.SaveZoneTypeCmd(attribute.message_type, attribute.short_address,
                                   attribute.endpoint + "", (short) attribute.attribValue);
                    Constants.sendMessage(zt_bt);
                }
            }
            if (bytes[11] == MessageType.A.UPLOAD_DEVICE_INFO.value()){
                /**
                 * 读取设备亮度值
                 */
                if (attribute.message_type.contains("8100") & attribute.clusterID == 8) {
                    DataSources.getInstance().getDeviceLevel(attribute.device_mac, attribute.attribValue);
                }
                /**
                 * 读取的设备开关值
                 */
                if (attribute.message_type.contains("8100") & attribute.clusterID == 6) {
                    DataSources.getInstance().getDeviceState(attribute.device_mac, attribute.attribValue);
                }
            }

            //-------------------------------设备end--------------------------------
            //-------------------------------场景start------------------------------
            /**
             * 添加场景名称
             */
            if ((int) MessageType.A.ADD_SCENE_NAME.value() == bytes[11]) {
                byte[] scene_name = ADD_SCENE_NAME.getBytes("utf-8");
                ParseSceneData.ParseAddSceneInfo parseAddSceneInfo = new ParseSceneData.ParseAddSceneInfo();
                parseAddSceneInfo.parseBytes(bytes,scene_name.length);
            }
            /**
             * 修改场景名称
             */
            if (bytes[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
                //解析数据
                ParseSceneData.ParseModifySceneInfo parseData = new ParseSceneData.ParseModifySceneInfo();
                parseData.parseBytes(bytes, NEW_SCENE_NAME.length());
                DataSources.getInstance().ChangeSencesName(parseData.scene_id, parseData.scene_name);
            }
            /**
             * 删除场景
             */
            if (bytes[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
                byte btToint = bytes[32];
                int i = btToint & 0xFF;
                DataSources.getInstance().DeleteSences(i);
            }
            //-------------------------------场景end--------------------------------
            //-------------------------------房间start------------------------------
            /**
             * 创建房间名称
             */
            if ((int) MessageType.A.ADD_GROUP_NAME.value() == bytes[11]) {
                byte[] group_name_bt = ADD_GROUP_NAME.getBytes("utf-8");
                ParseGroupData.ParseAddGroupInfo groupInfo = new ParseGroupData.ParseAddGroupInfo();
                groupInfo.parseBytes(bytes,group_name_bt.length);
            }
            /**
             * 修改房间名称
             */
            if (bytes[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                byte[] group_name_bt = NEW_GROUP_NAME.getBytes("utf-8");
                ParseGroupData.ParseModifyGroupInfo groupInfo = new ParseGroupData.ParseModifyGroupInfo();
                groupInfo.parseBytes(bytes,group_name_bt.length);
                DataSources.getInstance().ChangeGroupName(groupInfo.group_id, groupInfo.group_name);
            }
            /**
             * 删除房间
             */
            if (bytes[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
                parseData.parseBytes(bytes);
                DataSources.getInstance().DeleteGroupResult(parseData.group_id);
            }
            //----------------------------房间end-----------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

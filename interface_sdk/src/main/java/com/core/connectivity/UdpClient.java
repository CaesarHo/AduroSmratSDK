package com.core.connectivity;

import android.content.Context;
import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.DEVICE_GLOBAL.sdkappDevice;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;

/**
 * Created by best on 2016/11/3.
 */

public class UdpClient implements Runnable{

    private byte[] bt_send = null;
    private DatagramSocket socket = null;
    private Context mContext;
    public static final int DEFAULT_TIMEOUT_DURATION = 6000;
    public static int MAX_BUSY_COUNT = 20;

    public UdpClient(Context context, byte[] bt_send){
        this.mContext = context;
        this.bt_send = bt_send;
        this.MAX_BUSY_COUNT = 20;
    }

    @Override
    public void run() {
        try {
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    socket.setSoTimeout(DEFAULT_TIMEOUT_DURATION);
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (MAX_BUSY_COUNT <= 20) {
                    MAX_BUSY_COUNT--;
                    final byte[] recbytes = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbytes, recbytes.length);
                    socket.receive(packet);
                    String isK64 = new String(recbytes).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    System.out.println("设备信息UdpClient = " + Arrays.toString(recbytes));

                    //-------------------------------------device start-------------------------------------
                    /**
                     * MQTT获取网关新设备及其老设备
                     */
                    if ((int) MessageType.A.UPLOAD_TXT.value() == recbytes[11]) {
                        //新入网的设备
                        ParseDeviceData.ParseGetDeviceInfo(recbytes, true);
                        return;
                    } else if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbytes[11]) {
                        //解析获取设备信息
                        ParseDeviceData.ParseGetDeviceInfo(recbytes, false);
                        return;
                    }
                    /**
                     * MQTT获取网关IEEE地址
                     */
                    if ((int) MessageType.A.GET_GATEWAY_IEEE.value() == recbytes[11]) {
                        ParseDeviceData.ParseIEEEData parseIEEEData = new ParseDeviceData.ParseIEEEData();
                        parseIEEEData.parseBytes(recbytes);
                        byte[] bt = DeviceCmdData.BindDeviceCmd(sdkappDevice,parseIEEEData.gateway_mac);
                        Constants.sendMessage(bt);
                    }
                    /**
                     * MQTT绑定指定设备
                     */
                    if ((int)MessageType.A.BIND_DEVICE.value() == recbytes[11]){
                        ParseDeviceData.ParseBindVCPFPData parseBindVCPFPData = new ParseDeviceData.ParseBindVCPFPData();
                        parseBindVCPFPData.parseBytes(recbytes);
                    }
                    /**
                     * MQTT接收上传数据包括传感器数据，设备属性
                     */
                    if (recbytes[11] == MessageType.A.UPLOAD_DEVICE_INFO.value()){
                        ParseDeviceData.ParseAttributeData parseAttributeData = new ParseDeviceData.ParseAttributeData();
                        parseAttributeData.parseBytes(recbytes);
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
                    if ((int) MessageType.A.GET_GW_INFO.value() == recbytes[11]) {
                        ParseDeviceData.ParseGWInfoData gwInfoData = new ParseDeviceData.ParseGWInfoData();
                        gwInfoData.parseBytes(recbytes);
                    }

                    /**
                     * 设备返回的状态
                     */
                    ParseDeviceData.ParseDeviceStateOrLevel pDevStateOrLevel = new ParseDeviceData.ParseDeviceStateOrLevel();
                    pDevStateOrLevel.parseBytes(recbytes);
                    if (pDevStateOrLevel.message_type.contains("8101") & pDevStateOrLevel.clusterID == 6) {
                        DataSources.getInstance().getDeviceState(pDevStateOrLevel.short_address, pDevStateOrLevel.state);
                    }

                    /**
                     * 接受传感器数据并解析
                     */
                    ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
                    sensorData.parseBytes(recbytes);
                    if (sensorData.message_type.contains("8401")) {
                        //当有传感器数据上传时读取ZONETYPE
                        byte[] bt = DeviceCmdData.ReadZoneTypeCmd(sensorData.sensor_mac, sensorData.short_address);
                        Constants.sendMessage(bt);
                        DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
                    }

                    /**
                     * 解析设备属性数据
                     */
                    ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                    attribute.parseBytes(recbytes);

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
                            Constants.sendMessage(zt_bt);
                        }
                    }
                    //--------------------------------------device end--------------------------------------

                    //--------------------------------------group start-------------------------------------
                    /**
                     * 创建group返回的id和name
                     */
                    if ((int) MessageType.A.ADD_GROUP_NAME.value() == recbytes[11]) {
                        //解析添加组返回数据
                        byte[] group_name_bt = Constants.GROUP_GLOBAL.ADD_GROUP_NAME.getBytes("utf-8");
                        ParseGroupData.ParseAddGroupInfo groupInfo = new ParseGroupData.ParseAddGroupInfo();
                        groupInfo.parseBytes(recbytes, group_name_bt.length);
                    }
                    /**
                     * MQTT获取groups
                     */
                    if ((int) MessageType.A.GET_ALL_GROUP.value() == recbytes[11]) {
                        //解析接受到的数据
                        ParseGroupData.ParseGetGroupsInfo(recbytes);
                        return;
                    }
                    /**
                     * MQTT删除group返回的结果
                     */
                    if (recbytes[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                        //解析数据
                        ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
                        parseData.parseBytes(recbytes);
                        DataSources.getInstance().DeleteGroupResult(parseData.group_id);
                    }
                    /**
                     * MQTT修改group返回的数据
                     */
                    if (recbytes[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                        byte[] group_name_bt = NEW_GROUP_NAME.getBytes("utf-8");
                        ParseGroupData.ParseModifyGroupInfo groupInfo = new ParseGroupData.ParseModifyGroupInfo();
                        groupInfo.parseBytes(recbytes, group_name_bt.length);
                        DataSources.getInstance().ChangeGroupName(groupInfo.group_id, groupInfo.group_name);
                    }

                    //-------------------------------------group end----------------------------------------

                    //-------------------------------------scene start--------------------------------------
                    /**
                     * 创建scene返回的id和name
                     */
                    if ((int) MessageType.A.ADD_SCENE_NAME.value() == recbytes[11]) {
                        //解析添加场景返回数据
                        byte[] scene_name = ADD_SCENE_NAME.getBytes("utf-8");
                        ParseSceneData.ParseAddSceneInfo parseAddSceneInfo = new ParseSceneData.ParseAddSceneInfo();
                        parseAddSceneInfo.parseBytes(recbytes, scene_name.length);
                    }

                    /**
                     * MQTT获取scenes
                     */
                    if ((int) MessageType.A.GET_ALL_SCENE.value() == recbytes[11]) {
                        //解析获取sceneinfo
                        ParseSceneData.ParseGetScenesInfo(recbytes);
                        return;
                    }
                    /**
                     * MQTT删除场景返回结果
                     */
                    if (recbytes[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
                        byte btToint = recbytes[32];
                        int i = btToint & 0xFF;
                        DataSources.getInstance().DeleteSences(i);
                    }
                    /**
                     * MQTT修改场景返回数据
                     */
                    if (recbytes[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
                        byte[] bt = NEW_SCENE_NAME.getBytes("utf-8");
                        ParseSceneData.ParseModifySceneInfo parseData = new ParseSceneData.ParseModifySceneInfo();
                        parseData.parseBytes(recbytes, bt.length);
                        DataSources.getInstance().ChangeSencesName(parseData.scene_id, parseData.scene_name);
                    }
                    //------------------------------------scene end-----------------------------------------

                    //------------------------------------task start----------------------------------------
                    /**
                     * MQTT获取tasks
                     */
                    if (recbytes[11] == MessageType.A.GET_ALL_TASK.value()) {
                        ParseTaskData.ParseGetTaskInfo2 parseGetTaskInfo2 = new ParseTaskData.ParseGetTaskInfo2();
                        parseGetTaskInfo2.parseBytes(recbytes);
                    }

                    //------------------------------------task end------------------------------------------
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

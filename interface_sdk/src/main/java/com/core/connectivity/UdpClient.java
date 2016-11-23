package com.core.connectivity;

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
import com.core.mqtt.MqttManager;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
            if (!NetworkUtil.NetWorkType(mContext)) {
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
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
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("设备信息UdpClient = " + Arrays.toString(recbuf));

                    //旧设备
                    if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == recbuf[11]) {
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, false);
                        System.out.println("设备信息_11 = " + recbuf[11]);
                    }
                    //新入网设备
                    if ((int) MessageType.A.UPLOAD_TXT.value() == recbuf[11]) {
                        ParseDeviceData.ParseGetDeviceInfo(recbuf, true);
                        System.out.println("设备信息_11 = " + recbuf[11]);
                    }

                    //接受传感器数据并解析
                    ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
                    sensorData.parseBytes(recbuf);
                    if (sensorData.message_type.contains("8401")) {
                        System.out.println("传感器返回数据stateSDK = " + sensorData.sensor_mac);
                        DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
                    }

                    //解析设备属性数据
                    ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                    attribute.parseBytes(recbuf);
                    if (attribute.message_type.contains("8100")) {
                        System.out.println("返回设备属性Value" + attribute.attribValue);
                        //如果设备属性簇ID等于5则发送保存zonetypecmd
                        if (attribute.clusterID == 5) {
                            byte[] saveZoneType = DeviceCmdData.SaveZoneTypeCmd(attribute.message_type,
                                    attribute.short_address, attribute.endpoint + "",
                                    (short) attribute.attribValue);
                            Constants.sendMessage(saveZoneType);
                        }

                        if (attribute.message_type.contains("8100") & attribute.clusterID == 6) {
                            DataSources.getInstance().getDeviceState(attribute.message_type, attribute.attribValue);
                        }

                        if (attribute.message_type.contains("8100") & attribute.clusterID == 8) {
                            DataSources.getInstance().getDeviceLevel(attribute.message_type, attribute.attribValue);
                        }
                    }

                    //-------------------------房间start-----------------------------
                    //创建房间名称时返回的ID
                    if ((int) MessageType.A.ADD_GROUP_NAME.value() == recbuf[11]) {
                        ParseGroupData.ParseAddGroupBack(recbuf, Constants.GROUP_GLOBAL.ADD_GROUP_NAME.length());
                        System.out.println("设备信息_11 = " + recbuf[11]);
                    }
                    //获取到的放间
                    if ((int) MessageType.A.GET_ALL_GROUP.value() == recbuf[11]){
                        //解析接受到的数据
                        ParseGroupData.ParseGetGroupsInfo(recbuf);
                        System.out.println("设备信息_11 = " + recbuf[11]);
                    }

                    //删除房间的返回值
                    ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
                    parseData.parseBytes(recbuf);
                    if (parseData.group_id == 0) {
                        return;
                    }
                    DataSources.getInstance().DeleteGroupResult(parseData.group_id);

                    //解析修改房间返回值
                    String str = FtFormatTransfer.bytesToUTF8String(recbuf);
                    int strToint = str.indexOf(":");
                    String isGroup = "";
                    if (strToint >= 0) {
                        isGroup = str.substring(strToint - 4, strToint);
                        Log.i("isGroup = ", isGroup);
                    }
                    if (str.contains("GW") && !str.contains("K64") && isGroup.contains("upId")) {
                        String[] group_data = str.split(",");
                        short group_id = -1;
                        String group_name = "";
                        for (int i = 1; i < group_data.length; i++) {
                            if (group_data.length <= 2) {
                                return;
                        }

                            String[] Id_Source = group_data[0].split(":");
                            String[] Name_Source = group_data[1].split(":");

                            if (Id_Source.length > 1 && Name_Source.length > 1) {
                                group_name = Utils.toStringHex(Name_Source[1]);
                            }
                        }
                        DataSources.getInstance().ChangeGroupName(group_id, group_name);
                    }
                    //---------------------房间end------------------------
                    //---------------------场景start----------------------
                    if ((int) MessageType.A.ADD_SCENE_NAME.value() == recbuf[11]) {
                        ParseSceneData.ParseAddSceneBackInfo(recbuf, Constants.SCENE_GLOBAL.ADD_SCENE_NAME.length());
                    }
                    //解析获取场景信息
                    if ((int) MessageType.A.GET_ALL_SCENE.value() == recbuf[11]) {
                        ParseSceneData.ParseGetScenesInfo(recbuf);
                        System.out.println("Scene_out2 = " + recbuf[11]);
                    }

                    //解析修改场景返回数据
                    ParseSceneData.ParseModifySceneInfo modifySceneInfo = new ParseSceneData.ParseModifySceneInfo();
                    modifySceneInfo.parseBytes(recbuf, Constants.SCENE_GLOBAL.NEW_SCENE_NAME.length());
                    if (modifySceneInfo.scene_name.equalsIgnoreCase(Constants.SCENE_GLOBAL.NEW_SCENE_NAME)) {
                        DataSources.getInstance().ChangeSencesName(modifySceneInfo.scene_id, modifySceneInfo.scene_name);
                    }
                    //------------------------场景end--------------------------
                    //------------------------任务start------------------------
                    //解析获取到的任务信息
                    ParseTaskData.ParseGetTaskInfo parseGetTaskInfo = new ParseTaskData.ParseGetTaskInfo();
                    parseGetTaskInfo.parseBytes(recbuf);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] bt_send, DatagramSocket socket) throws IOException {
        InetAddress address = InetAddress.getByName(Constants.GW_IP_ADDRESS);
        DatagramPacket dp = new DatagramPacket(bt_send, bt_send.length, address, Constants.UDP_PORT);
        socket.send(dp);
        System.out.println("parseIEEEData.mDevMac = " + Utils.binary(bt_send, 16));
        byte[] bs = new byte[1024];
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        socket.receive(packet);
        System.out.println("getMessage = " + Arrays.toString(bs));
    }

//    public void identify_data(byte[] bt_send){
//        switch (bt_send[11]) {
//            case 0:
//
//                break;
//            case 1:
//
//                break;
//            case 2:
//
//                break;
//            case 3:
//
//                break;
//            case 4:
//
//                break;
//            case 5:
//
//                break;
//            case 6:
//
//                break;
//            case 7:
//
//                break;
//            case 8://??????
//                ParseDeviceData.ParseAttributeData parseAttributeData = new ParseDeviceData.ParseAttributeData();
//                parseAttributeData.parseBytes(bt_send);
//
//                if (parseAttributeData.zigbee_type.contains("8100") & parseAttributeData.clusterID == 6) {
//                    DataSources.getInstance().getDeviceState(parseAttributeData.dev_mac, parseAttributeData.attribValue);
//                }
//
//                if (parseAttributeData.zigbee_type.contains("8100") & parseAttributeData.clusterID == 8) {
//                    DataSources.getInstance().getDeviceLevel(parseAttributeData.dev_mac, parseAttributeData.attribValue);
//                }
//                break;
//            case 9:
//
//                break;
//            case 10://???????
//                ParseDeviceData.ParseGetDeviceInfo(bt_send, true);
//                break;
//            case 11://?????
//                ParseDeviceData.ParseGetDeviceInfo(bt_send, false);
//                System.out.println("设备信息 = " + Arrays.toString(bt_send));
//                break;
//            case 12:
//
//                break;
//            case 13:
//
//                break;
//            case 14:
//
//                break;
//            case 15://????????ID
//                ParseGroupData.ParseAddGroupBack(bt_send, Constants.GROUP_GLOBAL.ADD_GROUP_NAME.length());
//                break;
//            case 16:
//
//                break;
//            case 17://??????????
//                ParseGroupData.ParseGetGroupsInfo(bt_send);
//                break;
//            case 18://?????????ID
//                ParseSceneData.ParseAddSceneBackInfo(bt_send, Constants.SCENE_GLOBAL.ADD_SCENE_NAME.length());
//                break;
//            case 19:
//
//                break;
//            case 20://??????????
//                ParseSceneData.ParseGetScenesInfo(bt_send);
//                break;
//            case 21:
//
//                break;
//            case 22:
//
//                break;
//            case 23:
//
//                break;
//            case 24:
//
//                break;
//            case 25:
//
//                break;
//            case 26:
//
//                break;
//            case 27:
//
//                break;
//            case 28:
////                ParseDeviceData.ParseIEEEData parseIEEEData = new ParseDeviceData.ParseIEEEData();
////                parseIEEEData.parseBytes(recbuf);
////
////                byte[] bt = DeviceCmdData.BindDeviceCmd(appDevice,parseIEEEData.gateway_mac);
////                sendMessage(bt, socket);
//                break;
//            case 29:
//
//                break;
//            case 30:
//
//                break;
//            case 31:
//
//                break;
//        }
//    }
}

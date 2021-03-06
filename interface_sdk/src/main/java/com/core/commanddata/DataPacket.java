package com.core.commanddata;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.appdata.GatewayCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.commanddata.gwdata.ParseGatewayData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.entity.AppGateway;
import com.core.gatewayinterface.DataSources;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.MessageType;
import com.core.utils.Utils;
import com.interfacecallback.BuildConfig;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.core.global.Constants.GROUP_GLOBAL.ADD_GROUP_NAME;
import static com.core.global.Constants.GROUP_GLOBAL.NEW_GROUP_NAME;
import static com.core.global.Constants.GatewayInfo.COUNT;
import static com.core.global.Constants.GatewayInfo.GATEWAY_UPDATE_FILE_NEXT;
import static com.core.global.Constants.GatewayInfo.PACKETS;
import static com.core.global.Constants.GatewayInfo.SEND_SIZE;
import static com.core.global.Constants.GatewayInfo.UPDATE_FILE_BT;
import static com.core.global.Constants.SCENE_GLOBAL.ADD_SCENE_NAME;
import static com.core.global.Constants.SCENE_GLOBAL.NEW_SCENE_NAME;
import static com.core.global.Constants.isScanGwNodeVer;

/**
 * Created by best on 2016/12/8.
 */

public class DataPacket {
    public Context context;
    public static DataPacket dataPacket = null;
    public ParseGatewayData.ParseUpdateCountData startUpdateData;

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

    public void BytesDataPacket(final Context context, byte[] bytes) {
        this.context = context;
        try {
            //-----------------------------网关相关--------------------------------32
            if ((int) MessageType.A.FACTORY_RESET.value() == bytes[11]) {
                DataSources.getInstance().ResetGatewayResult(bytes[32]);
            }

            /**
             * 解析网关信息
             */
            if ((int) MessageType.A.GET_GW_INFO.value() == bytes[11]) {
                ParseGatewayData.ParseGWInfoData gwInfoData = new ParseGatewayData.ParseGWInfoData();
                gwInfoData.parseBytes(bytes);
            }

            /**
             * 解析服务器地址
             */
            if ((int) MessageType.A.GET_SET_SERVER_ADDRESS.value() == bytes[11]) {
                ParseGatewayData.ParseServerAddress parseAddress = new ParseGatewayData.ParseServerAddress();
                parseAddress.parseBytes(bytes);
                DataSources.getInstance().GatewayServerAddress(parseAddress.server_address);
            }

            /**
             * 获取网关IEEE地址然后绑定设备
             */
            if ((int) MessageType.A.GET_GATEWAY_IEEE.value() == bytes[11]) {
                ParseGatewayData.ParseIEEEData parseIEEEData = new ParseGatewayData.ParseIEEEData();
                parseIEEEData.parseBytes(bytes);

                if (context == null){
                    System.out.println("上下文Context为空");
                    return;
                }
                GatewayInfo.getInstance().setGwIEEEAddress(context, parseIEEEData.gateway_mac);
            }

            if ((int) MessageType.A.GET_GW_INFO.value() == bytes[11]) {
                ParseGatewayData.ParseGWInfoData gwInfoData = new ParseGatewayData.ParseGWInfoData();
                gwInfoData.parseBytes(bytes);

                if (context == null){
                    System.out.println("上下文Context为空");
                    return;
                }

                GatewayInfo.getInstance().setFirmwareVersion(context, gwInfoData.gw_version);
                GatewayInfo.getInstance().setGatewayMac(context, gwInfoData.gw_mac);
                GatewayInfo.getInstance().setBootrodr(context, gwInfoData.gw_bootrodr);

                Thread.sleep(1500);
                SerialHandler.getInstance().GetCoordinatorVersion();
            }
            if ((int) MessageType.A.GET_NODE_VER.value() == bytes[11]) {
                ParseGatewayData.ParseNodeVer parseNodeVer = new ParseGatewayData.ParseNodeVer();
                parseNodeVer.parseBytes(bytes);

                if (context == null){
                    System.out.println("上下文Context为空");
                    return;
                }
                String ip_address = GatewayInfo.getInstance().getInetAddress(context);
                String gateway_no = GatewayInfo.getInstance().getGatewayNo(context);
                String gateway_mac = GatewayInfo.getInstance().getGatewayMac(context);
                int gateway_version = GatewayInfo.getInstance().getFirmwareVersion(context);
                int gateway_bootrodr = GatewayInfo.getInstance().getBootrodr(context);

                AppGateway appGateway = new AppGateway();
                appGateway.setGateway_no(gateway_no);
                appGateway.setGateway_mac(gateway_mac);
                appGateway.setBootrodr(gateway_bootrodr);
                appGateway.setIp_address(ip_address);
                appGateway.setGateway_version(gateway_version);
                appGateway.setNode_main_version(parseNodeVer.major_ver);
                appGateway.setNode_installed_version(parseNodeVer.Install_ver);
                DataSources.getInstance().GatewayInfo(appGateway);
                isScanGwNodeVer = true;
            }

            /**
             * 启动下载返回数据包的大小
             */
            if ((int) MessageType.A.START_UPDATE_GW_VER.value() == bytes[11]) {
                ParseGatewayData.ParseStartUpdateData startUpdateData = new ParseGatewayData.ParseStartUpdateData();
                startUpdateData.parseBytes(bytes);
                GatewayInfo.getInstance().setPacketSize(context, startUpdateData.update_size);
            }

            /**
             * 发送文件返回数据
             */
            if ((int) MessageType.A.SEND_UPDATE_FILE_TO_GATEWAY.value() == bytes[11]) {
                startUpdateData = new ParseGatewayData.ParseUpdateCountData();
                startUpdateData.parseBytes(bytes);
                GATEWAY_UPDATE_FILE_NEXT = startUpdateData.packet_count;
                System.out.println("GATEWAY_UPDATE_FILE_NEXT = " + GATEWAY_UPDATE_FILE_NEXT);
                if (PACKETS == GATEWAY_UPDATE_FILE_NEXT) {
                    String crc32 = GatewayInfo.getInstance().getGatewayUpdateCRC32(context);
                    byte[] bt = GatewayCmdData.FinallyUpdateCmd(crc32);
                    new Thread(new UdpClient(context, bt)).start();
                    System.out.println("最后一包确定更新");
                }
            }

            //-----------------------------设备start-------------------------------

            /**
             * 获取网关所有设备及其新入网设备
             */
            if ((int) MessageType.A.UPLOAD_ALL_TXT.value() == bytes[11]) {
//                        byte[] bt = AESUtils.decode(recbuf);
                ParseDeviceData.ParseGetDeviceInfo(context, bytes, false);
            } else if ((int) MessageType.A.UPLOAD_TXT.value() == bytes[11]) {//新入网设备
//                        byte[] bt = AESUtils.decode(recbuf);
                ParseDeviceData.ParseGetDeviceInfo(context, bytes, true);
            }

            /**
             * 设备返回的状态
             */
            if ((int) MessageType.A.RETURN_DEVICE_STATS.value() == bytes[11]) {
                ParseDeviceData.ParseDeviceStateOrLevel stateOrLevel = new ParseDeviceData.ParseDeviceStateOrLevel();
                stateOrLevel.parseBytes(bytes);
                if (stateOrLevel.clusterID == 6) {
                    DataSources.getInstance().getDeviceState(stateOrLevel.short_address, stateOrLevel.state);
                } else if (stateOrLevel.clusterID == 8) {
//                    DataSources.getInstance().getDeviceLevel(stateOrLevel.short_address, stateOrLevel.level);
                }
            }

            /**
             * 设备上传的数据
             */
            if (bytes[11] == MessageType.A.UPLOAD_DEVICE_INFO.value()) {
                /**
                 * 接受传感器数据并解析
                 */
                ParseDeviceData.ParseSensorData sensorData = new ParseDeviceData.ParseSensorData();
                sensorData.parseBytes(bytes);
                if (sensorData.message_type.contains("8401")) {
                    //当有传感器数据上传时读取ZONETYPE
                    byte[] bt = DeviceCmdData.ReadZoneTypeCmd(sensorData.sensor_mac, sensorData.short_address);
                    new Thread(new UdpClient(context, bt)).start();
//                    DataSources.getInstance().getReceiveSensor(sensorData.sensor_mac, sensorData.state);
                    DataSources.getInstance().getReceiveSensor(sensorData.short_address, sensorData.state);
                }

                /**
                 * 解析设备属性数据
                 */
                ParseDeviceData.ParseAttributeData attribute = new ParseDeviceData.ParseAttributeData();
                attribute.parseBytes(context, bytes);

                /**
                 * 传感器电量返回值
                 */
                if (attribute.message_type.contains("8102")) {
                    DataSources.getInstance().responseBatteryValue(attribute.device_mac, attribute.attribValue);
                }

//                if (attribute.message_type.contains("8100")) {
//                    //如果设备属性簇ID等于5则发送保存zonetypecmd
//                    if (attribute.clusterID == 5 & attribute.u8AttribType == 0x31) {
//                        byte[] zt_bt = DeviceCmdData.SaveZoneTypeCmd(attribute.message_type, attribute.short_address,
//                                attribute.endpoint + "", (short) attribute.attribValue);
//                        new Thread(new UdpClient(context, zt_bt)).start();
////                    Constants.sendMessage(zt_bt);
//                    }
//                }
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

            /**
             * 绑定设备返回数据
             */
//            if ((int) MessageType.A.BIND_DEVICE.value() == bytes[11]) {
//                ParseDeviceData.ParseBindVCPFPData parseBindVCPFPData = new ParseDeviceData.ParseBindVCPFPData();
//                parseBindVCPFPData.parseBytes(bytes);
//            }
            /**
             * 智能插座数据
             */
            if ((int) MessageType.A.SMART_SOKET_DATA.value() == bytes[11]) {
                if (bytes.length < 60) {
                    return;
                }
                String time = String.valueOf(System.currentTimeMillis());
                ParseDeviceData.ParseBindData parsePData = new ParseDeviceData.ParseBindData();
                parsePData.parseBytes(bytes);
            }

            //-------------------------------设备end--------------------------------
            //-------------------------------场景start------------------------------
            /**
             * 枚举A判断是否是场景（场景列表）
             */
            if ((int) MessageType.A.GET_ALL_SCENE.value() == bytes[11]) {
                ParseSceneData.ParseGetScenesInfo(bytes);
            }

            /**
             * 添加场景名称
             */
            if ((int) MessageType.A.ADD_SCENE_NAME.value() == bytes[11]) {
                byte[] scene_name = ADD_SCENE_NAME.getBytes("utf-8");
                ParseSceneData.ParseAddSceneInfo parseAddSceneInfo = new ParseSceneData.ParseAddSceneInfo();
                parseAddSceneInfo.parseBytes(bytes, scene_name.length);
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
             * 枚举A判断是否是房间（获取所有房间列表）
             */
            if ((int) MessageType.A.GET_ALL_GROUP.value() == bytes[11]){
                ParseGroupData.ParseGetGroupsInfo(bytes);
            }
            /**
             * 创建房间名称
             */
            if ((int) MessageType.A.ADD_GROUP_NAME.value() == bytes[11]) {
                byte[] group_name_bt = ADD_GROUP_NAME.getBytes("utf-8");
                ParseGroupData.ParseAddGroupInfo groupInfo = new ParseGroupData.ParseAddGroupInfo();
                groupInfo.parseBytes(bytes, group_name_bt.length);
            }
            /**
             * 修改房间名称
             */
            if (bytes[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                byte[] group_name_bt = NEW_GROUP_NAME.getBytes("utf-8");
                ParseGroupData.ParseModifyGroupInfo groupInfo = new ParseGroupData.ParseModifyGroupInfo();
                groupInfo.parseBytes(bytes, group_name_bt.length);
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
            //----------------------------任务start---------------------------------
            /**
             * 枚举A判断是否是任务列表
             */
            if (bytes[11] == MessageType.A.GET_ALL_TASK.value()) {
                ParseTaskData.ParseGetTaskInfo2 parseGetTaskInfo2 = new ParseTaskData.ParseGetTaskInfo2();
                parseGetTaskInfo2.parseBytes(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

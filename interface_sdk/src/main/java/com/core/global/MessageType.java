package com.core.global;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;
import static com.core.global.Constants.MessageType.MAX_VALUE_U_INT_8;

/**
 * Created by best on 2016/9/27.
 */

public class MessageType {

    public enum A {
        CHECK_NEW_DEVICE((byte) 0x00),
        DELETE_DEVICE((byte) 0x01),
        CONTROL_DEVICE((byte) 0x02),
        ADD_SCENE((byte) 0x03),
        DELETE_SCENE((byte) 0x04),
        CONTROL_SCENE((byte) 0x05),
        ADD_IFTT((byte) 0x06),
        DELETE_IFTT((byte) 0x07),
        UPLOAD_DEVICE_INFO((byte) 0x08),
        STORE_TXT((byte) 0x09),
        UPLOAD_TXT((byte) 0x0A),
        UPLOAD_ALL_TXT((byte) 0x0B),

        ADD_DEVICE_TO_GROUP((byte) 0x0C),
        GET_GROUP_SINGLE((byte) 0x0D),
        REMOVE_GROUP((byte) 0x0E),
        ADD_GROUP_NAME((byte) 0x0F),
        CHANGE_GROUP_NAME((byte) 0x10),
        GET_ALL_GROUP((byte) 0x11),
        ADD_SCENE_NAME((byte) 0x12),
        CHANGE_SCENE_NAME((byte) 0x13),
        GET_ALL_SCENE((byte) 0x14),
        CHANGE_DEVICE_NAME((byte) 0x15),
        SAVE_ZONE_TYPE((byte) 0x16),
        SET_GETEWAY_TIME((byte) 0x17),
        CREATE_AND_EDITTASK((byte) 0x18),
        DELETE_TASK((byte) 0x19),
        CHECK_TASK((byte) 0x1A),
        GET_ALL_TASK((byte) 0x1B),
        GET_GATEWAY_IEEE((byte)0x1C),
        BIND_DEVICE((byte)0x1D),
        ADD_DEVICE_FORM_GROUP_FILE((byte)0x20),
        DELETE_DEVICE_FORM_GROUP_FILE((byte)0x21),
        ADD_DEVICE_FORM_SCENE_FILE((byte)0x22),
        DELETE_DEVICE_FORM_SCENE_FILE((byte)0x23),
        GET_NODE_VER((byte)0x40),
        GET_GW_INFO((byte)0x80),
        FACTORY_RESET((byte)0x82);

        private byte value = 0;

        A(byte value) {    //    必须是private的或null，否则编译错误
            this.value = value;
        }

        public static A valueOf(byte value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0x00:
                    return CHECK_NEW_DEVICE;
                case 0x01:
                    return DELETE_DEVICE;
                default:
                    return null;
            }
        }

        public byte value() {
            return this.value;
        }
    }


    public enum B {
        E_SL_MSG_DEFAULT((short)0xFFFF),
        E_SL_MSG_DATA_INDICATION((short) 0x8002),
        E_SL_MSG_SET_EXT_PANID((short) 0x0020),

        E_SL_MSG_SET_CHANNELMASK((short) 0x0021),
        E_SL_MSG_SET_SECURITY((short) 0x0022),
        E_SL_MSG_SET_DEVICETYPE((short) 0x0023),


        E_SL_MSG_START_NETWORK((short) 0x0024),
        E_SL_MSG_START_SCAN((short) 0x0025),
        E_SL_MSG_NETWORK_JOINED_FORMED((short) 0x8024),
        E_SL_MSG_NETWORK_REMOVE_DEVICE((short) 0x0026),
        E_SL_MSG_NETWORK_WHITELIST_ENABLE((short) 0x0027),
        E_SL_MSG_ADD_AUTHENTICATE_DEVICE((short) 0x0028),
        E_SL_MSG_AUTHENTICATE_DEVICE_RESPONSE((short) 0x8028),

        E_SL_MSG_RESET((short) 0x0011),
        E_SL_MSG_ERASE_PERSISTENT_DATA((short) 0x0012),
        E_SL_MSG_ZLL_FACTORY_NEW((short) 0x0013),
        E_SL_MSG_GET_PERMIT_JOIN((short) 0x0014),
        E_SL_MSG_GET_PERMIT_JOIN_RESPONSE((short) 0x8014),
        E_SL_MSG_BIND((short) 0x0030),
        E_SL_MSG_BIND_RESPONSE((short) 0x8030),
        E_SL_MSG_UNBIND((short) 0x0031),
        E_SL_MSG_UNBIND_RESPONSE((short) 0x8031),
        E_SL_MSG_BIND_GROUP((short) 0x0032),
        E_SL_MSG_BIND_GROUP_RESPONSE((short) 0x8032),
        E_SL_MSG_UNBIND_GROUP((short) 0x0033),
        E_SL_MSG_UNBIND_GROUP_RESPONSE((short) 0x8033),
        E_SL_MSG_GET_NODE_VER((short) 0x8010),

        E_SL_MSG_NETWORK_ADDRESS_REQUEST((short) 0x0040),
        E_SL_MSG_NETWORK_ADDRESS_RESPONSE((short) 0x8040),
        E_SL_MSG_IEEE_ADDRESS_REQUEST((short) 0x0041),
        E_SL_MSG_IEEE_ADDRESS_RESPONSE((short) 0x8041),
        E_SL_MSG_NODE_DESCRIPTOR_REQUEST((short) 0x0042),
        E_SL_MSG_NODE_DESCRIPTOR_RESPONSE((short) 0x8042),
        E_SL_MSG_SIMPLE_DESCRIPTOR_REQUEST((short) 0x0043),
        E_SL_MSG_SIMPLE_DESCRIPTOR_RESPONSE((short) 0x8043),
        E_SL_MSG_POWER_DESCRIPTOR_REQUEST((short) 0x0044),
        E_SL_MSG_POWER_DESCRIPTOR_RESPONSE((short) 0x8044),
        E_SL_MSG_ACTIVE_ENDPOINT_REQUEST((short) 0x0045),
        E_SL_MSG_ACTIVE_ENDPOINT_RESPONSE((short) 0x8045),
        E_SL_MSG_MATCH_DESCRIPTOR_REQUEST((short) 0x0046),
        E_SL_MSG_MATCH_DESCRIPTOR_RESPONSE((short) 0x8046),
        E_SL_MSG_MANAGEMENT_LEAVE_REQUEST((short) 0x0047),
        E_SL_MSG_MANAGEMENT_LEAVE_RESPONSE((short) 0x8047),
        E_SL_MSG_LEAVE_INDICATION((short) 0x8048),
        E_SL_MSG_PERMIT_JOINING_REQUEST((short) 0x0049),
        E_SL_MSG_MANAGEMENT_NETWORK_UPDATE_REQUEST((short) 0x004A),
        E_SL_MSG_MANAGEMENT_NETWORK_UPDATE_RESPONSE((short) 0x804A),
        E_SL_MSG_SYSTEM_SERVER_DISCOVERY((short) 0x004B),
        E_SL_MSG_SYSTEM_SERVER_DISCOVERY_RESPONSE((short) 0x804B),
        E_SL_MSG_LEAVE_REQUEST((short) 0x004C),
        E_SL_MSG_APS_DEVICE_LEFT((short) 0x804C),
        E_SL_MSG_DEVICE_ANNOUNCE((short) 0x004D),
        E_SL_MSG_MANAGEMENT_LQI_REQUEST((short) 0x004E),
        E_SL_MSG_MANAGEMENT_LQI_RESPONSE((short) 0x804E),
        E_SL_MSG_MANY_TO_ONE_ROUTE_REQUEST((short) 0x004F),

        /* Group Cluster */
        E_SL_MSG_ADD_GROUP((short) 0x0060),
        E_SL_MSG_ADD_GROUP_RESPONSE((short) 0x8060),
        E_SL_MSG_VIEW_GROUP((short) 0x0061),
        E_SL_MSG_VIEW_GROUP_RESPONSE((short) 0x8061),
        E_SL_MSG_GET_GROUP_MEMBERSHIP((short) 0x0062),
        E_SL_MSG_GET_GROUP_MEMBERSHIP_RESPONSE((short) 0x8062),
        E_SL_MSG_REMOVE_GROUP((short) 0x0063),
        E_SL_MSG_REMOVE_GROUP_RESPONSE((short) 0x8063),
        E_SL_MSG_REMOVE_ALL_GROUPS((short) 0x0064),
        E_SL_MSG_ADD_GROUP_IF_IDENTIFY((short) 0x0065),

        /* Identify Cluster */
        E_SL_MSG_IDENTIFY_SEND((short) 0x0070),
        E_SL_MSG_IDENTIFY_QUERY((short) 0x0071),

        /* Level Cluster */
        E_SL_MSG_MOVE_TO_LEVEL((short) 0x0080),
        E_SL_MSG_MOVE_TO_LEVEL_ONOFF((short) 0x0081),
        E_SL_MSG_MOVE_STEP((short) 0x0082),
        E_SL_MSG_MOVE_STOP_MOVE((short) 0x0083),
        E_SL_MSG_MOVE_STOP_ONOFF((short) 0x0084),

        /* Scenes Cluster */
        E_SL_MSG_VIEW_SCENE((short) 0x00A0),
        E_SL_MSG_VIEW_SCENE_RESPONSE((short) 0x80A0),
        E_SL_MSG_ADD_SCENE((short) 0x00A1),
        E_SL_MSG_ADD_SCENE_RESPONSE((short) 0x80A1),
        E_SL_MSG_REMOVE_SCENE((short) 0x00A2),
        E_SL_MSG_REMOVE_SCENE_RESPONSE((short) 0x80A2),
        E_SL_MSG_REMOVE_ALL_SCENES((short) 0x00A3),
        E_SL_MSG_REMOVE_ALL_SCENES_RESPONSE((short) 0x80A3),
        E_SL_MSG_STORE_SCENE((short) 0x00A4),
        E_SL_MSG_STORE_SCENE_RESPONSE((short) 0x80A4),
        E_SL_MSG_RECALL_SCENE((short) 0x00A5),
        E_SL_MSG_SCENE_MEMBERSHIP_REQUEST((short) 0x00A6),
        E_SL_MSG_SCENE_MEMBERSHIP_RESPONSE((short) 0x80A6),

        /* Colour Cluster */
        E_SL_MSG_MOVE_TO_HUE((short) 0x00B0),
        E_SL_MSG_MOVE_HUE((short) 0x00B1),
        E_SL_MSG_STEP_HUE((short) 0x00B2),
        E_SL_MSG_MOVE_TO_SATURATION((short) 0x00B3),
        E_SL_MSG_MOVE_SATURATION((short) 0x00B4),
        E_SL_MSG_STEP_SATURATION((short) 0x00B5),
        E_SL_MSG_MOVE_TO_HUE_SATURATION((short) 0x00B6),
        E_SL_MSG_MOVE_TO_COLOUR((short) 0x00B7),
        E_SL_MSG_MOVE_COLOUR((short) 0x00B8),
        E_SL_MSG_STEP_COLOUR((short) 0x00B9),

        /* ZLL Commands */
        /* Touchlink */
        E_SL_MSG_INITIATE_TOUCHLINK((short) 0x00D0),
        E_SL_MSG_TOUCHLINK_STATUS((short) 0x00D1),
        E_SL_MSG_TOUCHLINK_FACTORY_RESET((short) 0x00D2),
        /* Identify Cluster */
        E_SL_MSG_IDENTIFY_TRIGGER_EFFECT((short) 0x00E0),

        /* On/Off Cluster */
        E_SL_MSG_ONOFF_NOEFFECTS((short) 0x0092),
        E_SL_MSG_ONOFF_TIMED((short) 0x0093),
        E_SL_MSG_ONOFF_EFFECTS((short) 0x0094),
        E_SL_MSG_ONOFF_UPDATE((short) 0x8095),

        /* Scenes Cluster */
        E_SL_MSG_ADD_ENHANCED_SCENE((short) 0x00A7),
        E_SL_MSG_VIEW_ENHANCED_SCENE((short) 0x00A8),
        E_SL_MSG_COPY_SCENE((short) 0x00A9),

        /* Colour Cluster */
        E_SL_MSG_ENHANCED_MOVE_TO_HUE((short) 0x00BA),
        E_SL_MSG_ENHANCED_MOVE_HUE((short) 0x00BB),
        E_SL_MSG_ENHANCED_STEP_HUE((short) 0x00BC),
        E_SL_MSG_ENHANCED_MOVE_TO_HUE_SATURATION((short) 0x00BD),
        E_SL_MSG_COLOUR_LOOP_SET((short) 0x00BE),
        E_SL_MSG_STOP_MOVE_STEP((short) 0x00BF),
        E_SL_MSG_MOVE_TO_COLOUR_TEMPERATURE((short) 0x00C0),
        E_SL_MSG_MOVE_COLOUR_TEMPERATURE((short) 0x00C1),
        E_SL_MSG_STEP_COLOUR_TEMPERATURE((short) 0x00C2),

        /* Door Lock Cluster */
        E_SL_MSG_LOCK_UNLOCK_DOOR((short) 0x00F0),

        /* ZHA Commands */
        E_SL_MSG_READ_ATTRIBUTE_REQUEST((short) 0x0100),
        E_SL_MSG_READ_ATTRIBUTE_RESPONSE((short) 0x8100),
        E_SL_MSG_DEFAULT_RESPONSE((short) 0x8101),
        E_SL_MSG_REPORT_IND_ATTR_RESPONSE((short) 0x8102),
        E_SL_MSG_WRITE_ATTRIBUTE_REQUEST((short) 0x0110),
        E_SL_MSG_WRITE_ATTRIBUTE_RESPONSE((short) 0x8110),
        E_SL_MSG_CONFIG_REPORTING_REQUEST((short) 0x0120),
        E_SL_MSG_CONFIG_REPORTING_RESPONSE((short) 0x8120),
        E_SL_MSG_REPORT_ATTRIBUTES((short) 0x8121),
        E_SL_MSG_READ_REPORT_CONFIG_REQUEST((short) 0x0122),
        E_SL_MSG_READ_REPORT_CONFIG_RESPONSE((short) 0x8122),
        E_SL_MSG_ATTRIBUTE_DISCOVERY_REQUEST((short) 0x0140),
        E_SL_MSG_ATTRIBUTE_DISCOVERY_RESPONSE((short) 0x8140),
        E_SL_MSG_ATTRIBUTE_EXT_DISCOVERY_REQUEST((short) 0x0141),
        E_SL_MSG_ATTRIBUTE_EXT_DISCOVERY_RESPONSE((short) 0x8141),
        E_SL_MSG_COMMAND_RECEIVED_DISCOVERY_REQUEST((short) 0x0150),
        E_SL_MSG_COMMAND_RECEIVED_DISCOVERY_INDIVIDUAL_RESPONSE((short) 0x8150),
        E_SL_MSG_COMMAND_RECEIVED_DISCOVERY_RESPONSE((short) 0x8151),
        E_SL_MSG_COMMAND_GENERATED_DISCOVERY_REQUEST((short) 0x0160),
        E_SL_MSG_COMMAND_GENERATED_DISCOVERY_INDIVIDUAL_RESPONSE((short) 0x8160),
        E_SL_MSG_COMMAND_GENERATED_DISCOVERY_RESPONSE((short) 0x8161),

        E_SL_MSG_SAVE_PDM_RECORD((short) 0x0200),
        E_SL_MSG_SAVE_PDM_RECORD_RESPONSE((short) 0x8200),
        E_SL_MSG_LOAD_PDM_RECORD_REQUEST((short) 0x0201),
        E_SL_MSG_LOAD_PDM_RECORD_RESPONSE((short) 0x8201),
        E_SL_MSG_DELETE_PDM_RECORD((short) 0x0202),

        E_SL_MSG_PDM_HOST_AVAILABLE((short) 0x0300),
        E_SL_MSG_ASC_LOG_MSG((short) 0x0301),
        E_SL_MSG_ASC_LOG_MSG_RESPONSE((short) 0x8301),
        E_SL_MSG_PDM_HOST_AVAILABLE_RESPONSE((short) 0x8300),
        /* IAS Cluster */
        E_SL_MSG_SEND_IAS_ZONE_ENROLL_RSP((short) 0x0400),
        E_SL_MSG_IAS_ZONE_STATUS_CHANGE_NOTIFY((short) 0x8401),

        /* OTA Cluster */
        E_SL_MSG_LOAD_NEW_IMAGE((short) 0x0500),
        E_SL_MSG_BLOCK_REQUEST((short) 0x8501),
        E_SL_MSG_BLOCK_SEND((short) 0x0502),
        E_SL_MSG_UPGRADE_END_REQUEST((short) 0x8503),
        E_SL_MSG_UPGRADE_END_RESPONSE((short) 0x0504),
        E_SL_MSG_IMAGE_NOTIFY((short) 0x0505),

        E_SL_MSG_ROUTE_DISCOVERY_CONFIRM((short) 0x8701),
        E_SL_MSG_APS_DATA_CONFIRM_FAILED((short) 0x8702);


        private short value = 0;

        B(short value) {    //    必须是private的或null，否则编译错误
            this.value = value;
        }

        public static A valueOf(short value) {    //    手写的从int到enum的转换函数
            switch (value) {
//                case (short) 0x8002:
//                    return E_SL_MSG_DATA_INDICATION;
//                case (short) 0x0020:
//                    return E_SL_MSG_SET_EXT_PANID;
                default:
                    return null;
            }
        }

        public short value() {
            return this.value;
        }
    }
}

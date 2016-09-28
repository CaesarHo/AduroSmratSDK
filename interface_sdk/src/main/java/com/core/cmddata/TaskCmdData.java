package com.core.cmddata;

import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.utils.CRC8;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by best on 2016/9/23.
 */

public class TaskCmdData {
    /**
     * 创建任务
     * @throws UnsupportedEncodingException
     */
    public static byte[] EditTask(String taskname, byte isRun, byte task_type, byte task_cycle, int hour, int minute, int device_action,
                                    String action_mac, String device_mac, String device_shortaddr, String device_main_point, int cmd_size,
                                    String dev_switch, int switch_state, String dev_level, int level_value, String dev_hue, int hue_value,
                                    int sat_value, String dev_temp, int temp_value, String recall_scene, int group_id, int scene_id) throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = TaskCmdData.tCmdData(device_mac, device_shortaddr, device_main_point, dev_switch,
                switch_state, dev_level, level_value, dev_hue, hue_value, sat_value, dev_temp, temp_value,
                recall_scene, group_id, scene_id);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = task_type;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = task_cycle;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = (byte) hour;//任务小时-15点
        commandDataByte[6] = (byte) minute;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001
        if (task_type == 1 | task_type == 2) {
            commandDataByte[7] = (byte) 0x80;
            commandDataByte[8] = 0x01;
            commandDataByte[9] = 0x00;
            commandDataByte[10] = 0x00;
            commandDataByte[11] = 0x11;
            commandDataByte[12] = 0x22;
            commandDataByte[13] = 0x33;
            commandDataByte[14] = 0x44;
            commandDataByte[15] = 0x55;
            commandDataByte[16] = 0x66;
            commandDataByte[17] = 0x77;
            commandDataByte[18] = (byte) 0x88;
        } else {
            commandDataByte[7] = (byte) 0x84;//串口数据类型
            commandDataByte[8] = 0x01;//串口数据类型
            commandDataByte[9] = (byte) (device_action << 8);//设备动作
            commandDataByte[10] = (byte) device_action;//设备动作
            //产生动作的设备mac开始   00158d0001310e1b
            commandDataByte[11] = Utils.HexString2Bytes(action_mac)[0];
            commandDataByte[12] = Utils.HexString2Bytes(action_mac)[1];
            commandDataByte[13] = Utils.HexString2Bytes(action_mac)[2];
            commandDataByte[14] = Utils.HexString2Bytes(action_mac)[3];
            commandDataByte[15] = Utils.HexString2Bytes(action_mac)[4];
            commandDataByte[16] = Utils.HexString2Bytes(action_mac)[5];
            commandDataByte[17] = Utils.HexString2Bytes(action_mac)[6];
            commandDataByte[18] = Utils.HexString2Bytes(action_mac)[7];
        }

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CREATE_AND_EDITTASK.value();//0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        if (task_type == 1 | task_type == 2) {
            bt_send[23] = Utils.HexString2Bytes(device_mac)[0];
            bt_send[24] = Utils.HexString2Bytes(device_mac)[1];
            bt_send[25] = Utils.HexString2Bytes(device_mac)[2];
            bt_send[26] = Utils.HexString2Bytes(device_mac)[3];
            bt_send[27] = Utils.HexString2Bytes(device_mac)[4];
            bt_send[28] = Utils.HexString2Bytes(device_mac)[5];
            bt_send[29] = Utils.HexString2Bytes(device_mac)[6];
            bt_send[30] = Utils.HexString2Bytes(device_mac)[7];
        } else {
            bt_send[23] = Utils.HexString2Bytes(action_mac)[0];
            bt_send[24] = Utils.HexString2Bytes(action_mac)[1];
            bt_send[25] = Utils.HexString2Bytes(action_mac)[2];
            bt_send[26] = Utils.HexString2Bytes(action_mac)[3];
            bt_send[27] = Utils.HexString2Bytes(action_mac)[4];
            bt_send[28] = Utils.HexString2Bytes(action_mac)[5];
            bt_send[29] = Utils.HexString2Bytes(action_mac)[6];
            bt_send[30] = Utils.HexString2Bytes(action_mac)[7];
        }
        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }





    /**
     * 创建定时设备任务
     */
    public static byte[] CreateTimingDeviceTaskCmd(String taskname, byte isRun,byte task_cycle, int hour, int minute,
                                                String device_mac, String device_shortaddr, String device_main_point,
                                                int cmd_size,
                                                String dev_switch, int switch_state,
                                                String dev_level, int level_value,
                                                String dev_hue, int hue_value, int sat_value,
                                                String dev_temp, int temp_value)throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = TaskCmdData.tCmdData(device_mac, device_shortaddr, device_main_point,
                dev_switch, switch_state,
                dev_level, level_value,
                dev_hue, hue_value, sat_value,
                dev_temp, temp_value,
                null, 0, 0);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = 0x01;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = task_cycle;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = (byte) hour;//任务小时-15点
        commandDataByte[6] = (byte) minute;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001
        commandDataByte[7] = (byte) 0x80;//串口数据类型
        commandDataByte[8] = 0x01;//串口数据类型
        //0001
        commandDataByte[9] = 0x00;//设备动作
        commandDataByte[10] = 0x00;//设备动作
        //产生动作的设备mac开始   00158d0001310e1b
        commandDataByte[11] = 0x11;
        commandDataByte[12] = 0x22;
        commandDataByte[13] = 0x33;
        commandDataByte[14] = 0x44;
        commandDataByte[15] = 0x55;
        commandDataByte[16] = 0x66;
        commandDataByte[17] = 0x77;
        commandDataByte[18] = (byte) 0x88;

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CREATE_AND_EDITTASK.value();//0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        System.out.println("commandDataByte.length = " + dataByte_Len);
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = Utils.HexString2Bytes(device_mac)[0];
        bt_send[24] = Utils.HexString2Bytes(device_mac)[1];
        bt_send[25] = Utils.HexString2Bytes(device_mac)[2];
        bt_send[26] = Utils.HexString2Bytes(device_mac)[3];
        bt_send[27] = Utils.HexString2Bytes(device_mac)[4];
        bt_send[28] = Utils.HexString2Bytes(device_mac)[5];
        bt_send[29] = Utils.HexString2Bytes(device_mac)[6];
        bt_send[30] = Utils.HexString2Bytes(device_mac)[7];

        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }



    /**
     * 创建定时场景任务
     */
    public static byte[] CreateTimingSceneTaskCmd(String taskname, byte isRun,byte task_cycle, int hour, int minute,
                                                int cmd_size,int group_id, int scene_id)throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = TaskCmdData.tCmdData(null, null, null, null,
                0, null, 0, null, 0, 0, null, 0,
                "00A5", group_id, scene_id);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = 0x01;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = task_cycle;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = (byte) hour;//任务小时-15点
        commandDataByte[6] = (byte) minute;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001
        commandDataByte[7] = (byte) 0x80;//串口数据类型
        commandDataByte[8] = 0x01;//串口数据类型
        //0001
        commandDataByte[9] = 0x00;//设备动作
        commandDataByte[10] = 0x00;//设备动作
        //产生动作的设备mac开始   00158d0001310e1b
        commandDataByte[11] = 0x11;
        commandDataByte[12] = 0x22;
        commandDataByte[13] = 0x33;
        commandDataByte[14] = 0x44;
        commandDataByte[15] = 0x55;
        commandDataByte[16] = 0x66;
        commandDataByte[17] = 0x77;
        commandDataByte[18] = (byte) 0x88;

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CREATE_AND_EDITTASK.value();//0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        System.out.println("commandDataByte.length = " + dataByte_Len);
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;

        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }

    /**
     * 创建触发设备任务
     */
    public static byte[] CreateTriggerDeviceTaskCmd(String taskname, byte isRun,
                                                    int device_action, String action_mac,
                                                    String device_mac, String device_shortaddr, String device_main_point,
                                                    int cmd_size,
                                                    String dev_switch, int switch_state,
                                                    String dev_level, int level_value,
                                                    String dev_hue, int hue_value, int sat_value,
                                                    String dev_temp, int temp_value) throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = TaskCmdData.tCmdData(device_mac, device_shortaddr, device_main_point, dev_switch,
                switch_state, dev_level, level_value, dev_hue, hue_value, sat_value, dev_temp, temp_value,
                null, 0, 0);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = 0x03;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = 0x00;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = 0x00;//任务小时-15点
        commandDataByte[6] = 0x00;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001
        commandDataByte[7] = (byte) 0x84;//串口数据类型
        commandDataByte[8] = 0x01;//串口数据类型
        //0001
        commandDataByte[9] = (byte) (device_action << 8);//设备动作
        commandDataByte[10] = (byte) device_action;//设备动作
        //产生动作的设备mac开始   00158d0001310e1b
        commandDataByte[11] = Utils.HexString2Bytes(action_mac)[0];
        commandDataByte[12] = Utils.HexString2Bytes(action_mac)[1];
        commandDataByte[13] = Utils.HexString2Bytes(action_mac)[2];
        commandDataByte[14] = Utils.HexString2Bytes(action_mac)[3];
        commandDataByte[15] = Utils.HexString2Bytes(action_mac)[4];
        commandDataByte[16] = Utils.HexString2Bytes(action_mac)[5];
        commandDataByte[17] = Utils.HexString2Bytes(action_mac)[6];
        commandDataByte[18] = Utils.HexString2Bytes(action_mac)[7];

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CREATE_AND_EDITTASK.value();//0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        System.out.println("commandDataByte.length = " + dataByte_Len);
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = Utils.HexString2Bytes(action_mac)[0];
        bt_send[24] = Utils.HexString2Bytes(action_mac)[1];
        bt_send[25] = Utils.HexString2Bytes(action_mac)[2];
        bt_send[26] = Utils.HexString2Bytes(action_mac)[3];
        bt_send[27] = Utils.HexString2Bytes(action_mac)[4];
        bt_send[28] = Utils.HexString2Bytes(action_mac)[5];
        bt_send[29] = Utils.HexString2Bytes(action_mac)[6];
        bt_send[30] = Utils.HexString2Bytes(action_mac)[7];

        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }

    /**
     * 创建触发场景任务cmd
     */
    public static byte[] CreateTriggerSceneTaskCmd(String taskname, byte isRun,
                                                   int device_action, String action_mac,
                                                   int cmd_size,
                                                   int group_id, int scene_id) throws UnsupportedEncodingException {
        byte[] strTobt = taskname.getBytes("UTF-8");
        int task_name_len = taskname.length();

        byte[] cmddatabt = TaskCmdData.tCmdData(null, null, null, null,
                0, null, 0, null, 0, 0, null, 0,
                "00A5", group_id, scene_id);

        int commandDataByte_Len = cmddatabt.length;

        //---------------------------------数据开始------------------------------
        byte[] commandDataByte = new byte[22 + task_name_len + commandDataByte_Len];
        //任务相关
        commandDataByte[0] = 0x00;//任务编号
        commandDataByte[1] = 0x00;//任务编号
        commandDataByte[2] = isRun;//任务是否启用(0x00启用，0x01禁用)
        commandDataByte[3] = 0x04;//任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
        commandDataByte[4] = 0x00;//任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
        commandDataByte[5] = 0x00;//任务小时-15点
        commandDataByte[6] = 0x00;//任务分钟-58分
        //串口数据类型SerialLinkMessageTypes 8001

        commandDataByte[7] = (byte) 0x84;//串口数据类型
        commandDataByte[8] = 0x01;//串口数据类型
        //0001
        commandDataByte[9] = (byte) (device_action << 8);//设备动作
        commandDataByte[10] = (byte) device_action;//设备动作
        //产生动作的设备mac开始   00158d0001310e1b
        commandDataByte[11] = Utils.HexString2Bytes(action_mac)[0];
        commandDataByte[12] = Utils.HexString2Bytes(action_mac)[1];
        commandDataByte[13] = Utils.HexString2Bytes(action_mac)[2];
        commandDataByte[14] = Utils.HexString2Bytes(action_mac)[3];
        commandDataByte[15] = Utils.HexString2Bytes(action_mac)[4];
        commandDataByte[16] = Utils.HexString2Bytes(action_mac)[5];
        commandDataByte[17] = Utils.HexString2Bytes(action_mac)[6];
        commandDataByte[18] = Utils.HexString2Bytes(action_mac)[7];

        //产生动作的设备mac结束   0004
        commandDataByte[19] = (byte) (task_name_len << 8);//任务名称长度
        commandDataByte[20] = (byte) task_name_len;//任务名称长度

        //任务名称   65643963
        String task_str = "";
        byte[] task_data = null;//任务byte数组
        for (int i = 0; i < strTobt.length; i++) {
            task_str += Integer.toHexString(strTobt[i] & 0xFF);
            task_data = Utils.HexString2Bytes(task_str);
        }

        System.out.println("task_name_len = " + task_name_len);
        for (int i = 0; i < task_name_len; i++) {
            commandDataByte[21 + i] = task_data[i];//任务名称-字节数根据任务名称长度来定
        }

        //被触发要执行的命令列表  01
        commandDataByte[21 + task_name_len] = (byte) cmd_size;//要执行的命令列表数量

        for (int i = 0; i < cmddatabt.length; i++) {
            commandDataByte[(22 + task_name_len + i)] = cmddatabt[i];
        }

        int dataByte_Len = commandDataByte.length + 18 ;
        int dataByte_Len2 = commandDataByte.length;
        //415050c0a801050101aa
        byte[] bt_send = new byte[33];
        //415050c0a801020101bc
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100180065
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.CREATE_AND_EDITTASK.value();//0x18;//数据类型 枚举A
        bt_send[13] = (byte)( dataByte_Len << 8);
        bt_send[14] = (byte) dataByte_Len;//数据体长度
        System.out.println("commandDataByte.length = " + dataByte_Len);
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号  01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;//枚举B
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = Utils.HexString2Bytes(action_mac)[0];
        bt_send[24] = Utils.HexString2Bytes(action_mac)[1];
        bt_send[25] = Utils.HexString2Bytes(action_mac)[2];
        bt_send[26] = Utils.HexString2Bytes(action_mac)[3];
        bt_send[27] = Utils.HexString2Bytes(action_mac)[4];
        bt_send[28] = Utils.HexString2Bytes(action_mac)[5];
        bt_send[29] = Utils.HexString2Bytes(action_mac)[6];
        bt_send[30] = Utils.HexString2Bytes(action_mac)[7];
        //数据长度   0053
        bt_send[31] = (byte) (dataByte_Len2 << 8);
        bt_send[32] = (byte) dataByte_Len2;

        byte[] task_cmd = FtFormatTransfer.byteMerger(bt_send, commandDataByte);

        //将前面数据CRC8校验
        byte bt_crc8 = (CRC8.calc(task_cmd, task_cmd.length));
        String hex = Integer.toHexString(bt_crc8 & 0xFF);
        byte[] bt_crcdata = Utils.HexString2Bytes(hex);

        //Cmd 数据与CRC8相加
        byte[] bt_send_cmd = FtFormatTransfer.byteMerger(task_cmd, bt_crcdata);

        return bt_send_cmd;
    }

    /**
     * 获取网关所有任务
     * @return
     */
    public static byte[] GetAllTasks() {

        byte[] bt_send = new byte[34];
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;
        bt_send[8] = 0x01;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.GET_ALL_TASK.value();//0x1b;//数据类型   枚举A
        bt_send[13] = 0x00;
        bt_send[14] = 0x12;
        //数据体-----头
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        bt_send[23] = (byte) 0x00;
        bt_send[24] = (byte) 0x12;
        bt_send[25] = (byte) 0x4b;
        bt_send[26] = (byte) 0x00;
        bt_send[27] = (byte) 0x07;
        bt_send[28] = (byte) 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = (byte) 0x09;
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x00;
        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[33] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[33] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }

        return bt_send;
    }

    /*
       *删除task
       */
    public static byte[] DeleteTaskCmd(int task_id){
        byte[] bt_send = new byte[36];
        //415050c0a8016b010143
        bt_send[0] = 0x41;
        bt_send[1] = 0x50;
        bt_send[2] = 0x50;
        bt_send[3] = (byte) Constants.IpAddress.int_1;
        bt_send[4] = (byte) Constants.IpAddress.int_2;
        bt_send[5] = (byte) Constants.IpAddress.int_3;
        bt_send[6] = (byte) Constants.IpAddress.int_4;
        bt_send[7] = 0x01;//序号
        bt_send[8] = 0x01;//消息段数

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, 9))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, 9));
            bt_send[9] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[9] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, 9))[0];
        }
        //消息体   0100 0f 0018  0100100016
        bt_send[10] = 0x01;
        bt_send[11] = 0x00;
        bt_send[12] = MessageType.A.DELETE_TASK.value();//0x19;//数据类型
        bt_send[13] = 0x00;
        bt_send[14] = (byte) 0x14;//数据体长度
        //数据体头   415f5a4947
        bt_send[15] = 0x41;
        bt_send[16] = 0x5F;
        bt_send[17] = 0x5A;
        bt_send[18] = 0x49;
        bt_send[19] = 0x47;
        //数据体序号   01ffff
        bt_send[20] = 0x01;
        bt_send[21] = (byte)(MessageType.B.E_SL_MSG_DEFAULT.value() >> 8);//(byte) 0xFF;
        bt_send[22] = (byte) MessageType.B.E_SL_MSG_DEFAULT.value();      //(byte) 0xFF;
        //macaddr  00124b00076afe09
        bt_send[23] = 0x00;
        bt_send[24] = 0x12;
        bt_send[25] = 0x4b;
        bt_send[26] = 0x00;
        bt_send[27] = 0x07;
        bt_send[28] = 0x6a;
        bt_send[29] = (byte) 0xfe;
        bt_send[30] = 0x09;
        //数据长度   00060001
        bt_send[31] = (byte) 0x00;
        bt_send[32] = (byte) 0x02;

        bt_send[33] = (byte) (task_id >> 8);
        bt_send[34] = (byte) task_id;

        if (!Utils.isCRC8Value(Utils.CrcToString(bt_send, bt_send.length - 1))) {
            String ss = Utils.StringToHexString(Utils.CrcToString(bt_send, bt_send.length - 1));
            bt_send[35] = Utils.HexString2Bytes(ss)[0];
        } else {
            bt_send[35] = Utils.HexString2Bytes(Utils.CrcToString(bt_send, bt_send.length - 1))[0];
        }
        return bt_send;
    }


    /**
     * 合并任务数据命令
     * @return
     */
    public static byte[] tCmdData(String device_mac, String device_shortaddr, String device_main_point,
                                  String dev_switch, int switch_state, String dev_level, int level_value,
                                  String dev_hue, int hue_value, int sat_value, String dev_temp, int temp_value,
                                  String recall_scene, int group_id, int scene_id) {
        int switch_cmd_len = 0;
        if (dev_switch != null) {
            switch_cmd_len = 50;
        }
        if (dev_switch != null & dev_level == null & dev_hue == null & dev_temp == null & recall_scene == null) {
            switch_cmd_len = 57;
        }
        System.out.println("switch_cmd_len = " + switch_cmd_len);

        byte[] switch_cmd = new byte[switch_cmd_len];
        if (dev_switch != null) {
            switch_cmd[0] = 0x63;
            switch_cmd[1] = 0x6f;
            switch_cmd[2] = 0x6d;
            switch_cmd[3] = 0x6d;
            switch_cmd[4] = 0x61;
            switch_cmd[5] = 0x6e;
            switch_cmd[6] = 0x64;

            switch_cmd[7] = 0x00;
            byte[] cmdNo_1 = DeviceCmdData.DevSwitchCmd(device_mac, device_shortaddr, device_main_point, switch_state);
            int cmd_switch_len = cmdNo_1.length;
            switch_cmd[8] = ((byte) (cmd_switch_len << 8));
            switch_cmd[9] = ((byte) cmd_switch_len);

            for (int i = 0; i < cmd_switch_len; i++) {
                switch_cmd[(10 + i)] = cmdNo_1[i];
            }

            if (dev_level == null & dev_hue == null & dev_temp == null & recall_scene == null) {
                switch_cmd[(10 + cmdNo_1.length)] = 0x63;
                switch_cmd[(11 + cmdNo_1.length)] = 0x6f;
                switch_cmd[(12 + cmdNo_1.length)] = 0x6d;
                switch_cmd[(13 + cmdNo_1.length)] = 0x6d;
                switch_cmd[(14 + cmdNo_1.length)] = 0x61;
                switch_cmd[(15 + cmdNo_1.length)] = 0x6e;
                switch_cmd[(16 + cmdNo_1.length)] = 0x64;
            }
        }

        int level_cmd_len = 0;
        if (dev_level != null) {
            level_cmd_len = 53;
        }
        if (dev_level != null & dev_hue == null & dev_temp == null & recall_scene == null) {
            level_cmd_len = 60;
        }
        System.out.println("level_cmd_len = " + level_cmd_len);
        byte[] level_cmd = new byte[level_cmd_len];
        if (dev_level != null) {
            level_cmd[0] = 0x63;
            level_cmd[1] = 0x6f;
            level_cmd[2] = 0x6d;
            level_cmd[3] = 0x6d;
            level_cmd[4] = 0x61;
            level_cmd[5] = 0x6e;
            level_cmd[6] = 0x64;

            level_cmd[7] = 0x01;

            byte[] cmdNo_2 = DeviceCmdData.setDeviceLevelCmd(device_mac, device_shortaddr, device_main_point, level_value);

            int cmd_level_len = cmdNo_2.length;
            System.out.println("cmd_level_len2 = " + cmd_level_len);
            level_cmd[8] = ((byte) (cmd_level_len << 8));
            level_cmd[9] = ((byte) cmd_level_len);
            for (int i = 0; i < cmd_level_len; i++) {
                level_cmd[(10 + i)] = cmdNo_2[i];
            }

            if (dev_hue == null & dev_temp == null & recall_scene == null) {
                level_cmd[(10 + cmdNo_2.length)] = 0x63;
                level_cmd[(11 + cmdNo_2.length)] = 0x6f;
                level_cmd[(12 + cmdNo_2.length)] = 0x6d;
                level_cmd[(13 + cmdNo_2.length)] = 0x6d;
                level_cmd[(14 + cmdNo_2.length)] = 0x61;
                level_cmd[(15 + cmdNo_2.length)] = 0x6e;
                level_cmd[(16 + cmdNo_2.length)] = 0x64;
            }

        }

        int hue_cmd_len = 0;
        if (dev_hue != null) {
            hue_cmd_len = 54;
        }
        if (dev_hue != null & dev_temp == null & recall_scene == null) {
            hue_cmd_len = 61;
        }
        System.out.println("hue_cmd_len = " + hue_cmd_len);
        byte[] hue_cmd = new byte[hue_cmd_len];
        if (dev_hue != null) {
            hue_cmd[0] = 0x63;
            hue_cmd[1] = 0x6f;
            hue_cmd[2] = 0x6d;
            hue_cmd[3] = 0x6d;
            hue_cmd[4] = 0x61;
            hue_cmd[5] = 0x6e;
            hue_cmd[6] = 0x64;

            hue_cmd[7] = 0x02;

            byte[] cmdNo_3 = DeviceCmdData.setDeviceHueSatCmd(device_mac, device_shortaddr, device_main_point, hue_value, sat_value);

            int cmd_hue_sat_len = cmdNo_3.length;
            System.out.println("cmd_hue_sat_len3 = " + cmd_hue_sat_len);
            hue_cmd[8] = ((byte) (cmd_hue_sat_len << 8));
            hue_cmd[9] = ((byte) cmd_hue_sat_len);
            for (int i = 0; i < cmd_hue_sat_len; i++) {
                hue_cmd[(10 + i)] = cmdNo_3[i];
            }

            if (dev_temp == null & recall_scene == null) {
                hue_cmd[(10 + cmdNo_3.length)] = 0x63;
                hue_cmd[(11 + cmdNo_3.length)] = 0x6f;
                hue_cmd[(12 + cmdNo_3.length)] = 0x6d;
                hue_cmd[(13 + cmdNo_3.length)] = 0x6d;
                hue_cmd[(14 + cmdNo_3.length)] = 0x61;
                hue_cmd[(15 + cmdNo_3.length)] = 0x6e;
                hue_cmd[(16 + cmdNo_3.length)] = 0x64;
            }

        }

        int temp_cmd_len = 0;
        if (dev_temp != null) {
            temp_cmd_len = 53;
        }
        if (dev_temp != null & recall_scene == null) {
            temp_cmd_len = 60;
        }
        System.out.println("temp_cmd_len = " + temp_cmd_len);
        byte[] temp_cmd = new byte[temp_cmd_len];
        if (dev_temp != null) {
            temp_cmd[0] = 0x63;
            temp_cmd[1] = 0x6f;
            temp_cmd[2] = 0x6d;
            temp_cmd[3] = 0x6d;
            temp_cmd[4] = 0x61;
            temp_cmd[5] = 0x6e;
            temp_cmd[6] = 0x64;

            temp_cmd[7] = 0x03;

            byte[] cmdNo_4 = DeviceCmdData.setDeviceColorsTemp(device_mac, device_shortaddr, device_main_point, temp_value);

            int cmd_colors_temp_len = cmdNo_4.length;
            System.out.println("cmdNo_len = " + cmd_colors_temp_len);
            temp_cmd[8] = ((byte) (cmd_colors_temp_len << 8));
            temp_cmd[9] = ((byte) cmd_colors_temp_len);
            for (int i = 0; i < cmd_colors_temp_len; i++) {
                temp_cmd[(10 + i)] = cmdNo_4[i];
            }

            if (recall_scene == null) {
                temp_cmd[(10 + cmdNo_4.length)] = 0x63;
                temp_cmd[(11 + cmdNo_4.length)] = 0x6f;
                temp_cmd[(12 + cmdNo_4.length)] = 0x6d;
                temp_cmd[(13 + cmdNo_4.length)] = 0x6d;
                temp_cmd[(14 + cmdNo_4.length)] = 0x61;
                temp_cmd[(15 + cmdNo_4.length)] = 0x6e;
                temp_cmd[(16 + cmdNo_4.length)] = 0x64;
            }

        }

        int recall_cmd_len = 0;
        if (recall_scene != null) {
            recall_cmd_len = 52;
        }
        if (recall_scene != null & recall_scene != null) {
            recall_cmd_len = 59;
        }
        System.out.println("recall_cmd_len = " + recall_cmd_len);
        byte[] recall_cmd = new byte[recall_cmd_len];
        if (recall_scene != null) {
            recall_cmd[0] = 0x63;
            recall_cmd[1] = 0x6f;
            recall_cmd[2] = 0x6d;
            recall_cmd[3] = 0x6d;
            recall_cmd[4] = 0x61;
            recall_cmd[5] = 0x6e;
            recall_cmd[6] = 0x64;

            recall_cmd[7] = 0x01;

            byte[] cmdNo_5 = SceneCmdData.RecallScene(group_id, scene_id);

            int cmd_recall_scene_len = cmdNo_5.length;
            recall_cmd[8] = ((byte) (cmd_recall_scene_len << 8));
            recall_cmd[9] = ((byte) cmd_recall_scene_len);
            for (int i = 0; i < cmd_recall_scene_len; i++) {
                recall_cmd[(10 + i)] = cmdNo_5[i];
            }

            recall_cmd[(10 + cmdNo_5.length)] = 0x63;
            recall_cmd[(11 + cmdNo_5.length)] = 0x6f;
            recall_cmd[(12 + cmdNo_5.length)] = 0x6d;
            recall_cmd[(13 + cmdNo_5.length)] = 0x6d;
            recall_cmd[(14 + cmdNo_5.length)] = 0x61;
            recall_cmd[(15 + cmdNo_5.length)] = 0x6e;
            recall_cmd[(16 + cmdNo_5.length)] = 0x64;
        }
        byte[] bt_1 = FtFormatTransfer.byteMerger(switch_cmd, level_cmd);
        byte[] bt_2 = FtFormatTransfer.byteMerger(hue_cmd, temp_cmd);
        byte[] bt_3 = FtFormatTransfer.byteMerger(bt_1, bt_2);

        byte[] cmddata = FtFormatTransfer.byteMerger(bt_3, recall_cmd);
        System.out.println("cmddata = " + cmddata.length);
        String task_str = "";
        for (int i = 0; i < cmddata.length; i++) {
            task_str = task_str + Integer.toHexString(cmddata[i] & 0xFF);
        }
        System.out.println("task_str = " + task_str);
        return cmddata;
    }

}

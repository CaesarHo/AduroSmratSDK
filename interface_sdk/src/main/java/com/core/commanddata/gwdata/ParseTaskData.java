package com.core.commanddata.gwdata;

import com.core.entity.AppTask;
import com.core.entity.AppTask2;
import com.core.gatewayinterface.DataSources;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

import java.util.Arrays;

/**
 * Created by best on 2016/10/11.
 */

public class ParseTaskData {

    //return data
    public static class ParseGetTaskInfo {
        public int task_no, task_is_enabled, task_type, task_cycle, task_hour, task_minute, task_cmd_size, sensor_state;
        public String task_name, sensor_mac, device_mac;

        public String device_switch = "", device_level = "", device_hue = "", device_temp = "", scene_type = "";

        public int device_switch_state = -1;//设备开关状态（0关1开）
        public int device_level_value = -1;//设备亮度值
        public int device_hue_value = -1;//设备颜色值
        public int device_sat_value = -1;//设备饱和度
        public int device_temp_value = -1;//设备色温值

        public int scene_id = -1;//场景id
        public int group_id = -1;//场景里面组ID

        public void parseBytes(byte[] data) throws Exception {
            String taskinfo = TransformUtils.bytesToString(data);
            int strToint = taskinfo.indexOf(":");
            String isTask = "";
            if (strToint >= 0) {
                isTask = taskinfo.substring(strToint - 4, strToint);
            }

            String[] task_data_str_arr = taskinfo.split(",");
            if (taskinfo.contains("GW") && !taskinfo.contains("K64") && isTask.contains("TTid")) {

                String[] task_id = task_data_str_arr[0].split(":");//任务编号

                String task_str = "";
                String task_btToStr = "";
                for (int i = 62; i < data.length; i++) {
                    task_btToStr += Integer.toHexString(data[i] & 0xFF);
                }
                task_str = TransformUtils.toStringHex(task_btToStr);

                String task_btToStr2 = "";
                String is = task_str.substring(0, 1);
                if (is.contains(":")) {
                    task_str = "";
                    for (int i = 63; i < data.length; i++) {
                        task_btToStr2 += Integer.toHexString(data[i] & 0xFF);
                    }
                    task_str = TransformUtils.toStringHex(task_btToStr2);
                }

                System.out.println("task_btToStr = " + task_str);

                //任务编号
                task_no = Integer.parseInt(task_id[1]);
                System.out.println("task_no = " + task_no);


                //任务是否启用(0x00启用，0x01禁用)
                String task_is_enabled_hex = task_str.substring(4, 6);
                byte task_is_enabled_bt = TransformUtils.HexString2Bytes(task_is_enabled_hex)[0];
                task_is_enabled = task_is_enabled_bt & 0xFF;
                System.out.println("task_is_enabled = " + task_is_enabled);


                //任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
                String task_type_hex = task_str.substring(6, 8);
                byte task_type_bt = TransformUtils.HexString2Bytes(task_type_hex)[0];
                task_type = task_type_bt & 0xFF;
                System.out.println("task_type = " + task_type);


                //任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
                String task_cycle_hex = task_str.substring(8, 10);
                byte task_cycle_bt = TransformUtils.HexString2Bytes(task_cycle_hex)[0];
                task_cycle = task_cycle_bt & 0xFF;
                System.out.println("task_cycle = " + task_cycle);


                //任务小时-15点
                String task_hour_hex = task_str.substring(10, 12);
                byte task_hour_bt = TransformUtils.HexString2Bytes(task_hour_hex)[0];
                task_hour = task_hour_bt & 0xFF;
                System.out.println("task_hour = " + task_hour);


                //任务分钟-58分
                String task_minute_str = task_str.substring(12, 14);
                byte task_minute_bt = TransformUtils.HexString2Bytes(task_minute_str)[0];
                task_minute = task_minute_bt & 0xFF;
                System.out.println("task_minute = " + task_minute);


                //串口数据类型
                String task_serial_type = task_str.substring(14, 18);
                System.out.println("task_serial_type = " + task_serial_type);


                //设备动作
                String task_action_hex = task_str.substring(18, 22);
                byte[] action_bt = new byte[2];
                action_bt[0] = TransformUtils.HexString2Bytes(task_action_hex)[0];
                action_bt[1] = TransformUtils.HexString2Bytes(task_action_hex)[1];
                sensor_state = TransformUtils.hBytesToShort(action_bt);
                System.out.println("sensor_state = " + sensor_state);


                //产生动作的设备mac
                sensor_mac = task_str.substring(22, 38);
                System.out.println("sensor_mac = " + sensor_mac);


                //任务长度
                String task_name_len_hex = task_str.substring(38, 42);
                String task_name_len = task_name_len_hex.replaceAll("^(0+)", "");
                int task_name_len_int = Integer.parseInt(task_name_len);
                System.out.println("task_name_len_int = " + task_name_len_int);


                //任务名称
                String task_name_hex = task_str.substring(42, 42 + task_name_len_int * 2);
                task_name = TransformUtils.toStringHex(task_name_hex);
                System.out.println("task_name = " + task_name);


                //任务数据
                String task_data_hex = task_str.substring(42 + task_name_len_int * 2, task_str.length());
                String task_data_sub = task_data_hex.substring(16);//截取D6D616E64以切割String

                String[] task_cmd_data = task_data_sub.split("636f6d6d616e64");
                System.out.println("task_data_sub = " + task_data_sub);

                //命令数量
                String cmd_size_hex = task_data_sub.substring(0, 2);
                byte bytes = TransformUtils.HexString2Bytes(cmd_size_hex)[0];
                task_cmd_size = bytes & 0xFF;

                //命令里面的device_mac
                String task_device_mac = task_data_hex.substring(68, 84);
                System.out.println("task_device_mac = " + task_device_mac);

                for (int i = 0; i < task_cmd_data.length; i++) {
                    String task_cmd_single = task_cmd_data[i];//单条命令，比如：灯的开关状态的一窜命令
                    System.out.println("task_cmd_single = " + task_cmd_data[i]);

                    String data_mac_1 = task_cmd_single.substring(52, 68);
                    String data_short_1 = task_cmd_single.substring(74, 78);
                    String data_main_point_1 = task_cmd_single.substring(78, 80);
                    System.out.println("data_mac_1 = " + data_mac_1);
                    System.out.println("data_short_1 = " + data_short_1);
                    System.out.println("data_main_point_1 = " + data_main_point_1);

                    System.out.println("当前cmd数量 = " + i);
                    switch (i) {
                        case 0://第一条返回的命令数据解析
                            String data_no_1 = task_cmd_single.substring(0, 2);
                            String serial_type1 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                            SerialType(serial_type1, task_cmd_single);
                            break;

                        case 1:
                            String data_no_2 = task_cmd_single.substring(0, 2);
                            String serial_type2 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                            SerialType(serial_type2, task_cmd_single);
                            break;

                        case 2:
                            String data_no_3 = task_cmd_single.substring(0, 2);
                            String serial_type3 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                            SerialType(serial_type3, task_cmd_single);
                            break;

                        case 3:
                            String data_no_4 = task_cmd_single.substring(0, 2);
                            String serial_type4 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                            SerialType(serial_type4, task_cmd_single);
                            break;

                        case 4:
                            String data_no_5 = task_cmd_single.substring(0, 2);
                            String serial_type5 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                            SerialType(serial_type5, task_cmd_single);
                            break;
                    }
                }

                if (task_no >= 0 & task_name != null) {
                    AppTask appTask = new AppTask();
                    appTask.setTask_no(task_no);
                    appTask.setTask_name(task_name);
                    appTask.setIsEnabled(task_is_enabled);
                    appTask.setTask_type(task_type);
                    appTask.setTask_cycle(task_cycle);
                    appTask.setTask_hour(task_hour);
                    appTask.setTask_minute(task_minute);
                    appTask.setTask_action(sensor_state);
                    appTask.setSensor_mac(sensor_mac);
                    appTask.setDevice_mac(device_mac);
                    appTask.setCmd_size(task_cmd_size);

                    appTask.setIs_device_switch(device_switch);
                    appTask.setIs_device_level(device_level);
                    appTask.setIs_device_temp(device_temp);
                    appTask.setIs_device_hue(device_hue);
                    appTask.setIs_scene_type(scene_type);

                    appTask.setDevice_State(device_switch_state);
                    appTask.setDevice_level(device_level_value);
                    appTask.setDevice_temp(device_temp_value);
                    appTask.setDevice_colorHue(device_hue_value);
                    appTask.setDevice_colorSat(device_sat_value);
                    appTask.setTask_scene_id(scene_id);
                    appTask.setTask_group_id(group_id);

//                    DataSources.getInstance().getAllTasks(appTask);
                }
            }
        }

        public void SerialType(String type, String task_cmd_single) {
            switch (type) {

                case "0092"://设备开关  41  43
                    device_switch = type;
                    String device_state_str_1 = task_cmd_single.substring(82, 84);
                    byte device_state_str_bt_1 = TransformUtils.HexString2Bytes(device_state_str_1)[0];
                    device_switch_state = device_state_str_bt_1 & 0xFF;
                    System.out.println("device_switch_state = " + device_switch_state);
                    break;

                case "0081"://设备亮度
                    device_level = type;
                    String device_state_str_2 = task_cmd_single.substring(84, 86);
                    byte device_state_str_bt_2 = TransformUtils.HexString2Bytes(device_state_str_2)[0];
                    device_level_value = device_state_str_bt_2 & 0xFF;
                    System.out.println("device_level_value = " + device_level_value);
                    break;

                case "00B6"://设备颜色
                    device_hue = type;
                    String device_state_hue_3 = task_cmd_single.substring(82, 84);
                    String device_state_sat_3 = task_cmd_single.substring(84, 86);
                    byte device_state_hue_bt_3 = TransformUtils.HexString2Bytes(device_state_hue_3)[0];
                    byte device_state_sat_bt_3 = TransformUtils.HexString2Bytes(device_state_sat_3)[0];
                    device_hue_value = device_state_hue_bt_3 & 0xFF;
                    device_sat_value = device_state_sat_bt_3 & 0xFF;
                    System.out.println("device_hue_value = " + device_hue_value + "," + "device_sat_value = " + device_sat_value);
                    break;

                case "00C0"://色温
                    device_temp = type;
                    String device_state_str_4 = task_cmd_single.substring(82, 86);
                    byte[] device_state_str_bt_4 = TransformUtils.HexString2Bytes(device_state_str_4);
                    device_temp_value = TransformUtils.hBytesToInt(device_state_str_bt_4);
                    System.out.println("device_temp_value = " + device_temp_value);
                    break;

                case "00A5"://调用场景
                    scene_type = type;
                    String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                    String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                    byte[] device_state_group_bt_5 = TransformUtils.HexString2Bytes(device_state_group_id_5);
                    byte device_state_scene_bt_5 = TransformUtils.HexString2Bytes(device_state_scene_id_5)[0];
                    System.out.println("device_state_group_bt_5 = " + Arrays.toString(device_state_group_bt_5));
                    group_id = TransformUtils.hBytesToShort(device_state_group_bt_5);
                    scene_id = device_state_scene_bt_5 & 0xFF;
                    System.out.println("group_id = " + group_id);
                    break;
            }
        }
    }

    public static class ParseGetTaskInfo2 {
        public int task_no, task_is_enabled, task_type, task_cycle, task_hour, task_minute, sensor_state;
        public int scene_id = -1;//场景id
        public int group_id = -1;//场景里面组ID
        public String task_name, sensor_mac, short_address;

        public void parseBytes(byte[] data){
            String task_info = new String(data);
            int strToint = task_info.indexOf(":");
            String isTask = "";
            if (strToint >= 0) {
                isTask = task_info.substring(strToint - 4, strToint);
            }

            if (isTask.equalsIgnoreCase("TCnt")){
                return;
            }

            String[] task_info_array = task_info.split(",");
            for (int i = 0; i < task_info_array.length; i++) {
                System.out.println("task_info_array = " + task_info_array[i] + "," + i);
                switch (i){
                    case 0:
                        task_no = TransformUtils.string2Int(task_info_array[0].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[0]);
                        break;
                    case 1:
                        scene_id = TransformUtils.string2Int(task_info_array[1].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;
                    case 2:
                        group_id = TransformUtils.string2Int(task_info_array[2].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;
                    case 3:
                        task_is_enabled = TransformUtils.string2Int(task_info_array[3].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;

                    case 4:
                        task_name = task_info_array[4].split(":")[1];
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;

                    case 5:
                        task_type = TransformUtils.string2Int(task_info_array[5].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;
                    case 6:
                        short_address = task_info_array[6].split(":")[1];
                        String task_cycle_str = task_info_array[6].split(":")[1].substring(2);
                        byte[] bt = TransformUtils.HexString2Bytes(task_cycle_str);
                        task_cycle = bt[0] & 0xFF;
                        System.out.println("task_info_array = " + task_cycle);
                        break;
                    case 7:
                        sensor_mac = task_info_array[7].split(":")[1];
                        task_hour = TransformUtils.string2Int(task_info_array[7].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;
                    case 8:
                        sensor_state = TransformUtils.string2Int(task_info_array[8].split(":")[1]);
                        task_minute = TransformUtils.string2Int(task_info_array[8].split(":")[1]);
                        System.out.println("task_info_array = " + task_info_array[i]);
                        break;
                }
            }

            if (task_type == 0) {
                AppTask2 appTask = new AppTask2();
                appTask.setTask_no(task_no);
                appTask.setTask_scene_id(scene_id);
                appTask.setTask_group_id(group_id);
                appTask.setIsEnabled(task_is_enabled);
                appTask.setTask_name(task_name);
                appTask.setTask_type(task_type);
                appTask.setSensor_short(short_address.substring(2));
                appTask.setSensor_mac(sensor_mac.substring(2));
                appTask.setTask_status(sensor_state);
                DataSources.getInstance().getAllTasks(appTask);
            }else if (task_type == 1){
                AppTask2 appTask = new AppTask2();
                appTask.setTask_no(task_no);
                appTask.setTask_scene_id(scene_id);
                appTask.setTask_group_id(group_id);
                appTask.setIsEnabled(task_is_enabled);
                appTask.setTask_name(task_name);
                appTask.setTask_type(task_type);
                appTask.setTask_cycle(task_cycle);
                appTask.setTask_hour(task_hour);
                appTask.setTask_minute(task_minute);
                DataSources.getInstance().getAllTasks(appTask);
            }
        }
    }
}

package com.core.threadhelper;

import android.util.Log;
import com.core.data.TaskCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by best on 2016/9/12.
 */
public class GetAllTasks implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;

    public int task_no;
    public String task_name;
    public int task_is_enabled;
    public int task_cycle;
    public int task_hour;
    public int task_minute;
    public int task_type;
    public int task_cmd_size;

    public int sensor_state;
    public String sensor_mac;

    public String device_mac;
    private String serial_type1;
    private int action_state1;
    private int action_state6;

    private String serial_type2;
    private int action_state2;
    private int action_state7;

    private String serial_type3;
    private int action_state3;
    private int action_state8;

    private String serial_type4;
    private int action_state4;
    private int action_state9;

    private String serial_type5;
    private int action_state5;
    private int action_state10;

    @Override
    public void run() {
        try {
            //获取组列表命令
            Log.i("网关IP = ", Constants.ipaddress);
            bt_send = TaskCmdData.GetAllTasks();
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("getTask发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final byte[] recbuf = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
        while (true) {
            try {
                Thread.sleep(500);
                socket.receive(packet);
                System.out.println("CreateTask接收byte = " + Arrays.toString(recbuf));

                String str = new String(packet.getData()).trim();
                int strToint = str.indexOf(":");
                String isTask = "";
                if (strToint >= 0) {
                    isTask = str.substring(strToint - 4, strToint);
                }

                String[] task_data_str_arr = str.split(",");
                if (str.contains("GW") && !str.contains("K64") && isTask.contains("TTid")) {
//                    String receive_task_str = "";
//                    for (int i = 0; i < recbuf.length; i++) {
//                        receive_task_str += Integer.toHexString(recbuf[i] & 0xFF);
//                    }
//                    String task_str2222 = Utils.toStringHex2(receive_task_str);
//                    System.out.println("receive_task_str = " + task_str2222);

                    String[] task_id = task_data_str_arr[0].split(":");//任务编号

                    String task_str = "";
                    String task_btToStr = "";
                    for (int i = 62; i < recbuf.length; i++) {
                        task_btToStr += Integer.toHexString(recbuf[i] & 0xFF);
                    }
                    task_str = Utils.toStringHex2(task_btToStr);

                    String task_btToStr2 = "";
                    String is = task_str.substring(0,1);
                    if (is.contains(":")){
                        task_str = "";
                        for (int i = 63; i < recbuf.length; i++) {
                            task_btToStr2 += Integer.toHexString(recbuf[i] & 0xFF);
                        }
                        task_str = Utils.toStringHex2(task_btToStr2);
                    }

                    System.out.println("task_btToStr = " + task_str);

                    //任务编号
//                            String task_no = task_str.substring(0,4);
//                            System.out.println("task_no = " + task_no );
                    task_no = Integer.parseInt(task_id[1]);
                    System.out.println("task_no = " + task_no);


                    //任务是否启用(0x00启用，0x01禁用)
                    String task_is_enabled_hex = task_str.substring(4, 6);
                    byte task_is_enabled_bt = Utils.HexString2Bytes(task_is_enabled_hex)[0];
                    task_is_enabled = task_is_enabled_bt & 0xFF;
                    System.out.println("task_is_enabled = " + task_is_enabled);


                    //任务类型-（0x01定时设备、0x02定时场景、0x03触发设备、0x04触发场景）
                    String task_type_hex = task_str.substring(6, 8);
                    byte task_type_bt = Utils.HexString2Bytes(task_type_hex)[0];
                    task_type = task_type_bt & 0xFF;
                    System.out.println("task_type = " + task_type);


                    //任务周期 //0x01 周一 0x02 周二 0x04 周三 0x08 周四 0x10 周五 0x20 周六 0x40 周日(可任意组合)
                    String task_cycle_hex = task_str.substring(8, 10);
                    byte task_cycle_bt = Utils.HexString2Bytes(task_cycle_hex)[0];
                    task_cycle = task_cycle_bt & 0xFF;
                    System.out.println("task_cycle = " + task_cycle);


                    //任务小时-15点
                    String task_hour_hex = task_str.substring(10, 12);
                    byte task_hour_bt = Utils.HexString2Bytes(task_hour_hex)[0];
                    task_hour = task_hour_bt & 0xFF;
                    System.out.println("task_hour = " + task_hour);


                    //任务分钟-58分
                    String task_minute_str = task_str.substring(12, 14);
                    byte task_minute_bt = Utils.HexString2Bytes(task_minute_str)[0];
                    task_minute = task_minute_bt & 0xFF;
                    System.out.println("task_minute = " + task_minute);


                    //串口数据类型
                    String task_serial_type = task_str.substring(14, 18);
                    System.out.println("task_serial_type = " + task_serial_type);


                    //设备动作
                    String task_action_hex = task_str.substring(18, 22);
                    byte[] action_bt = new byte[2];
                    action_bt[0] = Utils.HexString2Bytes(task_action_hex)[0];
                    action_bt[1] = Utils.HexString2Bytes(task_action_hex)[1];
                    sensor_state = FtFormatTransfer.hBytesToShort(action_bt);
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
                    task_name = Utils.toStringHex2(task_name_hex);
                    System.out.println("task_name = " + task_name);


                    //任务数据
                    String task_data_hex = task_str.substring(42 + task_name_len_int * 2, task_str.length());
                    String task_data_sub = task_data_hex.substring(16);//截取D6D616E64以切割String
                    String[] task_cmd_data = task_data_sub.split("636f6d6d616e64");
                    System.out.println("task_data_sub = " + task_data_sub);

                    //命令数量
                    String cmd_size_hex = task_data_sub.substring(0, 2);
                    byte bytes = Utils.HexString2Bytes(cmd_size_hex)[0];
                    task_cmd_size = bytes & 0xFF;

                    //命令里面的device_mac
                    String task_mac = task_data_hex.substring(68, 84);
                    System.out.println("task_mac = " + task_mac);

                    for (int i = 0; i < task_cmd_data.length; i++) {
                        String task_cmd_single = task_cmd_data[i];//单条命令，比如：灯的开关状态的一窜命令
                        System.out.println("task_cmd_single = " + task_cmd_data[i]);

                        String data_mac_1 = task_cmd_single.substring(52, 68);
                        String data_short_1 = task_cmd_single.substring(74, 78);
                        String data_mian_point_1 = task_cmd_single.substring(78, 80);

                        System.out.println("当前cmd数量 = " + i);
                        switch (i) {
                            case 0://第一条返回的命令数据解析
                                String data_no_1 = task_cmd_single.substring(0, 2);
                                serial_type1 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                                switch (serial_type1) {

                                    case "0092"://设备开关
                                        String device_state_str_1 = task_cmd_single.substring(82, 84);
                                        byte device_state_str_bt_1 = Utils.HexString2Bytes(device_state_str_1)[0];
                                        action_state1 = device_state_str_bt_1 & 0xFF;
                                        System.out.println("action_state1 = " + action_state1);
                                        break;

                                    case "0081"://设备亮度
                                        String device_state_str_2 = task_cmd_single.substring(84, 86);
                                        byte device_state_str_bt_2 = Utils.HexString2Bytes(device_state_str_2)[0];
                                        action_state1 = device_state_str_bt_2 & 0xFF;
                                        System.out.println("action_state1 = " + action_state1);
                                        break;

                                    case "00B6"://设备颜色
                                        String device_state_hue_3 = task_cmd_single.substring(82, 84);
                                        String device_state_sat_3 = task_cmd_single.substring(84, 86);
                                        byte device_state_hue_bt_3 = Utils.HexString2Bytes(device_state_hue_3)[0];
                                        byte device_state_sat_bt_3 = Utils.HexString2Bytes(device_state_sat_3)[0];
                                        action_state1 = device_state_hue_bt_3 & 0xFF;
                                        action_state6 = device_state_sat_bt_3 & 0xFF;
                                        System.out.println("action_state1 = " + action_state1 + "," + "action_state6 = " + action_state6);
                                        break;

                                    case "00C0"://色温
                                        String device_state_str_4 = task_cmd_single.substring(82, 86);
                                        byte[] device_state_str_bt_4 = Utils.HexString2Bytes(device_state_str_4);
                                        action_state1 = FtFormatTransfer.hBytesToShort(device_state_str_bt_4);
                                        System.out.println("action_state1 = " + action_state1);
                                        break;

                                    case "00A5"://调用场景
                                        String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                                        String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                                        byte[] device_state_group_bt_5 = Utils.HexString2Bytes(device_state_group_id_5);
                                        byte device_state_scene_bt_5 = Utils.HexString2Bytes(device_state_scene_id_5)[0];
                                        action_state1 = FtFormatTransfer.hBytesToShort(device_state_group_bt_5);
                                        action_state6 = device_state_scene_bt_5 & 0xFF;
                                        System.out.println("action_state1 = " + action_state1 + "," + "action_state6 = " + action_state6);
                                        break;
                                }

                                break;
                            case 1:
                                String data_no_2 = task_cmd_single.substring(0, 2);
                                serial_type2 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                                switch (serial_type2) {

                                    case "0092"://设备开关  41  43
                                        String device_state_str_1 = task_cmd_single.substring(82, 84);
                                        byte device_state_str_bt_1 = Utils.HexString2Bytes(device_state_str_1)[0];
                                        action_state2 = device_state_str_bt_1 & 0xFF;
                                        System.out.println("action_state2 = " + action_state2);
                                        break;

                                    case "0081"://设备亮度
                                        String device_state_str_2 = task_cmd_single.substring(84, 86);
                                        byte device_state_str_bt_2 = Utils.HexString2Bytes(device_state_str_2)[0];
                                        action_state2 = device_state_str_bt_2 & 0xFF;
                                        System.out.println("action_state2 = " + action_state2);
                                        break;

                                    case "00B6"://设备颜色
                                        String device_state_hue_3 = task_cmd_single.substring(82, 84);
                                        String device_state_sat_3 = task_cmd_single.substring(84, 86);
                                        byte device_state_hue_bt_3 = Utils.HexString2Bytes(device_state_hue_3)[0];
                                        byte device_state_sat_bt_3 = Utils.HexString2Bytes(device_state_sat_3)[0];
                                        action_state2 = device_state_hue_bt_3 & 0xFF;
                                        action_state7 = device_state_sat_bt_3 & 0xFF;
                                        System.out.println("action_state2 = " + action_state2 + "," + "action_state7 = " + action_state7);
                                        break;

                                    case "00C0"://色温
                                        String device_state_str_4 = task_cmd_single.substring(82, 86);
                                        byte[] device_state_str_bt_4 = Utils.HexString2Bytes(device_state_str_4);
                                        action_state2 = FtFormatTransfer.hBytesToInt(device_state_str_bt_4);
                                        System.out.println("action_state2 = " + action_state2);
                                        break;

                                    case "00A5"://调用场景
                                        String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                                        String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                                        byte[] device_state_group_bt_5 = Utils.HexString2Bytes(device_state_group_id_5);
                                        byte device_state_scene_bt_5 = Utils.HexString2Bytes(device_state_scene_id_5)[0];
                                        action_state2 = FtFormatTransfer.hBytesToInt(device_state_group_bt_5);
                                        action_state7 = device_state_scene_bt_5 & 0xFF;
                                        System.out.println("action_state1 = " + action_state1 + "," + "action_state7 = " + action_state7);
                                        break;
                                }

                                break;
                            case 2:
                                String data_no_3 = task_cmd_single.substring(0, 2);
                                serial_type3 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                                switch (serial_type3) {
                                    case "0092"://设备开关  41  43
                                        String device_state_str_1 = task_cmd_single.substring(82, 84);
                                        byte device_state_str_bt_1 = Utils.HexString2Bytes(device_state_str_1)[0];
                                        action_state3 = device_state_str_bt_1 & 0xFF;
                                        System.out.println("action_state3 = " + action_state3);
                                        break;

                                    case "0081"://设备亮度
                                        String device_state_str_2 = task_cmd_single.substring(84, 86);
                                        byte device_state_str_bt_2 = Utils.HexString2Bytes(device_state_str_2)[0];
                                        action_state3 = device_state_str_bt_2 & 0xFF;
                                        System.out.println("action_state3 = " + action_state3);
                                        break;

                                    case "00B6"://设备颜色
                                        String device_state_hue_3 = task_cmd_single.substring(82, 84);
                                        String device_state_sat_3 = task_cmd_single.substring(84, 86);
                                        byte device_state_hue_bt_3 = Utils.HexString2Bytes(device_state_hue_3)[0];
                                        byte device_state_sat_bt_3 = Utils.HexString2Bytes(device_state_sat_3)[0];
                                        action_state3 = device_state_hue_bt_3 & 0xFF;
                                        action_state8 = device_state_sat_bt_3 & 0xFF;
                                        System.out.println("action_state3 = " + action_state3 + "," + "action_state8 = " + action_state8);
                                        break;

                                    case "00C0"://色温
                                        String device_state_str_4 = task_cmd_single.substring(82, 86);
                                        byte[] device_state_str_bt_4 = Utils.HexString2Bytes(device_state_str_4);
                                        action_state3 = FtFormatTransfer.hBytesToInt(device_state_str_bt_4);
                                        System.out.println("action_state3 = " + action_state3);
                                        break;

                                    case "00A5"://调用场景
                                        String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                                        String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                                        byte[] device_state_group_bt_5 = Utils.HexString2Bytes(device_state_group_id_5);
                                        byte device_state_scene_bt_5 = Utils.HexString2Bytes(device_state_scene_id_5)[0];
                                        action_state3 = FtFormatTransfer.hBytesToInt(device_state_group_bt_5);
                                        action_state8 = device_state_scene_bt_5 & 0xFF;
                                        System.out.println("action_state3 = " + action_state3 + "," + "action_state8 = " + action_state8);
                                        break;
                                }

                                break;
                            case 3:
                                String data_no_4 = task_cmd_single.substring(0, 2);
                                serial_type4 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                                switch (serial_type4) {
                                    case "0092"://设备开关  41  43
                                        String device_state_str_1 = task_cmd_single.substring(82, 84);
                                        byte device_state_str_bt_1 = Utils.HexString2Bytes(device_state_str_1)[0];
                                        action_state4 = device_state_str_bt_1 & 0xFF;
                                        System.out.println("action_state4 = " + action_state4);
                                        break;

                                    case "0081"://设备亮度
                                        String device_state_str_2 = task_cmd_single.substring(84, 86);
                                        byte device_state_str_bt_2 = Utils.HexString2Bytes(device_state_str_2)[0];
                                        action_state4 = device_state_str_bt_2 & 0xFF;
                                        System.out.println("action_state4 = " + action_state4);
                                        break;

                                    case "00B6"://设备颜色
                                        String device_state_hue_3 = task_cmd_single.substring(82, 84);
                                        String device_state_sat_3 = task_cmd_single.substring(84, 86);
                                        byte device_state_hue_bt_3 = Utils.HexString2Bytes(device_state_hue_3)[0];
                                        byte device_state_sat_bt_3 = Utils.HexString2Bytes(device_state_sat_3)[0];
                                        action_state4 = device_state_hue_bt_3 & 0xFF;
                                        action_state9 = device_state_sat_bt_3 & 0xFF;
                                        System.out.println("action_state4 = " + action_state4 + "," + "action_state9 = " + action_state9);
                                        break;

                                    case "00C0"://色温
                                        String device_state_str_4 = task_cmd_single.substring(82, 86);
                                        byte[] device_state_str_bt_4 = Utils.HexString2Bytes(device_state_str_4);
                                        action_state4 = FtFormatTransfer.hBytesToInt(device_state_str_bt_4);
                                        System.out.println("action_state4 = " + action_state4);
                                        break;

                                    case "00A5"://调用场景
                                        String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                                        String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                                        byte[] device_state_group_bt_5 = Utils.HexString2Bytes(device_state_group_id_5);
                                        byte device_state_scene_bt_5 = Utils.HexString2Bytes(device_state_scene_id_5)[0];
                                        action_state4 = FtFormatTransfer.hBytesToInt(device_state_group_bt_5);
                                        action_state8 = device_state_scene_bt_5 & 0xFF;
                                        System.out.println("action_state4 = " + action_state4 + "," + "action_state8 = " + action_state8);
                                        break;
                                }

                                break;
                            case 4:
                                String data_no_5 = task_cmd_single.substring(0, 2);
                                serial_type5 = task_cmd_single.substring(48, 52);//SerialLinkMessageTypes
                                switch (serial_type5) {
                                    case "0092"://设备开关  41  43
                                        String device_state_str_1 = task_cmd_single.substring(82, 84);
                                        byte device_state_str_bt_1 = Utils.HexString2Bytes(device_state_str_1)[0];
                                        action_state5 = device_state_str_bt_1 & 0xFF;
                                        System.out.println("action_state5 = " + action_state5);
                                        break;

                                    case "0081"://设备亮度
                                        String device_state_str_2 = task_cmd_single.substring(84, 86);
                                        byte device_state_str_bt_2 = Utils.HexString2Bytes(device_state_str_2)[0];
                                        action_state5 = device_state_str_bt_2 & 0xFF;
                                        System.out.println("action_state5 = " + action_state5);
                                        break;

                                    case "00B6"://设备颜色
                                        String device_state_hue_3 = task_cmd_single.substring(82, 84);
                                        String device_state_sat_3 = task_cmd_single.substring(84, 86);
                                        byte device_state_hue_bt_3 = Utils.HexString2Bytes(device_state_hue_3)[0];
                                        byte device_state_sat_bt_3 = Utils.HexString2Bytes(device_state_sat_3)[0];
                                        action_state5 = device_state_hue_bt_3 & 0xFF;
                                        action_state10 = device_state_sat_bt_3 & 0xFF;
                                        System.out.println("action_state5 = " + action_state5 + "," + "action_state10 = " + action_state10);
                                        break;

                                    case "00C0"://色温
                                        String device_state_str_4 = task_cmd_single.substring(82, 86);
                                        byte[] device_state_str_bt_4 = Utils.HexString2Bytes(device_state_str_4);
                                        action_state5 = FtFormatTransfer.hBytesToInt(device_state_str_bt_4);
                                        System.out.println("action_state5 = " + action_state5);
                                        break;

                                    case "00A5"://调用场景
                                        String device_state_group_id_5 = task_cmd_single.substring(82, 86);
                                        String device_state_scene_id_5 = task_cmd_single.substring(86, 88);
                                        byte[] device_state_group_bt_5 = Utils.HexString2Bytes(device_state_group_id_5);
                                        byte device_state_scene_bt_5 = Utils.HexString2Bytes(device_state_scene_id_5)[0];
                                        action_state5 = FtFormatTransfer.hBytesToInt(device_state_group_bt_5);
                                        action_state10 = device_state_scene_bt_5 & 0xFF;
                                        System.out.println("action_state5 = " + action_state5 + "," + "action_state10 = " + action_state10);
                                        break;
                                }
                                break;
                        }
                    }
                }
                /**
                 * String task_no,String task_name,int isEnabled,int task_type,
                 int task_cycle,int task_hour,int task_minute,int task_action,
                 String device_mac,int cmd_size,
                 String serial_type1, int action_state1,int action_state6,
                 String serial_type2, int action_state2,int action_state7,
                 String serial_type3, int action_state3,int action_state8,
                 String serial_type4, int action_state4,int action_state9,
                 String serial_type5, int action_state5,int action_state10
                 */
                if (task_no != 0 | task_name != null | sensor_mac != null){
                    DataSources.getInstance().getAllTasks(
                            task_no, task_name, task_is_enabled, task_type, task_cycle, task_hour,
                            task_minute, sensor_state, sensor_mac,device_mac, task_cmd_size,
                            serial_type1, action_state1, action_state6,
                            serial_type2, action_state2, action_state7,
                            serial_type3, action_state3, action_state8,
                            serial_type4, action_state4, action_state9,
                            serial_type5, action_state5, action_state10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

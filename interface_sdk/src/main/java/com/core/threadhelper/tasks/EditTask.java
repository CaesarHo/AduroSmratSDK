package com.core.threadhelper.tasks;

import android.util.Log;

import com.core.cmddata.TaskCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;
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
public class EditTask implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String task_name = "";
    private byte is_run;
    private byte task_type;
    private byte task_cycle;
    private int task_hour;
    private int task_minute;
    private int task_device_action;
    private String action_mac;

    private AppDevice appDevice;
    private int cmd_size;
    private String dev_switch;
    private String dev_level;
    private String dev_hue;
    private String dev_temp;
    private String recall_scene;

    private int group_id;
    private int scene_id;

    public EditTask(String task_name, byte is_run, byte task_type, byte task_cycle, int task_hour, int task_minute,
                    int device_action, String action_mac, AppDevice appDevice, int cmd_size, String dev_switch,
                    String dev_level, String dev_hue,
                    String dev_temp, String recall_scene, int group_id, int scene_id) {
        this.task_name = task_name;
        this.is_run = is_run;
        this.task_type = task_type;
        this.task_cycle = task_cycle;
        this.task_hour = task_hour;
        this.task_minute = task_minute;
        this.task_device_action = device_action;
        this.action_mac = action_mac;
        this.appDevice = appDevice;
        this.cmd_size = cmd_size;

        this.dev_switch = dev_switch;
        this.dev_level = dev_level;
        this.dev_hue = dev_hue;
        this.dev_temp = dev_temp;
        this.recall_scene = recall_scene;
        this.group_id = group_id;
        this.scene_id = scene_id;
    }

    public void run() {
        try {
            Log.i("task_name = ", this.task_name);

            this.bt_send = TaskCmdData.EditTask(this.task_name, this.is_run, this.task_type, this.task_cycle, this.task_hour,
                    this.task_minute, this.task_device_action, this.action_mac,appDevice,this.cmd_size, this.dev_switch,this.dev_level,
                    this.dev_hue, this.dev_temp,this.recall_scene, this.group_id, this.scene_id);

            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (this.socket == null) {
                this.socket = new DatagramSocket(null);
                this.socket.setReuseAddress(true);
                this.socket.bind(new InetSocketAddress(8888));
            }

            DatagramPacket datagramPacket = new DatagramPacket(this.bt_send, this.bt_send.length, inetAddress, 8888);
            this.socket.send(datagramPacket);
            System.out.println("CreateTask发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(this.bt_send, 16)), 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            byte[] recbuf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            try {
                this.socket.receive(packet);
                System.out.println("CreateTask接收 = " + Arrays.toString(recbuf));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = FtFormatTransfer.bytesToUTF8String(recbuf);

            int strToint = str.indexOf(":");
            String isGroup = "";
            if (strToint >= 0) {
                isGroup = str.substring(strToint - 4, strToint);
                Log.i("isGroup = ", isGroup);
            }

            if ((!str.contains("GW")) || (str.contains("K64")) || (!isGroup.contains("upId"))) ;
        }
    }
}

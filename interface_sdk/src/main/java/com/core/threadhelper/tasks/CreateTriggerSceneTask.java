package com.core.threadhelper.tasks;

import android.util.Log;

import com.core.cmddata.TaskCmdData;
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
 * Created by best on 2016/9/23.
 */

public class CreateTriggerSceneTask implements Runnable{
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private String task_name = "";
    private byte is_run;

    private int sensor_state,group_id,scene_id,cmd_size;
    private String sensor_mac;

    public CreateTriggerSceneTask(String task_name, byte is_run, int sensor_state,
                                  String sensor_mac,int cmd_size, int group_id, int scene_id) {
        this.task_name = task_name;
        this.is_run = is_run;
        this.cmd_size = cmd_size;
        this.sensor_state = sensor_state;
        this.sensor_mac = sensor_mac;

        this.group_id = group_id;
        this.scene_id = scene_id;
    }

    public void run() {
        try {
            Log.i("task_name = ", this.task_name);

            this.bt_send = TaskCmdData.CreateTriggerSceneTaskCmd(task_name,is_run,
                    sensor_state,sensor_mac,cmd_size,group_id,scene_id);

            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (this.socket == null) {
                this.socket = new DatagramSocket(null);
                this.socket.setReuseAddress(true);
                this.socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(this.bt_send, this.bt_send.length, inetAddress, Constants.UDP_PORT);
            this.socket.send(datagramPacket);
            System.out.println("CreateTask发送的十六进制数据 = " + Utils.binary(bt_send, 16));
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

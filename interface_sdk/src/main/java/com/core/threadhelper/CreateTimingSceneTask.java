package com.core.threadhelper;

import android.util.Log;

import com.core.data.TaskCmdData;
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

public class CreateTimingSceneTask implements Runnable{
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String task_name = "";
    private byte is_run;
    private byte task_cycle;
    private int task_hour;
    private int task_minute;
    private int group_id;
    private int scene_id;
    private int cmd_size;

    public CreateTimingSceneTask(String task_name, byte is_run, byte task_cycle, int task_hour, int task_minute,
                                 int cmd_size, int group_id, int scene_id) {
        this.task_name = task_name;
        this.is_run = is_run;
        this.task_cycle = task_cycle;
        this.task_hour = task_hour;
        this.task_minute = task_minute;
        this.cmd_size = cmd_size;

        this.group_id = group_id;
        this.scene_id = scene_id;
    }

    public void run() {
        try {
            Log.i("task_name = ", this.task_name);

            this.bt_send = TaskCmdData.CreateTimingSceneTaskCmd(task_name,is_run,task_cycle,task_hour,
                    task_minute,cmd_size,group_id,scene_id);

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

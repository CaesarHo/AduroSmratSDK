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
 * Created by best on 2016/9/23.
 */

public class CreateTriggerDeviceTask implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String task_name = "";
    private byte is_run;
    private int sensor_state;
    private String sensor_mac;

    private AppDevice appDevice;
    private int cmd_size;
    private String dev_switch;
    private String dev_level;
    private String dev_hue;
    private String dev_temp;

    public CreateTriggerDeviceTask(String task_name, byte is_run, int sensor_state,String sensor_mac, AppDevice appDevice,
                                   int cmd_size, String dev_switch, String dev_level, String dev_hue, String dev_temp) {
        this.task_name = task_name;
        this.is_run = is_run;
        this.appDevice = appDevice;
        this.cmd_size = cmd_size;
        this.sensor_state = sensor_state;
        this.sensor_mac = sensor_mac;

        this.dev_switch = dev_switch;
        this.dev_level = dev_level;
        this.dev_hue = dev_hue;
        this.dev_temp = dev_temp;
    }

    public void run() {
        try {
            Log.i("task_name = ", this.task_name);
            this.bt_send = TaskCmdData.CreateTriggerDeviceTaskCmd(task_name,is_run,
                    sensor_state,sensor_mac, appDevice, cmd_size,dev_switch,dev_level, dev_hue,dev_temp);
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

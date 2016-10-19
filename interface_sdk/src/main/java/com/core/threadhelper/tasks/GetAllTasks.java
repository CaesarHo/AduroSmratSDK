package com.core.threadhelper.tasks;

import android.util.Log;

import com.core.cmddata.TaskCmdData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.cmddata.parsedata.ParseTaskData;
import com.core.entity.AppTask;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.io.IOException;
import java.io.Serializable;
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
    private DatagramSocket socket = null;
    @Override
    public void run() {
        try {
            //获取组列表命令
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);
            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            byte[] bt_send = TaskCmdData.GetAllTasks();
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("getTask发送的十六进制数据 = " + Utils.binary(bt_send, 16));

            final byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            while (true) {
                socket.receive(packet);
                System.out.println("GetAllTasks接收byte = " + Arrays.toString(recbuf));
                //解析获取到的任务信息
                ParseTaskData.ParseGetTaskInfo parseGetTaskInfo = new ParseTaskData.ParseGetTaskInfo();
                parseGetTaskInfo.parseBytes(recbuf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.core.threadhelper.tasks;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.TaskCmdData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/9/12.
 */
public class GetAllTasks implements Runnable {
    private DatagramSocket socket = null;
    private Context context;

    public GetAllTasks(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            byte[] bt_send = TaskCmdData.GetAllTasks();
            if (!NetworkUtil.NetWorkType(context)) {
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                //获取组列表命令
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据GetAllTasks = " + Arrays.toString(recbuf));
                    if ((int) MessageType.A.GET_ALL_TASK.value() == recbuf[11]){
                        //解析获取到的任务信息
                        ParseTaskData.ParseGetTaskInfo parseGetTaskInfo = new ParseTaskData.ParseGetTaskInfo();
                        parseGetTaskInfo.parseBytes(recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.core.threadhelper.tasks;

import android.content.Context;
import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.TaskCmdData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static com.core.global.Constants.GW_IP_ADDRESS;

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
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context), 2);
                System.out.println("当前为远程通讯 = " + "GetAllDeviceListen");
            } else {
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);

                while (true) {
                    byte[] recbuf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    DataPacket.getInstance().BytesDataPacket(context,recbuf);
                    /**
                     * 枚举A判断是否是任务列表
                     */
                    if (recbuf[11] == MessageType.A.GET_ALL_TASK.value()) {
                        ParseTaskData.ParseGetTaskInfo2 parseGetTaskInfo2 = new ParseTaskData.ParseGetTaskInfo2();
                        parseGetTaskInfo2.parseBytes(recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

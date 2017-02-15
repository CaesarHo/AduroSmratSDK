package com.core.threadhelper.tasks;

import android.content.Context;
import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.TaskCmdData;
import com.core.commanddata.gwdata.ParseTaskData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
                boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.MQTT_CLIENT_ID);
                if (!isConnect){
                    return;
                }
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context), 2);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
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
                    try {
                        socket.receive(packet);
                    } catch (InterruptedIOException e) {
                        System.out.println("continue....................");
                        continue;  //非阻塞循环Operation not permitted
                    }
                    if (!Utils.isK6(recbuf)) {
                        System.out.println("当前接收的数据GetAllTasks = " + Arrays.toString(recbuf));
                        DataPacket.getInstance().BytesDataPacket(context, recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

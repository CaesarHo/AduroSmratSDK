package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.isRemote;

/**
 * Created by best on 2016/7/11.
 */
public class DeleteGroup implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short group_id;

    public DeleteGroup(Context context, short group_id) {
        this.group_id = group_id;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            bt_send = GroupCmdData.sendDeleteGroupCmd(group_id);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "DeleteGroup");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                InetAddress serverAddr = InetAddress.getByName(GW_IP_ADDRESS);
                DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
                socket.send(packet_send);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                // 接收数据
                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据DeleteGroup = " + Arrays.toString(recbuf));
                    if (recbuf[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                        ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
                        parseData.parseBytes(recbuf);
                        DataSources.getInstance().DeleteGroupResult(parseData.group_id);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

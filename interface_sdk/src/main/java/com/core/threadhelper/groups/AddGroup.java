package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
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
 * Created by best on 2016/7/11.
 */
public class AddGroup implements Runnable {
    private Context mContext;
    private String group_name = "";
    private DatagramSocket socket = null;
    private byte[] bt_send;
    private int count = -1;
    private String dev_mac;

    public AddGroup(Context context, String group_name,int count,String dev_mac) {
        this.group_name = group_name;
        this.mContext = context;
        this.count = count;
        this.dev_mac = dev_mac;
    }

    @Override
    public void run() {
        try {
            Constants.GROUP_GLOBAL.ADD_GROUP_NAME = group_name;
            bt_send = GroupCmdData.sendAddGroupCmd(group_name,count,dev_mac);
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "CreateGroup");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
                socket.send(packet_send);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                // 接收数据
                while (true) {
                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据AddGroup: =" + Arrays.toString(recbuf));
                    if ((int) MessageType.A.ADD_GROUP_NAME.value() == recbuf[11]) {
                        byte[] group_name_len = group_name.getBytes("utf-8");
                        ParseGroupData.ParseAddGroupBack(recbuf, group_name_len.length);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

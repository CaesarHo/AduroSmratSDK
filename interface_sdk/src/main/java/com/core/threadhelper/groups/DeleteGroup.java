package com.core.threadhelper.groups;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/11.
 */
public class DeleteGroup implements Runnable {

    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short group_id;

    public DeleteGroup(short group_id) {
        this.group_id = group_id;
    }

    @Override
    public void run() {
        if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
            DataSources.getInstance().SendExceptionResult(0);
            return;
        }
        try {
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);

            bt_send = GroupCmdData.sendDeleteGroupCmd(group_id);
            System.out.println("十六进制HUE = " + Utils.binary(bt_send, 16));

            DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
            socket.send(packet_send);

            // 接收数据
            while (true) {
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                String str = new String(recbuf);
                if (str.contains("GW") && !str.contains("K64")) {
                    //解析数据
                    ParseGroupData.ParseDeleteGroupResult parseData = new ParseGroupData.ParseDeleteGroupResult();
                    parseData.parseBytes(recbuf);
                    if (parseData.mGroupID == 0) {
                        return;
                    }
                    DataSources.getInstance().DeleteGroupResult(parseData.mGroupID);
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

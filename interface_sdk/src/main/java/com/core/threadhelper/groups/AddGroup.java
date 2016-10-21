package com.core.threadhelper.groups;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.io.IOException;
import java.io.PipedReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/11.
 */
public class AddGroup implements Runnable {
    private String group_name = "";
    private DatagramSocket socket = null;
    private byte[] bt_send;

    public AddGroup(String group_name) {
        this.group_name = group_name;
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

            bt_send = GroupCmdData.sendAddGroupCmd(group_name);
            System.out.println("添加组CMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
            socket.send(packet_send);

            // 接收数据
            while (true) {
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);

                System.out.println("添加房间返回数据: =" + Arrays.toString(recbuf));
                String str = new String(recbuf);
                if (str.contains("GW") && !str.contains("K64")) {
                    ParseGroupData.ParseAddGroupBack(recbuf,group_name.length());
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

package com.core.threadhelper.groups;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.threadhelper.UDPHelper;
import com.core.utils.ParseData;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/11.
 */
public class AddGroup implements Runnable{
    public static int PORT = 8888;
    private String group_name = "";
    DatagramSocket socket = null;
    byte[] bt_send;

    public AddGroup(String group_name){
        this.group_name = group_name;
    }

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        try {
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);

            bt_send = GroupCmdData.sendAddGroupCmd(group_name);
            System.out.println("添加组CMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, PORT);
            socket.send(packet_send);

            // 接收数据
            while(true){
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf,recbuf.length);
                try {
                    socket.receive(packet);

                    System.out.println("添加房间返回数据: ‘" + new String(packet.getData()).trim() + "’\n");
                    String str = new String(recbuf);
                    ParseData.ParseGroupInfo parseData = new ParseData.ParseGroupInfo();
                    //解析数据
                    parseData.parseBytes(recbuf,group_name.length());
                    if(str.contains("GW") && !str.contains("K64") && parseData.mGroupName.equals(group_name)){

                        if (parseData.mGroupID == 0 && parseData.mGroupName == null){
                            return;
                        }

                        DataSources.getInstance().AddGroupResult(parseData.mGroupID,group_name);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

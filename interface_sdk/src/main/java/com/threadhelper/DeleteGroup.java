package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.interfacecallback.UDPHelper;
import com.utils.NewCmdData;
import com.utils.ParseData;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/11.
 */
public class DeleteGroup implements Runnable{
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private short group_id;
    public DeleteGroup(short group_id){
        this.group_id = group_id;
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

            bt_send = NewCmdData.sendDeleteGroupCmd(group_id);
            System.out.println("十六进制HUE = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, PORT);
            socket.send(packet_send);

            // 接收数据
            while(true){
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf,recbuf.length);
                try {
                    socket.receive(packet);

                    String str = new String(recbuf);
                    if(str.contains("GW")&&!str.contains("K64")){
                        //解析数据
                        ParseData.ParseDeleteGroupResult parseData = new ParseData.ParseDeleteGroupResult();
                        parseData.parseBytes(recbuf);
                        if (parseData.mGroupID == 0){
                            return;
                        }
                        DataSources.getInstance().DeleteGroupResult(parseData.mGroupID);
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

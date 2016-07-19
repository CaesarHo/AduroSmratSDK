package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public class AddGroup implements Runnable{
    private String ipaddress;
    int port = -1;
    String roomname;
    String roomid;
    DatagramSocket m_CMDSocket = null;

    public AddGroup(String ipaddress, int port, String roomname, String roomid){
        this.ipaddress = ipaddress;
        this.port = port;
        this.roomname = roomname;
        this.roomid = roomid;
    }

    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(ipaddress);

            String mAddRoom = "AddRoom";
//            NewCmdData.add_cmd_remove cmdInfo = new NewCmdData.add_cmd_remove();
//            cmdInfo.id = id;
//            cmdInfo.Cmd = cmd;
//            cmdInfo.Length = 20;
//            cmdInfo.app_id = app_id;

            DatagramPacket packet_send = new DatagramPacket(mAddRoom.getBytes(),mAddRoom.getBytes().length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
//            NewCmdData.add_cmd_remove_Rep repdata = new NewCmdData.add_cmd_remove_Rep();
//            repdata.parseBytes(packet.getData());

            //当result等于1时添加成功,0添加失败
            DataSources.getInstance().AddGroupResult(1);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}
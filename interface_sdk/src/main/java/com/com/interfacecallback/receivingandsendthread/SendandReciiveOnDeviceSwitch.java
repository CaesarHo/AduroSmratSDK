package com.com.interfacecallback.receivingandsendthread;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/12.
 */
public class SendandReciiveOnDeviceSwitch implements Runnable {
    private String ipaddress;
    int port = -1;
    String devicename;
    int uid = -1;
    Short deviceid = 0;
    DatagramSocket m_CMDSocket = null;

    public SendandReciiveOnDeviceSwitch(String ipaddress, int port,Short deviceid, String devicename, int uid,int state){
        this.ipaddress = ipaddress;
        this.port = port;
        this.devicename = devicename;
        this.deviceid = deviceid;
        this.uid = uid;
    }

    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(ipaddress);

            String mDeleteRoom = "DeleteRoom";
//            NewCmdData.add_cmd_remove cmdInfo = new NewCmdData.add_cmd_remove();
//            cmdInfo.id = id;
//            cmdInfo.Cmd = cmd;
//            cmdInfo.Length = 20;
//            cmdInfo.app_id = app_id;
            DatagramPacket packet_send = new DatagramPacket(mDeleteRoom.getBytes(),mDeleteRoom.getBytes().length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
//            NewCmdData.add_cmd_remove_Rep repdata = new NewCmdData.add_cmd_remove_Rep();
//            repdata.parseBytes(packet.getData());

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().setDeviceState(1);
            m_CMDSocket.close();
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

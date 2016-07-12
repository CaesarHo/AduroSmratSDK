package com.com.interfacecallback.receivingandsendthread;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/12.
 */
public class SendandReceiveOnDeleteDevice implements Runnable{
    private String ipaddress;
    int port = -1;
    String devicename;
    int deviceid = -1;
    DatagramSocket m_CMDSocket = null;

    public SendandReceiveOnDeleteDevice(String ipaddress, int port, String devicename, int deviceid){
        this.ipaddress = ipaddress;
        this.port = port;
        this.devicename = devicename;
        this.deviceid = deviceid;
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
            DataSources.getInstance().DeleteDeviceResult(1);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

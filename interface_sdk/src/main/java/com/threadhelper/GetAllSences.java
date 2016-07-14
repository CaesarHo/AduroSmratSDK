package com.threadhelper;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllSences implements Runnable {
    private String ipaddress;
    int port = -1;
    DatagramSocket m_CMDSocket = null;

    public GetAllSences(String ipaddress, int port){
        this.ipaddress = ipaddress;
        this.port = port;
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
            DatagramPacket packet_send = new DatagramPacket(mDeleteRoom.getBytes(), mDeleteRoom.getBytes().length, serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
//            NewCmdData.add_cmd_remove_Rep repdata = new NewCmdData.add_cmd_remove_Rep();
//            repdata.parseBytes(packet.getData());

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().getAllSences(123,"场景1","");
            m_CMDSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

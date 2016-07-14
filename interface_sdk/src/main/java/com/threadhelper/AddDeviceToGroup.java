package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/13.
 */
public class AddDeviceToGroup  implements Runnable{
    private DatagramSocket m_CMDSocket = null;
    private String ipaddress;
    private int port = -1;
    private String groupName;
    private String deviceid;

    public AddDeviceToGroup(String ipaddress,int port,String groupName,String deviceid){
        this.ipaddress = ipaddress;
        this.port = port;
        this.groupName = groupName;
        this.deviceid = deviceid;
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

            //当result等于1时修改成功,0修改失败
            DataSources.getInstance().addDeviceToGroup(1);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

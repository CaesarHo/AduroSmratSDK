package com.threadhelper;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/11.
 */
public class AddDevice implements Runnable{
    private String ipaddress;
    int port = -1;
    private String deviceName;
    private String deviceId;
    private int deviceTypeId = -1;
    private String deviceType;
    private short zoneType;
    DatagramSocket m_CMDSocket = null;

    public AddDevice(String ipaddress, int port,String deviceName,String deviceId,int deviceTypeId,String deviceType,short zoneType){
        this.ipaddress = ipaddress;
        this.port = port;
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.deviceTypeId = deviceTypeId;
        this.deviceType = deviceType;
        this.zoneType = zoneType;
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
//            DataSources.getInstance().AddDeviceResult(1);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

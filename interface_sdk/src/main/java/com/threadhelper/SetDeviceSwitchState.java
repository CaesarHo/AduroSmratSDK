package com.threadhelper;

import android.util.Log;

import com.interfacecallback.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by best on 2016/7/12.
 */
public class SetDeviceSwitchState implements Runnable {
    private String ipaddress;
    int port = -1;
    String devicename;
    String deviceid ;
    int state;
    DatagramSocket m_CMDSocket = null;

    public SetDeviceSwitchState(String ipaddress, int port, String devicename, String deviceid, int state){
        this.ipaddress = ipaddress;
        this.port = port;
        this.devicename = devicename;
        this.deviceid = deviceid;
        this.state = state;
    }

    @Override
    public void run() {
        try {
            m_CMDSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(ipaddress);
            String mDeleteRoom = "DeleteRoom";
            DatagramPacket packet_send = new DatagramPacket(mDeleteRoom.getBytes(),mDeleteRoom.getBytes().length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().setDeviceStateResule(1);
            m_CMDSocket.close();
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

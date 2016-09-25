package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/13.
 */
public class SetGroupState implements Runnable {
    private int port = 8888;
    private int value = -1;
    private int group_id = -1;
    private DatagramSocket m_CMDSocket = null;

    public SetGroupState(int group_id,int value){
        this.group_id = group_id;
        this.value = value;
    }

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        try {
            if (m_CMDSocket == null){
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.bind(new InetSocketAddress(port));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);

            byte[] bt_send = GroupCmdData.setGroupState(group_id,value);
            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[1024];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().setDeviceStateResule(1);

        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

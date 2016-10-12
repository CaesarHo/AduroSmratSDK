package com.core.threadhelper.groups;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/13.
 */
public class SetGroupLevel implements Runnable {

    private DatagramSocket m_CMDSocket = null;
    private int group_id = -1;
    private int value = -1;

    public SetGroupLevel(int group_id,int value){
        this.group_id = group_id;
        this.value = value;
    }

    @Override
    public void run() {
        if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        try {
            if (m_CMDSocket == null){
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);

            byte[] bt_send = GroupCmdData.setGroupLevel(group_id,value);
            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, Constants.UDP_PORT);
            m_CMDSocket.send(packet_send);
            System.out.println("房间亮度调节 = " + Utils.binary(bt_send, 16));
            // 接收数据
            byte[] buf = new byte[1024];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);

            Log.i("房间亮度调节 = " , packet_receive.getData().toString().trim());
            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().setDeviceStateResule(1);

        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

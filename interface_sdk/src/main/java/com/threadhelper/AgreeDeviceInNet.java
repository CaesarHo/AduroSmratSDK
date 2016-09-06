package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.interfacecallback.UDPHelper;
import com.utils.NewCmdData;
import com.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/18.
 */
public class AgreeDeviceInNet implements  Runnable{
    private DatagramSocket m_CMDSocket = null;
    private int port = 8888;

    @Override
    public void run() {

        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }

        try {
            if(m_CMDSocket==null){
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.bind(new InetSocketAddress(port));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);
            byte[] bt_send = NewCmdData.Allow_DevicesAccesstoBytes();
            Utils.hexStringToByteArray(Utils.binary(bt_send,16));
            System.out.println("AgreeDeviceInNet = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));
            DatagramPacket packet_send = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),Utils.hexStringToByteArray(Utils.binary(bt_send,16)).length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[1024];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);

            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().AgreeDeviceInNet(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

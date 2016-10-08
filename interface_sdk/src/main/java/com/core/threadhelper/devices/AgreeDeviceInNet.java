package com.core.threadhelper.devices;

import com.core.cmddata.DeviceCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

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
            byte[] bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
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

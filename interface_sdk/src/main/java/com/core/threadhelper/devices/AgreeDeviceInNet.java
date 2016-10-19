package com.core.threadhelper.devices;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

/**
 * Created by best on 2016/7/18.
 */
public class AgreeDeviceInNet implements  Runnable{
    private DatagramSocket m_CMDSocket = null;
    @Override
    public void run() {
        try {
            if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null){
                DataSources.getInstance().SendExceptionResult(0);
                return ;
            }

            if(m_CMDSocket==null){
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.setBroadcast(true);
                m_CMDSocket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            byte[] bt_send = DeviceCmdData.Allow_DevicesAccesstoBytes();
            System.out.println("AgreeDeviceInNet = " + Utils.binary(bt_send,16));
            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, Constants.UDP_PORT);
            m_CMDSocket.send(packet_send);

            while (true){

                // 接收数据
                byte[] buf = new byte[1024];
                DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
                m_CMDSocket.receive(packet_receive);
                String str = new String(packet_receive.getData()).trim();

                System.out.println("新设备信息 = " + Arrays.toString(buf));
                System.out.println("新设备信息.A.UPLOAD_TXT.value()  = " + MessageType.A.UPLOAD_TXT.value());
                System.out.println("新设备信息[11]  = " + buf[11]);
                if ((int)MessageType.A.UPLOAD_TXT.value() == buf[11]) {
                    //解析获取设备信息
                    ParseDeviceData.ParseGetDeviceInfo(str,true);
                }

                //当result等于1时删除成功,0删除失败
                DataSources.getInstance().AgreeDeviceInNet(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

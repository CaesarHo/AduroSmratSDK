package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/13.
 */
public class SetColorTemperature implements Runnable {
    private int value = 1;
    private static final int PORT = 8888;
    private DatagramSocket m_CMDSocket = null;
    private String  devicemac;
    private String shortaddr;
    private String main_point;
    public SetColorTemperature (String devicemac , String shortaddr , String main_point,int value){
        this.devicemac = devicemac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
        this.value = value;
    }

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        try {
            if (m_CMDSocket == null) {
                m_CMDSocket = new DatagramSocket(null);
                m_CMDSocket.setReuseAddress(true);
                m_CMDSocket.bind(new InetSocketAddress(PORT));
            }
            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);

            byte[] bt_send = DeviceCmdData.setDeviceColorsTemp(devicemac,shortaddr,main_point,value);
            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, PORT);
            System.out.println("调色温CMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[24];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);
            //当result等于1时删除成功,0删除失败
//            DataSources.getInstance().setDeviceLevel(deviceId,(byte)value);
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

package com.core.threadhelper.devices;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/12.
 */
public class SetDeviceSwitchState implements Runnable {
    private int port = 8888;
    private int value = -1;
    private DatagramSocket m_CMDSocket = null;
    private AppDevice appDevice;

    public SetDeviceSwitchState(AppDevice appDevice, int value){
        this.appDevice = appDevice;
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
            byte[] bt_send = DeviceCmdData.DevSwitchCmd(appDevice,value);
            System.out.println("SetDeviceSwitchStateCMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
            DatagramPacket packet_send = new DatagramPacket(bt_send,bt_send.length,serverAddr, port);
            m_CMDSocket.send(packet_send);

            // 接收数据
            byte[] buf = new byte[1024];
            DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
            m_CMDSocket.receive(packet_receive);

            Log.i("SetDeviceSwitchState = " , packet_receive.getData().toString().trim());
            //当result等于1时删除成功,0删除失败
            DataSources.getInstance().setDeviceStateResule(1);

        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

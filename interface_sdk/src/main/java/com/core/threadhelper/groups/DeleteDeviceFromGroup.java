package com.core.threadhelper.groups;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/7/13.
 */
public class DeleteDeviceFromGroup implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short group_id;
    private AppDevice appDevice;

    public DeleteDeviceFromGroup(AppDevice appDevice,short group_id){
        this.group_id = group_id;
        this.appDevice = appDevice;
    }

    @Override
    public void run() {
        if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }

        try {
            bt_send = GroupCmdData.DeleteDeviceFromGroup(appDevice,(int)group_id);
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);
            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("发送的十六进制数据 = " + Utils.binary(bt_send, 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
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
    private short group_id;
    private DatagramSocket socket = null;
    private String device_mac;
    private int port = 8888;
    private String shortaddr;
    private String main_endpoint;

    public DeleteDeviceFromGroup(short group_id,String device_mac,String shortaddr,String main_endpoint){
        this.group_id = group_id;
        this.device_mac = device_mac;
        this.shortaddr = shortaddr;
        this.main_endpoint = main_endpoint;
    }

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }

        try {
            int groupidToint = (int)group_id;
            bt_send = GroupCmdData.DeleteDeviceFromGroup(groupidToint,device_mac,shortaddr,main_endpoint);
            Log.i("网关IP = ", Constants.ipaddress);
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(port));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, port);
            socket.send(datagramPacket);
            System.out.println("发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

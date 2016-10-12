package com.core.threadhelper.devices;

import com.core.cmddata.DeviceCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/9/20.
 */

public class IdentifyDevice implements Runnable{
    private int second = 1;
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    public IdentifyDevice (AppDevice appDevice,int second){
        this.appDevice = appDevice;
        this.second = second;
    }
    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if(socket==null){
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }
            byte[] bt_send = DeviceCmdData.IdentifyDeviceCmd(appDevice,second);
            DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length,inetAddress,Constants.UDP_PORT);
            socket.send(datagramPacket);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

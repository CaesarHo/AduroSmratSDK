package com.threadhelper;

import com.interfacecallback.Constants;
import com.utils.NewCmdData;
import com.utils.Utils;

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
    private static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String  devicemac;
    private String shortaddr;
    private String main_point;
    public IdentifyDevice (String devicemac , String shortaddr , String main_point,int second){
        this.devicemac = devicemac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
        this.second = second;
    }
    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if(socket==null){
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }
            byte[] bt_send = NewCmdData.IdentifyDeviceCmd(devicemac,shortaddr,main_point,second);
            DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length,inetAddress,PORT);
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

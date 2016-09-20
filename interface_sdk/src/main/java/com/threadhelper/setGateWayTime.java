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
 * Created by best on 2016/9/13.
 */
public class setGateWayTime implements Runnable {
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public setGateWayTime(int year,int month ,int day,int hour,int minute,int second){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
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
            byte[] bt_send = NewCmdData.setGateWayTimeCmd(year,month,day,hour,minute,second);
            DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length,inetAddress,PORT);
            socket.send(datagramPacket);
            System.out.println("开关十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));

        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf,recbuf.length);
            try {
                socket.receive(packet);
                String isGW = new String(recbuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

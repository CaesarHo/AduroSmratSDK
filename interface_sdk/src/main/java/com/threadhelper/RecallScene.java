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
 * Created by best on 2016/8/28.
 */
public class RecallScene implements Runnable {
    private static final int PORT = 8888;
    private DatagramSocket socket = null;
    private Short Group_Id;
    private Short Scene_Id;

    public RecallScene(Short Group_Id, Short Scene_Id) {
        this.Group_Id = Group_Id;
        this.Scene_Id = Scene_Id;
    }

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }
            int grouId_i = (int)Group_Id;
            int SceneId_i = (int)Scene_Id;
            byte[] bt_send = NewCmdData.RecallScene(grouId_i, SceneId_i);
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("控制场景CMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] recbuf = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("控制场景收到的数据: ‘" + new String(packet.getData()).trim() + "’\n");
    }
}

package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.utils.NewCmdData;
import com.utils.ParseData;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by best on 2016/7/13.
 */
public class DeleteScence implements Runnable{
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private short scene_id;
    private String Group_Id = "";
    public DeleteScence(short scene_id){
        this.scene_id = scene_id;
    }

    @Override
    public void run() {
        int sToint = (int)scene_id;
        try {
            //获取组列表命令
            bt_send = NewCmdData.sendDeleteSceneCmd(sToint);
            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("Delete发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
            final byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = new String(recbuf);
            if(str.contains("GW")&&!str.contains("K64")){
                byte btToint = recbuf[32];
                int i = btToint & 0xFF;
                DataSources.getInstance().DeleteSences(i);
            }
        }
    }
}

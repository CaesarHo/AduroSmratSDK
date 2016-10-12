package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
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
public class DeleteScence implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short scene_id;

    public DeleteScence(short scene_id) {
        this.scene_id = scene_id;
    }

    @Override
    public void run() {
        int sToint = (int) scene_id;
        try {
            //获取组列表命令
            bt_send = SceneCmdData.sendDeleteSceneCmd(sToint);
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("Delete发送的十六进制数据 = " + Utils.binary(bt_send, 16));

            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);

                String str = new String(recbuf);
                if (str.contains("GW") && !str.contains("K64")) {
                    byte btToint = recbuf[32];
                    int i = btToint & 0xFF;
                    DataSources.getInstance().DeleteSences(i);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

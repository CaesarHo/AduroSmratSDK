package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by best on 2016/8/29.
 */
public class StoreScene implements Runnable{
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private int group_id = -1;
    private int scene_id = -1;

    public StoreScene(AppDevice appDevice, int group_id, int scene_id){
        this.appDevice = appDevice;
        this.group_id = group_id;
        this.scene_id = scene_id;
    }
    @Override
    public void run() {
        try {
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);
            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            byte[] store_scene = SceneCmdData.StoreScene(appDevice,group_id,scene_id);

            DatagramPacket datagramPacket = new DatagramPacket(store_scene,store_scene.length,inetAddress,Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("存储场景 = " + Utils.binary(store_scene, 16));

            final byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            socket.receive(packet);
            System.out.println("存储场景返回数据 = " + Arrays.toString(recbuf));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }
    }
}

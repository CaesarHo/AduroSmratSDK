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
    private final int PORT = 8888;
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
            Log.i("网关IP = ", Constants.ipaddress);
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            byte[] bt_send_store_scene = SceneCmdData.StoreScene(appDevice,group_id,scene_id);

            DatagramPacket datagramPacket = new DatagramPacket(bt_send_store_scene, bt_send_store_scene.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("存储场景 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send_store_scene, 16)), 16));

            final byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            socket.receive(packet);
            System.out.println("存储场景返回数据 = " + Arrays.toString(recbuf));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (socket != null){
                socket.close();
            }
        }
    }
}

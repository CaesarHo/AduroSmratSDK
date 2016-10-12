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
 * Created by best on 2016/7/13.
 */
public class AddDeviceToSence implements Runnable {
    private DatagramSocket socket = null;
    private byte[] bt_send;
    private Short scene_id;
    private Short group_id;
    private AppDevice appDevice;

    public AddDeviceToSence(AppDevice appDevice, short group_id, short scene_id){
        this.appDevice = appDevice;
        this.scene_id = scene_id;
        this.group_id = group_id;
    }

    @Override
    public void run() {
        try {
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);
            bt_send = SceneCmdData.Add_DeviceToScene(appDevice,(int)group_id,scene_id);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("添加设备到场景for数据 = " + Utils.binary(bt_send, 16));

            new Thread(new StoreScene(appDevice,(int)group_id,scene_id)).start();
            try {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("添加设备到场景返回数据 = " + Arrays.toString(recbuf));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null){
//                socket.close();
            }
        }
    }
}

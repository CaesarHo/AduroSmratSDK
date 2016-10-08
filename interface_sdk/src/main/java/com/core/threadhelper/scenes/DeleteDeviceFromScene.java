package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.threadhelper.UDPHelper;
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
public class DeleteDeviceFromScene implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private int port = 8888;
    private short scene_id;
    private AppDevice appDevice;

    public DeleteDeviceFromScene(AppDevice appDevice,short scene_id){
        this.appDevice = appDevice;
        this.scene_id = scene_id;
    }

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }

        try {
            bt_send = SceneCmdData.DeleteDeviceFromScene(appDevice,(int)scene_id);
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

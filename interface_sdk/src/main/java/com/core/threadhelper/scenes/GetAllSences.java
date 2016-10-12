package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.entity.AppScene;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.FtFormatTransfer;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllSences implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private Short Scene_Group_Id = 0;
    private Short Scene_Id = 0;
    private String Scene_Name = "";
    private boolean isRun = true;
    ArrayList<String> SceneFormGroupDevlist =new ArrayList<>();

    @Override
    public void run() {
        try {
            //获取组列表命令
            bt_send = SceneCmdData.GetAllScenesListCmd();
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("GetAllScenes = " + Utils.binary(bt_send, 16));
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
                System.out.println("Scene_out2 = " + Arrays.toString(recbuf));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = FtFormatTransfer.bytesToUTF8String(recbuf);

            ParseSceneData.ParseGetScenesInfo(str);
        }
    }
}

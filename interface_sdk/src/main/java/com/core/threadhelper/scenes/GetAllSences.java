package com.core.threadhelper.scenes;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.db.GatewayInfo;
import com.core.entity.AppScene;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
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
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;

    public GetAllSences(Context context) {
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "getSences");
                byte[] bt_send = SceneCmdData.GetAllScenesListCmd();
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
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

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    String str = FtFormatTransfer.bytesToUTF8String(recbuf);
                    System.out.println("Scene_out2 = " + recbuf[11]);
                    if ((int) MessageType.A.GET_ALL_SCENE.value() == recbuf[11]) {
                        ParseSceneData.ParseGetScenesInfo(recbuf);
                        System.out.println("Scene_out2 = " + recbuf[11]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

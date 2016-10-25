package com.core.threadhelper.scenes;

import android.content.Context;

import com.core.cmddata.SceneCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

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
    private Context mContext;
    private DatagramSocket socket = null;
    private Short group_Id;
    private Short scene_Id;

    public RecallScene(Context context, Short group_Id, Short scene_Id) {
        this.mContext = context;
        this.group_Id = group_Id;
        this.scene_Id = scene_Id;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "RecallScene");
                byte[] bt_send = SceneCmdData.RecallScene((int) group_Id, (int) scene_Id);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                int grouId_i = (int) group_Id;
                int SceneId_i = (int) scene_Id;
                byte[] bt_send = SceneCmdData.RecallScene(grouId_i, SceneId_i);
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("控制场景CMD = " + Utils.binary(bt_send, 16));

                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

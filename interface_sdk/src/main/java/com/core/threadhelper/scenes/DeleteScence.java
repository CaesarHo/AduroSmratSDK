package com.core.threadhelper.scenes;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
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
 * Created by best on 2016/7/13.
 */
public class DeleteScence implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short scene_id;
    private boolean isRun = true;

    public DeleteScence(Context context,short scene_id) {
        this.mContext = context;
        this.scene_id = scene_id;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "deleteScence");
                byte[] bt_send = SceneCmdData.sendDeleteSceneCmd((int)scene_id);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                //获取组列表命令
                bt_send = SceneCmdData.sendDeleteSceneCmd((int) scene_id);
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

                while (isRun) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (socket != null){
                socket.close();
                isRun = false;
            }
        }
    }
}

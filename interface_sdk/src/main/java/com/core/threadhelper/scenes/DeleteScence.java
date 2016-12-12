package com.core.threadhelper.scenes;

import android.content.Context;
import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.SceneCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/13.
 */
public class DeleteScence implements Runnable {
    private Context mContext;
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
            byte[] bt_send = SceneCmdData.sendDeleteSceneCmd((int)scene_id);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "deleteScence");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    socket.setSoTimeout(2000);
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));
                while (isRun) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);

                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }

                    System.out.println("当前接收的数据DeleteScence = " + Arrays.toString(recbuf));
                    DataPacket.getInstance().BytesDataPacket(mContext,recbuf);
                    if (recbuf[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
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

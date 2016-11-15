package com.core.threadhelper.scenes;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.SceneCmdData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                //获取组列表命令
                bt_send = SceneCmdData.GetAllScenesListCmd();
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据GetAllSences = " + Arrays.toString(recbuf));
                    if ((int) MessageType.A.GET_ALL_SCENE.value() == recbuf[11]) {
                        ParseSceneData.ParseGetScenesInfo(recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

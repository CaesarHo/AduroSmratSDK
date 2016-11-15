package com.core.threadhelper.scenes;

import android.content.Context;

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
 * Created by best on 2016/7/14.
 */
public class AddSence implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private String scene_Name = "";
    private short group_Id = -1;
    private boolean isRun = true;

    public AddSence(Context context, String scene_Name, short group_Id) {
        this.scene_Name = scene_Name;
        this.group_Id = group_Id;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "AddSence");
                byte[] bt_send = SceneCmdData.sendAddSceneCmd(scene_Name, group_Id);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                bt_send = SceneCmdData.sendAddSceneCmd(scene_Name, group_Id);
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (isRun) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据AddSence = " + Arrays.toString(recbuf));
                    //解析数据
                    if ((int) MessageType.A.ADD_SCENE_NAME.value() == recbuf[11]) {
                        byte[] scene_name_len = Constants.SCENE_GLOBAL.ADD_SCENE_NAME.getBytes("utf-8");
                        ParseSceneData.ParseAddSceneBackInfo(recbuf, scene_name_len.length);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if (socket != null) {
//                socket.close();
//                isRun = false;
//            }
        }
    }
}

package com.core.threadhelper.scenes;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.SceneCmdData;
import com.core.commanddata.gwdata.ParseSceneData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
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

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.isRemote;

/**
 * Created by best on 2016/7/13.
 */
public class UpdateSceneName implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short scene_id;
    private String scene_name;
    private boolean isRun = true;

    public UpdateSceneName(Context context, short scene_id, String scene_name) {
        this.mContext = context;
        this.scene_id = scene_id;
        this.scene_name = scene_name;
    }

    @Override
    public void run() {
        try {
            bt_send = SceneCmdData.sendUpdateSceneCmd(scene_id, scene_name);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "ChangeSceneName");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
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
                    System.out.println("当前接收的数据UpdateSceneName = " + Arrays.toString(recbuf));

                    if (recbuf[11] == MessageType.A.CHANGE_SCENE_NAME.value()) {
                        //解析数据
                        ParseSceneData.ParseModifySceneInfo parseData = new ParseSceneData.ParseModifySceneInfo();
                        parseData.parseBytes(recbuf, scene_name.length());
                        DataSources.getInstance().ChangeSencesName(parseData.scene_id, parseData.scene_name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
                isRun = false;
            }
        }
    }
}

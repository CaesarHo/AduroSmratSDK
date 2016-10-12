package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.cmddata.parsedata.ParseSceneData;
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
import java.util.Arrays;

/**
 * Created by best on 2016/7/13.
 */
public class UpdateSceneName implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short scene_id;
    private String scene_name;

    public UpdateSceneName(short scene_id, String scene_name) {
        this.scene_id = scene_id;
        this.scene_name = scene_name;
    }

    @Override
    public void run() {
        try {
            //获取组列表命令
            bt_send = SceneCmdData.sendUpdateSceneCmd(scene_id, scene_name);
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("Update场景发送的十六进制数据 = " + Utils.binary(bt_send, 16));


            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("UpdateScene_out2 = " + Arrays.toString(recbuf));
                String str = FtFormatTransfer.bytesToUTF8String(recbuf);

//                int strToint = str.indexOf(":");
//                String isScene = "";
//                if (strToint >= 0) {
//                    isScene = str.substring(strToint - 3, strToint);
//                    Log.i("isScene = ", isScene);
//                }

                if (str.contains("GW") && !str.contains("K64")) {

                    //解析数据
                    ParseSceneData.ParseModifySceneInfo parseData = new ParseSceneData.ParseModifySceneInfo();
                    parseData.parseBytes(recbuf,scene_name.length());
                    if (parseData.mSceneID == 0 && parseData.mSceneName.contains(scene_name)){
                        return;
                    }

                    DataSources.getInstance().ChangeSencesName(parseData.mSceneID,parseData.mSceneName);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

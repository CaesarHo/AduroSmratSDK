package com.core.threadhelper;

import android.util.Log;

import com.core.data.SceneCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.FtFormatTransfer;
import com.core.utils.ParseData;
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
    public static final int PORT = 8888;
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
            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("Update场景发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));


            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("UpdateScene_out2 = " + Arrays.toString(recbuf));

                //71, 87, -64, -88, 1, 10, -101, 1, -80, 1, 0, 19, 0, 25, 65, 95, 90, 73, 71, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 23, 0, 4, 120, 120, 120, 120, 122, 120, 73
                String str = FtFormatTransfer.bytesToUTF8String(recbuf);

//                int strToint = str.indexOf(":");
//                String isScene = "";
//                if (strToint >= 0) {
//                    isScene = str.substring(strToint - 3, strToint);
//                    Log.i("isScene = ", isScene);
//                }

                if (str.contains("GW") && !str.contains("K64")) {

                    //解析数据
                    ParseData.ParseModifySceneInfo parseData = new ParseData.ParseModifySceneInfo();
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

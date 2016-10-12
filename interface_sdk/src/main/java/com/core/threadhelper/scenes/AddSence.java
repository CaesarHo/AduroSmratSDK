package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.cmddata.parsedata.ParseSceneData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
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
 * Created by best on 2016/7/14.
 */
public class AddSence implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private String Out_Scene_Name = "";
    private boolean isRun = true;

    public AddSence(String Out_Scene_Name, short Out_Group_Id) {
        this.Out_Scene_Name = Out_Scene_Name;
    }

    @Override
    public void run() {

        try {

            bt_send = SceneCmdData.sendAddSceneCmd(Out_Scene_Name, (int) Constants.SCENE_GLOBAL.ADD_SCENE_GROUP_ID);

            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            if (bt_send == null) {
                return;
            }
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("发送的十六进制数据 = " + Utils.binary(bt_send, 16));

            while (isRun) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("添加场景返回(byte) = " + Arrays.toString(recbuf));

                String str = FtFormatTransfer.bytesToUTF8String(recbuf);

                int strToint = str.indexOf(":");
                String isScene = "";
                if (strToint >= 0) {
                    isScene = str.substring(strToint - 3, strToint);
                }

                //解析数据
                if (str.contains("GW") && !str.contains("K64")) {
                    ParseSceneData.ParseAddSceneBackInfo(recbuf,Constants.SCENE_GLOBAL.ADD_SCENE_NAME.length());
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
                isRun = false;
            }
        }
    }
}

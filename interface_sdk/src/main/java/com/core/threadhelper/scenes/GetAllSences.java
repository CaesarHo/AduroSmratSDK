package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.entity.AppScene;
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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllSences implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private Short Scene_Group_Id = 0;
    private Short Scene_Id = 0;
    private String Scene_Name = "";
    private boolean isRun = true;
    ArrayList<String> SceneFormGroupDevlist =new ArrayList<String>();

    @Override
    public void run() {
        try {
            //获取组列表命令
            bt_send = SceneCmdData.GetAllScenesListCmd();
            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("GetAllScenes = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            final byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
            try {
                socket.receive(packet);
                System.out.println("Scene_out2 = " + Arrays.toString(recbuf));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = FtFormatTransfer.bytesToUTF8String(recbuf);

            int strToint = str.indexOf(":");
            String isScene = "";
            if (strToint >= 0){
                isScene = str.substring(strToint-3,strToint);
                Log.i("isScene = " ,isScene);
            }
            SceneFormGroupDevlist.clear();

            if (str.contains("GW") && !str.contains("K64") && isScene.contains("eId")) {

                String[] group_data = str.split(",");

                //get scenes
                for (int i = 1; i < group_data.length; i++) {
                    if (group_data.length < 2) {
                        continue;
                    }

                    if (group_data[i].contains("MAC:")){
                        SceneFormGroupDevlist.add(group_data[i].substring(4,group_data[i].length()));
                    }

                    String[] Id_Source = group_data[0].split(":");
                    String[] Group_Id_Source = group_data[1].split(":");
                    String[] Name_Source = group_data[2].split(":");

                    if (Id_Source.length > 1 && Name_Source.length > 1 && Group_Id_Source.length > 1) {
                        if (Id_Source.length >= 3){
                            Scene_Id = Short.valueOf(Id_Source[2]);
                            System.out.println("Scene_Id = " + Arrays.toString(Id_Source));
                        }else{
                            Scene_Id = Short.valueOf(Id_Source[1]);
                            System.out.println("Scene_Id = " + Arrays.toString(Id_Source));
                        }
                        Scene_Name = Utils.toStringHex2(Name_Source[1]);
                        Scene_Group_Id = Short.valueOf(Group_Id_Source[1]);

                    }

                    Log.i("Scene_Group_Id = ", "" + Scene_Group_Id);
                    Log.i("Scene_Id = ", "" + Scene_Id);
                    Log.i("Scene_Name = ", Scene_Name);
                }
                AppScene appScene = new AppScene();
                appScene.setSencesId(Scene_Id);
                appScene.setSencesName(Scene_Name);
                appScene.setGroups_id(Scene_Group_Id);
                appScene.setDevices_mac(SceneFormGroupDevlist);

                DataSources.getInstance().getAllScenes(appScene);
            }
        }
    }
}

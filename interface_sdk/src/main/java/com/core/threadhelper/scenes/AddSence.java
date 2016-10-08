package com.core.threadhelper.scenes;

import android.util.Log;

import com.core.cmddata.SceneCmdData;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
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
 * Created by best on 2016/7/14.
 */
public class AddSence implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private Short Out_Group_Id;
    private String Out_Scene_Name = "";

//    private Short Scene_Id;
//    private String Scene_Name = "";
//    private Short Scene_Group_Id;
    private boolean isRun = true;

    public AddSence(String Out_Scene_Name, short Out_Group_Id) {
        this.Out_Scene_Name = Out_Scene_Name;
        this.Out_Group_Id = Out_Group_Id;
    }

    @Override
    public void run() {

        try {

            bt_send = SceneCmdData.sendAddSceneCmd(Out_Scene_Name, (int)Out_Group_Id);

            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            if (bt_send == null) {
                return;
            }
            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            while (isRun) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                try {
                    socket.receive(packet);
                    System.out.println("添加场景返回(byte) = " + Arrays.toString(recbuf));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String str = FtFormatTransfer.bytesToUTF8String(recbuf);

                int strToint = str.indexOf(":");
                String isScene = "";
                if (strToint >= 0){
                    isScene = str.substring(strToint-3,strToint);
                }

                //解析数据
                ParseData.ParseAddSceneInfo parseData = new ParseData.ParseAddSceneInfo();
                parseData.parseBytes(recbuf,Out_Scene_Name.length());
                if (str.contains("GW") && !str.contains("K64") && parseData.mSceneName.equals(Out_Scene_Name)) {

                    if (parseData.mSceneID == 0){
                        return;
                    }

                    DataSources.getInstance().AddScene(parseData.mSceneID,parseData.mSceneName,Out_Group_Id);
                    //get scenes
//                    for (int i = 1; i < group_data.length; i++) {
//                        if (group_data.length <= 2) {
//                            return;
//                        }
//
//                        String[] Id_Source = group_data[0].split(":");
//                        String[] Group_Id_Source = group_data[1].split(":");
//                        String[] Name_Source = group_data[2].split(":");
//
//                        if (Id_Source.length >= 1 && Name_Source.length >= 1 && Group_Id_Source.length >= 1) {
//                            if (Id_Source.length >= 3){
//                                Scene_Id = Short.valueOf(Id_Source[2]);
//                            }else{
//                                Scene_Id = Short.valueOf(Id_Source[1]);
//                            }
//                            Scene_Name = Utils.toStringHex2(Name_Source[1]);
//                            Scene_Group_Id = Short.valueOf(Group_Id_Source[1]);
//                            Log.i("Scene_Group_Id = ", "" + Scene_Group_Id);
//                            Log.i("Scene_Id = ", "" + Scene_Id);
//                            Log.i("Scene_Name = ", Scene_Name);
//                        }
//                    }
//                    DataSources.getInstance().AddScene(Scene_Id,Scene_Name,Scene_Group_Id);
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

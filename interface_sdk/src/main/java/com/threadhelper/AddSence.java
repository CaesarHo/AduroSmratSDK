package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.utils.FtFormatTransfer;
import com.utils.NewCmdData;
import com.utils.Utils;

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
    private Short Out_Group_Id = 0;
    private String Out_Scene_Name = "";
    private Short Scene_Id = 0;
    private String Scene_Name = "";
    private Short Scene_Group_Id = 0;
    private boolean isRun = true;

    public AddSence(String Out_Scene_Name, short Out_Group_Id) {
        this.Out_Scene_Name = Out_Scene_Name;
        this.Out_Group_Id = Out_Group_Id;
    }

    @Override
    public void run() {

        try {

            bt_send = NewCmdData.sendAddSceneCmd(Out_Scene_Name, Scene_Group_Id);

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
                    System.out.println("Scene_out1=" + new String(packet.getData(), packet.getOffset(), packet.getLength(), "UTF-8"));
                    System.out.println("Scene_out2 = " + Arrays.toString(recbuf));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String str = FtFormatTransfer.bytesToUTF8String(recbuf);// new String(recbuf);

                if (str.contains("GW") && !str.contains("K64")) {
                    String[] group_data = str.split(",");
                    System.out.println("Scene分割的Scene数组 = " + Arrays.toString(group_data));
                    //get scenes
                    for (int i = 1; i < group_data.length; i++) {
                        if (group_data.length <= 2) {
                            return;
                        }

                        String[] Id_Source = group_data[0].split(":");
                        String[] Group_Id_Source = group_data[1].split(":");
                        String[] Name_Source = group_data[2].split(":");

                        if (Id_Source.length >= 1 && Name_Source.length >= 1 && Group_Id_Source.length >= 1) {
                            if (Id_Source.length >= 3){
                                Scene_Id = Short.valueOf(Id_Source[2]);
                            }else{
                                Scene_Id = Short.valueOf(Id_Source[1]);
                            }
                            Log.i("Id_Source[1] = ", "" + Id_Source[1]);
                            Scene_Name = Utils.toStringHex2(Name_Source[1]);
                            Scene_Group_Id = Short.valueOf(Group_Id_Source[1]);
                        }

                        Log.i("Scene_Id = ", "" + Scene_Id);
                        Log.i("Scene_Name = ", Scene_Name);

                        //场景分组
                        if (group_data.length <= 2 && Id_Source.length <= 1) {
                            return;
                        }
                        String[] add_scene_mac = new String[group_data.length];
                        for (int s = 0; s < add_scene_mac.length; s++) {
                            add_scene_mac[s] = group_data[i];
                            System.out.println("scene_mac = " + Arrays.toString(add_scene_mac));
                        }

                        DataSources.getInstance().AddSence(Scene_Id,Scene_Name,Scene_Group_Id,add_scene_mac);
                    }
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

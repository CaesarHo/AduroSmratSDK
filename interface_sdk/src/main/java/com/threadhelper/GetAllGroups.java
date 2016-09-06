package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.interfacecallback.UDPHelper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllGroups implements Runnable {
    //马丹，测试git
    //测试二号。
    //测试三号。
    byte[] bt_send;
    public static final int PORT = 8888;
    public DatagramSocket socket = null;
    private Short Group_Id = 0;
    private String Group_Name = "";

    ArrayList<String> Devicelist = new ArrayList<String>();

    @Override
    public void run() {
        if (UDPHelper.localip == null && Constants.ipaddress == null) {
            DataSources.getInstance().SendExceptionResult(0);
            return;
        }
        try {
            //获取组列表命令
            bt_send = NewCmdData.GetAllGroupListCmd();
            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("GetAllGroupsCMD = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
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
                System.out.println("GetAllGroups = " + Arrays.toString(recbuf));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = FtFormatTransfer.bytesToUTF8String(recbuf);

            int strToint = str.indexOf(":");
            String isGroup = "";
            if (strToint >= 0) {
                isGroup = str.substring(strToint - 4, strToint);
            }
            Devicelist.clear();

            if (str.contains("GW") && !str.contains("K64") && isGroup.contains("upId")) {
                String[] group_data = str.split(",");

                for (int i = 2; i < group_data.length; i++) {
                    if (group_data.length < 2) {
                        continue;
                    }
                    Devicelist.add(group_data[i]);

                    String[] Id_Source = group_data[0].split(":");
                    String[] Name_Source = group_data[1].split(":");

                    if (Id_Source.length > 1 && Name_Source.length > 1) {
                        if (Id_Source.length >= 3) {
                            Group_Id = Short.valueOf(Id_Source[2]);
                        } else {
                            Group_Id = Short.valueOf(Id_Source[1]);
                        }
                        Group_Name = Utils.toStringHex2(Name_Source[1]);
                    }
                }

                DataSources.getInstance().GetAllGroups(Group_Id, Group_Name, null, Devicelist);
            }
        }
    }


//        if (UDPHelper.localip == null && Constants.ipaddress == null) {
//            DataSources.getInstance().SendExceptionResult(0);
//            return;
//        }
//        try {
//            if (socket == null) {
//                socket = new DatagramSocket(null);
//                socket.setReuseAddress(true);
//                socket.bind(new InetSocketAddress(PORT));
//            }
//            InetAddress serverAddr = InetAddress.getByName(Constants.ipaddress);
//
//            bt_send = NewCmdData.GetAllGroupListCmd();
//            System.out.println("十六进制HUE = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
//
//            DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, PORT);
//            socket.send(packet_send);
//
//            // 接收数据
//            while (true) {
//                byte[] recbuf = new byte[1024];
//                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
//
//                socket.receive(packet);
//
//                System.out.println("收到的数据: ‘" + new String(packet.getData()).trim() + "’\n");
//                String str = new String(recbuf);
//                if (str.contains("GW") && ready && !str.contains("K64")) {
//
//                    String[] group_data = str.split(",");
//
//                    for (int i = 0; i < group_data.length; i++) {
//                        if (group_data.length <= 1) {
//                            return;
//                        }
//
//                        String[] Id_Source = group_data[0].split(":");
//                        String[] Name_Source = group_data[1].split(":");
//                        if (Id_Source.length <= 2) {
//                            return;
//                        }
//                        Group_Id = Short.valueOf(Id_Source[2]);
//                        Group_Name = Name_Source[1];
//
//                        strings = new String[group_data.length - 2];
//                        for (int s = 0; s < strings.length; s++) {
//                            if (group_data.length <= 3) {
//                                return;
//                            }
//                            strings[s] = group_data[i];
//                            System.out.println("strings = " + Arrays.toString(strings));
//                        }
//                    }
//                    DataSources.getInstance().GetAllGroups(Group_Id, Group_Name, null, strings);
//                }
//            }
//        } catch (Exception e) {
//            Log.e("deviceinfo IOException", "Client: Error!");
//        }
}

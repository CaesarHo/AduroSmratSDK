package com.core.threadhelper;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
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
 * Created by best on 2016/7/12.
 */
public class UpdateDeviceName implements Runnable {
    private byte[] bt_send;
    public static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String device_name = "";
    private String mac = "";
    private String shortaddr = "";
    private String main_point = "";

    public UpdateDeviceName(String device_name,String mac,String shortaddr , String main_point){
        this.device_name = device_name;
        this.mac = mac;
        this.shortaddr = shortaddr;
        this.main_point = main_point;
    }

    @Override
    public void run() {
        try {
            //获取组列表命令
            bt_send = DeviceCmdData.sendUpdateDeviceCmd(device_name,mac,shortaddr,main_point);
            Log.i("网关IP = ", Constants.ipaddress);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, PORT);
            socket.send(datagramPacket);
            System.out.println("修改设备名称发送的十六进制数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));
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
                System.out.println("UpdateGroup_out = " + Arrays.toString(recbuf));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = FtFormatTransfer.bytesToUTF8String(recbuf);// new String(recbuf);

            int strToint = str.indexOf(":");
            String isGroup = "";
            if (strToint >= 0){
                isGroup = str.substring(strToint-4,strToint);
                Log.i("isGroup = " ,isGroup);
            }

            if (str.contains("GW") && !str.contains("K64") && isGroup.contains("upId") ) {

                String[] group_data = str.split(",");
                //get groups
//                for (int i = 1; i < group_data.length; i++) {
//                    if (group_data.length <= 2) {
//                        return;
//                    }
//
//                    String[] Id_Source = group_data[0].split(":");
//                    String[] Name_Source = group_data[1].split(":");
//
//                    if (Id_Source.length > 1 && Name_Source.length > 1) {
//                        if (Id_Source.length >= 3){
//                            Group_Id = Id_Source[2];
//                            System.out.println("Source1 = " + Arrays.toString(Id_Source));
//                        }else{
//                            Group_Id = Id_Source[1];
//                            System.out.println("Source2 = " + Arrays.toString(Id_Source));
//                        }
//                        group_name = com.utils.Utils.toStringHex2(Name_Source[1]);
//                        System.out.println("Source3 = " + Arrays.toString(Name_Source));
//                    }
//
//                    Log.i("UpdateId_Source = ", Group_Id);
//                    Log.i("UpdateName_Source = ", group_name);
//                }
//                DataSources.getInstance().ChangeGroupName(group_id,group_name);
            }
        }
    }
}

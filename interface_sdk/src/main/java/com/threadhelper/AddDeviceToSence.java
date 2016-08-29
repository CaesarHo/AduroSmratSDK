package com.threadhelper;

import android.util.Log;

import com.interfacecallback.Constants;
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
 * Created by best on 2016/7/13.
 */
public class AddDeviceToSence implements Runnable {
    private String mac;
    private int port = 8888;
    private Short scene_id;
    private Short group_id;
    private String shortaddr;
    private String main_endpoint;
    private DatagramSocket socket = null;
    private byte[] bt_send;
    private int scenegroup_id = -1;

    public AddDeviceToSence(String mac,String shortaddr,String main_endpoint,short group_id,short scene_id){
        this.mac = mac;
        this.shortaddr = shortaddr;
        this.main_endpoint = main_endpoint;
        this.scene_id = scene_id;
        this.group_id = group_id;
    }

    @Override
    public void run() {
        try {

            Log.i("网关IP = ", Constants.ipaddress);
            scenegroup_id = (int)group_id;
            bt_send = NewCmdData.Add_DeviceToScene(mac,shortaddr,main_endpoint,scenegroup_id,scene_id);

            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(port));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, port);
            socket.send(datagramPacket);
            System.out.println("添加设备到场景for数据 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                try {
                    socket.receive(packet);
                    System.out.println("Scene_outadd=" + new String(packet.getData(), packet.getOffset(), packet.getLength(), "UTF-8"));
                    System.out.println("Scene_outadd = " + Arrays.toString(recbuf));

                    Thread.sleep(500);
                    //将设备存储至场景
                    new Thread(new StoreScene(mac,shortaddr,main_endpoint,scenegroup_id,scene_id)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null){
//                socket.close();
            }
        }
    }
}

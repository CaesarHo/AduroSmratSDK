package com.threadhelper;

import android.content.Context;
import android.util.Log;

import com.interfacecallback.Constants;
import com.interfacecallback.DataSources;
import com.utils.NewCmdData;
import com.utils.SearchUtils;
import com.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/7/14.
 */
public class GetAllDevices implements Runnable {
    private Context context;
    byte[] bt_send;
    private String ip = "192.168.1.100";
    private String GatewayString = "200004401331";
    public static final int PORT = 8888;
    public DatagramSocket socket = null;
    private boolean ready = true;
    @Override
    public void run() {
        new Thread(){
            @Override
            public void run() {
                try {
                    bt_send = NewCmdData.GetAllDeviceList();
                    Log.i("GATEWATEIPADDRESS = " , Constants.ipaddress);
                    InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
                    socket = new DatagramSocket();
                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(PORT));
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), Utils.hexStringToByteArray(Utils.binary(bt_send, 16)).length, inetAddress, PORT);
                    socket.send(datagramPacket);
                    System.out.println("send " + Utils.hexStringToByteArray(Utils.binary(bt_send, 16)));
                    System.out.println("十六进制 = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send, 16)), 16));

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                if (socket == null){
                    socket = new DatagramSocket(PORT);
                }
                socket.receive(packet);
                System.out.println("收到的数据: ‘" + new String(packet.getData()).trim() + "’\n");
                String str = new String(packet.getData()).trim();
                if(str.contains("GW")&&ready&&!str.contains("K64")){
                    ready = true;
                    int profile_id_int = SearchUtils.searchString(str, "PROFILE_ID:");
                    int device_id_int = SearchUtils.searchString(str, "DEVICE_ID:");
                    int device_mac_int = SearchUtils.searchString(str, "DEVICE_MAC:");
                    int device_shortaddr_int = SearchUtils.searchString(str, "DEVICE_SHORTADDR:");
                    int main_endpoint_int = SearchUtils.searchString(str, "MAIN_ENDPOINT:");
                    String profile_id = new String(str).substring(profile_id_int, profile_id_int + 6);
                    String device_id = new String(str).substring(device_id_int, device_id_int + 6);
                    String device_mac = new String(str).substring(device_mac_int, device_mac_int + 18);
                    String device_shortaddr = new String(str).substring(device_shortaddr_int, device_shortaddr_int + 6);
                    String main_endpoint = new String(str).substring(main_endpoint_int,device_shortaddr_int+4);

                    Log.i("device_mac = ", device_mac);
                    DataSources.getInstance().ScanDeviceResult("Device",profile_id ,device_mac ,device_shortaddr ,device_id,main_endpoint);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


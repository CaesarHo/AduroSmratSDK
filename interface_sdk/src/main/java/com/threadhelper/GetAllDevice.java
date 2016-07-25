package com.threadhelper;

import android.content.Context;
import android.util.Log;

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
public class GetAllDevice implements Runnable {
    private Context context;
    byte[] bt_send;
    private String ip = "192.168.1.100";
    private String GatewayString = "200004401331";
    public static final int PORT = 8888;
    public DatagramSocket socket = null;
    private boolean ready = true;
//    public GetAllDevice (Context context){
//        this.context = context;
//    }
    @Override
    public void run() {
        try {
            bt_send = NewCmdData.GetAllDeviceList();
            InetAddress inetAddress = InetAddress.getByName(ip);
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
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
        }

        while (true) {
            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

            try {
                socket.receive(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("收到的数据: ‘" + new String(packet.getData()).trim() + "’\n");
            String s = new String(packet.getData()).trim();
            if(s.contains("GW")&&ready&&!s.contains("K64")){
                ready = true;
                int profile_id_int = SearchUtils.searchString(new String(packet.getData()).trim(), "PROFILE_ID:");
                int device_id_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_ID:");
                int device_mac_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_MAC:");
                int device_shortaddr_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_SHORTADDR:");
                String profile_id = new String(packet.getData()).trim().substring(profile_id_int, profile_id_int + 6);
                String device_id = new String(packet.getData()).trim().substring(device_id_int, device_id_int + 6);
                String device_mac = new String(packet.getData()).trim().substring(device_mac_int, device_mac_int + 18);
                String device_shortaddr = new String(packet.getData()).trim().substring(device_shortaddr_int, device_shortaddr_int + 6);
                Log.i("device_mac = ", device_mac);
                DataSources.getInstance().ScanDeviceResult("Device",profile_id ,device_mac ,device_shortaddr ,device_id);
            }

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String s = new String(packet.getData()).trim();
//                    if (s.contains("GW") && ready && !s.contains("K64")) {
//                        ready = true;
////                            Toast.makeText(MainActivity.this,new String(packet.getData()).trim(),Toast.LENGTH_SHORT).show();
//                        int profile_id_int = SearchUtils.searchString(new String(packet.getData()).trim(), "PROFILE_ID:");
//                        int device_id_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_ID:");
//                        int device_mac_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_MAC:");
//                        int device_shortaddr_int = SearchUtils.searchString(new String(packet.getData()).trim(), "DEVICE_SHORTADDR:");
//                        String profile_id = new String(packet.getData()).trim().substring(profile_id_int, profile_id_int + 6);
//                        String device_id = new String(packet.getData()).trim().substring(device_id_int, device_id_int + 6);
//                        String device_mac = new String(packet.getData()).trim().substring(device_mac_int, device_mac_int + 18);
//                        String device_shortaddr = new String(packet.getData()).trim().substring(device_shortaddr_int, device_shortaddr_int + 6);
//
//                        TextView.class.cast(findViewById(R.id.txt)).setText(device_mac);
//
//                        if (mDevInfoList.size() == 0) {
//                            DevInfo item = new DevInfo();
//                            item.devId = device_id;
//                            item.devMac = device_mac;
//                            item.devShortaddr = device_shortaddr;
//                            mDevInfoList.add(item);
//                            infoadapter.notifyDataSetChanged();
//                        } else {
//                            synchronized (mDevInfoList) {
//                                //--------------搜索到一个设备-----------
//                                for (DevInfo devInfo : mDevInfoList) {
//                                    if (devInfo.devMac.endsWith(device_mac)) {
//                                        return;
//                                    }
//                                }
//
//                                DevInfo item = new DevInfo();
//                                item.devId = device_id;
//                                item.devMac = device_mac;
//                                item.devShortaddr = device_shortaddr;
//                                mDevInfoList.add(item);
//                                infoadapter.notifyDataSetChanged();
//                                System.out.println("Cmd id = " + device_mac);
//                            }
//                        }
//                        infoadapter.notifyDataSetChanged();
//                        Log.i("profile_id_int = ", device_id);
//                    }
//                }
//            });
        }
    }
}


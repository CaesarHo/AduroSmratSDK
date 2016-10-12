package com.core.threadhelper.devices;

import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.entity.AppDevice;
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
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private String device_name;

    public UpdateDeviceName(AppDevice appDevice, String device_name) {
        this.appDevice = appDevice;
        this.device_name = device_name;
    }

    @Override
    public void run() {
        try {
            //获取组列表命令
            bt_send = DeviceCmdData.sendUpdateDeviceCmd(appDevice, device_name);
            Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("修改设备名称发送的十六进制数据 = " + Utils.binary(bt_send, 16));


            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

                socket.receive(packet);
                System.out.println("UpdateGroup_out = " + Arrays.toString(recbuf));

                String str = FtFormatTransfer.bytesToUTF8String(recbuf);// new String(recbuf);

                int strToint = str.indexOf(":");
                String isGroup = "";
                if (strToint >= 0) {
                    isGroup = str.substring(strToint - 4, strToint);
                    Log.i("isGroup = ", isGroup);
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

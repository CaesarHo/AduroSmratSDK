package com.core.threadhelper.devices;

import android.content.Context;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/10/28.
 */

public class BindDevice implements Runnable {

    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private Context context;

    public BindDevice(Context context, AppDevice appDevice) {
        this.context = context;
        this.appDevice = appDevice;
    }

    @Override
    public void run() {
        try {
            byte[] bt_send = DeviceCmdData.Get_IEEEAddr_CMD();
            if (GW_IP_ADDRESS.equals("")) {
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context), 2);
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("当前接收的数据BindDevice = " + Arrays.toString(recbuf));

                    if ((int) MessageType.A.GET_GATEWAY_IEEE.value() == recbuf[11]) {
                        ParseDeviceData.ParseIEEEData parseIEEEData = new ParseDeviceData.ParseIEEEData();
                        parseIEEEData.parseBytes(recbuf);
                        byte[] bt = DeviceCmdData.BindDeviceCmd(appDevice, parseIEEEData.gateway_mac);
                        sendMessage(bt, socket);
                    }

                    if ((int) MessageType.A.BIND_DEVICE.value() == recbuf[11]) {
                        ParseDeviceData.ParseBindVCPFPData parseBindVCPFPData = new ParseDeviceData.ParseBindVCPFPData();
                        parseBindVCPFPData.parseBytes(recbuf);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] bt_send, DatagramSocket socket) throws IOException {
        InetAddress address = InetAddress.getByName(GW_IP_ADDRESS);
        DatagramPacket dp = new DatagramPacket(bt_send, bt_send.length, address, Constants.UDP_PORT);
        socket.send(dp);
        System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));
        byte[] bs = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        socket.receive(packet);
        System.out.println("当前接收的数据BindDevicesendMessage = " + Arrays.toString(bs));
    }
}

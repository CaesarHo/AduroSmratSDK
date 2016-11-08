package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.cmddata.parsedata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
            if (Constants.GW_IP_ADDRESS == null) {
                return;
            }

            InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
            }

            byte[] bt_send = DeviceCmdData.Get_IEEEAddr_CMD();

            DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
            socket.send(datagramPacket);
            System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

            while (true) {
                final byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("新设备 = " + Arrays.toString(recbuf));
                System.out.println("读IEEE地址返回数据 = " + Utils.binary(recbuf, 16));

                if ((int) MessageType.A.GET_GATEWAY_IEEE.value() == recbuf[11]) {
                    ParseDeviceData.ParseIEEEData parseIEEEData = new ParseDeviceData.ParseIEEEData();
                    parseIEEEData.parseBytes(recbuf);

                    byte[] bt = DeviceCmdData.BindDeviceCmd(appDevice,parseIEEEData.gateway_mac);
                    sendMessage(bt, socket);
                }

                ParseDeviceData.ParseBindVCPFPData parseBindVCPFPData = new ParseDeviceData.ParseBindVCPFPData();
                parseBindVCPFPData.parseBytes(recbuf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] bt_send, DatagramSocket socket) throws IOException {
        InetAddress address = InetAddress.getByName(Constants.GW_IP_ADDRESS);
        DatagramPacket dp = new DatagramPacket(bt_send, bt_send.length, address,Constants.UDP_PORT);
        socket.send(dp);
        System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));
        byte[] bs = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        socket.receive(packet);
        System.out.println("getMessage = " + Arrays.toString(bs));
    }
}

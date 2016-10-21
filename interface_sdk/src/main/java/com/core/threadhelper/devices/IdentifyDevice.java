package com.core.threadhelper.devices;

import android.content.Context;

import com.core.cmddata.DeviceCmdData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.NetworkUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/9/20.
 */

public class IdentifyDevice implements Runnable {
    private int second = 1;
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private Context mContext;

    public IdentifyDevice(Context context, AppDevice appDevice, int second) {
        this.appDevice = appDevice;
        this.second = second;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("当前为远程通讯 = " + "IdentifyDevice");
                byte[] bt_send = DeviceCmdData.IdentifyDeviceCmd(appDevice, second);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                byte[] bt_send = DeviceCmdData.IdentifyDeviceCmd(appDevice, second);
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

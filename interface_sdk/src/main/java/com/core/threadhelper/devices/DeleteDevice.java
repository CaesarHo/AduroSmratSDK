package com.core.threadhelper.devices;

import android.content.Context;

import com.core.cmddata.DeviceCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/7/12.
 */
public class DeleteDevice implements Runnable {
    private DatagramSocket socket = null;
    private String devicemac;
    private Context mContext;

    public DeleteDevice(Context context, String devicemac) {
        this.devicemac = devicemac;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("当前为远程通讯 = " + "DeleteDevice");
                byte[] bt_send = DeviceCmdData.DeleteDeviceCmd(devicemac);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.GW_IP_ADDRESS == null && Constants.APP_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                byte[] bt_send = DeviceCmdData.DeleteDeviceCmd(devicemac);
                System.out.println("SendDeleteDeviceCmd = " + Utils.binary(bt_send, 16));
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);

                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

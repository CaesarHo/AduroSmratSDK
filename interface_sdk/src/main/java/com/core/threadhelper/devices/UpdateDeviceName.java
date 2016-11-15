package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by best on 2016/7/12.
 */
public class UpdateDeviceName implements Runnable {
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private String device_name;
    private Context mContext;

    public UpdateDeviceName(Context context, AppDevice appDevice, String device_name) {
        this.appDevice = appDevice;
        this.device_name = device_name;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "UpdateDeviceName");
                byte[] bt_send = DeviceCmdData.sendUpdateDeviceCmd(appDevice, device_name);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
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
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));


                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

                    socket.receive(packet);
                    System.out.println("当前接收的数据UpdateDeviceName = " + Arrays.toString(recbuf));

                    String str = FtFormatTransfer.bytesToUTF8String(recbuf);// new String(recbuf);

                    int strToint = str.indexOf(":");
                    String isGroup = "";
                    if (strToint >= 0) {
                        isGroup = str.substring(strToint - 4, strToint);
                        Log.i("isGroup = ", isGroup);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

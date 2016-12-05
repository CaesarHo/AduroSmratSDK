package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/12.
 */
public class UpdateDeviceName implements Runnable {
    private Context mContext;
    private DatagramSocket socket = null;
    private AppDevice appDevice;
    private String device_name;

    public UpdateDeviceName(Context context, AppDevice appDevice, String device_name) {
        this.mContext = context;
        this.appDevice = appDevice;
        this.device_name = device_name;
    }

    @Override
    public void run() {
        try {
            byte[] bt_send = DeviceCmdData.sendUpdateDeviceCmd(appDevice, device_name);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "UpdateDeviceName");
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (!socket.isClosed()){
                socket.close();
            }
        }
    }
}

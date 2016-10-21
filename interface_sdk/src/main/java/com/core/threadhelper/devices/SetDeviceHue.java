package com.core.threadhelper.devices;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.DeviceCmdData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/13.
 */
public class SetDeviceHue implements Runnable {
    private DatagramSocket socket = null;
    private int hue = -1;
    private AppDevice appDevice;
    private Context mContext;

    public SetDeviceHue(Context context,AppDevice appDevice, int hue) {
        this.appDevice = appDevice;
        this.hue = hue;
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "setDeviceHue");
                byte[] bt_send = DeviceCmdData.setDeviceHueCmd(appDevice, hue);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                byte[] bt_send = DeviceCmdData.setDeviceHueCmd(appDevice, hue);
                System.out.println("十六进制HUE = " + Utils.binary(bt_send, 16));

                DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
                socket.send(packet_send);

                // 接收数据
                byte[] recbuf = new byte[1024];
                final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

                socket.receive(packet);
                Log.i("receive_huesat = ", packet.getData().toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

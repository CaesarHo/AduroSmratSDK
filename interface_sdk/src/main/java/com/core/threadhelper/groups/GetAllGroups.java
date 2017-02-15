package com.core.threadhelper.groups;

import android.content.Context;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllGroups implements Runnable {
    private Context mContext;
    public DatagramSocket socket = null;
    public GetAllGroups(Context context){
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "getGroups");
                boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.MQTT_CLIENT_ID);
                if (!isConnect){
                    return;
                }
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + TransformUtils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    try {
                        socket.receive(packet);
                    } catch (InterruptedIOException e) {
                        System.out.println("continue....................");
                        continue;  //非阻塞循环Operation not permitted
                    }
                    if (!Utils.isK6(recbuf)) {
                        System.out.println("当前接收的数据GetAllGroups = " + Arrays.toString(recbuf));
                        DataPacket.getInstance().BytesDataPacket(mContext, recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

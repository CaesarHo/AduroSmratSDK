package com.core.threadhelper.scenes;

import android.content.Context;
import com.core.commanddata.appdata.SceneCmdData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.TransformUtils;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/13.
 */
public class AddDeviceToSence implements Runnable {
    private Context mContext;
    private DatagramSocket socket = null;
    private byte[] bt_send;
    private Short scene_id;
    private Short group_id;
    private AppDevice appDevice;

    public AddDeviceToSence(Context context, AppDevice appDevice, short group_id, short scene_id) {
        this.mContext = context;
        this.appDevice = appDevice;
        this.scene_id = scene_id;
        this.group_id = group_id;
    }

    @Override
    public void run() {
        try {
            bt_send = SceneCmdData.Add_DeviceToScene(appDevice, (int) group_id, (int) scene_id);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "addDeviceToSence");
                MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(mContext), 2);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
                //添加设备到场景后，间隔两百毫秒发送存储场景
                Thread.sleep(500);
                byte[] store_scene = SceneCmdData.StoreScene(appDevice, group_id, scene_id);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, store_scene);
            } else {
                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + TransformUtils.binary(bt_send, 16));

                //添加设备到场景后，间隔两百毫秒发送存储场景
                Thread.sleep(500);
                byte[] store_scene = SceneCmdData.StoreScene(appDevice, group_id, scene_id);
                new Thread(new UdpClient(mContext,store_scene)).start();
//                Constants.sendMessage(store_scene);
                byte[] recbuf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                socket.receive(packet);
                System.out.println("当前接收到的数据AddDeviceToSence = " + Arrays.toString(recbuf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
//                socket.close();
            }
        }
    }
}

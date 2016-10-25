package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.utils.NetworkUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by best on 2016/7/13.
 */
public class SetGroupState implements Runnable {
    private Context mContext;
    private DatagramSocket m_CMDSocket = null;
    private int state = -1;
    private int group_id = -1;

    public SetGroupState(Context context, int group_id, int state) {
        this.mContext = context;
        this.group_id = group_id;
        this.state = state;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "setGroupState");
                byte[] bt_send = GroupCmdData.setGroupState(group_id, state);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                if (m_CMDSocket == null) {
                    m_CMDSocket = new DatagramSocket(null);
                    m_CMDSocket.setReuseAddress(true);
                    m_CMDSocket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                InetAddress serverAddr = InetAddress.getByName(Constants.GW_IP_ADDRESS);

                byte[] bt_send = GroupCmdData.setGroupState(group_id, state);
                DatagramPacket packet_send = new DatagramPacket(bt_send, bt_send.length, serverAddr, Constants.UDP_PORT);
                m_CMDSocket.send(packet_send);

                // 接收数据
                byte[] buf = new byte[1024];
                DatagramPacket packet_receive = new DatagramPacket(buf, buf.length);
                m_CMDSocket.receive(packet_receive);

                //当result等于1时删除成功,0删除失败
                DataSources.getInstance().setDeviceStateResule(1);
            }
        } catch (Exception e) {
            Log.e("deviceinfo IOException", "Client: Error!");
        }
    }
}

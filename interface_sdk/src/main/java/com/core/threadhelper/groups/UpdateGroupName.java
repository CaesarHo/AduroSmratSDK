package com.core.threadhelper.groups;

import android.content.Context;
import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/11.
 */
public class UpdateGroupName implements Runnable {
    private Context mContext;
    private DatagramSocket socket = null;
    private short group_id;
    private String group_name = "";

    public UpdateGroupName(Context context, short group_id, String group_name) {
        this.mContext = context;
        this.group_id = group_id;
        this.group_name = group_name;
    }

    @Override
    public void run() {
        try {
            byte[] bt_send = GroupCmdData.sendUpdateGroupCmd((int) group_id, group_name);
            if (GW_IP_ADDRESS.equals("")) {//!NetworkUtil.NetWorkType(mContext)
                System.out.println("远程打开 = " + "ChangeGroupName");
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

                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    System.out.println("当前接收的数据UpdateGroupName = " + Arrays.toString(recbuf));
                    DataPacket.getInstance().BytesDataPacket(mContext,recbuf);

                    if (recbuf[11] == MessageType.A.CHANGE_GROUP_NAME.value()) {
                        byte[] group_name_bt = group_name.getBytes("utf-8");
                        ParseGroupData.ParseModifyGroupInfo groupInfo = new ParseGroupData.ParseModifyGroupInfo();
                        groupInfo.parseBytes(recbuf,group_name_bt.length);
                        DataSources.getInstance().ChangeGroupName(groupInfo.group_id, groupInfo.group_name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

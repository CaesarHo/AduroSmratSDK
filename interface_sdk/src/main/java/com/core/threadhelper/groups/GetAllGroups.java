package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GroupCmdData;
import com.core.commanddata.gwdata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
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
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    String isK64 = new String(recbuf).trim();
                    if (isK64.contains("K64")) {
                        return;
                    }
                    System.out.println("当前接收的数据GetAllGroups = " + Arrays.toString(recbuf));
                    DataPacket.getInstance().BytesDataPacket(mContext,recbuf);
                    /**
                     * 枚举A判断是否是房间
                     */
                    if ((int) MessageType.A.GET_ALL_GROUP.value() == recbuf[11]){
                        ParseGroupData.ParseGetGroupsInfo(recbuf);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

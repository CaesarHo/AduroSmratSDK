package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.cmddata.parsedata.ParseGroupData;
import com.core.db.GatewayInfo;
import com.core.entity.AppGroup;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.threadhelper.UDPHelper;
import com.core.threadhelper.devices.GetAllDevices;
import com.core.utils.FtFormatTransfer;
import com.core.utils.NetworkUtil;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by best on 2016/7/13.
 */
public class GetAllGroups implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    public DatagramSocket socket = null;
    public GetAllGroups(Context context){
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "getGroups");
                byte[] bt_send = GroupCmdData.GetAllGroupListCmd();
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                if (Constants.APP_IP_ADDRESS == null && Constants.GW_IP_ADDRESS == null) {
                    DataSources.getInstance().SendExceptionResult(0);
                    return;
                }

                //获取组列表命令
                bt_send = GroupCmdData.GetAllGroupListCmd();
                Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("GetAllGroupsCMD = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("GetAllGroups = " + Arrays.toString(recbuf));
                    String str = FtFormatTransfer.bytesToUTF8String(recbuf);
                    if ((int) MessageType.A.GET_ALL_GROUP.value() == recbuf[11]){
                        //解析接受到的数据
                        ParseGroupData.ParseGetGroupsInfo(str);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

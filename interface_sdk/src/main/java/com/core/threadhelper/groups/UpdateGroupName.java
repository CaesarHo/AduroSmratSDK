package com.core.threadhelper.groups;

import android.content.Context;
import android.util.Log;

import com.core.cmddata.GroupCmdData;
import com.core.db.GatewayInfo;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.mqtt.MqttManager;
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
import java.util.Arrays;

/**
 * Created by best on 2016/7/11.
 */
public class UpdateGroupName implements Runnable {
    private Context mContext;
    private byte[] bt_send;
    private DatagramSocket socket = null;
    private short group_id;
    private String group_name = "";
    private String Group_Id = "";

    public UpdateGroupName(Context context,short group_id, String group_name) {
        this.mContext = context;
        this.group_id = group_id;
        this.group_name = group_name;
    }

    @Override
    public void run() {
        try {
            if (!NetworkUtil.NetWorkType(mContext)) {
                System.out.println("远程打开 = " + "ChangeGroupName");
                byte[] bt_send = GroupCmdData.sendUpdateGroupCmd((int) group_id, group_name);
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(mContext), 2, bt_send);
            } else {
                //获取组列表命令
                bt_send = GroupCmdData.sendUpdateGroupCmd(group_id, group_name);
                Log.i("网关IP = ", Constants.GW_IP_ADDRESS);

                InetAddress inetAddress = InetAddress.getByName(Constants.GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }

                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("Update发送的十六进制数据 = " + Utils.binary(bt_send, 16));

                while (true) {
                    final byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("UpdateGroup_out = " + Arrays.toString(recbuf));

                    String str = FtFormatTransfer.bytesToUTF8String(recbuf);

                    int strToint = str.indexOf(":");
                    String isGroup = "";
                    if (strToint >= 0) {
                        isGroup = str.substring(strToint - 4, strToint);
                        Log.i("isGroup = ", isGroup);
                    }

                    if (str.contains("GW") && !str.contains("K64") && isGroup.contains("upId")) {
                        String[] group_data = str.split(",");
                        //get groups
                        for (int i = 1; i < group_data.length; i++) {
                            if (group_data.length <= 2) {
                                return;
                            }

                            String[] Id_Source = group_data[0].split(":");
                            String[] Name_Source = group_data[1].split(":");

                            if (Id_Source.length > 1 && Name_Source.length > 1) {
                                if (Id_Source.length >= 3) {
                                    Group_Id = Id_Source[2];
                                    System.out.println("Source1 = " + Arrays.toString(Id_Source));
                                } else {
                                    Group_Id = Id_Source[1];
                                    System.out.println("Source2 = " + Arrays.toString(Id_Source));
                                }
                                group_name = Utils.toStringHex2(Name_Source[1]);
                                System.out.println("Source3 = " + Arrays.toString(Name_Source));
                            }

                            Log.i("UpdateId_Source = ", Group_Id);
                            Log.i("UpdateName_Source = ", group_name);
                        }
                        DataSources.getInstance().ChangeGroupName(group_id, group_name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
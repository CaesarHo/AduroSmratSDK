package com.core.threadhelper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.core.commanddata.DataPacket;
import com.core.commanddata.appdata.GatewayCmdData;
import com.core.commanddata.gwdata.ParseGatewayData;
import com.core.connectivity.UdpClient;
import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.FTPUtils;
import com.core.utils.TransformUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.GatewayInfo.COUNT;
import static com.core.global.Constants.GatewayInfo.GATEWAY_UPDATE_FILE_NEXT;
import static com.core.global.Constants.GatewayInfo.PACKETS;
import static com.core.global.Constants.GatewayInfo.SEND_SIZE;
import static com.core.global.Constants.GatewayInfo.UPDATE_FILE_BT;
import static com.core.global.Constants.context;

/**
 * Created by best on 2016/12/26.
 */

public class UpdateHelper implements Runnable {
    public static final String TAG = "UpdateHelper";
    private DatagramSocket socket = null;
    private Context context;

    public UpdateHelper(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            String file_name = GatewayInfo.getInstance().getGatewayUpdateFileName(context);
            String local_path = Environment.getExternalStorageDirectory().getPath() + "/gateway_update_file/" + file_name;
            if (file_name.equals("")){
                System.out.println("文件名称为空");
                return;
            }
            File local_file = new File(local_path);

            int size = new Long(local_file.length()).intValue();//本地文件总的大小
            System.out.println("文件总的大小" + " = " + size);
            if (size == 0) {
                SerialHandler.getInstance().CheckUpdateGatewayInfo();
                return;
            }
            int version = GatewayInfo.getInstance().getGateWayUpdateVserion(context);
            byte[] bytes_data = GatewayCmdData.StartUpdateVerCmd(version, size);
            new Thread(new UdpClient(context, bytes_data)).start();
            Log.i(TAG + " 启动更新命令 = " , Arrays.toString(bytes_data));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SEND_SIZE = GatewayInfo.getInstance().getPacketSize(context);//发送的大小
            System.out.println("每包发送的长度" + " = " + SEND_SIZE);
            if (SEND_SIZE == 0) {
                System.out.println("启动更新错误，版本相同无需更新！");
                return;
            }else if (SEND_SIZE == -1){
                SerialHandler.getInstance().CheckUpdateGatewayInfo();
                return;
            }

            PACKETS = (size / SEND_SIZE) + 1;
            System.out.println("发送总的包数" + " = " + PACKETS);

            InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                socket.setSoTimeout(3000);
            }
            int i;
            int count = 1;
            FileInputStream fin = new FileInputStream(local_file);
            byte[] bytes = new byte[SEND_SIZE];
            while ((i = fin.read(bytes, 0, SEND_SIZE)) != -1) {
                try {
                    byte[] send_byte = GatewayCmdData.FileDataToGatewayCmd(PACKETS, count, bytes);
                    DatagramPacket data = new DatagramPacket(send_byte, send_byte.length, inetAddress, Constants.UDP_PORT);
                    socket.send(data);
                    Log.i(TAG + " 发送的byte = ", Arrays.toString(bytes));
                    while (true) {
                        byte[] rec_byte = new byte[128];
                        DatagramPacket getack = new DatagramPacket(rec_byte, rec_byte.length);

                        socket.receive(getack);
                        if ((int) MessageType.A.SEND_UPDATE_FILE_TO_GATEWAY.value() == rec_byte[11]) {
                            ParseGatewayData.ParseUpdateCountData startUpdateData = new ParseGatewayData.ParseUpdateCountData();
                            startUpdateData.parseBytes(rec_byte);

                            if (count == startUpdateData.packet_count) {
                                if (startUpdateData.packet_count == PACKETS) {//最后确认包
                                    String crc32 = GatewayInfo.getInstance().getGatewayUpdateCRC32(context);
                                    byte[] bt = GatewayCmdData.FinallyUpdateCmd(crc32);
                                    DatagramPacket packet = new DatagramPacket(bt, bt.length, inetAddress, Constants.UDP_PORT);
                                    socket.send(packet);
                                    Log.i(TAG + " = ", TransformUtils.bytesToHexString(bt));
                                    System.out.println("发送最后一包 = " + TransformUtils.bytesToHexString(bt));
                                    boolean isRun = true;
                                    while (isRun){
                                        try {
                                            try {
                                                socket.receive(getack);
                                            }catch (SocketTimeoutException s){
                                                s.getLocalizedMessage();
                                                System.out.println("网络超时");
                                            }

                                            if (rec_byte[32] == 0){
                                                System.out.println("更新成功");
                                                fin.close();
                                                if (socket != null){
                                                    socket.close();
                                                }
                                                isRun = false;
                                                DataSources.getInstance().GatewayUpdateResult(0);
                                            }else{
                                                System.out.println("更新失败");
                                                DataSources.getInstance().GatewayUpdateResult(1);
                                            }
                                            Log.i(TAG , " 最后一包返回值 = " + Arrays.toString(rec_byte));
                                        } catch (SocketTimeoutException e) {
                                            e.getLocalizedMessage();
                                        }
                                    }
                                }
                                System.out.println("发送下一包 = " + count);
                                count++;
                                break;
                            }
                        } else {
                            Thread.sleep(1000);
                            System.out.println("未接收到返回重新发送 = " + count);
                            socket.send(data);
                        }
                    }
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally = " + "UpdateHelper");
        }
    }
}

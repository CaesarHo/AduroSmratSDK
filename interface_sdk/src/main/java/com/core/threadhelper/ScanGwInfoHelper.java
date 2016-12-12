package com.core.threadhelper;

import android.content.Context;

import com.core.commanddata.appdata.DeviceCmdData;
import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.entity.AppGateway;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.global.MessageType;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static com.core.global.Constants.GW_IP_ADDRESS;
import static com.core.global.Constants.isScanGwNodeVer;

/**
 * Created by best on 2016/11/30.
 */

public class ScanGwInfoHelper implements Runnable {

    private DatagramSocket socket = null;
    private Context context;
    private byte[] bt_send = null;
    private byte[] recbuf = new byte[1024];
    public ScanGwInfoHelper(Context context, byte[] bt_send) {
        this.context = context;
        this.bt_send = bt_send;
    }

    @Override
    public void run() {
        try {
            if (GW_IP_ADDRESS.equals("")){
                MqttManager.getInstance().publish(GatewayInfo.getInstance().getGatewayNo(context), 2, bt_send);
            }else{
                InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                if (socket == null) {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                }
                DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                socket.send(datagramPacket);
                System.out.println("当前发送的数据 = " + Utils.binary(bt_send, 16));

                while (!isScanGwNodeVer) {
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                    socket.receive(packet);
                    System.out.println("ScanGwInfoHelper = " + Arrays.toString(recbuf));
                    /**
                     * 解析网关信息
                     */
                    int gw_info = (int) MessageType.A.GET_GW_INFO.value();
                    int node_ver = (int) MessageType.A.GET_NODE_VER.value();

                    if (gw_info == recbuf[11]) {
                        ParseDeviceData.ParseGWInfoData gwInfoData = new ParseDeviceData.ParseGWInfoData();
                        gwInfoData.parseBytes(recbuf);
                        GatewayInfo.getInstance().setFirmwareVersion(context,gwInfoData.gw_version);
                        GatewayInfo.getInstance().setGatewayMac(context,gwInfoData.gw_mac);
                        GatewayInfo.getInstance().setBootrodr(context,gwInfoData.gw_bootrodr);
                    }
                    if (node_ver == recbuf[11]){
                        ParseDeviceData.ParseNodeVer parseNodeVer = new ParseDeviceData.ParseNodeVer();
                        parseNodeVer.parseBytes(recbuf);

                        String ip_address = GatewayInfo.getInstance().getInetAddress(context);
                        String gateway_no = GatewayInfo.getInstance().getGatewayNo(context);
                        String gateway_mac = GatewayInfo.getInstance().getGatewayMac(context);
                        int gateway_version = GatewayInfo.getInstance().getFirmwareVersion(context);
                        int gateway_bootrodr = GatewayInfo.getInstance().getBootrodr(context);

                        AppGateway appGateway = new AppGateway();
                        appGateway.setGateway_no(gateway_no);
                        appGateway.setGateway_mac(gateway_mac);
                        appGateway.setBootrodr(gateway_bootrodr);
                        appGateway.setIp_address(ip_address);
                        appGateway.setGateway_version(gateway_version);
                        appGateway.setNode_main_version(parseNodeVer.major_ver);
                        appGateway.setNode_installed_version(parseNodeVer.Install_ver);
                        DataSources.getInstance().GatewayInfo(appGateway);
                        isScanGwNodeVer = true;
                        recbuf = null;
                        if (!socket.isClosed()){
                            socket.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.core.threadhelper;

import com.core.data.DeviceCmdData;
import com.core.global.Constants;
import com.core.gatewayinterface.DataSources;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/7/12.
 */
public class SendDeleteDeviceCmd implements Runnable{
    private static final int PORT = 8888;
    private DatagramSocket socket = null;
    private String  devicemac;
    public SendDeleteDeviceCmd (String devicemac){
        this.devicemac = devicemac;
    }

    @Override
    public void run() {

        if (UDPHelper.localip == null && Constants.ipaddress == null){
            DataSources.getInstance().SendExceptionResult(0);
            return ;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.ipaddress);
            if(socket==null){
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            }

            byte[] bt_send = DeviceCmdData.DeleteDeviceCmd(devicemac);
            System.out.println("SendDeleteDeviceCmd = " + Utils.binary(Utils.hexStringToByteArray(Utils.binary(bt_send,16)),16));
            DatagramPacket datagramPacket = new DatagramPacket(bt_send,bt_send.length,inetAddress,PORT);
            socket.send(datagramPacket);

            byte[] recbuf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(recbuf,recbuf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("SendDeleteDeviceCmd_Rec : ‘" + new String(packet.getData()).trim() + "’\n");
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

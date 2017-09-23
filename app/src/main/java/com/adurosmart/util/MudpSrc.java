package com.adurosmart.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by best on 2016/10/28.
 */

public class MudpSrc {

    int port = 6789;

    public void sendMessage(String msg, MulticastSocket socket) throws IOException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(ostream);
        dataStream.writeUTF(msg);
        dataStream.close();

        byte[] data = ostream.toByteArray();

        InetAddress address = InetAddress.getByName("230.3.3.3");
        socket.joinGroup(address);
        DatagramPacket dp = new DatagramPacket(data, data.length, address, port);
        socket.send(dp);
    }

    public void getMessage(MulticastSocket socket) throws IOException {
        byte[] bs = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        socket.receive(packet);

        DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength()));

        String msg = istream.readUTF();

        System.out.println(msg);
    }

//    public static void main(String args[]) throws IOException {
//        MudpSrv srv = new MudpSrv();
//        MulticastSocket socket = new MulticastSocket(srv.port);
//        srv.sendMessage("萤火虫，皎白月光，游过四季", socket);
//        srv.getMessage(socket);
//    }


}

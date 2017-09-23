package com.adurosmart.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by best on 2017/1/9.
 */

public class TcpClient {
    Socket socket = null;
    int port = 2016;
    InetAddress ipAddress;
    byte[] data;
    public static final int SEARCH_AP_DEVICE = 0x66;

    public TcpClient(byte[] data) {
        this.data = data;
    }

    public void setIpdreess(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void createClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("receivedata");
                    socket = new Socket(ipAddress, port);
                    if (socket.isConnected()) {
                        startListen();
                        System.out.println("receivedata , connect---------------------");
                        OutputStream os = socket.getOutputStream();
                        os.write(data);
                        os.flush();
                    } else {
                        int isconnect = 1;
                        while (isconnect == 1) {
                            try {
                                socket = new Socket(ipAddress, port);
                                Thread.sleep(100);
                                System.out.println("receivedata , connect+++++++++++++++");
                                if (socket.isConnected()) {
                                    startListen();
                                    OutputStream os = socket.getOutputStream();
                                    os.write(data);
                                    os.flush();
                                    isconnect = 0;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startListen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream;
                try {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InputStream in = socket.getInputStream();
                    byte[] buffer = new byte[272];
                    in.read(buffer);
                    String s = "";
                    for (int i = 0; i < buffer.length; i++) {
                        s = s + buffer[i] + " ";
                    }
                    System.out.println("receivedata , data=" + s);
                    if (buffer.length < 0) {
                        return;
                    }
                    if (buffer[0] == 3) {
                        int result = bytesToInt(buffer, 4);
                        System.out.println("receivedata , result=" + result);
                    } else if (buffer[0] == 1 && buffer[4] == 1 && buffer.length >= 20) {
                        int id = bytesToInt(buffer, 16);
                        int ip = bytesToInt(buffer, 12);
                        System.out.println("receivedata , contactId=" + id + "--" + "ip=" + ip);

                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }
}

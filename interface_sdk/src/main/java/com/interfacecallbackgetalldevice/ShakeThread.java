package com.interfacecallbackgetalldevice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class ShakeThread extends Thread {
    public static final int DEFAULT_PORT = 8888;
    public static final int RECEIVE_IPC_INFO = 0;
    public static final int CLOSE_SERVER = 999;
    public int SEND_TIMES;

    private int port;
    private boolean isRun;

    private DatagramSocket server;
    private DatagramSocket broadcast;
    private Selector selector;
    private DatagramChannel channel;
    private Handler handler;

    private InetAddress host;

    public ShakeThread(Handler handler) {
        this.port = DEFAULT_PORT;
        this.SEND_TIMES = 10;
    }

    public void setSearchTime(long time) {
        SEND_TIMES = (int) (time / 1000);
    }

    public void setInetAddress(InetAddress host) {
        this.host = host;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        isRun = true;
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            server = channel.socket();
            server.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

            new Thread() {
                @Override
                public void run() {
                    try {
                        int times = 0;
                        broadcast = new DatagramSocket();
                        broadcast.setBroadcast(true);
                        while (times < SEND_TIMES) {
                            if (!isRun) {
                                return;
                            }
                            times++;
                            Log.e("my", "shake thread send broadcast.");

                            ShakeData data = new ShakeData();
                            data.setCmd(ShakeData.Cmd.GET_DEVICE_LIST);
                            DatagramPacket packet = new DatagramPacket(ShakeData.getBytes(data), 100,
                                    InetAddress.getByName("255.255.255.255"), port);
                            broadcast.send(packet);
                            Thread.sleep(1000);
                        }

                        Log.e("my", "shake thread broadcast end.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            broadcast.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ShakeManager.getInstance().stopShaking();
                    }
                }
            }.start();

            while (isRun) {

                int n = selector.select(100);
                if (n > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (SelectionKey key : keys) {
                        keys.remove(key);
                        if (key.isReadable()) {
                            DatagramChannel dc = (DatagramChannel) key.channel();
                            InetSocketAddress client = (InetSocketAddress) dc.receive(receiveBuffer);
                            key.interestOps(SelectionKey.OP_READ);
                            receiveBuffer.flip();
                            ShakeData data = ShakeData.getShakeData(receiveBuffer);

                            switch (data.getCmd()) {
                                case ShakeData.Cmd.RECEIVE_DEVICE_LIST:

                                    if (data.getError_code() == 1) {
                                        if (null != handler) {
                                            Message msg = new Message();
                                            msg.what = ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO;
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("address", client.getAddress());
                                            bundle.putString("id", data.getId() + "");
                                            bundle.putString("name", data.getName() + "");
                                            bundle.putInt("flag", data.getFlag());
                                            bundle.putInt("type", data.getType());
                                            bundle.putInt("rtspflag", data.getRightCount());
                                            msg.setData(bundle);
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    }
                                    receiveBuffer.clear();
                            }
                        }
                    }
                }
            }
            Log.e("my", "shake thread end.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ShakeManager.getInstance().stopShaking();

            if (null != handler) {
                Message msg = new Message();
                msg.what = ShakeManager.HANDLE_ID_SEARCH_END;
                handler.sendMessage(msg);
            }

            try {
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void killThread() {
        if (isRun) {
            selector.wakeup();
            isRun = false;
        }
    }
}

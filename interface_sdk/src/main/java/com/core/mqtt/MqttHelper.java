package com.core.mqtt;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import com.core.db.GatewayInfo;
import com.core.gatewayinterface.DataSources;
import com.core.global.Constants;
import com.core.mqtt.MqttManager;
import com.core.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by best on 2016/9/28.
 */

public class MqttHelper implements Runnable {

    @Override
    public void run() {
        //連接MQTT服務器
        boolean isConnect = MqttManager.getInstance().creatConnect(Constants.uri, null, null, Constants.clientId);
        System.out.println("isConnected: " + isConnect);
        if (isConnect) {
            MqttManager.getInstance().subscribe("170005203637", 2);
        }
//        while (!isConnect){
//            //連接MQTT服務器
//            boolean isConnect2 = MqttManager.getInstance().creatConnect(Constants.uri, null, null, Constants.clientId);
//            //如果連接成功則訂閱指定主題
//            if (isConnect2) {
//                MqttManager.getInstance().subscribe("170005203637", 2);
//            }
//        }
    }
}

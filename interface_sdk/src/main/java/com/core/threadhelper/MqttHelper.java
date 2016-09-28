package com.core.threadhelper;

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

public class MqttHelper implements Runnable{
    private Context context;

    public MqttHelper(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Constants.clientId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("clientId: " + Constants.clientId);
        //連接MQTT服務器
        boolean isConnect = MqttManager.getInstance().creatConnect(Constants.uri,null,null,Constants.clientId);
        System.out.println("isConnected: " + isConnect);
        //如果連接成功則訂閱指定主題
        if (isConnect){
            MqttManager.getInstance().subscribe(GatewayInfo.getInstance().getGatewayNo(context),2);
        }
    }
}

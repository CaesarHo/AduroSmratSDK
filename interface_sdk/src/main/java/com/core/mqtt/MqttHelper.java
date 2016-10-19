package com.core.mqtt;

import com.core.global.Constants;

/**
 * Created by best on 2016/9/28.
 */

public class MqttHelper implements Runnable {

    @Override
    public void run() {
        //連接MQTT服務器
        boolean isConnect = MqttManager.getInstance().creatConnect(Constants.URI, null, null, Constants.CLIENT_ID);
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

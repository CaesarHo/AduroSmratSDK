package com.core.mqtt;

import android.content.Context;
import android.util.Log;

import com.core.gatewayinterface.SerialHandler;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import static com.core.global.Constants.URI;

/**
 * Created by best on 2016/9/28.
 */

public class MqttManager {
    private static final String TAG = "MqttManager";
    private Context mContext;
    // 单例
    private static MqttManager mInstance = null;
    // 回调
    private static MqttCallback mCallback;
    // Private instance variables
    private MqttClient client;
    private MqttConnectOptions conOpt;
    private boolean clean = true;

    public MqttManager() {

    }

    public static MqttManager getInstance() {
        if (null == mInstance) {
            mInstance = new MqttManager();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mCallback = new MqttCallbackBus(mContext);
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {
            System.out.println(TAG + "release = " +e.getMessage());
        }
    }

    /**
     * 创建Mqtt 连接
     *
     * @param brokerUrl Mqtt服务器地址
     * @param userName  用户名
     * @param password  密码
     * @param clientId  clientId
     * @return
     */
    public boolean creatConnect(String brokerUrl, String userName, String password, String clientId) {
        boolean flag = false;
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
        try {
            // Construct the connection options object that contains connection parameters such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            conOpt.setCleanSession(clean);
            if (password != null) {
                conOpt.setPassword(password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(userName);
            }
            if (clientId != null){
                // Construct an MQTT blocking mode client
                client = new MqttClient(brokerUrl, clientId, dataStore);
            }
            // Set this wrapper as the callback handler
            client.setCallback(mCallback);
            if (!client.isConnected()) {
                System.out.println("正在连接 creatConnect = " + client.getClientId());
                flag = doConnect();
            }
        } catch (MqttException e) {
            System.out.println(TAG + "creatConnect = " +e.getMessage());
        }
        return flag;
    }

    /**
     * 建立连接
     *
     * @return
     */
    public boolean doConnect() {
        boolean flag = false;
        if (client != null) {
            try {
                client.connect(conOpt);
                Log.d(TAG, "Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
                flag = true;
            } catch (Exception e) {
                System.out.println(TAG + "doConnect = " +e.getMessage());
            }
        }
        return flag;
    }

    /**
     * Publish / send a message to an MQTT server
     *
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    public boolean publish(String topicName, int qos, byte[] payload) {
        boolean flag = false;
        if (client.isConnected()) {
            Log.d(TAG, "Publishing to topic \"" + topicName + "\" qos " + qos);
            // Create and configure a message
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);
            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client.publish(topicName, message);
                flag = true;
                System.out.println("发布状态 = " + flag);
            } catch (MqttException e) {
                System.out.println(TAG + "publish = " +e.getMessage());
            }
        } else {
            System.out.println("正在连接 publish = " + client.getClientId());
            SerialHandler.getInstance().setMqttCommunication(mContext,topicName);
        }
        return flag;
    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client.isConnected()) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            Log.e(TAG, "Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
                System.out.println("订阅状态 = " + flag);
            } catch (MqttException e) {
                System.out.println(TAG + " publish = " +e.getMessage());
            }
        } else {
            System.out.println("正在连接 subscribe = " + client.getClientId());
            SerialHandler.getInstance().setMqttCommunication(mContext,topicName);
        }
        return flag;
    }

    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }
}

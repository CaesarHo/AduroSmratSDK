package org.eclipse.paho.android.service.sample;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.adurosmart.global.Constants;
import com.adurosmart.mqtt.MQTTHelper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MyApp extends Application {
	public static final String MAIN_SERVICE_START = Constants.PACKAGE_NAME  + "service.MAINSERVICE";
	public static MyApp app;
	public static String mServer = "www.adurosmart.com";
	public static String clientId;
	public static int port = 1883;
	public static String mAndroidId;
	public String clientHandle = null;
	MQTTHelper mqttHelper;
	Thread thread;

	@Override
	public void onCreate() {
		app = this;
		super.onCreate();
		mAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		clientId = mAndroidId;
		connectAction();
		new Thread(new MyThread()).start();//创建线程判断如果MQTT掉线能重连

		//订阅MQTT
		mqttHelper = new MQTTHelper(app,clientHandle);
		thread = new Thread(mqttHelper);
		thread.start();
	}

	public void connectAction(){
		String uri = "tcp://" + mServer + ":" + port;
		MqttConnectOptions conOpt = new MqttConnectOptions();
		MqttAndroidClient client = Connections.getInstance(this).createClient(this, uri, clientId);

		// create a client handle
		clientHandle = uri + clientId;
		Connection connection = new Connection(clientHandle, clientId, mServer, port, this, client, false);
		client.setCallback(new MqttCallbackHandler(this, clientHandle));

		//set traceCallback
		client.setTraceCallback(new MqttTraceCallback());

		connection.addConnectionOptions(conOpt);
		Connections.getInstance(this).addConnection(connection);
		final ActionListener callback = new ActionListener(this, ActionListener.Action.CONNECT, clientHandle, clientId);
		try {
			client.connect(conOpt, null, callback);
		} catch (MqttException e) {
			Log.e(this.getClass().getCanonicalName(), "MqttException Occured", e);
		}
	}

	/**
	 * Reconnect the selected client
	 */
	private void reconnect() {
//		Connections.getInstance(app).getConnection(clientHandle).changeConnectionStatus(ConnectionStatus.CONNECTING);
		Connection c = Connections.getInstance(app).getConnection(clientHandle);
		try {
			c.getClient().connect(c.getConnectionOptions(), null, new ActionListener(app, ActionListener.Action.CONNECT, clientHandle, null));
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle, e);
			c.addAction("Client failed to connect");
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					if (!Connections.getInstance(app).getConnection(clientHandle).isConnected()){
						reconnect();
					}
					break;
			}
		}
	};

	public class MyThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);// 线程暂停1秒，单位毫秒
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);// 发送消息
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

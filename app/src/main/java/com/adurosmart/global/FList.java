package com.adurosmart.global;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.adurosmart.bean.LocalDevice;
import com.adurosmart.udpshake.ShakeManager;
import com.adurosmart.utils.Utils;

import org.eclipse.paho.android.service.sample.MyApp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class FList {
	private static FList manager = null;
	private static List<LocalDevice> localdevices = new ArrayList<LocalDevice>();
	private static List<LocalDevice> tempLocalDevices = new ArrayList<LocalDevice>();
	private static List<LocalDevice> foundLocalDevices = new ArrayList<LocalDevice>();
	// 局域网内搜索到的全部的设备
	private static List<LocalDevice> allLocalDevices = new ArrayList<LocalDevice>();

	public FList() {
		if (null != localdevices) {
			localdevices.clear();
		}
		if (null != foundLocalDevices) {
			foundLocalDevices.clear();
		}
		if (null != allLocalDevices) {
			allLocalDevices.clear();
		}
		manager = this;
	}

	public static FList getInstance() {
		if (manager == null) {
			manager = new FList();
		}
		return manager;
	}

	public synchronized void searchLocalDevice() {
		try {
			ShakeManager.getInstance().setSearchTime(3000);
			ShakeManager.getInstance().setInetAddress(Utils.getIntentAddress(MyApp.app));
			ShakeManager.getInstance().setHandler(mHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (ShakeManager.getInstance().shaking()) {
			tempLocalDevices.clear();
		}
	}

	public List<LocalDevice> getLocalDevices() {
		return this.localdevices;
	}

	public String getCompleteIPAddress(String contactid) {
		String ip = "";
		for (LocalDevice ss : tempLocalDevices) {
			if (ss.contactId.equals(contactid)) {
				ip = ss.address.getHostAddress();
			}
		}
		return ip;
	}

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ShakeManager.HANDLE_ID_SEARCH_END:
				localdevices.clear();
				allLocalDevices.clear();
				foundLocalDevices.clear();
				for (LocalDevice localDevice : tempLocalDevices) {
					localdevices.add(localDevice);
					foundLocalDevices.add(localDevice);
					allLocalDevices.add(localDevice);
				}

				Intent i = new Intent();
				i.setAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
				MyApp.app.sendBroadcast(i);
				break;
			case ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO:
				Bundle bundle = msg.getData();
				String id = bundle.getString("id");
				String name = bundle.getString("name");
				int flag = bundle.getInt("flag", Constants.DeviceFlag.ALREADY_SET_PASSWORD);
				int type = bundle.getInt("type", 0);
                int rflag=bundle.getInt("rtspflag", 0);
                int rtspflag=(rflag>>2)&1;
				InetAddress address = (InetAddress) bundle.getSerializable("address");
				LocalDevice localDevice = new LocalDevice();
				localDevice.setContactId(id);
				localDevice.setFlag(flag);
				localDevice.setType(type);
				localDevice.setAddress(address);
				localDevice.setName(name);
				localDevice.setRtspFrag(rtspflag);
				if (!tempLocalDevices.contains(localDevice)) {
					tempLocalDevices.add(localDevice);
				}
				Log.e("ipdress","id="+id+"rtspflag="+rtspflag+"rflag="+rflag);
				String mark=address.getHostAddress();
				String ip=mark.substring(mark.lastIndexOf(".")+1, mark.length());

				break;
			case ShakeManager.HANDLE_ID_APDEVICE_END:
				Log.e("dxswifi", "wifi搜索结束");
//				WifiUtils.getInstance().wifiUnlock();
				break;
			}
			return false;
		}
	});

//	public void gainDeviceMode(String id) {
//		for (int i = 0; i < apdevices.size(); i++) {
//			if (apdevices.get(i).contactId.equals(id)) {
//				TcpClient tcpClient = new TcpClient(Utils.gainWifiMode());
//				try {
//					tcpClient.setIpdreess(InetAddress.getByName("192.168.1.1"));
//					tcpClient.setCallBack(myHandler);
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				}
//				tcpClient.createClient();
//				break;
//			}
//		}
//	}

//	private static Handler myHandler = new Handler(new Handler.Callback() {
//		@Override
//		public boolean handleMessage(Message msg) {
//			switch (msg.what) {
//			case TcpClient.SEARCH_AP_DEVICE:
//				Bundle bundle = msg.getData();
//				String id = bundle.getString("contactId");
//				for (int i = 0; i < apdevices.size(); i++) {
//					if (apdevices.get(i).contactId.equals(id)) {
//						apdevices.get(i).flag = Constants.DeviceFlag.AP_MODE;
//					}
//				}
//				break;
//			default:
//				break;
//			}
//			return false;
//		}
//	});
}

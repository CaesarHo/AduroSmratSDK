package com.adurosmart.thread;

import android.content.Context;
import android.util.Log;

import com.adurosmart.utils.Utils;

public class MainThread {
	static MainThread manager;
	boolean isRun;
	private String version;
	private int serVersion;
	private static final long SYSTEM_MSG_INTERVAL = 60 * 60 * 1000;
	long lastSysmsgTime;
	private Main main;
	private SearchUpdate update;
	Context context;
	private static boolean isOpenThread;

	public MainThread(Context context) {
		manager = this;
		this.context = context;
	}

	public static MainThread getInstance() {
		return manager;
	}

	class Main extends Thread {
		@Override
		public void run() {
			isRun = true;
			Utils.sleepThread(3000);
			while (isRun) {
				if (isOpenThread == true) {
//					checkUpdate();
					Log.e("my", "updateOnlineState");
					try {

					} catch (Exception e) {
						e.printStackTrace();
					}
					Utils.sleepThread(20 * 1000);
				} else {
					Utils.sleepThread(10 * 1000);
				}
			}
		}
	};
	
	class SearchUpdate extends Thread {
		@Override
		public void run() {
			Utils.sleepThread(3000);
			while (isRun) {
				if (isOpenThread == true) {
					try {
//						FList.getInstance().getCheckUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Utils.sleepThread(4*60*60 * 1000);
				} else {
					Utils.sleepThread(10 * 1000);
				}
			}
		}
	};

	public void go() {

		if (null == main || !main.isAlive()) {
			main = new Main();
			main.start();
		}
		
		if (null == update || !update.isAlive()) {
			update = new SearchUpdate();
			update.start();
		}
	}

	public void kill() {

		isRun = false;
		main = null;
		update=null;
	}

	public static void setOpenThread(boolean isOpenThread) {
		MainThread.isOpenThread = isOpenThread;
	}

//	public void checkUpdate() {
//		try {
//			long last_check_update_time = SharedPreferencesManager.getInstance().getLastAutoCheckUpdateTime(MyApp.app);
//			long now_time = System.currentTimeMillis();
//
//			if ((now_time - last_check_update_time) > 1000 * 60 * 60 * 12) {
//				SharedPreferencesManager.getInstance().putLastAutoCheckUpdateTime(now_time, MyApp.app);
//				Log.e("my", "后台检查更新");
//				if (UpdateManager.getInstance().checkUpdate()) {
//					String data = "";
//					if (Utils.isZh(MyApp.app)) {
//						data = UpdateManager.getInstance().getUpdateDescription();
//					} else {
//						data = UpdateManager.getInstance().getUpdateDescription_en();
//					}
//					Intent i = new Intent(Constants.Action.ACTION_UPDATE);
//					i.putExtra("updateDescription", data);
//					MyApp.app.sendBroadcast(i);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e("my", "后台检查更新失败");
//		}
//	}
}

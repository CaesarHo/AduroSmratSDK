package com.adurosmart.global;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.eclipse.paho.android.service.sample.MyApp;

/**
 * Created by best on 2016/6/27.
 */
public class AutoStartReceiver extends BroadcastReceiver {
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals(action_boot)) {
            Intent service = new Intent(MyApp.MAIN_SERVICE_START);
            context.startService(service);
//            context.bindService(service,conn,Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    public AutoStartReceiver() {
        super();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.MyBinder binder = (MainService.MyBinder)service;
            MainService bindService = binder.getService();
            bindService.MyMethod();
        }
    };
}

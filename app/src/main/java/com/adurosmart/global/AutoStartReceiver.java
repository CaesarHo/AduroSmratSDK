package com.adurosmart.global;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by best on 2016/6/27.
 */
public class AutoStartReceiver extends BroadcastReceiver {
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
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

        }
    };
}

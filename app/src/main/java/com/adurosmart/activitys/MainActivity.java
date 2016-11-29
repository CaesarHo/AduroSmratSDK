package com.adurosmart.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.adurosmart.global.Constants;
import org.eclipse.paho.android.service.sample.MyApp;
import org.eclipse.paho.android.service.sample.R;

/**
 * Created by best on 2016/7/8.
 */
public class MainActivity extends  TraceableActivity{
    Context context;
    boolean isRegFilter = false;
    TextView ipaddress,port,uid;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.main_activity);
        //Start a server listening udp and mqtt
        Intent service = new Intent(MyApp.MAIN_SERVICE_START);
        context.startService(service);
        regFilter();
        init();
    }

    public void init(){
        ipaddress = (TextView)findViewById(R.id.ipaddress);
        port = (TextView)findViewById(R.id.port);
        uid = (TextView)findViewById(R.id.uid);
        list = (ListView) findViewById(R.id.list);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.ACTION_LISTEN_UDP_DATA);
        filter.addAction("getdevice_callback");
        context.registerReceiver(broadcastReceiver, filter);
        isRegFilter = true;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.ACTION_LISTEN_UDP_DATA)){

            } else if (intent.getAction().equals("getdevice_callback")){

            }
        }
    };
}

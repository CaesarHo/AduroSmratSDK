package com.adurosmart.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adurosmart.bean.DeviceInfo;
import com.adurosmart.bean.GatewayInfo;
import com.adurosmart.global.Constants;
import com.adurosmart.utils.SerialHelper;

import org.eclipse.paho.android.service.sample.MyApp;
import org.eclipse.paho.android.service.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/7/8.
 */
public class MainActivity extends  TraceableActivity{
    Context context;
    boolean isRegFilter = false;
    TextView ipaddress,port,uid;
    DeviceInfo deviceInfo;
    private static List<DeviceInfo> localdevices = new ArrayList<DeviceInfo>();
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
        SerialHelper.getInstance(context).GetDevice();
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
                SerialHelper.getInstance(context).GetDevice();
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
                String ipstr = intent.getStringExtra("ipaddrss");
                String str =  intent.getStringExtra("data");
                int port_int = intent.getIntExtra("port",-1);

                ipaddress.setText(GatewayInfo.getInstance().getInetAddress().getHostAddress().toString());
                port.setText(String.valueOf(GatewayInfo.getInstance().getPort()));
                uid.setText(GatewayInfo.getInstance().getGatewayUniqueId());

                Toast t3=Toast.makeText(context, "当前数据=" + str, Toast.LENGTH_SHORT);
                t3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
                t3.setMargin(0f, 0.5f);
                t3.show();
            }
            else if (intent.getAction().equals("getdevice_callback")){
                //获取设备
                deviceInfo = (DeviceInfo) intent.getSerializableExtra("dinfo");
                if (deviceInfo == null) {
                    return;
                }
                localdevices.add(deviceInfo);
            }
        }
    };
}

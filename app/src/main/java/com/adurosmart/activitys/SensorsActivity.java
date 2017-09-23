package com.adurosmart.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.adurosmart.adapters.SensorMsgAdapter;
import com.adurosmart.entity.SensorMsgInfo;
import com.adurosmart.sdk.R;
import com.core.entity.AppDevice;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by best on 2016/10/25.
 */

public class SensorsActivity extends BaseActivity {
    @BindView(R.id.scenes_list)
    ListView scenesList;
    @BindView(R.id.txt_value)
    TextView txt_value;
    @BindView(R.id.ibtn_left)
    ImageButton ibtn_laft;
    private Context context;
    private SensorMsgAdapter sensorMsgAdapter;
    private List<SensorMsgInfo> msgInfoList = new ArrayList<>();
    private boolean isRegFilter = false;
    private AppDevice appDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);
        ButterKnife.bind(this);
        context = SensorsActivity.this;
        appDevice = (AppDevice) getIntent().getSerializableExtra("AppDeviceInfo");

        regFilter();
        sensorMsgAdapter = new SensorMsgAdapter(context, msgInfoList);
        initView();
    }

    @Override
    public void initView() {
        scenesList = (ListView) findViewById(R.id.scenes_list);
        scenesList.setAdapter(sensorMsgAdapter);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SENSORS_INFO");
        filter.addAction("SENSORS_BatteryValue");
        context.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SENSORS_INFO")) {
                String device_mac = intent.getStringExtra("device_mac");
                String msg_time = intent.getStringExtra("msg_time");
                int msg_state = intent.getIntExtra("msg_state", -1);
                synchronized (msgInfoList) {
                    for (SensorMsgInfo sensorMsgInfo : msgInfoList) {
                        if (sensorMsgInfo.time.equalsIgnoreCase(msg_time)) {
                            return;
                        }
                    }
                }

                if (device_mac.equalsIgnoreCase(appDevice.getShortaddr()) | device_mac.equalsIgnoreCase(appDevice.getDeviceMac())) {
                    SensorMsgInfo msgInfo = new SensorMsgInfo();
                    msgInfo.device_mac = device_mac;
                    msgInfo.time = msg_time;
                    msgInfo.state = msg_state;
                    msgInfoList.add(msgInfo);
                    sensorMsgAdapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals("SENSORS_BatteryValue")) {
                String device_mac = intent.getStringExtra("device_mac");
                int value = intent.getIntExtra("value", -1);
                System.out.println("value = " + value);
                txt_value.setText(value / 2 + "%");
            }
        }
    };

    @OnClick(R.id.ibtn_left)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_left:
                this.finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }
}

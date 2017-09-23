package com.adurosmart.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.adurosmart.global.Constants;
import com.adurosmart.sdk.R;
import com.core.entity.AppDevice;
import com.core.entity.AppGroup;
import com.adurosmart.adapters.DeviceInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by best on 2016/10/24.
 */

public class GroupDetailActivity extends BaseActivity implements View.OnClickListener{
    public static List<AppDevice> mGrouDevInfoList = new ArrayList<>();

    private Context context;
    private ListView list;
    private ImageButton iLeft_btn,iRight_btn;
    private DeviceInfoAdapter deviceInfoAdapter;
    private AppGroup appGroup;
    public static short group_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        context = this;
        mGrouDevInfoList.clear();
        appGroup = (AppGroup) getIntent().getSerializableExtra("GROUP_INFO");
        group_id = appGroup.getGroup_id();
        deviceInfoAdapter = new DeviceInfoAdapter(context, mGrouDevInfoList, true);
        initData();
        initView();
    }

    @Override
    public void initView() {
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(deviceInfoAdapter);
        iLeft_btn = (ImageButton)findViewById(R.id.ibtn_left);
        iRight_btn = (ImageButton)findViewById(R.id.ibtn_right);
        iLeft_btn.setOnClickListener(this);
        iRight_btn.setOnClickListener(this);
    }

    public void initData() {
        if (appGroup.getMac_data() == null) {
            return;
        }
        String group_mac = "";
        for (int i = 0; i < appGroup.getMac_data().size(); i++) {
            if (appGroup.getMac_data().get(i).length() <= 4) {
                return;
            }
            group_mac = new String(appGroup.getMac_data().get(i));
            for (int j = 0; j < Constants.appDeviceList.size(); j++) {
                if (Constants.appDeviceList.get(j).getDeviceMac().equalsIgnoreCase(group_mac)) {
                    if (mGrouDevInfoList.size() == 0) {
                        mGrouDevInfoList.add(Constants.appDeviceList.get(j));
                        deviceInfoAdapter.notifyDataSetInvalidated();
                    } else {
                        synchronized (mGrouDevInfoList) {
                            //--------------搜索到一个设备-----------
                            for (AppDevice groupDevInfo : mGrouDevInfoList) {
                                if (groupDevInfo.getDeviceMac().endsWith(Constants.appDeviceList.get(j).getDeviceMac())) {
                                    return;
                                }
                            }
                            mGrouDevInfoList.add(Constants.appDeviceList.get(j));
                            deviceInfoAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn_left:
                this.finish();
                break;

            case R.id.ibtn_right:

                break;
        }
    }
}


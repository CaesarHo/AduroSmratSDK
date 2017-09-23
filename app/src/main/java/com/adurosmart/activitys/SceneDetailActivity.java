package com.adurosmart.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.adurosmart.global.Constants;
import com.adurosmart.sdk.R;
import com.core.entity.AppDevice;
import com.core.entity.AppScene;
import com.adurosmart.adapters.DeviceInfoAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by best on 2016/10/25.
 */

public class SceneDetailActivity extends BaseActivity implements View.OnClickListener{
    public static List<AppDevice> mSceneDevList = new ArrayList<>();

    private Context context;
    private ListView list;
    private ImageButton iLeft_btn,iRight_btn;
    private DeviceInfoAdapter deviceInfoAdapter;
    private AppScene appScene;
    public static boolean isSceneDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        context = this;
        mSceneDevList.clear();
        appScene = (AppScene) getIntent().getSerializableExtra("SCENE_INFO");
        deviceInfoAdapter = new DeviceInfoAdapter(context, mSceneDevList,true);
        isSceneDetail = true;
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
        if (appScene.getDevices_mac() == null) {
            return;
        }
        String scene_mac = "";
        for (int i = 0; i < appScene.getDevices_mac().size(); i++) {
            if (appScene.getDevices_mac().get(i).length() <= 4) {
                return;
            }
            scene_mac = new String(appScene.getDevices_mac().get(i));
            for (int j = 0; j < Constants.appDeviceList.size(); j++) {
                if (Constants.appDeviceList.get(j).getDeviceMac().equalsIgnoreCase(scene_mac)) {
                    if (mSceneDevList.size() == 0) {
                        mSceneDevList.add(Constants.appDeviceList.get(j));
                        deviceInfoAdapter.notifyDataSetInvalidated();
                    } else {
                        synchronized (mSceneDevList) {
                            //--------------搜索到一个设备-----------
                            for (AppDevice groupDevInfo : mSceneDevList) {
                                if (groupDevInfo.getDeviceMac().endsWith(Constants.appDeviceList.get(j).getDeviceMac())) {
                                    return;
                                }
                            }
                            mSceneDevList.add(Constants.appDeviceList.get(j));
                            deviceInfoAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            isSceneDetail = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSceneDetail = false;
    }
}

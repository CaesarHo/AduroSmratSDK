package com.adurosmart.activitys;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.adurosmart.MyApp;
import com.adurosmart.sdk.R;
import com.adurosmart.util.LogUtil;
import com.adurosmart.global.Constants;

/**
 * Created by best on 2016/10/17.
 */

public class SectionActivity extends TabActivity {

    private static String Tag = SectionActivity.class.getSimpleName();
    public static TabHost tabHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_tab_view);
        tabHost = getTabHost();
        MyApp.getInstance().addActivity(this);
        // 设备
//        setItem(R.drawable.device_car, R.string.device, new Intent(this, DeviceListActivity.class));
//        // 房间
//        setItem(R.drawable.group_car, R.string.group, new Intent(this, GroupsActivity.class));
//        // 场景
//        setItem(R.drawable.scene_car, R.string.scene, new Intent(this, ScenesActivity.class));
//        // 租车
//        setItem(R.drawable.task_car, R.string.task, new Intent(this, TasksActivity.class));

        tabHost.setCurrentTab(0);
        // 显示通知
//        NotificationUtil.showNotification(MainApplication.getInstance(),
//                NotificationUtil.NOTIFICATION_ACTIVITY_ID, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.getInstance();
    }

    @Override
    protected void onDestroy() {
        LogUtil.debug(Tag, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        NotificationUtil.showNotification(MainApplication.getInstance(),
//                NotificationUtil.NOTIFICATION_ACTIVITY_ID, this);
    }

    @Override
    public void onBackPressed() {
        getCurrentActivity().onBackPressed();
        super.onBackPressed();
    }

    /**
     * 创建设置栏目方法
     *
     * @param icon   图标的资源ID
     * @param title  标题的资源ID
     * @param intent 显示意图
     */
    private void setItem(int icon, int title, Intent intent) {
        Constants.isTask_Mac = false;
        String tabTitle = getResources().getString(title);
        View localView = LayoutInflater.from(tabHost.getContext()).inflate(R.layout.secton_activity_tab_item, null);
        ImageView imageView = (ImageView) localView.findViewById(R.id.main_activity_tab_image);
        imageView.setImageResource(icon);
        TextView textView = (TextView) localView.findViewById(R.id.main_activity_tab_text);
        textView.setText(tabTitle);
        textView.setTextColor(Color.GRAY);
        TabHost.TabSpec localTabSpec = tabHost.newTabSpec(tabTitle).setIndicator(localView).setContent(intent);
        tabHost.addTab(localTabSpec);
    }
}

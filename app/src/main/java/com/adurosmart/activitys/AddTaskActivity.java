package com.adurosmart.activitys;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.adurosmart.adapters.DateNumericAdapter;
import com.adurosmart.adapters.DeviceInfoAdapter;
import com.adurosmart.adapters.SceneInfoAdapter;
import com.adurosmart.global.Constants;
import com.adurosmart.sdk.R;
import com.adurosmart.util.DelayThread;
import com.adurosmart.util.ScreenUtils;
import com.adurosmart.util.Utils;
import com.adurosmart.widget.OnWheelScrollListener;
import com.adurosmart.widget.WheelView;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.SerialHandler;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.adurosmart.global.Constants.device_mac;
import static com.adurosmart.global.Constants.device_short;
import static com.adurosmart.global.Constants.isSwitch;
import static com.adurosmart.global.Constants.isTask_Mac;
import static com.adurosmart.global.Constants.level;
import static com.adurosmart.global.Constants.sceneId;
import static com.adurosmart.global.Constants.scene_group_id;
import static com.adurosmart.global.Constants.temp;
//import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/10/25.
 */

public class AddTaskActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.date_year)
    WheelView date_year;
    @BindView(R.id.date_month)
    WheelView date_month;
    @BindView(R.id.date_day)
    WheelView date_day;
    @BindView(R.id.date_hour)
    WheelView date_hour;
    @BindView(R.id.date_minute)
    WheelView date_minute;
    @BindView(R.id.date_second)
    WheelView date_second;
    @BindView(R.id.tv_setting_urban)
    TextView tvSettingUrban;
    @BindView(R.id.tv_utc)
    TextView tvUtc;
    @BindView(R.id.w_urban)
    WheelView w_urban;
    @BindView(R.id.bt_set_timezone)
    Button bt_set_timezone;
    @BindView(R.id.time_text)
    TextView time_text;
    @BindView(R.id.choose_device)
    Button choose_device;
    @BindView(R.id.choose_scene)
    Button choose_scene;
    @BindView(R.id.swc)
    Switch switch_btn;
    @BindView(R.id.trigger_device)
    Button trigger_device;
    @BindView(R.id.trigger_scene)
    Button trigger_scene;
    @BindView(R.id.btn_one)
    Button btn_one;
    @BindView(R.id.btn_two)
    Button btn_two;
    @BindView(R.id.btn_three)
    Button btn_three;
    @BindView(R.id.btn_four)
    Button btn_four;
    @BindView(R.id.btn_five)
    Button btn_five;
    @BindView(R.id.btn_six)
    Button btn_six;
    @BindView(R.id.btn_seven)
    Button btn_seven;
    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.edit_task)
    EditText edit_task;
    @BindView(R.id.btn_add_task)
    Button btn_add_task;
    @BindView(R.id.setting_time)
    RelativeLayout setting_time;
    @BindView(R.id.ibtn_right)
    ImageButton ibtn_right;
    private Context mContext;
    private boolean isRegFilter = false;
    private boolean wheelScrolled = false;

    private String cur_modify_time;
    private int current_urban;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        ButterKnife.bind(this);
        mContext = AddTaskActivity.this;
        deviceInfoAdapter = new DeviceInfoAdapter(mContext, Constants.appDeviceList, false);
        sceneInfoAdapter = new SceneInfoAdapter(mContext, Constants.appSceneList);

        initView();
        regFilter();
    }

    @Override
    public void initView() {
        Calendar calendar = Calendar.getInstance();
        setting_time.setOnClickListener(this);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        date_year.setViewAdapter(new DateNumericAdapter(mContext, 2016, 2042));
        date_year.setCurrentItem(curYear - 2016);
        date_year.addScrollingListener(scrolledListener);
        date_year.setCyclic(true);

        int curMonth = calendar.get(Calendar.MONTH) + 1;
        date_month.setViewAdapter(new DateNumericAdapter(mContext, 1, 12));
        date_month.setCurrentItem(curMonth - 1);
        date_month.addScrollingListener(scrolledListener);
        date_month.setCyclic(true);

        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 31));
        date_day.setCurrentItem(curDay - 1);
        date_day.addScrollingListener(scrolledListener);
        date_day.setCyclic(true);

        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        date_hour.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        date_hour.setCurrentItem(curHour);
        date_hour.setCyclic(true);

        int curMinute = calendar.get(Calendar.MINUTE);
        date_minute.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        date_minute.setCurrentItem(curMinute);
        date_minute.setCyclic(true);

        int curSecond = calendar.get(Calendar.SECOND);
        date_second.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        date_second.setCurrentItem(curSecond);
        date_second.setCyclic(true);

        w_urban.setViewAdapter(new DateNumericAdapter(mContext, -11, 12));
        w_urban.setCyclic(true);

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 开启switch，设置提示信息
                    isSwitch = 1;
                    Toast.makeText(AddTaskActivity.this, isChecked + "=" + isSwitch, Toast.LENGTH_SHORT).show();
                } else {
                    // 关闭swtich，设置提示信息
                    isSwitch = 0;
                    Toast.makeText(AddTaskActivity.this, isChecked + "=" + isSwitch, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
            updateStatus();
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    public void updateStatus() {
        int year = date_year.getCurrentItem() + 2016;
        int month = date_month.getCurrentItem() + 1;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 31));
        } else if (month == 2) {
            boolean isLeapYear = false;
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    isLeapYear = true;
                } else {
                    isLeapYear = false;
                }
            } else {
                if (year % 4 == 0) {
                    isLeapYear = true;
                } else {
                    isLeapYear = false;
                }
            }
            if (isLeapYear) {
                if (date_day.getCurrentItem() > 28) {
                    date_day.scroll(30, 2000);
                }
                date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 29));
            } else {
                if (date_day.getCurrentItem() > 27) {
                    date_day.scroll(30, 2000);
                }
                date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 28));
            }
        } else {
            if (date_day.getCurrentItem() > 29) {
                date_day.scroll(30, 2000);
            }
            date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 30));
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SECENS_INFO");
        filter.addAction("TaskDeviceInfoAction");
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("SECENS_INFO")) {
                //获取grouplist
//                appScene = (AppScene) intent.getSerializableExtra("AppSceneInfo");
//                for (AppScene appSceneInfo : taskScenes) {
//                    if (appSceneInfo.getSencesId() == appScene.getSencesId()) {
//                        return;
//                    }
//                }
//                taskScenes.add(appScene);
//                sceneInfoAdapter.notifyDataSetChanged();
//                showAppSceneInfoDialog();
            } else if (intent.getAction().equals("TaskDeviceInfoAction")) {

                //获取设备
//                appDevice = (AppDevice) intent.getSerializableExtra("DeviceInfo");
//                for (AppDevice devInfo : taskDevices) {
//                    if (devInfo.getDeviceMac().endsWith(appDevice.getDeviceMac())) {
//                        return;
//                    }
//                }
//                taskDevices.add(appDevice);
//                deviceInfoAdapter.notifyDataSetChanged();
//                showAppDeviceInfoDialog();

            } else if (intent.getAction().equals("level_value_action")) {
                level = intent.getIntExtra("level_value", -1);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent it = new Intent();
        it.setAction("CONTROL_BACK");
        mContext.sendBroadcast(it);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isTask_Mac = false;
            if (appidDlg != null) {
                appidDlg.dismiss();
            }
            this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private int task_cycle;
    private int task_type;

    public static Dialog appidDlg = null;
    private ListView mTaskList;

    private SceneInfoAdapter sceneInfoAdapter;
    private DeviceInfoAdapter deviceInfoAdapter;

    public void showAppSceneInfoDialog() {
        if (appidDlg != null) {
            appidDlg.dismiss();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout, null);

        appidDlg = new Dialog(mContext, R.style.dialog);
        appidDlg.setContentView(view);
        mTaskList = (ListView) view.findViewById(R.id.listView1);
        mTaskList.setAdapter(sceneInfoAdapter);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sceneInfoAdapter.notifyDataSetChanged();
                        SerialHandler.getInstance().GetAllTask();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });

        appidDlg.show();
        FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams) view.getLayoutParams();
        layout.width = ScreenUtils.getScreenWidth(mContext) - 150;
        layout.height = ScreenUtils.getScreenHeight(mContext) - 300;
        view.setLayoutParams(layout);

        appidDlg.setCanceledOnTouchOutside(true);
        Window window = appidDlg.getWindow();
        window.setWindowAnimations(R.style.dialog_normal);
    }

    public void showAppDeviceInfoDialog() {
        isTask_Mac = true;
        if (appidDlg != null) {
            appidDlg.dismiss();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout, null);

        appidDlg = new Dialog(mContext, R.style.dialog);
        appidDlg.setContentView(view);
        mTaskList = (ListView) view.findViewById(R.id.listView1);
        mTaskList.setAdapter(deviceInfoAdapter);

        appidDlg.show();

        FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams) view.getLayoutParams();
        layout.width = ScreenUtils.getScreenWidth(mContext) - 150;
        layout.height = ScreenUtils.getScreenHeight(mContext) - 300;
        view.setLayoutParams(layout);

        appidDlg.setCanceledOnTouchOutside(true);
        Window window = appidDlg.getWindow();
        window.setWindowAnimations(R.style.dialog_normal);
    }

    PopupMenu popup = null;

    @OnClick({R.id.bt_set_timezone, R.id.time_text, R.id.setting_time, R.id.choose_device, R.id.choose_scene, R.id.swc,
            R.id.trigger_device, R.id.trigger_scene, R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four, R.id.btn_five,
            R.id.btn_six, R.id.btn_seven, R.id.btn_add_task, R.id.ibtn_left, R.id.ibtn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_right:
                popup = new PopupMenu(this, ibtn_right);
                getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.choose_scene:
                                SerialHandler.getInstance().getSences();
                                showAppSceneInfoDialog();
                                break;
                            case R.id.choose_device:
                                isTask_Mac = true;
                                SerialHandler.getInstance().GetAllDeviceListen();
                                showAppDeviceInfoDialog();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                break;
            case R.id.ibtn_left:
                this.finish();
                break;
            case R.id.bt_set_timezone:
                current_urban = w_urban.getCurrentItem();
                break;
            case R.id.time_text:
                break;
            case R.id.setting_time:
                new DelayThread(0, new DelayThread.OnRunListener() {
                    @Override
                    public void run() {
                        cur_modify_time = Utils.convertDeviceTime(
                                date_year.getCurrentItem() + 16,
                                date_month.getCurrentItem() + 1,
                                date_day.getCurrentItem() + 1,
                                date_hour.getCurrentItem(),
                                date_minute.getCurrentItem(),
                                date_second.getCurrentItem());
                    }
                }).start();
                time_text.setText(cur_modify_time);
                break;
//            case R.id.choose_device:
//                choose_scene.setAlpha(0.2f);
//                trigger_scene.setAlpha(0.2f);
//                trigger_device.setAlpha(0.2f);
//                choose_device.setAlpha(1.0f);
//                task_type = 1;
//                break;

            case R.id.choose_scene:
                choose_device.setAlpha(0.2f);
                trigger_scene.setAlpha(0.2f);
                trigger_device.setAlpha(0.2f);
                choose_scene.setAlpha(1.0f);
                task_type = 1;
                break;

            case R.id.swc:
                break;

//            case R.id.trigger_device:
//                trigger_scene.setAlpha(0.2f);
//                choose_scene.setAlpha(0.2f);
//                choose_device.setAlpha(0.2f);
//                trigger_device.setAlpha(1.0f);
//                task_type = 3;
//                break;

            case R.id.trigger_scene:
                choose_scene.setAlpha(0.2f);
                choose_device.setAlpha(0.2f);
                trigger_device.setAlpha(0.2f);
                trigger_scene.setAlpha(1.0f);
                task_type = 0;
                break;
            case R.id.btn_one:
                task_cycle = 1;
                btn_one.setBackgroundColor(Color.BLUE);
                break;

            case R.id.btn_two:
                task_cycle = 2;
                btn_two.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_three:
                task_cycle = 4;
                btn_three.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_four:
                task_cycle = 8;
                btn_four.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_five:
                task_cycle = 16;
                btn_five.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_six:
                task_cycle = 32;
                btn_six.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_seven:
                task_cycle = 64;
                btn_seven.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_add_task:
                if (edit_task.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "请输入任务名称！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String task_name = edit_task.getText().toString().trim();
                if(task_type == 0){
                    AppDevice appDevice = new AppDevice();
                    appDevice.setDeviceMac(device_mac);
                    appDevice.setShortaddr(device_short);
                    SerialHandler.getInstance().CreateEditLinkTask(appDevice,0,sceneId,scene_group_id,isSwitch,task_name,task_type,isSwitch);
                }else{
                    SerialHandler.getInstance().CreateEditTimeTask(0,task_cycle,date_hour.getCurrentItem(),
                            date_minute.getCurrentItem(),sceneId,scene_group_id,isSwitch,task_name,task_type);
                }

//                AppDevice appDevice = new AppDevice();
//                if (edit_task.getText().toString().isEmpty()) {
//                    Toast.makeText(mContext, "请输入任务名称！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                System.out.println("sceneId = " + sceneId);
//                System.out.println("scene_group_id = " + scene_group_id);
//                if (task_type == 2 | task_type == 4) {
//                    device_mac = "00158d0000ecc9a7";
//                    if (sceneId == 0 | scene_group_id == 0) {
//                        Toast.makeText(mContext, "请选择场景！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                String dev_switch = null;
//                String dev_level = null;
//                String dev_hue = null;
//                String dev_temp = null;
//                if (task_type == 1 | task_type == 3) {
//                    appDevice.setDeviceMac(device_mac);
//                    appDevice.setShortaddr(device_short);
//                    appDevice.setEndpoint(device_mainpoint);
//                    appDevice.setDeviceState(isSwitch);
//                    if (isSwitch != -1) {
//                        dev_switch = "0092";
//                    }
//                    if (level != 0) {
//                        dev_level = "0081";
//                    }
//                    if (hue != 0) {
//                        dev_hue = "00B6";
//                    }
//                    if (temp != 0) {
//                        dev_temp = "00C0";
//                    }
//                }
//
//                if (task_type == 1) {
//                    SerialHandler.getInstance().CreateTimingDeviceTask(edit_task.getText().toString(), (byte) 0x00, (byte) task_cycle,
//                            date_hour.getCurrentItem(), date_minute.getCurrentItem(),
//                            appDevice, 1, dev_switch, dev_level, dev_hue, dev_temp);
//                }
//
//                if (task_type == 2) {
//                    SerialHandler.getInstance().CreateTimingSceneTask(edit_task.getText().toString(), (byte) 0x00, (byte) task_cycle,
//                            date_hour.getCurrentItem(), date_minute.getCurrentItem(), 1, scene_group_id, sceneId);
//                }
//
//                if (task_type == 3) {
//                    if (Sensor_Mac == null) {
//                        return;
//                    }
//                    SerialHandler.getInstance().CreateTriggerDeviceTask(edit_task.getText().toString(), (byte) 0x00,
//                            isSwitch, Sensor_Mac, appDevice, 1, dev_switch, dev_level,
//                            dev_hue, dev_temp);
//                }
//
//                if (task_type == 4) {
//                    if (Sensor_Mac == null) {
//                        return;
//                    }
//                    SerialHandler.getInstance().CreateTriggerSceneTask(edit_task.getText().toString(),
//                            (byte) 0x00, isSwitch, Sensor_Mac, 1, scene_group_id, sceneId);
//                }

                break;
        }
    }
}

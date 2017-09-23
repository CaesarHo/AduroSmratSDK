package com.adurosmart.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adurosmart.GwInterfaceCallback;
import com.adurosmart.MyApp;
import com.adurosmart.adapters.BasePagerAdapter;
import com.adurosmart.fragments.DevicesFragment;
import com.adurosmart.fragments.GroupsFragment;
import com.adurosmart.fragments.ScenesFragment;
import com.adurosmart.fragments.SettingFragment;
import com.adurosmart.fragments.TasksFragment;
import com.adurosmart.sdk.R;
import com.adurosmart.util.ColorUtils;
import com.adurosmart.widget.CompatViewpager;
import com.adurosmart.widget.PopwUtil;
import com.core.gatewayinterface.SerialHandler;
import com.sideslipmenu.CoordinatorMenu;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by best on 2016/11/9.
 */

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";
    private CoordinatorMenu mCoordinatorMenu;
    @BindView(R.id.device_image)
    ImageView deviceImage;
    @BindView(R.id.device_text)
    TextView deviceText;
    @BindView(R.id.device_layout)
    RelativeLayout deviceLayout;
    @BindView(R.id.group_image)
    ImageView groupImage;
    @BindView(R.id.group_text)
    TextView groupText;
    @BindView(R.id.group_layout)
    RelativeLayout groupLayout;
    @BindView(R.id.scene_image)
    ImageView sceneImage;
    @BindView(R.id.scene_text)
    TextView sceneText;
    @BindView(R.id.scene_layout)
    RelativeLayout sceneLayout;

    @BindView(R.id.task_image)
    ImageView taskImage;
    @BindView(R.id.task_text)
    TextView taskText;
    @BindView(R.id.task_layout)
    RelativeLayout taskLayout;

    @BindView(R.id.setting_image)
    ImageView settingImage;
    @BindView(R.id.setting_text)
    TextView settingText;
    @BindView(R.id.setting_layout)
    RelativeLayout settingLayout;

    @BindView(R.id.vp)
    CompatViewpager vp;
    @BindView(R.id.ibtn_left)
    ImageButton ibtnLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_right)
    ImageButton ibtnRight;
    private DevicesFragment devicesFragment;
    private GroupsFragment groupsFragment;
    private ScenesFragment scenesFragment;
    private TasksFragment tasksFragment;
    private SettingFragment settingFragment;
    public static int postion = 0;
    PopwUtil menuWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming);
        ButterKnife.bind(this);
        initView();
        setListener();
        applyDevice();
        transportStatus(this);
        //侧滑代码
        mCoordinatorMenu = (CoordinatorMenu) findViewById(R.id.menu);
    }

    public void initView() {
        tvTitle.setText(R.string.device);
        ibtnLeft.setImageResource(R.drawable.device_car);
        ibtnLeft.setVisibility(View.VISIBLE);
        ibtnRight.setImageResource(R.drawable.ic_more);
        ibtnRight.setVisibility(View.GONE);
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(), getFragmentList());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
    }

    public List<Fragment> getFragmentList() {
        List<Fragment> list = new ArrayList<>();
        devicesFragment = DevicesFragment.getInstance("Room");
        groupsFragment = GroupsFragment.getInstance("Devices");
        scenesFragment = ScenesFragment.getInstance("Scene");
        tasksFragment = TasksFragment.getInstance("Schedule");
        settingFragment = SettingFragment.getInstance("Setting");
        list.add(devicesFragment);
        list.add(groupsFragment);
        list.add(scenesFragment);
        list.add(tasksFragment);
        list.add(settingFragment);
        return list;
    }

    public void setListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        applyDevice();
                        break;
                    case 1:
                        applyGroup();
                        break;
                    case 2:
                        applyScene();
                        break;
                    case 3:
                        applyTask();
                        break;
                    case 4:
                        applySetting();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void applyDevice() {
        postion = 0;
        vp.setCurrentItem(0, false);
        tvTitle.setText(R.string.device);
        deviceImage.setImageResource(R.drawable.ic_devices_click);
        groupImage.setImageResource(R.drawable.ic_home_unclick);
        sceneImage.setImageResource(R.drawable.ic_scene_unclick);
        taskImage.setImageResource(R.drawable.ic_schedules_unclick);
        ibtnRight.setVisibility(View.VISIBLE);

        deviceText.setTextColor(ColorUtils.getColor(this, R.color.colorPrimary));
        deviceImage.setColorFilter(ColorUtils.getColor(this, R.color.colorPrimary));

        groupText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        groupImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        sceneText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        sceneImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        taskText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        taskImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        settingText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        settingImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));
    }

    private void applyGroup() {
        postion = 1;
        vp.setCurrentItem(1, false);
        tvTitle.setText(R.string.group);
        deviceImage.setImageResource(R.drawable.ic_devices_unclick);
        groupImage.setImageResource(R.drawable.ic_home_click);
        sceneImage.setImageResource(R.drawable.ic_scene_unclick);
        taskImage.setImageResource(R.drawable.ic_schedules_click);
        ibtnRight.setVisibility(View.VISIBLE);

        deviceText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        deviceImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        groupText.setTextColor(ColorUtils.getColor(this, R.color.colorPrimary));
        groupImage.setColorFilter(ColorUtils.getColor(this, R.color.colorPrimary));

        sceneText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        sceneImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        taskText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        taskImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        settingText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        settingImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));
    }

    private void applyScene() {
        postion = 2;
        vp.setCurrentItem(2, false);
        tvTitle.setText(R.string.scene);
        deviceImage.setImageResource(R.drawable.ic_devices_unclick);
        groupImage.setImageResource(R.drawable.ic_home_unclick);
        sceneImage.setImageResource(R.drawable.ic_scene_click);
        taskImage.setImageResource(R.drawable.ic_schedules_unclick);
        ibtnRight.setVisibility(View.GONE);

        deviceText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        deviceImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        groupText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        groupImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        sceneText.setTextColor(ColorUtils.getColor(this, R.color.colorPrimary));
        sceneImage.setColorFilter(ColorUtils.getColor(this, R.color.colorPrimary));

        taskText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        taskImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        settingText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        settingImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));
    }

    private void applyTask() {
        postion = 3;
        vp.setCurrentItem(3, false);
        tvTitle.setText(R.string.task);
        deviceImage.setImageResource(R.drawable.ic_devices_unclick);
        groupImage.setImageResource(R.drawable.ic_home_unclick);
        sceneImage.setImageResource(R.drawable.ic_scene_unclick);
        taskImage.setImageResource(R.drawable.ic_schedules_click);

        deviceText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        deviceImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        groupText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        groupImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        sceneText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        sceneImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        taskText.setTextColor(ColorUtils.getColor(this, R.color.colorPrimary));
        taskImage.setColorFilter(ColorUtils.getColor(this, R.color.colorPrimary));

        settingText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        settingImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));
    }

    private void applySetting() {
        postion = 4;
        vp.setCurrentItem(4, false);
        tvTitle.setText(R.string.setting);
        deviceImage.setImageResource(R.drawable.ic_devices_unclick);
        groupImage.setImageResource(R.drawable.ic_home_unclick);
        sceneImage.setImageResource(R.drawable.ic_scene_unclick);
        taskImage.setImageResource(R.drawable.ic_schedules_unclick);
        settingImage.setImageResource(R.mipmap.ic_setting_click);

        deviceText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        deviceImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        groupText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        groupImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        sceneText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        sceneImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        taskText.setTextColor(ColorUtils.getColor(this, android.R.color.darker_gray));
        taskImage.setColorFilter(ColorUtils.getColor(this, android.R.color.darker_gray));

        settingText.setTextColor(ColorUtils.getColor(this, R.color.colorPrimary));
        settingImage.setColorFilter(ColorUtils.getColor(this, R.color.colorPrimary));
    }

    @OnClick({R.id.ibtn_left, R.id.ibtn_right, R.id.device_layout, R.id.group_layout, R.id.scene_layout, R.id.task_layout, R.id.setting_layout, R.id.vp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.device_layout:
                applyDevice();
                break;
            case R.id.group_layout:
                applyGroup();
                break;
            case R.id.scene_layout:
                applyScene();
                break;
            case R.id.task_layout:
                applyTask();
                break;
            case R.id.setting_layout:
                applySetting();
                break;
            case R.id.ibtn_left:
                if (mCoordinatorMenu.isOpened()) {
                    mCoordinatorMenu.closeMenu();
                } else {
                    mCoordinatorMenu.openMenu();
                }
                break;
            case R.id.ibtn_right:
                // 实例化SelectPicPopupWindow
                menuWindow = new PopwUtil(this, itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(this.findViewById(R.id.layout_ll),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
        }
    }

//    public void InitGateway() {
//        DataSources.getInstance().setSettingInterface(new GwInterfaceCallback());
//        SerialHandler.getInstance().Init(MyApp.app, "200004401331", new GwInterfaceCallback());
//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        SerialHandler.getInstance().ScanGatewayInfo(wifiManager);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
//        InitGateway();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialHandler.getInstance().release();
        Log.e(TAG, "onDestroy");
    }

    //退出时间
    private long  currentBackPressedTime= 0;
    //退出间隔
    private static final int BACK_PRESSED_INTERVAL= 2000;
    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        if (mCoordinatorMenu.isOpened()) {
            mCoordinatorMenu.closeMenu();
        } else {
            //判断时间间隔
            if(System.currentTimeMillis()-currentBackPressedTime>BACK_PRESSED_INTERVAL) {
                currentBackPressedTime= System.currentTimeMillis();
                Toast.makeText(this,"再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            }else{
                //退出
                finish();
            }
            super.onBackPressed();
        }
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: {
                    Toast.makeText(MainActivity.this, "一", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.btn_pick_photo: {
                    Toast.makeText(MainActivity.this, "二", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
            }
        }
    };

    private int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }

    private int getNavigationBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    public void transportStatus(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (!checkDeviceHasNavigationBar(activity)){
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    @SuppressLint("NewApi")
    public boolean checkDeviceHasNavigationBar(Activity activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            System.out.println("有导航栏");
            return true;
        }else{
            System.out.println("没有导航栏");
            return false;
        }
    }
}

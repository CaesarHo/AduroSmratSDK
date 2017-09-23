package com.adurosmart.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adurosmart.adapters.GroupInfoAdapter;
import com.adurosmart.adapters.GroupsDeviceAdapter;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.CompatSeekBar;
import com.adurosmart.widget.LoadDialog;
import com.core.entity.AppDevice;
import com.core.entity.AppGroup;
import com.core.gatewayinterface.SerialHandler;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.adurosmart.global.Constants.appDeviceList;

public class GroupsFragment extends Fragment {
    public static final String TAG = "GroupsFragment";
    @BindView(R.id.room_name)
    TextView roomName;
    @BindView(R.id.edit_txt)
    EditText editTxt;
    @BindView(R.id.add_groups)
    Button addGroups;
    @BindView(R.id.groups_list)
    ListView groupsList;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.device_list)
    ListView deviceList;
    @BindView(R.id.group_state)
    ImageView group_state;
    @BindView(R.id.seekbar)
    CompatSeekBar compatSeekBar;
    private Context context;
    private DatagramSocket socket = null;
    private boolean isRegFilter = false;
    private boolean isAllRoom = false;
    private GroupInfoAdapter groupInfoAdapter;
    private GroupsDeviceAdapter groupsDeviceAdapter;
    public static List<AppGroup> mGroupInfoList = new ArrayList<>();
    private AppGroup appGroup;
    private int Count = 0;

    private static final String ARG_PARAM = "title";
    private String mParam;

    public GroupsFragment() {

    }

    public static GroupsFragment getInstance(String param) {
        GroupsFragment groupsFragment = new GroupsFragment();
        Bundle bundle = new Bundle();
        bundle.getString(ARG_PARAM, param);
        groupsFragment.setArguments(bundle);
        return groupsFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println(TAG + "=" + "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG + "=" + "onAttach");
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        System.out.println(TAG + "=" + "onAttach");
        context = getActivity();
        ButterKnife.bind(this, view);
        groupInfoAdapter = new GroupInfoAdapter(context, mGroupInfoList);
        groupsDeviceAdapter = new GroupsDeviceAdapter(context, appDeviceList);
        groupsDeviceAdapter.notifyDataSetInvalidated();
        initView();
        regFilter();
        return view;
    }

    public void initView() {
        compatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SerialHandler.getInstance().DimmingAllDevices(255 * progress / 100);
            }
        });
        srl.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                groupsDeviceAdapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGroupInfoList.clear();
                        groupInfoAdapter.notifyDataSetInvalidated();
                        SerialHandler.getInstance().getGroups();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });

        groupsList.setAdapter(groupInfoAdapter);
        deviceList.setAdapter(groupsDeviceAdapter);
        deviceList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("GROUPS_INFO");
        filter.addAction("ADD_GROUP_CALLBACK");
        filter.addAction("AppDeviceInfo_CallBack");
        context.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("GROUPS_INFO")) {
                //获取grouplist
                appGroup = (AppGroup) intent.getSerializableExtra("AppGroupInfo");
                for (AppGroup appGroupInfo : mGroupInfoList) {
                    if (appGroupInfo.getGroup_id().equals(appGroup.getGroup_id())) {
                        appGroupInfo.setMac_data(appGroup.getMac_data());
                        return;
                    }
                }
                mGroupInfoList.add(appGroup);
                groupInfoAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals("ADD_GROUP_CALLBACK")) {
                final Short group_id = intent.getShortExtra("Group_Id", (short) 0);
                String group_name = intent.getStringExtra("Group_Name");
                if (group_name.equals(editTxt.getText().toString())) {
                    if (mGroupInfoList.size() == 0) {
                        AppGroup appGroup = new AppGroup();
                        appGroup.setGroup_id(group_id);
                        appGroup.setGroup_name(group_name);
                        mGroupInfoList.add(appGroup);
                        groupInfoAdapter.notifyDataSetChanged();
                    } else {
                        synchronized (mGroupInfoList) {
                            //--------------搜索到一个设备-----------
                            for (AppGroup appGroup : mGroupInfoList) {
                                if (appGroup.getGroup_id() == group_id) {
                                    return;
                                }
                            }
                            AppGroup appGroup = new AppGroup();
                            appGroup.setGroup_id(group_id);
                            appGroup.setGroup_name(group_name);
                            mGroupInfoList.add(appGroup);
                            groupInfoAdapter.notifyDataSetChanged();
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            mHandler.sendEmptyMessageDelayed(1, Constants.groupDeviceList.size() * 1000);
                            AddDeviceToGroup(group_id);
                        }
                    }, 500);
                }
            } else if (intent.getAction().equals("AppDeviceInfo_CallBack")) {
                //获取设备
                AppDevice appDevice = (AppDevice) intent.getSerializableExtra("AppDeviceInfo");
                for (AppDevice devInfo : appDeviceList) {
                    if (devInfo.getDeviceMac().endsWith(appDevice.getDeviceMac())) {
                        return;
                    }
                }
                appDeviceList.add(appDevice);
                groupsDeviceAdapter.notifyDataSetChanged();
            }
        }
    };

    public void AddDeviceToGroup(final Short group_id) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        for (int i = 0; i < Constants.groupDeviceList.size(); i++) {
//            final int index = i;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            cachedThreadPool.execute(new Runnable() {
//                public void run() {
//                    SerialHandler.getInstance().addDeviceToGroup(Constants.groupDeviceList.get(index), group_id);
//                    System.out.println("addDeviceToGroup = " + Constants.groupDeviceList.get(index).getDeviceMac());
//                }
//            });
//        }

        for (int i = 0; i < appDeviceList.size(); i++) {
            if (appDeviceList.get(i).getSelected()) {
                SerialHandler.getInstance().addDeviceToGroup(appDeviceList.get(i), group_id);
                System.out.println("addDeviceToGroup = " + appDeviceList.get(i).getDeviceMac());
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.add_groups, R.id.group_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_state:
                if (isAllRoom) {
                    isAllRoom = false;
                    SerialHandler.getInstance().SwitchAllDevices(1);
                    group_state.setBackgroundResource(R.mipmap.btn_on);
                } else {
                    isAllRoom = true;
                    SerialHandler.getInstance().SwitchAllDevices(0);
                    group_state.setBackgroundResource(R.mipmap.btn_off);
                }
                break;
            case R.id.add_groups:
//                SerialHandler.getInstance().AddDeviceFromGroupFile((short) 4,1,"00158d0000732fdb");
//
//                try {
//                    Thread.sleep(500);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                AppDevice appDevice = new AppDevice();
//                appDevice.setDeviceMac("00158d0000732fdb");
//                appDevice.setShortaddr("b9bd");
//                appDevice.setEndpoint("01");
//
//                SerialHandler.getInstance().addDeviceToGroup(appDevice,(short) 4);

                if (editTxt.getText().toString().equals("")) {
                    return;
                }
                String mac = "";
                for (int i = 0; i < appDeviceList.size(); i++) {
                    if (appDeviceList.get(i).getSelected()) {
                        mac = mac + appDeviceList.get(i).getDeviceMac();
                    }
                }
                System.out.println("GetAllGroupsCMD = " + mac);
                System.out.println("GetAllGroupsCMD = " + appDeviceList.size());
                SerialHandler.getInstance().CreateGroup(editTxt.getText().toString(), appDeviceList.size(), mac);
//                LoadDialog.show(context, R.string.adding);
//                mHandler.sendEmptyMessageDelayed(1,Constants.appDeviceList.size()*2000);
                break;
        }
    }

    public static int mSce20Count = 0;
    public Timer m20SecTimer = null;

    public class TwSecTask extends TimerTask {
        @Override
        public void run() {
            if (mSce20Count == Count) {
                if (m20SecTimer != null) {
                    m20SecTimer.cancel();
                    m20SecTimer = null;
                }
            } else {
                mHandler.sendEmptyMessage(2);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoadDialog.dismiss(context);
//                    appDeviceList.clear();
//                    groupsDeviceAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });
}

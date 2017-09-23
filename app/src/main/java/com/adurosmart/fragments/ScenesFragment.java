package com.adurosmart.fragments;

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
import android.widget.ListView;
import android.widget.Toast;
import com.adurosmart.FirstEvent;
import com.adurosmart.adapters.SceneInfoAdapter;
import com.adurosmart.adapters.ScenesGroupsAdapter;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.LoadDialog;
import com.core.entity.AppGroup;
import com.core.entity.AppScene;
import com.core.gatewayinterface.SerialHandler;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.adurosmart.global.Constants.appDeviceList;
import static com.adurosmart.global.Constants.appGroupList;

public class ScenesFragment extends Fragment {
    @BindView(R.id.edit_txt)
    EditText editTxt;
    @BindView(R.id.add_scenes)
    Button addScenes;
    @BindView(R.id.scenes_list)
    ListView scenesList;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.group_list)
    ListView groupList;
    private Context context;
    private boolean isRegFilter = false;
    private SceneInfoAdapter sceneInfoAdapter;
    private ScenesGroupsAdapter scenesGroupsAdapter;
    private List<AppScene> scene_List = new ArrayList<>();
    private AppScene appScene;
    private Short scene_Group_Id = 0;
    private Short scene_Id = 0;
    private String scene_Name = "";
    private boolean isRun = true;

    private static final String ARG_PARAM = "title";
    private String mParam;

    public ScenesFragment() {

    }

    public static ScenesFragment getInstance(String param) {
        ScenesFragment scenesFragment = new ScenesFragment();
        Bundle bundle = new Bundle();
        bundle.getString(ARG_PARAM, param);
        scenesFragment.setArguments(bundle);
        return scenesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenes, container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        //注册EventBus
        EventBus.getDefault().register(this);
        sceneInfoAdapter = new SceneInfoAdapter(context, scene_List);
        scenesGroupsAdapter = new ScenesGroupsAdapter(context, appGroupList);
        initview();
        regFilter();
        return view;
    }

    public void initview() {
        srl.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scenesGroupsAdapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scene_List.clear();
                        sceneInfoAdapter.notifyDataSetInvalidated();
                        SerialHandler.getInstance().getSences();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });

        scenesList.setAdapter(sceneInfoAdapter);
        groupList.setAdapter(scenesGroupsAdapter);
        groupList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SECENS_INFO");
        filter.addAction("GROUPS_INFO");
        filter.addAction("ADD_SCENE_CALLBACK");
        context.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SECENS_INFO")) {
                //获取grouplist
                appScene = (AppScene) intent.getSerializableExtra("AppSceneInfo");
                for (AppScene appSceneInfo : scene_List) {
                    if (appSceneInfo.getSencesId() == appScene.getSencesId()) {
                        return;
                    }
                }
                scene_List.add(appScene);
                sceneInfoAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals("GROUPS_INFO")) {

                //获取grouplist
//                appGroup = (AppGroup) intent.getSerializableExtra("AppGroupInfo");
//                for (AppGroup appGroupInfo : scene_groups) {
//                    if (appGroupInfo.getGroup_id().equals(appGroup.getGroup_id())) {
//                        return;
//                    }
//                }
//                scene_groups.add(appGroup);
//                scenesGroupsAdapter.notifyDataSetChanged();

            } else if (intent.getAction().equals("ADD_SCENE_CALLBACK")) {
                scene_Group_Id = intent.getShortExtra("Scene_Group_Id", (short) 0);
                scene_Id = intent.getShortExtra("Scene_Id", (short) 0);
                scene_Name = intent.getStringExtra("Scene_Name");

                if (scene_List.size() == 0) {
                    AppScene item2 = new AppScene();
                    item2.setSencesId(scene_Id);
                    item2.setSencesName(scene_Name);
                    item2.setGroups_id(scene_Group_Id);
                    scene_List.add(item2);
                    sceneInfoAdapter.notifyDataSetChanged();
                } else {
                    synchronized (scene_List) {
                        //--------------搜索到一个设备-----------
                        for (AppScene sceneId : scene_List) {
                            if (sceneId.getSencesId() == scene_Id) {
                                return;
                            }
                        }

                        AppScene item2 = new AppScene();
                        item2.setSencesId(scene_Id);
                        item2.setSencesName(scene_Name);
                        item2.setGroups_id(scene_Group_Id);
                        scene_List.add(item2);
                        sceneInfoAdapter.notifyDataSetChanged();
                        System.out.println("parseData.Scene_Name = " + scene_Name);
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AddDeviceToScene();
                    }
                }, 200);
            }
        }
    };


    public void AddDeviceToScene() {
        for (int j = 0; j < appDeviceList.size(); j++) {
            if (appDeviceList.get(j).getSelected()) {
                SerialHandler.getInstance().addDeviceToSence(appDeviceList.get(j), scene_Group_Id, scene_Id);
                System.out.println("SerialHandler AddDeviceToScene= " + appDeviceList.get(j).getDeviceMac());
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class AddScene implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessageDelayed(1,appDeviceList.size()*2000);
            String mac = "";
            for (int i = 0; i < appDeviceList.size(); i++) {
                if (appDeviceList.get(i).getSelected()){
                    mac = mac + appDeviceList.get(i).getDeviceMac();
                }
            }
            System.out.println("GetAllGroupsCMD = " + mac);
            System.out.println("GetAllGroupsCMD = " + appDeviceList.size());
            for (AppGroup groupInfo : appGroupList) {
                if (groupInfo.getSelected()) {
                    String scene_name = editTxt.getText().toString();
                    SerialHandler.getInstance().AddSence(scene_name, groupInfo.getGroup_id(), appDeviceList.size(), mac);
                }
            }
        }
    }

    @OnClick(R.id.add_scenes)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_scenes:
//                AppDevice appDevice = new AppDevice();
//                appDevice.setDeviceMac("00158d0000737221");
//                appDevice.setShortaddr("1804");
//                appDevice.setEndpoint("01");

//                SerialHandler.getInstance().AddDeviceFromSceneFile((short)2,(short)2,"00158d0000737221");
//                try {
//                    Thread.sleep(1000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                SerialHandler.getInstance().addDeviceToSence(appDevice, (short)2, (short) 2);
                if (editTxt.getText().toString().equals("")){
                    return;
                }
                new Thread(new AddScene()).start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
        isRun = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(final FirstEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getAppScene() == null) {
                    return;
                }
                Toast.makeText(context, event.getAppScene().getSencesName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoadDialog.dismiss(context);
                    break;
            }
            return false;
        }
    });
}

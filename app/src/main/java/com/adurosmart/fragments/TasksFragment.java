package com.adurosmart.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adurosmart.activitys.AddTaskActivity;
import com.adurosmart.adapters.vRecyclerAdapter;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.DividerGridItemDecoration;
//import com.core.commanddata.appdata.DeviceCmdData;
//import com.core.commanddata.appdata.TaskCmdData;
//import com.core.commanddata.gwdata.ParseDeviceData;
import com.core.entity.AppTask2;
import com.core.gatewayinterface.SerialHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksFragment extends Fragment {
    private static final String TAG = "TasksFragment";
//    @BindView(R.id.task_list)
//    ListView taskList;
    @BindView(R.id.task_list)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Context mContext;
    private boolean isRegFilter = false;
    private static final String ARG_PARAM = "title";
    private String mParam;
    private List<AppTask2> taskInfoList = new ArrayList<>();
    private AppTask2 appTask;
//    private TaskInfoAdapter taskInfoAdapter = null;
    private vRecyclerAdapter recycleAdapter;

    public TasksFragment() {

    }

    public static TasksFragment getInstance(String param) {
        TasksFragment tasksFragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.getString(ARG_PARAM, param);
        tasksFragment.setArguments(bundle);
        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetasks, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
//        taskInfoAdapter = new TaskInfoAdapter(mContext, taskInfoList);
//        taskList.setAdapter(taskInfoAdapter);
        initComponent();
        regFilter();
        return view;
    }

    public void initComponent() {
        recycleAdapter= new vRecyclerAdapter(mContext , taskInfoList );
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        recyclerView .setLayoutManager(new GridLayoutManager(mContext,2));
//        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter( recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL));//正常item的分割线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext));//GridItem是左右的分割线
        recycleAdapter.setOnItemClickListener(new vRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(final int position) {
                Toast.makeText(mContext,"onLongClick事件       您点击了第："+position+"个Item",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_view, null);
                dialog.setView(layout);
                final EditText et_search = (EditText)layout.findViewById(R.id.searchC);
                et_search.setVisibility(View.GONE);
                dialog.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SerialHandler.getInstance().DeleteTask(taskInfoList.get(position).getTask_no());
                        taskInfoList.remove(position);
                        recycleAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            @Override
            public void onClick(int position) {
                Toast.makeText(mContext,"onClick事件       您点击了第："+position+"个Item",Toast.LENGTH_SHORT).show();
            }
        });

        srl.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorPrimary));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        taskInfoList.clear();
                        recycleAdapter.notifyDataSetChanged();
                        SerialHandler.getInstance().GetAllTask();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("TASK_INFO");
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("TASK_INFO")) {
                //获取任务列表
                appTask = (AppTask2) intent.getSerializableExtra("AppTaskInfo");
                for (AppTask2 appTaskInfo : taskInfoList) {
                    if (appTaskInfo.getTask_no() == appTask.getTask_no()) {
                        return;
                    }
                }
                taskInfoList.add(appTask);
                recycleAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(TAG + "onDestroy");
        Intent it = new Intent();
        it.setAction("CONTROL_BACK");
        mContext.sendBroadcast(it);
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println(TAG + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println(TAG + "=" + "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println(TAG + "=" + "onStop");
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Intent i = new Intent(mContext, AddTaskActivity.class);
                startActivity(i);
//                new Thread(new getDevice()).start();
                break;
        }
    }
}

package com.adurosmart.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.adurosmart.FirstEvent;
import com.adurosmart.sdk.R;
import com.facecropper.FaceActivity;
import com.ftp.FtpActivity;
import com.loading.LoadingActivity;
import com.mediacodec.RenderActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by best on 2016/12/16.
 */

public class SettingFragment extends Fragment {
    @BindView(R.id.btn_3d_desktop)
    Button btn3dDesktop;
    @BindView(R.id.btn_facer)
    Button btn_facer;
    @BindView(R.id.btn_ftp)
    Button btn_ftp;
    @BindView(R.id.btn_scrol)
    Button btn_scrol;
    @BindView(R.id.dec_btn)
    Button dec_btn;
    private Context context;
    private boolean isRegFilter = false;

    private static final String ARG_PARAM = "title";
    private String mParam;

    public SettingFragment() {

    }

    public static SettingFragment getInstance(String param) {
        SettingFragment settingFragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.getString(ARG_PARAM, param);
        settingFragment.setArguments(bundle);
        return settingFragment;
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        //注册EventBus
        EventBus.getDefault().register(this);
        initview();
        regFilter();
        return view;
    }

    public void initview() {

    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SECENS_INFO");
        context.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SECENS_INFO")) {

            }
        }
    };

    @OnClick({ R.id.btn_3d_desktop,R.id.btn_facer,R.id.btn_ftp,R.id.btn_scrol,R.id.dec_btn,R.id.ffmpeg_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_3d_desktop:
                Intent intent = new Intent(context, LoadingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_facer:
                Intent intent1 = new Intent(context, FaceActivity.class);
                startActivity(intent1);
                break;

            case R.id.btn_ftp:
                Intent intent2 = new Intent(context, FtpActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_scrol:

                break;
            case R.id.ffmpeg_btn:
                Intent intent4 = new Intent(context, RenderActivity.class);
                startActivity(intent4);
                break;
            case R.id.dec_btn:
//                Intent intent4 = new Intent(context, ComingActivity.class);
//                startActivity(intent4);

                //ok http btn
//                Intent intent4 = new Intent(context, OkhttpActivity.class);
//                startActivity(intent4);

                break;
        }
    }

    public String getString(){
        String str = new String("sb  mandi");

        str.charAt(3);
        return str;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
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
}

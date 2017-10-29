package com.adurosmart.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adurosmart.FirstEvent;
import com.adurosmart.MyApp;
import com.adurosmart.adapters.DeviceInfoAdapter;
import com.adurosmart.sdk.R;
import com.adurosmart.util.TcpClient;
import com.adurosmart.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.core.db.GatewayInfo;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.utils.NetworkUtil;
import com.core.utils.TransformUtils;
import com.okhttp.SmartClient;
import com.okhttp.StrRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.adurosmart.global.Constants.isTask_Mac;
import static com.core.global.Constants.GW_IP_ADDRESS;


public class DevicesFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "DevicesFragment";
    @BindView(R.id.remote_switch)
    SwitchCompat remoteSwitch;
    @BindView(R.id.net_btn)
    Button netBtn;
    @BindView(R.id.read_attribute)
    Button readAttribute;
    @BindView(R.id.layout_ll)
    LinearLayout layoutLl;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.ip_text)
    TextView ip_text;
    @BindView(R.id.btn_reset)
    Button btn_reset;
    @BindView(R.id.update_info)
    Button update_info;
    @BindView(R.id.update_btn)
    Button update_btn;
    private static final String ARG_PARAM = "title";
    public static Context context;
    private boolean flag;
    private boolean isRegFilter = false;
    public static DeviceInfoAdapter deviceInfoAdapter;
    private AppDevice deviceInfo;
    public List<AppDevice> appDeviceList = new ArrayList<>();
    private MyApp myApp = MyApp.getInstance();
    private Handler handler = new Handler();
    private String mParam;

    public DevicesFragment() {

    }

    public static DevicesFragment getInstance(String param) {
        DevicesFragment devicesFragment = new DevicesFragment();
        Bundle bundle = new Bundle();
        bundle.getString(ARG_PARAM, param);
        devicesFragment.setArguments(bundle);
        return devicesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        //注册EventBus
        EventBus.getDefault().register(this);
        regFilter();
        deviceInfoAdapter = new DeviceInfoAdapter(context, appDeviceList, false);
        list.setAdapter(deviceInfoAdapter);
        initView();
        isTask_Mac = false;
        return view;
    }

    public void initView() {
        remoteSwitch.setSwitchPadding(40);
        remoteSwitch.setOnCheckedChangeListener(this);
        srl.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        appDeviceList.clear();
                        deviceInfoAdapter.notifyDataSetInvalidated();
                        SerialHandler.getInstance().GetAllDeviceListen();

                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                        ip_text.setText(GW_IP_ADDRESS);
                    }
                }, 500);
            }
        });
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("DEVICE_INFO");
        filter.addAction("SEND_EXCEPTION_CALLBACK");
        filter.addAction("AppDeviceInfo_CallBack");
        filter.addAction("DeviceState_CallBack");
        filter.addAction("DeviceLevelCallback");
        filter.addAction("bingdevicecallback");
        filter.addAction("RetDeviceZoneType");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SEND_EXCEPTION_CALLBACK")) {
                int result = intent.getIntExtra("Callback_Value", -1);
                Toast.makeText(context, "网关未连接", Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals("AppDeviceInfo_CallBack")) {
                //获取设备
                deviceInfo = (AppDevice) intent.getSerializableExtra("AppDeviceInfo");
                for (AppDevice devInfo : appDeviceList) {
                    if (devInfo.getDeviceMac().endsWith(deviceInfo.getDeviceMac())) {
                        devInfo.setEndpoint(deviceInfo.getEndpoint());
                        devInfo.setDeviceid(deviceInfo.getDeviceid());
                        deviceInfoAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                appDeviceList.add(deviceInfo);
                deviceInfoAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals("DeviceState_CallBack")) {
                String device_mac = intent.getStringExtra("Device_Mac");
                int state = intent.getIntExtra("DeviceState", -1);
                System.out.println("device_mac = " + device_mac + "," + state);
                synchronized (appDeviceList) {
                    for (AppDevice devInfo : appDeviceList) {
                        if (devInfo.getDeviceMac().endsWith(device_mac) || devInfo.getShortaddr().equalsIgnoreCase(device_mac)) {
                            devInfo.setDeviceState(state);
                            deviceInfoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } else if (intent.getAction().equals("DeviceLevelCallback")) {
                String device_mac = intent.getStringExtra("Device_Mac");
                int value = intent.getIntExtra("DeviceValue", -1);
                System.out.println("getDeviceLevel device_mac = " + device_mac);
                System.out.println("getDeviceLevel value = " + value);
                synchronized (appDeviceList) {
                    for (AppDevice devInfo : appDeviceList) {
                        if (devInfo.getDeviceMac().endsWith(device_mac)) {
                            devInfo.setBright(value);
                            deviceInfoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } else if (intent.getAction().equals("bingdevicecallback")) {
                String short_address = intent.getStringExtra("short_address");
                short frequency = intent.getShortExtra("frequency", (short) -1);
                double voltage = intent.getDoubleExtra("voltage", 0.0);
                double current = intent.getDoubleExtra("current", 0.0);
                double power = intent.getDoubleExtra("power", 0.0);
                double power_factor = intent.getDoubleExtra("power_factor", 0.0);

                for (int i = 0; i < appDeviceList.size(); i++) {
                    if (appDeviceList.get(i).getDeviceid().contains("0051")) {
                        if (appDeviceList.get(i).getShortaddr().equalsIgnoreCase(short_address)) {
                            AppDevice appDevice = appDeviceList.get(i);
                            appDevice.setFrequency(frequency);
                            appDevice.setVoltage(voltage);
                            appDevice.setCurrent(current);
                            appDevice.setPower(power);
                            appDevice.setPower_factor(power_factor);
                            deviceInfoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } else if (intent.getAction().equals("RetDeviceZoneType")) {
                String device_mac = intent.getStringExtra("DEVICE_MAC");
                String zone_type = intent.getStringExtra("ZONE_TYPE");
                synchronized (appDeviceList) {
                    for (AppDevice devInfo : appDeviceList) {
                        if (devInfo.getDeviceMac().equalsIgnoreCase(device_mac)) {
                            devInfo.setDeviceName(Constants.DeviceZoneType(zone_type));
                            devInfo.setZonetype(zone_type);
                            deviceInfoAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                WifiManager wifiManager = (WifiManager) MyApp.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                ConnectivityManager connectMgr = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
                if (connectMgr != null) {
                    /**
                     * 获取网络信息实体
                     * 由于从系统服务中获取数据属于进程间通信，基本类型外的数据必须实现Parcelable接口，
                     * NetworkInfo实现了Parcelable，获取到的activeNetInfo相当于服务中网络信息实体对象的一个副本（拷贝），
                     * 所以，不管系统网络服务中的实体对象是否置为了null，此处获得的activeNetInfo均不会发生变化
                     */
                    NetworkInfo activeNetInfo = connectMgr.getActiveNetworkInfo();
                    if (activeNetInfo != null) {
                        System.out.println("isAvailable = " + activeNetInfo.isAvailable());
                    }
                }

                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        SerialHandler.getInstance().ScanGatewayInfo(wifiManager);
                        Toast.makeText(context, networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        String topicName = GatewayInfo.getInstance().getGatewayNo(context);
                        if (!topicName.equals("") & NetworkUtil.isNetworkAvailable(context)){
                            SerialHandler.getInstance().setMqttCommunication(context,topicName);
                        }
                        Toast.makeText(context, networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "unconnect", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
            }
        }, 500);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (srl.isRefreshing() & getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            srl.setRefreshing(false);
                        }
                    });
                }
            }
        }, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @OnClick({R.id.remote_switch, R.id.net_btn, R.id.read_attribute, R.id.btn_reset, R.id.update_info, R.id.update_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:

//                try {
//                    String pws = "roy@doc";
//                    String admin = "roy@adurolight.com";
//                    Log.e("wocao = " , getResultForHttpGet(admin,pws));
//                }catch (IOException e){
//                    e.getMessage();
//                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        try {
//                            String uriAPI = "https://data.adurosmart.com/adurosmart/index.php/Sensor/smartctrl/readone";  //声明网址字符串
//                            HttpPost httpRequest = new HttpPost(uriAPI);   //建立HTTP POST联机
//                            List<NameValuePair> params = new ArrayList<NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存
//                            params.add(new BasicNameValuePair("str", "I am Post String"));
//                            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));   //发出http请求
//                            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);   //取得http响应
//                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                                String strResult = EntityUtils.toString(httpResponse.getEntity());   //获取字符串
//                                Log.e("wocao = ", strResult);
//                            }
//
//                        } catch (UnsupportedEncodingException e) {
//
//                        } catch (IOException e) {
//
//                        }

                    }
                }).start();


//                TelephonyManager TelephonyMgr = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
//                String szImei = TelephonyMgr.getDeviceId();
//                System.out.println("szImei = " + GatewayInfo.getInstance().getGwIEEEAddress(context));
//                System.out.println("wocao  = " + Constants.APP_IP_ADDRESS);
//                SerialHandler.getInstance().GetGwServerAddress();
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setMessage("Please enter the password reset?");
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_view, null);
//                final EditText searchC = (EditText) layout.findViewById(R.id.searchC);
//                dialog.setView(layout);
//                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String pwd = searchC.getText().toString().trim();
//                        if (pwd.length() == 0) {
//                            Toast.makeText(context, "请输入密码！！！", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        SerialHandler.getInstance().vRecoveryFactory(pwd);
//                    }
//                });
//
//                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();

                break;
            case R.id.remote_switch:
                byte[] bt = {0, 21, -115, 0, 0, -20, -55, 124};
//                6B6974

                System.out.println("shortaddr = " + GatewayInfo.getInstance().getGatewayNo(context));
                break;
            case R.id.net_btn:
//                String address = "120.24.242.83";
//                SerialHandler.getInstance().GetGwServerAddress();

//                new Thread(new getDevice()).start();
                SerialHandler.getInstance().AgreeDeviceInNet();
//                getInfo(context);
                break;
            case R.id.read_attribute:

                String address_ = "120.24.242.83";
                SerialHandler.getInstance().SetGwServerAddress(address_);

//                LoadDialog.show(context, "加载中");
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            //读取设备属性
//                            synchronized (appDeviceList) {
//                                for (AppDevice devInfo : appDeviceList) {
//                                    if (!devInfo.getDeviceid().contains("0402")) {
//                                        SerialHandler.getInstance().getDeviceSwitchState(devInfo);
//                                        Thread.sleep(1000);
//                                        SerialHandler.getInstance().getDeviceLevel(devInfo);
//                                        Thread.sleep(1000);
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        LoadDialog.dismiss(context);
//                    }
//                }, appDeviceList.size() * 1000);

                break;

            case R.id.update_info:
                SerialHandler.getInstance().CheckUpdateGatewayInfo();
                break;

            case R.id.update_btn:

                int i_array[] = {1,2,3,4};
                int i_len = i_array.length;
                int i_result[] = new int[i_len];
                for(int i = 0;i<i_result.length;i++){
                    i_len--;
                    i_result[i] = i_array[i_len];
                }

                System.out.println("反转结果 = " + Arrays.toString(i_result));

                int s;
                for (int i = 1; i <= 1000; i++) {
                    s = 0;
                    for (int j = 1; j < i; j++)
                        if (i % j == 0)
                            s = s + j;
                    if (s == i)
                        System.out.print(i + " " );
                }
                System.out.println();
                getsensor();
//                SerialHandler.getInstance().doUpdateGateway();

//                try {
//                    String str = "我是客户一";
//                    TcpClient tcpClient = new TcpClient(str.getBytes("utf-8"));
//                    tcpClient.setIpdreess(InetAddress.getByName("192.168.0.92"));
//                    tcpClient.createClient();
//                } catch (UnknownHostException u) {
//                    u.getMessage();
//                } catch (UnsupportedEncodingException u) {
//                    u.getMessage();
//                }

                break;
        }
    }

    public void getsensor() {
        String uri = "https://data.adurosmart.com/adurosmart/index.php/Sensor/smartctrl/readone";
        //网关信息请求
        Map<String, String> keyMap = new HashMap<>();
//            keyMap.put("user_id", SmartInforCache.getInstance().getUser().getResult().getUser_id());
        StrRequest strRequest = new StrRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                    parseKey(s);
                Log.e("shadiao", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("错了", "傻傻");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> keyMap = new HashMap<>();
//                    keyMap.put("user_id", SmartInforCache.getInstance().getUser().getResult().getUser_id());
                return keyMap;
            }
        };
        SmartClient.getInstance(context).addToRequestQueue(strRequest);

    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.remote_switch:
                if (isChecked) {
                    GW_IP_ADDRESS = "";
                } else {
                    GW_IP_ADDRESS = GatewayInfo.getInstance().getInetAddress(context);
                }
                break;
        }
    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public static void getInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb = android.os.Build.BRAND;// 手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        Log.i("text", "手机IMEI号：" + imei + "手机IESI号：" + imsi + "手机型号：" + mtype + "手机品牌：" + mtyb + "手机号码" + numer);
    }

    public class getDevice implements Runnable {
        public DatagramSocket socket = null;

        @Override
        public void run() {
            try {
                byte[] bt_send = null;
                if (GW_IP_ADDRESS.equals("")) {
                    System.out.println("getDevicewww = " + "AgreeDeviceInNet");
                } else {
                    InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);
                    if (socket == null) {
                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(Constants.UDP_PORT));
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(bt_send, bt_send.length, inetAddress, Constants.UDP_PORT);
                    socket.send(datagramPacket);
                    System.out.println("getDevice = " + TransformUtils.binary(bt_send, 16));

                    while (true) {
                        byte[] recbuf = new byte[1024];
                        final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);
                        try {
                            socket.receive(packet);
                            System.out.println("getDevice = " + Arrays.toString(recbuf));
                            String isK64 = new String(recbuf).trim();
                            if (isK64.contains("K64")) {
                                return;
                            }

                            System.out.println("getDevice = " + Arrays.toString(recbuf));
                        } catch (InterruptedIOException e) {
                            System.out.println("getDevice....................");
                            continue;  //非阻塞循环Operation not permitted
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(final FirstEvent event) {
        Log.d("best", "onEventMainThread收到了消息：" + event.getAppDevice().getDeviceMac());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getAppDevice() == null) {
                    return;
                }
                Toast.makeText(context, event.getAppDevice().getDeviceMac(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe
    public void onEvent(final FirstEvent event) {
        Log.d("best", "onEvent收到了消息：" + event.getAppDevice().getDeviceMac());
    }
    @Subscribe
    public void onEventBackground(final FirstEvent event) {
        Log.d("best", "onEventBackground收到了消息：" + event.getAppDevice().getDeviceMac());
    }
    @Subscribe
    public void onEventAsync(final FirstEvent event) {
        Log.d("best", "onEventAsync收到了消息：" + event.getAppDevice().getDeviceMac());
    }
}

package com.adurosmart;

/**
 * Created by best on 2016/10/25.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.adurosmart.util.LogUtil;
import com.core.gatewayinterface.DataSources;
import com.core.gatewayinterface.SerialHandler;
import com.core.global.Constants;
import com.core.utils.TransformUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.core.global.Constants.GW_IP_ADDRESS;

/**
 * Created by best on 2016/7/25.
 */
public class MyApp extends Application {
    private final static String Tag = "MainApplication";
    public static final String PACKAGE_NAME = "com.aduro.";
    public static final String MAIN_SERVICE_START = PACKAGE_NAME + "service.MAINSERVICE";
    public static MyApp app;
    public WifiManager wifiManager;

    private List<Activity> activityList = new ArrayList<>();

    public MyApp() {

    }

    public synchronized static MyApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String wocao = "卧  槽";

                    DatagramSocket socket = new DatagramSocket(null);
                    InetAddress inetAddress = InetAddress.getByName(GW_IP_ADDRESS);

                    DatagramPacket datagramPacket = new DatagramPacket(wocao.getBytes(), wocao.getBytes().length, inetAddress, Constants.UDP_PORT);
                    socket.send(datagramPacket);
                    System.out.println("getDevice = " + TransformUtils.binary(wocao.getBytes(), 16));

                    byte[] recbuf = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(recbuf, recbuf.length);

                    socket.receive(packet);
                    System.out.println("getDevice = " + Arrays.toString(recbuf));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }).start();


        InitGateway();
//        CrashReport.initCrashReport(getApplicationContext(), "3f35ebbacf", false);
    }

    public void InitGateway() {
        DataSources.getInstance().setSettingInterface(new GwInterfaceCallback());
        SerialHandler.getInstance().Init(app, "200004401331", new GwInterfaceCallback());
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        SerialHandler.getInstance().ScanGatewayInfo(wifiManager);
    }

    public void addActivity(Activity activity) {
        LogUtil.debug(Tag, "添加Activity" + activity.getLocalClassName());
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    public boolean removeActivity(Activity activity) {
        boolean flag = activityList.remove(activity);
        LogUtil.debug(Tag, "移除Activity" + activity.getClass().getSimpleName()
                + ",mActivityList.size():" + activityList.size());
        return flag;
    }

    public List<Activity> getActivityList() {
        return this.activityList;
    }

    /**
     * 退出所有activity。
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null) {
                    activity.finish();
                    LogUtil.debug(Tag, "Activity" + activity.getClass()
                            + "is finished!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}

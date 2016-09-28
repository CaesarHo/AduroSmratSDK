package com.core.utils;
/**
 * Created by best on 2016/9/28.
 */

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 工具类。
 * 判断终端联网与否，及联网类型
 *
 * @author Bruce
 * @version V1.0 2012-12-12 友盟E用车版本号及日期
 * @since V1.0 启动模块版本号
 */
public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static enum NETWORK_TYPE {
        NETWORK_2GOR3G, NETWORK_WIFI,
    };

    private static Boolean mIsNetWorkAvailable = false;
    public static NETWORK_TYPE mNetWorkType = NETWORK_TYPE.NETWORK_WIFI;

    public static String mThreeNum;

    /**
     * 判断是否有网络连接
     * @return
     */

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Application.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     * @return
     */

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Application.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @return
     */

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Application.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInoInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInoInfo != null) {
            return mMobileNetworkInoInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息 one of TYPE_MOBILE,TYPE_WIFI,TYPE_WIMAX,TYPE_ETHERNET
     * TYPE_BLUETOOTH,or other types defined by ConnectivityManager
     * @return
     */
    public static int getConnectedType(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Application.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;// -1表示无网络
    }




    public static void setNetWorkState(boolean state) {
        mIsNetWorkAvailable = state;
    }

    public static boolean getNetWorkState() {
        return mIsNetWorkAvailable;
    }

    public static boolean verifyNetwork(Context context) {
        boolean isNetworkActive = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (activeNetInfo.isConnected()) {
                setNetWorkState(true);
                isNetworkActive = true;
            } else {
                setNetWorkState(false);
                isNetworkActive = false;
            }

            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                mNetWorkType = NETWORK_TYPE.NETWORK_WIFI;
            } else {
                mNetWorkType = NETWORK_TYPE.NETWORK_2GOR3G;
            }
        } else {
            setNetWorkState(false);
            isNetworkActive = false;
        }

        return isNetworkActive;
    }

    public static boolean NetWorkType(Context context){
        boolean isWiFi = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                mNetWorkType = NETWORK_TYPE.NETWORK_WIFI;
                isWiFi = true;
            } else {
                mNetWorkType = NETWORK_TYPE.NETWORK_2GOR3G;
                isWiFi = false;
            }
        }
        System.out.println("isWiFi = " + isWiFi);
        return isWiFi;
    }

}
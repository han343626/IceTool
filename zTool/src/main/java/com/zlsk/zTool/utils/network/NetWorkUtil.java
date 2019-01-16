package com.zlsk.zTool.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zlsk.zTool.utils.CommonToolUtil;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by IceWang on 2018/8/29.
 */

public class NetWorkUtil {
    public enum ENetType {
        wifi, CMNET, CMWAP, noneNet
    }

    public static void connectionTest(Class target,String url,int eventId){
        new Thread(() -> {
            RequestEvent event = new RequestEvent(eventId);
            event.setTarget(target);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setConnectTimeout(5 * 1000);
                event.setData(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                event.setData(false);
            }
            EventBusUtil.post(event);
        }).start();
    }

    /**
     * 网络是否可用
     *
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager mgr = (ConnectivityManager) CommonToolUtil.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
     * @param context
     *
     * @return
     */
    public static ENetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return ENetType.noneNet;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return ENetType.CMNET;
            }

            else {
                return ENetType.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return ENetType.wifi;
        }
        return ENetType.noneNet;

    }

    /**
     * 使用Wifi时获取IP 设置用户权限
     *
     * <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * <uses-permission
     * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
     *
     * <uses-permission
     * android:name="android.permission.WAKE_LOCK"></uses-permission>
     *
     * @return
     */
    public static String getWifiIp(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);

    }

    /**
     * 使用GPRS上网，时获取ip地址，设置用户上网权限
     *
     * <uses-permission
     * android:name="android.permission.INTERNET"></uses-permission>
     *
     * @return
     */
    public static String getGPRSIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    /*
     * 网络判断0代表无网络，1代表手机网络,2代表wifi，3代表网络已连接
     */
    public static int network_Identification(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isConnected()) {
                        if (0 == info[i].getType()) {
                            return 1;
                        }
                        if (1 == info[i].getType()) {
                            return 2;
                        }
                        return 3;
                    }

                }
            }
        }
        return 0;
    }

    /**
     * 检测网络连接
     *
     * @return
     */
    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * Wifi是否可用
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        if (null == mContext)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI");
    }

    /**
     * GPRS是否可用
     * @param mContext
     * @return
     */
    public static boolean isGPRS(Context mContext) {
        if (null == mContext)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null == wifiManager)
            return false;
        if (null == networkInfo)
            return false;

        NetworkInfo.State state = networkInfo.getState();
        if (state == NetworkInfo.State.CONNECTED && !(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)) {
            return true;
        } else if (state == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}

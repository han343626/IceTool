package com.zlsk.zTool.utils.date;

import android.location.Location;
import android.os.SystemClock;

import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;

public class WebTimeTask {
    private static WebTimeTask instance;
    private long initSystemClockMillis;
    private long initgtm8Millis = 0;
    private boolean GTMTimeInited = false;
    private boolean threadRuning = false;

    public static WebTimeTask getInstance() {
        if (null == instance) {
            instance = new WebTimeTask();
        }
        return instance;
    }

    public boolean isGTMTimeInited() {
        return GTMTimeInited;
    }

    private WebTimeTask() {
        requestGTMTime();
    }

    public void initGTMTimeByGPSLocation(Location location) {
        if (null == location)
            return;
        String time = DateUtil.changeLongToString(location.getTime());
        if (time == null || time.contains("1970")) {
            GTMTimeInited = false;
        } else {
            initSystemClockMillis = SystemClock.uptimeMillis();
            initgtm8Millis = location.getTime();
            GTMTimeInited = true;
        }
    }

    private void requestGTMTime() {
        initSystemClockMillis = SystemClock.uptimeMillis();
        initgtm8Millis = System.currentTimeMillis();
        if (!threadRuning) {
            try {
                NetWorkRunnable runable = new NetWorkRunnable();
                Thread thread = new Thread(runable);
                thread.start();
                threadRuning = true;
            } catch (Exception e) {
                // TODO: handle exception
                threadRuning = false;
            }
        }
    }

    private class NetWorkRunnable implements Runnable {
        public NetWorkRunnable() {
        }

        @Override
        public void run() {
            try {
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); // 时区设置
                URL url = new URL("http://open.baidu.com/special/time/");//取得资源对象
                URLConnection uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接

                initgtm8Millis = uc.getDate(); //取得网站日期时间（时间戳）
                initSystemClockMillis = SystemClock.uptimeMillis();
                GTMTimeInited = true;
            } catch (Exception e) {
                initSystemClockMillis = SystemClock.uptimeMillis();
                initgtm8Millis = System.currentTimeMillis();
                GTMTimeInited = false;
            } finally {
                threadRuning = false;
            }
        }
    }

    /***
     * 获得网络时间
     */
    public long getWebTime() {
        if (!GTMTimeInited) {
            requestGTMTime();
        }
        long timeOffset = SystemClock.uptimeMillis() - initSystemClockMillis;
        long time = initgtm8Millis + timeOffset;
        return time;
    }
}

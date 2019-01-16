package com.zlsk.zTool.utils.crash;

import android.content.Context;
import android.os.Build;

import com.zlsk.zTool.utils.system.DeviceUtil;
import com.zlsk.zTool.utils.file.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IceWang on 2018/9/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final Format FORMAT = new SimpleDateFormat("yy-MM-dd HH-mm-ss", Locale.getDefault());

    private static CrashHandler instance = null;
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    synchronized (CrashHandler.class) {
                        instance = new CrashHandler();
                    }
                }
            }
        }
        return instance;
    }

    private CrashHandler() {
    }

    public void init(Context context) {
        this.context = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private String getCrashHead(){
        return  "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + DeviceUtil.getAppVersionName(context) +
                "\nApp VersionCode    : " + DeviceUtil.getAppVersionNo(context) +
                "\n************* Crash Log Head ****************\n\n";
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        if (e == null) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                saveCrashToLocal(t,e);
            }
        }).start();
    }

    private void saveCrashToLocal(Thread t,final Throwable e){
        Date now = new Date(System.currentTimeMillis());
        String fileName = "crash_" + FORMAT.format(now) + ".log";
        final String fullPath = FileUtil.getInstance().getCrashPath() + "/" + fileName;
        FileUtil.getInstance().createNewFile(fullPath);

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fullPath, false));
            pw.write(getCrashHead());
            e.printStackTrace(pw);
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

        if (defaultHandler != null) {
            defaultHandler.uncaughtException(t, e);
        }
    }
}

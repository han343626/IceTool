package com.zlsk.zTool.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.zlsk.zTool.baseActivity.slideback.ActivityHelper;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.crash.CrashHandler;
import com.zlsk.zTool.utils.file.FileUtil;
import com.zlsk.zTool.utils.storage.SharedPreferencesUtil;

public class CommonToolUtil {
    @SuppressLint("StaticFieldLeak")
    private static CommonToolUtil instance;
    private Context context;
    private String mainPackage;

    private CommonToolUtil() {
    }

    public static synchronized CommonToolUtil getInstance() {
        if (instance == null) {
            instance = new CommonToolUtil();
        }
        return instance;
    }

    /**
     * @param context 全局ApplicationContext
     * @param mainPackage 主程序包名,用以反射
     */
    public void init(Context context,String mainPackage){
        this.context = context;
        this.mainPackage = mainPackage;
        ZToast.getInstance(context);
        FileUtil.getInstance(context);
        SharedPreferencesUtil.getInstance(context);
        CrashHandler.getInstance().init(context);
        SDKInitializer.initialize(context);
        ((Application)context).registerActivityLifecycleCallbacks(ActivityHelper.getInstance());
    }

    public void initBaiduMap(){
        SDKInitializer.initialize(context);
    }

    public Context getContext() {
        if(context != null){
            return context;
        }
        throw new NullPointerException("请先调用CommonToolUtil.init()方法");
    }

    public String getMainPackage() {
        return mainPackage;
    }
}
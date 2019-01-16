package com.zlsk.zTool.utils.system;

import android.app.Application;

import com.zlsk.zTool.helper.ActivityLifeManage;

import org.xutils.x;

/**
 * Created by IceWang on 2018/8/27.
 */

public class CommonApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        registerActivityLifecycleCallbacks(ActivityLifeManage.getInstance());
    }
}

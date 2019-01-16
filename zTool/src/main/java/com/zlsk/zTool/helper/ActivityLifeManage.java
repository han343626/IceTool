package com.zlsk.zTool.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by IceWang on 2018/8/27.
 */

public class ActivityLifeManage implements Application.ActivityLifecycleCallbacks{
    private static ActivityLifeManage instance;

    public static ActivityLifeManage getInstance() {
        if(instance == null){
            instance = new ActivityLifeManage();
        }
        return instance;
    }

    public static final int APP_STATUS_DEAD = 0;
    public static final int APP_STATUS_FRONT = 1;
    public static final int APP_STATUS_BACKGROUND = 2;

    private int isBackgroundJudgeCount;
    private int isAliveJudgeCount;

    public int getAppStatus() {
        return isAliveJudgeCount == 0 ?
                APP_STATUS_DEAD : isBackgroundJudgeCount == 0 ?
                APP_STATUS_BACKGROUND : APP_STATUS_FRONT;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        isAliveJudgeCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        isBackgroundJudgeCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isBackgroundJudgeCount--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        isAliveJudgeCount--;
    }
}

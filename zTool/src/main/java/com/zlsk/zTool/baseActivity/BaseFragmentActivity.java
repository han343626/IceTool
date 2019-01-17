package com.zlsk.zTool.baseActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

/**
 * Created by IceWang on 2018/7/2.
 */

public class BaseFragmentActivity extends FragmentActivity {
    public Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//使状态栏透明
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        x.view().inject(this);
        if(isSubscriber()){
            EventBusUtil.register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(isSubscriber()){
            EventBusUtil.unregister(this);
        }
        LoadingDialog.getInstance().dismiss();
        super.onDestroy();
    }

    protected boolean isSubscriber(){
        return true;
    }
    protected void postEvent(Object event){
        EventBusUtil.post(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event){

    }
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPostThread(RequestEvent event){

    }

    protected void showToast(String msg) {
        ZToast.getInstance().show(msg);
    };
}

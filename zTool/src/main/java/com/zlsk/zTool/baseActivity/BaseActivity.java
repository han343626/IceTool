package com.zlsk.zTool.baseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zlsk.zTool.baseActivity.slideback.ActivityHelper;
import com.zlsk.zTool.baseActivity.slideback.SlideBackHelper;
import com.zlsk.zTool.baseActivity.slideback.SlideConfig;
import com.zlsk.zTool.baseActivity.slideback.callback.OnSlideListenerAdapter;
import com.zlsk.zTool.customControls.base.CustomViewInflater;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

/**
 * Created by IceWang on 2018/4/23.
 */

public class BaseActivity extends Activity {
    protected Context context;
    protected CustomViewInflater customViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//使状态栏透明
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        x.view().inject(this);
        if(isSubscriber()){
            EventBusUtil.register(this);
        }
        context = this;
        initSlideBack();
        customViewInflater = new CustomViewInflater(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        customViewInflater.onActivityResult(requestCode, resultCode, data);
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
    }

    /**
     * 是否支持右滑退出当前页
     */
    protected boolean supportSlideBack() {
        return true;
    }

    /**
     * 是否只允许边缘滑
     */
    protected boolean edgeOnlySlideBack() {
        return false;
    }

    /**
     * 当前页面退出(手动退出:滑动，点击back)
     */
    protected void onPageClose(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            onPageClose();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputManger != null){
                inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void initSlideBack() {
        SlideBackHelper.attach(
                // 当前Activity
                this, ActivityHelper.getInstance(),
                // 参数的配置
                new SlideConfig.Builder()
                        // 屏幕是否旋转
                        .rotateScreen(false)
                        // 是否只有边缘侧滑
                        .edgeOnly(edgeOnlySlideBack())
                        // 是否禁止侧滑
                        .lock(!supportSlideBack())
                        // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
                        .edgePercent(0.1f)
                        // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.5f).create(),
                // 滑动的监听
                new SlideListener());
    }

    class SlideListener extends OnSlideListenerAdapter {
        @Override
        public void onSlide(float percent) {
            super.onSlide(percent);

        }

        @Override
        public void onOpen() {
            super.onOpen();

        }

        @Override
        public void onClose() {
            super.onClose();
            onPageClose();
        }
    }
}

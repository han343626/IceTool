package com.zlsk.zTool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.event.RequestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by IceWang on 2018/5/23.
 */

public abstract class ABaseCustomDialog extends Dialog{
    protected Context context;

    protected abstract int getContentViewId();
    protected abstract boolean getCanceledOnTouchOutside();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ABaseCustomDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());
        setCanceledOnTouchOutside(getCanceledOnTouchOutside());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event){

    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputManger != null){
                inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}

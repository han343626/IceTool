package com.zlsk.zTool.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;

import com.zlsk.zTool.R;

public class LoadingProgressDialog extends ABaseCustomDialog {
    private TextView tvMsg;

    @Override
    protected int getContentViewId() {
        return R.layout.module_loading_progress;
    }

    @Override
    protected boolean getCanceledOnTouchOutside() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return false;
    }

    public LoadingProgressDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        tvMsg = findViewById(R.id.m_tv_loading_msg);
    }

    public void setMessage(String strMessage) {
        tvMsg.setText(strMessage);
    }
}

package com.zlsk.zTool.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.textview.CenterTextView;

/**
 * Created by IceWang on 2018/10/11.
 */

public class ConfirmDialog extends ABaseCustomDialog implements View.OnClickListener {
    private String showMsg;
    private OnConfirmCallback callback;

    public ConfirmDialog(Context context, String showMsg, OnConfirmCallback callback) {
        super(context);
        this.showMsg = showMsg;
        this.callback = callback;
        getWindow().setDimAmount(0);

        initUi();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected boolean getCanceledOnTouchOutside() {
        return false;
    }

    private void initUi(){
        CenterTextView tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setText(showMsg);

        Button btn_ok = findViewById(R.id.btn_ok);
        Button btn_cancel = findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        callback.callback(v.getId() == R.id.btn_ok);
        dismiss();
    }

    public interface OnConfirmCallback{
        void callback(boolean ok);
    }
}

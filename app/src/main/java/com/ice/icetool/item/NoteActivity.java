package com.ice.icetool.item;

import android.view.View;
import android.widget.TextView;

import com.ice.icetool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.dialog.ConfirmDialog;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;

/**
 * Created by IceWang on 2019/1/4.
 */

public class NoteActivity extends ATitleBaseActivity{
    @Override
    public int getContentViewId() {
        return R.layout.activity_note;
    }

    @Override
    public String getTitleString() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public String getActionString() {
        return null;
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    public void onClick(View view){
        String msg = ((TextView)view).getText().toString();
        switch (view.getId()){
            case R.id.tv_toast:
                ZToast.getInstance().show(msg);
                break;
            case R.id.tv_loading:
                LoadingDialog.getInstance().show(msg);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //即使是非UI线程也可直接调用,无需runOnUiThread
                    LoadingDialog.getInstance().dismiss();
                }).start();
                break;
            case R.id.tv_confirm:
                new ConfirmDialog(context, msg, ok -> ZToast.getInstance().show(msg + "  " + ok)).show();
                break;
        }
    }
}

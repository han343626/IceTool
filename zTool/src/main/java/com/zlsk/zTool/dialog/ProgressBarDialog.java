package com.zlsk.zTool.dialog;

import android.content.Context;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.other.ZProgressBar;

/**
 * Created by IceWang on 2018/9/18.
 */

public class ProgressBarDialog extends ABaseCustomDialog{
    private ZProgressBar zProgressBar;
    private TextView tvContent;

    public ProgressBarDialog(Context context) {
        super(context);
        initUi();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_z_progress_bar;
    }

    @Override
    protected boolean getCanceledOnTouchOutside() {
        return false;
    }

    private void initUi(){
        zProgressBar = findViewById(R.id.progressBar);
        zProgressBar.setStop(false);
        tvContent = findViewById(R.id.tv_content);
    }

    public void setProgress(float progress){
        zProgressBar.setProgress(progress);
    }

    public void setProgressContent(String[] strings,String tvContent){
        zProgressBar.setShowText(strings);

        this.tvContent.setText(tvContent);
    }
}

package com.zlsk.zTool.customControls.other;

import android.view.View;

import java.util.Calendar;

/**
 * Created by IceWang on 2018/12/13.
 */

public abstract class AvoidDoubleClickListener implements View.OnClickListener {
    public abstract void OnClick(View view);

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            OnClick(v);
        }
    }
}

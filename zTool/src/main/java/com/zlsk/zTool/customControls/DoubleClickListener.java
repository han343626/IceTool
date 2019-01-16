package com.zlsk.zTool.customControls;

import android.view.View;

/**
 * Created by IceWang on 2018/11/23.
 */

public  abstract class DoubleClickListener implements View.OnClickListener {
    private static final long DOUBLE_TIME = 500;
    private static long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            onDoubleClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    public abstract void onDoubleClick(View v);
}

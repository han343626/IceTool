package com.zlsk.zTool.dialog;

import android.content.Context;

/**
 * Created by IceWang on 2018/6/1.
 */

public class SpinnerDialog extends ABaseCustomDialog{
    public SpinnerDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected boolean getCanceledOnTouchOutside() {
        return false;
    }
}

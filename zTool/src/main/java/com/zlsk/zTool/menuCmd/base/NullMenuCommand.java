package com.zlsk.zTool.menuCmd.base;

import android.content.Context;

public class NullMenuCommand extends AMenuCommand {
    @Override
    public int getMenuIconResourceId(String iconName) {
        return 0;
    }

    @Override
    public boolean canExecute(Context context, Object menuInfo) {
        return true;
    }

    @Override
    public void execute(Context context, Object data) {

    }
}

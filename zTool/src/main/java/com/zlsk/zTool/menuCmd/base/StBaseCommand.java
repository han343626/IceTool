package com.zlsk.zTool.menuCmd.base;

import android.content.Context;

public interface StBaseCommand {
    boolean canExecute(Context context, Object data);
    void execute(Context context, Object data);
}

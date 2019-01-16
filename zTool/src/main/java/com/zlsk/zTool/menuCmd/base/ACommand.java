package com.zlsk.zTool.menuCmd.base;

import android.content.Context;


public abstract class ACommand {
    public abstract boolean canExecute(Context context, Object data);
    public abstract void execute(Context context, Object data);
}

package com.zlsk.zTool.menuCmd.command;

import android.content.Context;
import android.content.Intent;

import com.zlsk.zTool.menuCmd.base.StCommonBaseCommand;


public class StartActivityCommand implements StCommonBaseCommand {
    private Context context = null;
    private String name = "";
    private Class<?> targetClass = null;

    public StartActivityCommand(Context context, String name, Class<?> targetClass){
        this.context = context;
        this.name = name;
        this.targetClass = targetClass;
    }

    @Override
    public int getCommandId() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canExecute(Context context, Object data) {
        return true;
    }

    @Override
    public void execute(Context context, Object data) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
}

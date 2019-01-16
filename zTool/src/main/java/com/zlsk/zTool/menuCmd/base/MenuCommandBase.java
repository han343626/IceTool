package com.zlsk.zTool.menuCmd.base;

import android.content.Context;
import android.content.Intent;

import com.zlsk.zTool.utils.system.ResourceUtil;

import java.util.Map;


public class MenuCommandBase extends AMenuCommand {
    @Override
    public boolean canExecute(Context context, Object menuInfo) {
        return true;
    }

    @Override
    public void execute(Context context, Object menuInfo) {
        Map<String, Object> mi = (Map<String, Object>)menuInfo;
        if(mi != null && mi.containsKey("activity")){
            String cn = mi.get("activity").toString();
            try {
                Intent intent = new Intent(context.getApplicationContext(), Class.forName(cn));
                context.startActivity(intent);
            }catch (Exception e){

            }
        }
    }

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }
}

package com.zlsk.zTool.menuCmd.menu;

import android.content.Context;

import com.zlsk.zTool.dialog.CommandActionSheetDialog;
import com.zlsk.zTool.menuCmd.base.MenuCommandBase;
import com.zlsk.zTool.menuCmd.base.StCommonBaseCommand;
import com.zlsk.zTool.menuCmd.command.StartActivityCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectActivityMenuCommand extends MenuCommandBase {
    @Override
    public void execute(Context context, Object menuInfo) {
        Map<String, Object> mi = (Map<String, Object>)menuInfo;
        if(mi != null){
            List<Map<String, Object>> al = (List<Map<String, Object>>)mi.get("activities");
            List<StCommonBaseCommand> cl = new ArrayList<>();
            for(Map<String, Object> a : al){
                try{
                    StartActivityCommand sac = new StartActivityCommand(context, a.get("name").toString(), Class.forName(a.get("activity").toString()));
                    cl.add(sac);
                }catch (Exception e){
                    return;
                }
            }
            if(cl.size() > 0){
                CommandActionSheetDialog.show(context, null, cl);
            }
        }
    }
}

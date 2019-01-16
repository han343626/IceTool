package com.zlsk.zTool.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.zlsk.zTool.menuCmd.base.StCommonBaseCommand;

import java.util.ArrayList;
import java.util.List;


public class CommandActionSheetDialog extends ActionSheetDialog {
    public static void show(final Context context, String title, final List<StCommonBaseCommand> actions) {
        List<String> actionNames = new ArrayList<String>();
        for (StCommonBaseCommand action : actions) {
            actionNames.add(action.getName());
        }
        String[] actionNameArray = {};
        actionNameArray = actionNames.toArray(actionNameArray);

        ActionSheetDialog.show(context, title, actionNameArray, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StCommonBaseCommand menu = actions.get(position);
                menu.execute(context, menu.getCommandId());
                CommandActionSheetDialog.dismiss();
            }
        });
    }
}

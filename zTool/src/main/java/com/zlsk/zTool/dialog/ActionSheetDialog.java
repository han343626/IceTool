package com.zlsk.zTool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.string.StringUtil;

/**
 * Created by IceWang on 2018/5/25.
 */

public class ActionSheetDialog {

    private static Dialog dialog = null;

    public static Dialog show(Context context, String title, String[] choices,
                              AdapterView.OnItemClickListener listener) {
        return show(context, title, choices, listener, null);
    }

    public static Dialog show(Context context, String title, String[] choices,
                              AdapterView.OnItemClickListener listener, View.OnClickListener cancelListener) {
        if (null != dialog) {
            if (dialog.isShowing())
                dialog.dismiss();
        }

        dialog = new Dialog(context, R.style.ActionSheet);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.actionsheet, null);
        TextView titleTv = layout.findViewById(R.id.title);
        if (StringUtil.isNullOrWhiteSpace(title)) {
            titleTv.setVisibility(View.GONE);
        } else {
            titleTv.setText(title);
        }
        ListView listView = layout.findViewById(R.id.sheetList);
        listView.setAdapter(new ArrayAdapter<>(context,
                R.layout.sheet_item, choices));
        listView.setOnItemClickListener(listener);
        if (cancelListener != null) {
            layout.findViewById(R.id.cancel).setOnClickListener(cancelListener);
        } else {
            layout.findViewById(R.id.cancel).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
        }

        layout.setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.BOTTOM;
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setDimAmount(0.5f);
        return dialog;
    }

    public static void dismiss() {
        if (null != dialog) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
}

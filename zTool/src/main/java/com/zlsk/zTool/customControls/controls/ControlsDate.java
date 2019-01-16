package com.zlsk.zTool.customControls.controls;

import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.dialog.DatetimePickerDialog;
import com.zlsk.zTool.utils.date.TimeUtil;
import com.zlsk.zTool.utils.string.StringUtil;

public class ControlsDate extends ABaseControlItemView {
    private DatetimePickerDialog timeDialog;

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_select_date;
    }

    @Override
    protected void setup(View contentView) {
        final TextView btnSelect = contentView.findViewById(R.id.view_value);

        if(!mControlsItem.isNullDate()){
            if (!StringUtil.isBlank(mControlsItem.getValue())) {
                btnSelect.setText(mControlsItem.getValue());
            } else if (!StringUtil.isBlank(mControlsItem.getDefaultValue())) {
                btnSelect.setText(mControlsItem.getDefaultValue());
            }else {
                String time = TimeUtil.timeMill2Date(System.currentTimeMillis());
                btnSelect.setText(time);
                mControlsItem.setValue(time);
            }
        }else {
            btnSelect.setHint("点击选择");
        }

        ImageView iv_spn_value = contentView.findViewById(R.id.iv_spn_value);
        iv_spn_value.setVisibility(mControlsItem.isEdit() ? View.VISIBLE : View.GONE);

        if (mControlsItem.isEdit()) {
            btnSelect.setOnClickListener(v -> {
                if(mControlsItem.getePickerType() == null){
                    timeDialog = new DatetimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, input -> {
                        mControlsItem.setValue(input);
                        btnSelect.setText(input);
                    });
                }else {
                    timeDialog = new DatetimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, mControlsItem.getePickerType(),input -> {
                        mControlsItem.setValue(input);
                        btnSelect.setText(input);
                    });
                }

                WindowManager.LayoutParams attributes = timeDialog.getWindow().getAttributes();
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                attributes.width = (int) (metrics.widthPixels * 0.9);
                attributes.height = (int) (metrics.heightPixels * 0.6);
                attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                attributes.dimAmount = 0.5f;
                timeDialog.getWindow().setAttributes(attributes);
                timeDialog.show();
            });
        }
    }
}
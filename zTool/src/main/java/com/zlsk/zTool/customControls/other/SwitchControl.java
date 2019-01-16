package com.zlsk.zTool.customControls.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/7/6.
 */

public class SwitchControl implements View.OnClickListener {
    private Context context;
    private OnSwitchClickCallback onSwitchClickCallback;
    private LinearLayout parentView;
    private String[] title;

    private TextView[] textViews;
    private int[] idList;
    private int[] trueDrawableList;
    private int[] falseDrawableList;

    private int currentType;

    public SwitchControl(Context context,LinearLayout parentView,String[] title,OnSwitchClickCallback onSwitchClickCallback) {
        this.context = context;
        this.parentView = parentView;
        this.onSwitchClickCallback = onSwitchClickCallback;
        this.title = title;
        initUi();
    }

    public void setCurrentType(int type){
        if(type < idList.length){
            changeTypeUi(idList[type]);
        }
    }

    protected void initUi(){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_switch_control, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        parentView.addView(layout,params);

        idList = new int[]{R.id.tv_type_000,R.id.tv_type_001,R.id.tv_type_002,R.id.tv_type_003};
        textViews = new TextView[idList.length];
        for (int index = 0;index < idList.length ; index ++){
            TextView textView = layout.findViewById(idList[index]);
            textView.setText(title[index]);
            textView.setOnClickListener(this);

            textViews[index] = textView;
        }

        trueDrawableList = new int[]{R.drawable.bg_sheet_left_button_true,
                R.drawable.bg_sheet_middle_button_true,
                R.drawable.bg_sheet_middle_button_true,
                R.drawable.bg_sheet_right_button_true};

        falseDrawableList = new int[]{R.drawable.bg_sheet_left_button_false,
                R.drawable.bg_sheet_middle_button_false,
                R.drawable.bg_sheet_middle_button_false,
                R.drawable.bg_sheet_right_button_false};

        changeTypeUi(idList[0]);
    }

    private void changeTypeUi(int id){
        for (int index = 0;index < textViews.length;index ++){
            TextView button = textViews[index];
            if(id == button.getId()){
                button.setBackground(context.getResources().getDrawable(trueDrawableList[index]));
                button.setTextColor(Color.WHITE);
                currentType = index;
            }else {
                button.setBackground(context.getResources().getDrawable(falseDrawableList[index]));
                button.setTextColor(Color.GRAY);
            }
        }
        onSwitchClickCallback.onClick(currentType);
    }

    @Override
    public void onClick(View v) {
        changeTypeUi(v.getId());
    }

    public interface OnSwitchClickCallback{
        void onClick(int type);
    }
}

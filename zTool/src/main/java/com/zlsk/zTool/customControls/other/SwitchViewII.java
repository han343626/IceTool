package com.zlsk.zTool.customControls.other;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by IceWang on 2018/12/26.
 */

public class SwitchViewII extends SwitchView{
    private enum Position{first,middle,last}

    private static final int CORNER_RADIUS_DP = 10;
    private static final String COLOR_SELECT = "#0099CC";
    private static final String COLOR_NOT_SELECT = "#FFFFFF";


    public SwitchViewII(Context context, LinearLayout linearLayout, String[] contentArray, OnSwitchViewCallback onSwitchViewCallback) {
        super(context, linearLayout, contentArray, onSwitchViewCallback);
    }

    @Override
    protected void initUi(LinearLayout linearLayout) {
        LinearLayout rootView = getRootView();
        LinearLayout layout = getParentView(20,40);
        totalTextArray = new TextView[contentArray.length];
        contentTextArray = new TextView[contentArray.length];
        for (int index = 0; index < contentArray.length; index++) {
            RelativeLayout relativeLayout = getContainerView();
            relativeLayout.setTag(index);
            contentTextArray[index] = getContentView(relativeLayout,contentArray[index]);
            totalTextArray[index] = getTotalView(relativeLayout);
            layout.addView(relativeLayout);
        }
        rootView.addView(layout);
        linearLayout.addView(rootView);
        changeUi(0);
    }

    @Override
    public void changeUi(int position) {
        for (int index = 0; index < contentTextArray.length; index++) {
            totalTextArray[index].setTextColor(Color.parseColor(position == index ? "#FF0064" : "#808080"));
            if(index == 0){
                contentTextArray[index].setBackground(getDrawable(index == position,Position.first));
            }else if(index == contentTextArray.length - 1){
                contentTextArray[index].setBackground(getDrawable(index == position,Position.last));
            }else {
                contentTextArray[index].setBackground(getDrawable(index == position,Position.middle));
            }
            contentTextArray[index].setTextColor(index == position ? Color.WHITE : Color.GRAY);
        }
        if(onSwitchViewCallback != null){
            onSwitchViewCallback.callback(position);
        }
    }

    protected GradientDrawable getDrawable(boolean isSelect,Position position){
        int radiusPx = dip2px(context,CORNER_RADIUS_DP);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
        switch (position){
            case first:
                gradientDrawable.setCornerRadii(new float[]{radiusPx,radiusPx,0,0,0,0,radiusPx,radiusPx});
                break;
            case middle:
                gradientDrawable.setCornerRadii(new float[]{0,0,0,0,0,0,0,0});
                break;
            case last:
                gradientDrawable.setCornerRadii(new float[]{0,0,radiusPx,radiusPx,radiusPx,radiusPx,0,0});
                break;
        }
        gradientDrawable.setColor(Color.parseColor(isSelect ? COLOR_SELECT : COLOR_NOT_SELECT));
        return gradientDrawable;
    }
}

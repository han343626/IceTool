package com.zlsk.zTool.customControls.other;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by IceWang on 2018/12/20.
 */

public class SwitchView implements View.OnClickListener {
    public interface OnSwitchViewCallback{
        void callback(int position);
    }

    protected OnSwitchViewCallback onSwitchViewCallback;
    protected Context context;
    protected String[] contentArray;

    protected TextView[] totalTextArray ;
    protected TextView[] contentTextArray ;
    protected View[] dividerArray ;

    public SwitchView(Context context, LinearLayout linearLayout, String[] contentArray, OnSwitchViewCallback onSwitchViewCallback) {
        this.context = context;
        this.contentArray = contentArray;
        this.onSwitchViewCallback = onSwitchViewCallback;
        initUi(linearLayout);
    }

    public void setTotal(String[] totalArray){
        for (int index = 0; index < totalArray.length; index++) {
            totalTextArray[index].setText(totalArray[index]);
        }
    }

    public void setTotal(int[] total){
        for (int index = 0; index < total.length; index++) {
            totalTextArray[index].setText(String.valueOf(total[index]));
        }
    }

    public void changeUi(int position){
        for (int index = 0; index < contentTextArray.length; index++) {
            dividerArray[index].setVisibility(position == index ? View.VISIBLE : View.GONE);
            totalTextArray[index].setTextColor(Color.parseColor(position == index ? "#FF0064" : "#808080"));
        }
        if(onSwitchViewCallback != null){
            onSwitchViewCallback.callback(position);
        }
    }

    @Override
    public void onClick(View v) {
        changeUi((int) v.getTag());
    }

    protected void initUi(LinearLayout linearLayout){
        LinearLayout rootView = getRootView();
        LinearLayout layout = getParentView(10,40);
        totalTextArray = new TextView[contentArray.length];
        contentTextArray = new TextView[contentArray.length];
        dividerArray = new View[contentArray.length];
        for (int index = 0; index < contentArray.length; index++) {
            RelativeLayout relativeLayout = getContainerView();
            relativeLayout.setTag(index);
            contentTextArray[index] = getContentView(relativeLayout,contentArray[index]);
            totalTextArray[index] = getTotalView(relativeLayout);
            dividerArray[index] = getDividerView(relativeLayout);
            layout.addView(relativeLayout);
        }
        rootView.addView(layout);
        rootView.addView(getDividerView());
        linearLayout.addView(rootView);
        changeUi(0);
    }

    protected LinearLayout getParentView(int leftRightMargins,int height){
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip2px(context,height));
        layoutParams.setMargins(dip2px(context,leftRightMargins),dip2px(context,5),dip2px(context,leftRightMargins),0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        return linearLayout;
    }

    protected LinearLayout getRootView(){
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    protected RelativeLayout getContainerView(){
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setOnClickListener(this);
        return relativeLayout;
    }

    protected TextView getTotalView(RelativeLayout relativeLayout){
        TextView tv_total = new TextView(context);
        RelativeLayout.LayoutParams tvLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tvLayoutParams.setMargins(0,0,dip2px(context,5),0);
        tv_total.setLayoutParams(tvLayoutParams);
        tv_total.setGravity(Gravity.CENTER);
        tv_total.setTextColor(Color.parseColor("#FF0064"));
        tv_total.setTextSize(sp2px(context,10));
        relativeLayout.addView(tv_total);
        return tv_total;
    }

    protected TextView getContentView(RelativeLayout relativeLayout, String content){
        TextView tv_content = new TextView(context);
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        contentLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_content.setLayoutParams(contentLayoutParams);
        tv_content.setTextSize(sp2px(context,16));
        tv_content.setGravity(Gravity.CENTER);
        tv_content.setText(content);
        relativeLayout.addView(tv_content);
        return tv_content;
    }

    protected View getDividerView(RelativeLayout relativeLayout){
        View view = new View(context);
        RelativeLayout.LayoutParams viewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dip2px(context,3));
        viewLayoutParams.setMargins(dip2px(context,10),0,dip2px(context,10),0);
        viewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(viewLayoutParams);
        view.setBackgroundColor(Color.parseColor("#3B95EB"));
        relativeLayout.addView(view);
        return view;
    }

    protected View getDividerView(){
        View view = new View(context);
        RelativeLayout.LayoutParams viewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dip2px(context,1));
        viewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(viewLayoutParams);
        view.setBackgroundColor(Color.parseColor("#3B95EB"));
        return view;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, spValue,context.getResources().getDisplayMetrics());
    }
}

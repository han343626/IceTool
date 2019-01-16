package com.zlsk.zTool.baseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/7/2.
 */

public abstract class ATitleBaseFragmentActivity extends BaseFragmentActivity{
    private TextView tvAction = null;
    private TextView tvTitle;

    public abstract int getContentViewId();
    public abstract String getTitleString();
    public abstract String getActionString();
    public abstract boolean showRightImg();

    public void onBackButtonClicked(View view){
        finish();
    }
    public void onActionButtonClicked(View view){}
    public void onTitleClicked(View view){}
    public void onMoreButtonClick(View view){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initData();
        initUi();
    }

    protected void initData(){}
    protected void initUi(){
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getTitleString());
        tvAction = findViewById(R.id.tv_action);
        setActionName();
        ImageView iv_more = findViewById(R.id.iv_more);
        iv_more.setVisibility(showRightImg() ? View.VISIBLE : View.GONE);
    }
    protected void setActionName(){
        setActionName(getActionString());
    }
    protected void setActionName(String actionName){
        if(actionName == null || actionName.trim().isEmpty()){
            tvAction.setVisibility(View.GONE);
        }else{
            tvAction.setText(actionName);
            tvAction.setVisibility(View.VISIBLE);
        }
    }

    protected void updateTitle(String msg){
        tvTitle.setText(msg);
    }
}

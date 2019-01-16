package com.zlsk.zTool.baseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/4/23.
 */

public abstract class ATitleBaseActivity extends BaseActivity {
    private TextView tvAction = null;
    private TextView tvTitle;
    public ImageButton backButton;
    public boolean isBackBtnVisible;

    public abstract int getContentViewId();
    public abstract String getTitleString();
    public abstract String getActionString();
    public abstract boolean showRightImg();

    public void onBackButtonClicked(View view){
        onPageClose();
        finish();
    }
    public void onActionButtonClicked(View view){}
    public void onTitleClicked(View view){}
    public void onMoreButtonClick(View view){}
    public boolean showBackButton(){
        return true;
    }
    public int getBackBackground(){
        return R.drawable.css_icon_left_arrow;
    }

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
        backButton = findViewById(R.id.ib_back);
        setActionName();
        ImageView iv_more = findViewById(R.id.iv_more);
        iv_more.setVisibility(showRightImg() ? View.VISIBLE : View.GONE);
        isBackBtnVisible = showBackButton();
        setBackVisible(isBackBtnVisible);
        setBackBtnBackground();
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

    protected void setBackVisible(boolean visible){
        isBackBtnVisible = visible;
        backButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void setBackBtnBackground(){
        backButton.setBackgroundResource(getBackBackground());
    }
}

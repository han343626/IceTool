package com.zlsk.zTool.baseActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.system.DeviceUtil;

/**
 * Created by IceWang on 2018/6/26.
 */

public abstract class ASlideMenuActivity extends ATitleBaseActivity {
    protected abstract int getDecorView();
    protected abstract int getSideMenuView();
    protected abstract int getSideMenuContentView();

    protected View decorView;
    protected int screenWidth;
    protected float startX,startY,endX,endY,distanceX,distanceY;

    private long exitTime = 0;
    protected boolean isMenuLayoutOpen = false;
    private View sideMenuView;
    public float menuWidth = 0.618f;

    public boolean isOnlyEdgeScroll() {
        return false;
    }

    protected boolean getIsNeedSlide(){return true;}

    protected void initSlideMenu(){
        int screenWidth = DeviceUtil.getScreenWidth(context);
        int sideMenuWidth = (int) (screenWidth * menuWidth);

        LinearLayout layout_side_menu = findViewById(getSideMenuContentView());
        ViewGroup.LayoutParams params = layout_side_menu.getLayoutParams();
        params.width = sideMenuWidth;
        layout_side_menu.setLayoutParams(params);
    }

    @Override
    public int getBackBackground(){
        return R.drawable.icon_back;
    }

    @Override
    public void onBackButtonClicked(View view) {
        if(decorView.getX() == 0){
            setMenuOpenOrClose(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        initMainView();
        initSlideMenu();
    }

    private void initMainView() {
        decorView = findViewById(getDecorView());
        sideMenuView = findViewById(getSideMenuView());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!getIsNeedSlide()) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(isCanScroll()){
                    onActionMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isCanScroll()) {
                    onActionUp(event);
                }
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    protected boolean isCanScroll(){
        return isMenuLayoutOpen ||
                (!isOnlyEdgeScroll() && startX < decorView.getMeasuredWidth() * menuWidth) ||
                (isOnlyEdgeScroll() && startX < 50);

    }

    protected void onActionMove(MotionEvent event) {
        endX = event.getX();
        endY = event.getY();
        distanceX = endX - startX;
        distanceY = Math.abs(endY-startY);

        if(isMenuLayoutOpen){
            if(distanceX < 0 && -distanceX > distanceY && screenWidth * menuWidth + distanceX > 0){
                decorView.setX(screenWidth * menuWidth + distanceX);
                setBackButtonRotate();
                sideMenuView.setX(distanceX);
            }
            return;
        }
        //滑动判断
        if(distanceX > 0 && distanceX > distanceY && distanceX < screenWidth * menuWidth){
            decorView.setX(distanceX);
            setBackButtonRotate();
            sideMenuView.setX(distanceX - screenWidth * menuWidth);
        }
    }

    protected void onActionUp(MotionEvent event) {
        endX = event.getX();
        endY = event.getY();
        distanceX = endX - startX;
        distanceY = Math.abs(endY - startY);

        //当菜单栏显示的时候,点击菜单栏内部,不做此处不做响应
        if(isMenuLayoutOpen && (distanceX > 0 && distanceX > distanceY) ||
                (startX < screenWidth * menuWidth && distanceX == 0)){
            return;
        }

        if(distanceX > 0 && distanceX > distanceY && distanceX > screenWidth / 5){
            //滑动判断 横向滑动距离大于屏幕五分之一直接end
            setMenuOpenOrClose(true);
        } else if(distanceX > 0 && distanceX > distanceY){
            //滑动判断 横向滑动距离小于于屏幕三分之一还原
            setMenuOpenOrClose(false);
        }else{
            setMenuOpenOrClose(false);
        }
    }

    protected void setMenuOpenOrClose(boolean openOrClose){
        ObjectAnimator decorViewAnimator = ObjectAnimator.ofFloat(decorView,"X",decorView.getX(),openOrClose ? screenWidth * menuWidth : 0);
        decorViewAnimator.addUpdateListener(animation -> setBackButtonRotate());
        decorViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                backButton.setRotation(openOrClose ? 90 : 0);
                decorView.setX(openOrClose ? screenWidth * menuWidth : 0);
            }
        });
        decorViewAnimator.setDuration(300).start();
        ObjectAnimator.ofFloat(sideMenuView,"X", sideMenuView.getX(),openOrClose ? 0 : - screenWidth).setDuration(300).start();
        isMenuLayoutOpen = openOrClose;
        decorView.setEnabled(!openOrClose);
    }

    protected void setBackButtonRotate(){
        float degree = decorView.getX() / (screenWidth * menuWidth);
        System.out.println("ICE_FLAG " + degree);
        backButton.setRotation(degree * 90);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(isMenuLayoutOpen){
                setMenuOpenOrClose(false);
                return true;
            }
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

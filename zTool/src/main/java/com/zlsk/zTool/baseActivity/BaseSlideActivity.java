package com.zlsk.zTool.baseActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.zlsk.zTool.dialog.LoadingDialog;

/**
 * Created by IceWang on 2018/6/8.
 */

public class BaseSlideActivity extends Activity{
    protected View decorView;
    protected int screenWidth;
    protected float startX,startY,endX,endY,distanceX,distanceY;


    public boolean isLimitScrollMinDistance(){
        return true;
    }

    /**
     * 页面未还原不可相应点击事件
     */
    public boolean isViewClickable() {
        return decorView.getX() == 0;
    }

    protected boolean getIsNeedSlide(){return true;}

    protected void onActionMove(MotionEvent event){
        endX = event.getX();
        endY = event.getY();
        distanceX = endX - startX;
        distanceY = Math.abs(endY-startY);
        //滑动判断
        if(distanceX > 0 && distanceX > distanceY){
            decorView.setX(distanceX);
        }
    }

    protected void onActionUp(MotionEvent event){
        endX = event.getX();
        distanceX = endX - startX;
        endY = event.getY();
        distanceY = Math.abs(endY - startY);

        if(distanceX == 0){
            return;
        }

        if(distanceX > 0 && distanceX > distanceY && distanceX > screenWidth / 5){
            setCurrentActivityView();
        } else if(distanceX > 0 && distanceX > distanceY){
            ObjectAnimator.ofFloat(decorView,"X",decorView.getX(), 0).setDuration(300).start();

        } else{
            ObjectAnimator.ofFloat(decorView,"X",decorView.getX(), 0).setDuration(300).start();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
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
                if(!isLimitScrollMinDistance() || (isLimitScrollMinDistance() && startX < decorView.getMeasuredWidth() / 5)){
                    onActionMove(event);
                }

                break;
            case MotionEvent.ACTION_UP:
                if(!isLimitScrollMinDistance() || (isLimitScrollMinDistance() && startX < decorView.getMeasuredWidth() / 5)) {
                    onActionUp(event);
                }
                break;
        }

        return isViewClickable() && super.dispatchTouchEvent(event);
    }

    protected void setCurrentActivityView(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(decorView.getX(),screenWidth);
        valueAnimator.setDuration(300);
        valueAnimator.start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                decorView.setX((Float) animation.getAnimatedValue());
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LoadingDialog.getInstance().dismiss();
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

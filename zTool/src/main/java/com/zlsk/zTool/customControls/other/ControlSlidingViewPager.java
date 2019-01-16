package com.zlsk.zTool.customControls.other;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by IceWang on 2018/11/21.
 */

public class ControlSlidingViewPager extends ViewPager{
    private boolean isCanSlide = true;

    public ControlSlidingViewPager(Context context) {
        super(context);
    }

    public ControlSlidingViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public void setCanSlide(boolean canSlide) {
        isCanSlide = canSlide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return  isCanSlide;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

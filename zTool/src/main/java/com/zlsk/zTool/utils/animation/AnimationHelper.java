package com.zlsk.zTool.utils.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.zlsk.zTool.customControls.other.SwitchView;


/**
 * Created by IceWang on 2018/12/24.
 */

public class AnimationHelper {
    public static void startScaleAnimation(View view){
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public static void animation(Context context, final RelativeLayout parentView, View startView , View endView,Drawable drawable){
        startScaleAnimation(startView);

        final ZImageView moveView = new ZImageView(context);
//        moveView.setImageResource(moveImg);
        moveView.setImageDrawable(drawable);
        parentView.addView(moveView);
        moveView.getLayoutParams().width = SwitchView.dip2px(context,32);
        moveView.getLayoutParams().height = SwitchView.dip2px(context,32);

        PointF startPoint = getViewLocationPoint(context,startView);
        PointF endPoint = getViewLocationPoint(context,endView);
        PointF controlPoint = new PointF(startPoint.x / 2,startPoint.y);

        Path path = new Path();
        path.moveTo(startPoint.x,startPoint.y);
        path.quadTo(controlPoint.x,controlPoint.y,endPoint.x,endPoint.y);
        ObjectAnimator heartAnimator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //path自带二,三阶贝塞尔曲线
            heartAnimator = ObjectAnimator.ofFloat(moveView,moveView.X,moveView.Y,path);
        }else {
            //自定义属性 mPointF 自己实现贝塞尔曲线
            heartAnimator = ObjectAnimator.ofObject(moveView,"mPointF",new MyTypeEvaluator(controlPoint),startPoint,endPoint);
        }

        heartAnimator.setInterpolator(new AccelerateInterpolator());
        heartAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                moveView.setVisibility(View.GONE);
                parentView.removeView(moveView);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                moveView.setVisibility(View.VISIBLE);
            }
        });

        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(endView, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(endView, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(heartAnimator);
        animatorSet.setDuration(400);
        animatorSet.start();
    }

    /**
     * 若果有状态栏，减去状态栏高度，actionbar同理
     */
    private static PointF getViewLocationPoint(Context context,View view){
        int[] location_heat = new int[2];
        view.getLocationInWindow(location_heat);
        return new PointF(location_heat[0],location_heat[1] - getStatusBarHeight(context));
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

package com.zlsk.zTool.baseActivity.slideback;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.slideback.callback.OnInternalStateListener;
import com.zlsk.zTool.baseActivity.slideback.callback.OnSlideListener;
import com.zlsk.zTool.baseActivity.slideback.widget.SlideBackLayout;

/**
 * Created by IceWang on 2018/9/26.
 */

public class SlideBackHelper {

    public static ViewGroup getDecorView(Activity activity) {
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    public static Drawable getDecorViewDrawable(Activity activity) {
        return getDecorView(activity).getBackground();
    }

    public static View getContentView(Activity activity) {
        return getDecorView(activity).getChildAt(0);
    }

    /**
     * 附着Activity，实现侧滑
     *
     * @param curActivity 当前Activity
     * @param helper      Activity栈管理类
     * @param config      参数配置
     * @param listener    滑动的监听
     * @return 处理侧滑的布局，提高方法动态设置滑动相关参数
     */
    public static SlideBackLayout attach(@NonNull final Activity curActivity, @NonNull final ActivityHelper helper, @Nullable final SlideConfig config, @Nullable final OnSlideListener listener) {

        if (helper.getPreActivity()==null){
            // 内存不足应用被杀的话，直接返回一个空实现的SlideBackLayout，这时候滑动功能就失效了
            return new SlideBackLayout(curActivity);
        }

        final ViewGroup decorView = getDecorView(curActivity);
        final View contentView = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        View content = contentView.findViewById(android.R.id.content);
        if (content.getBackground() == null) {
            content.setBackground(decorView.getBackground());
        }

        final Activity[] preActivity = {helper.getPreActivity()};
        final View[] preContentView = {getContentView(preActivity[0])};
        Drawable preDecorViewDrawable = getDecorViewDrawable(preActivity[0]);
        content = preContentView[0].findViewById(android.R.id.content);
        if (content.getBackground() == null) {
            content.setBackground(preDecorViewDrawable);
        }

        final SlideBackLayout slideBackLayout;
        slideBackLayout = new SlideBackLayout(curActivity, contentView, preContentView[0], preDecorViewDrawable, config, new OnInternalStateListener() {

            @Override
            public void onSlide(float percent) {
                if (listener != null) {
                    listener.onSlide(percent);
                }
            }

            @Override
            public void onOpen() {
                if (listener != null) {
                    listener.onOpen();
                }
            }

            @Override
            public void onClose(Boolean finishActivity) {
                if (listener != null) {
                    listener.onClose();
                }

                if ((finishActivity == null || !finishActivity) && listener != null) {
                    listener.onClose();
                }

                if (config != null && config.isRotateScreen()) {

                    if (finishActivity != null && finishActivity) {
                        // remove了preContentView后布局会重新调整，这时候contentView回到原处，所以要设不可见
                        contentView.setVisibility(View.INVISIBLE);
                    }

                    if (preActivity[0] != null && preContentView[0].getParent() != getDecorView(preActivity[0])) {
                        // Log.e("TAG", ((SlideBackLayout) preContentView[0].getParent()).getTestName() + "这里把欠人的布局放回到上个Activity");
                        preContentView[0].setX(0);
                        ((ViewGroup) preContentView[0].getParent()).removeView(preContentView[0]);
                        getDecorView(preActivity[0]).addView(preContentView[0], 0);
                    }
                }

                if (finishActivity != null && finishActivity) {
                    curActivity.finish();
                    curActivity.overridePendingTransition(0, R.anim.anim_out_none);
                    helper.postRemoveActivity(curActivity);
                } else if (finishActivity == null) {
                    helper.postRemoveActivity(curActivity);
                }
            }

            @Override
            public void onCheckPreActivity(SlideBackLayout slideBackLayout) {
                Activity activity = helper.getPreActivity();
                if (activity != preActivity[0]) {
                    preActivity[0] = activity;
                    preContentView[0] = getContentView(preActivity[0]);
                    slideBackLayout.updatePreContentView(preContentView[0]);
                }
            }

        });

        decorView.addView(slideBackLayout);

        return slideBackLayout;
    }
}

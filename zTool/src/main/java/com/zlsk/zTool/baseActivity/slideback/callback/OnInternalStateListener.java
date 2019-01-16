package com.zlsk.zTool.baseActivity.slideback.callback;

import android.support.annotation.FloatRange;

import com.zlsk.zTool.baseActivity.slideback.widget.SlideBackLayout;

/**
 * Created by IceWang on 2018/9/26.
 */

public interface OnInternalStateListener {
    void onSlide(@FloatRange(from = 0.0,
            to = 1.0) float percent);

    void onOpen();

    void onClose(Boolean finishActivity);

    void onCheckPreActivity(SlideBackLayout slideBackLayout);

}


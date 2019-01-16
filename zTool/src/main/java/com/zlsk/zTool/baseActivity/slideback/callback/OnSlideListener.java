package com.zlsk.zTool.baseActivity.slideback.callback;

import android.support.annotation.FloatRange;

/**
 * Created by IceWang on 2018/9/26.
 */

public interface OnSlideListener {
    void onSlide(@FloatRange(from = 0.0,
            to = 1.0) float percent);

    void onOpen();

    void onClose();
}

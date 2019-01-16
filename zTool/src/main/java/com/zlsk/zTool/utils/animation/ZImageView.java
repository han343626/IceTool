package com.zlsk.zTool.utils.animation;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

/**
 * Created by IceWang on 2018/12/24.
 */

public class ZImageView extends android.support.v7.widget.AppCompatImageView{
    private PointF mPointF;

    public ZImageView(Context context) {
        super(context);
    }

    public ZImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }

    public PointF getmPointF() {
        return mPointF;
    }

    public void setmPointF(PointF mPointF) {
        setX(mPointF.x);
        setY(mPointF.y);
    }
}


package com.zlsk.zTool.utils.animation;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by IceWang on 2018/12/24.
 */

public class MyTypeEvaluator implements TypeEvaluator<PointF> {
    private PointF controlValue;

    public MyTypeEvaluator(PointF controlValue) {
        this.controlValue = controlValue;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return getBezierPoint(startValue,endValue,controlValue,fraction);
    }

    private PointF getBezierPoint(PointF start, PointF end, PointF control, float t){
        PointF pointF = new PointF();
        pointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x;
        pointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y;
        return pointF;
    }
}

package com.zlsk.zTool.customControls.progressing.animation.interpolator;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

class PathInterpolatorCompatApi21 {

    private PathInterpolatorCompatApi21() {
        // prevent instantiation
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(Path path) {
        return new PathInterpolator(path);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(float controlX, float controlY) {
        return new PathInterpolator(controlX, controlY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Interpolator create(float controlX1, float controlY1,
                                      float controlX2, float controlY2) {
        return new PathInterpolator(controlX1, controlY1, controlX2, controlY2);
    }
}

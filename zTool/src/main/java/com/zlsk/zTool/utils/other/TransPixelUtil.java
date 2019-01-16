package com.zlsk.zTool.utils.other;

import android.content.Context;

/**
 * Created by IceWang on 2018/5/23.
 */

public class TransPixelUtil {
    final static int CHINESE= 0;
    final static int NUMBER_OR_CHARACTER = 1;
    /**
     * dp转成px
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context,float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转成dp
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转成px
     * @param spValue
     * @param type CHINESE or NUMBER_OR_CHARACTER or others
     * @return
     */
    public static float sp2px(Context context,float spValue, int type) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        switch (type) {
            case CHINESE:
                return spValue * scaledDensity;
            case NUMBER_OR_CHARACTER:
                return spValue * scaledDensity * 10.0f / 18.0f;
            default:
                return spValue * scaledDensity;
        }
    }
}

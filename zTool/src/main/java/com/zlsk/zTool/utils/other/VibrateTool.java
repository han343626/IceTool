package com.zlsk.zTool.utils.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by IceWang on 2018/11/22.
 */

public class VibrateTool {
    private static Vibrator vibrator;

    /**
     * 简单震动
     * @param context     调用震动的Context
     * @param millisecond 震动的时间，毫秒
     */
    @SuppressLint("MissingPermission")
    @SuppressWarnings("static-access")
    public static void vibrateOnce(Context context, int millisecond) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
    }

    /**
     * 复杂的震动
     * @param context 调用震动的Context
     * @param pattern 震动形式
     *                数组参数意义：
     *                      第一个参数为等待指定时间后开始震动，
     *                      震动时间为第二个参数。
     *                      后边的参数依次为等待震动和震动的时间
     * @param repeate 震动的次数，-1不重复，非-1为从pattern的指定下标开始重复 0为一直震动
     *
     *
     */
    @SuppressLint("MissingPermission")
    @SuppressWarnings("static-access")
    public static void vibrateComplicated(Context context, long[] pattern, int repeate) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeate);
    }

    /**
     * 停止震动
     */
    @SuppressLint("MissingPermission")
    public static void vibrateStop() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}

package com.zlsk.zTool.baseActivity.slideback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by IceWang on 2018/9/26.
 */

public class CacheDrawView extends View {

    private View mCacheView;

    public CacheDrawView(Context context) {
        super(context);
    }

    public void drawCacheView(View cacheView) {
        mCacheView = cacheView;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCacheView != null) {
            //            canvas.drawColor(Color.YELLOW);
            mCacheView.draw(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCacheView = null;
    }
}

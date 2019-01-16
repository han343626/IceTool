package com.zlsk.zTool.customControls.progressing;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.progressing.sprite.Sprite;
import com.zlsk.zTool.customControls.progressing.style.CubeGrid;

/**
 * Created by IceWang on 2018/9/11.
 */

public class SpinKitView extends ProgressBar{
    private Sprite mSprite;

    public SpinKitView(Context context) {
        this(context, null);
    }

    public SpinKitView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SpinKitViewStyle);
    }

    public SpinKitView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.SpinKitView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpinKitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Sprite sprite = new CubeGrid();
        setIndeterminateDrawable(sprite);
        setIndeterminate(true);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (!(d instanceof Sprite)) {
            throw new IllegalArgumentException("this d must be instanceof Sprite");
        }
        setIndeterminateDrawable((Sprite) d);
    }

    @Override
    public Sprite getIndeterminateDrawable() {
        return mSprite;
    }

    public void setIndeterminateDrawable(Sprite d) {
        super.setIndeterminateDrawable(d);
        mSprite = d;
        if (mSprite.getColor() == 0) {
            mSprite.setColor(Color.parseColor("#515151"));
        }
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        if (getVisibility() == VISIBLE) {
            mSprite.start();
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who) {
        super.unscheduleDrawable(who);
        if (who instanceof Sprite) {
            ((Sprite) who).stop();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (mSprite != null && getVisibility() == VISIBLE) {
                mSprite.start();
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == View.SCREEN_STATE_OFF) {
            if (mSprite != null) {
                mSprite.stop();
            }
        }
    }
}

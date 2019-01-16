package com.zlsk.zTool.customControls.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IceWang on 2018/12/3.
 */

@SuppressLint("AppCompatCustomView")
public class StokeTextView extends TextView {
    private TextView outlineTextView = null;

    private Context mContext;

    public StokeTextView(Context context) {
        super(context);
        mContext = context;
        outlineTextView = new TextView(context);
        init();
    }

    public StokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        outlineTextView = new TextView(context, attrs);
        init();
    }

    public StokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        outlineTextView = new TextView(context, attrs, defStyle);
        init();
    }

    public void init() {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth(4);// 描边宽度
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(Color.parseColor("#ffffff"));// 描边颜色
        outlineTextView.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
}

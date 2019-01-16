package com.zlsk.zTool.customControls.scaleImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/12/12.
 */

public class ZAutoImageView extends FrameLayout {

    private ImageView mImageView;

    int resId;

    public ZAutoImageView(Context context) {
        super(context);
    }

    public ZAutoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //导入布局
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_auto_imageview, this);

        mImageView = findViewById(R.id.img_backgroud);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RxAutoImageView);

        try {
            //获得属性值
            resId = a.getResourceId(R.styleable.RxAutoImageView_ImageSrc, 0);
        } finally {
            //回收这个对象
            a.recycle();
        }

        if (resId != 0) {
            mImageView.setImageResource(resId);
        }

        new Handler().postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mImageView.startAnimation(animation);
        }, 200);
    }
}

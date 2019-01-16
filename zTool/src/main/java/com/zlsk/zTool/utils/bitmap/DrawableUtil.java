package com.zlsk.zTool.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/10/12.
 */

public class DrawableUtil {
    /**
     * @return 由于字体库的原因,将文字和图标做成VIEW,在做成图
     */
    public static Drawable createTextDrawable(Context context, String contents, int textColor,float textSize) {
        View linearLayout = LayoutInflater.from(context).inflate(R.layout.drawable_loction,null);
        TextView textView = linearLayout.findViewById(R.id.location_tv);
        textView.setText(contents);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);

        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = linearLayout.getMeasuredWidth();
        int height = linearLayout.getMeasuredHeight();
        linearLayout.layout(0, 0, width, height);

        linearLayout.buildDrawingCache();
        Bitmap bitmapCode = linearLayout.getDrawingCache();
        return new BitmapDrawable(context.getResources(), bitmapCode);
    }
}

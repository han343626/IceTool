package com.zlsk.zTool.customControls.other;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/9/20.
 */

public class ZTextView{
    private Context context;
    private LinearLayout linearLayout;
    private View view;
    public ZTextView(Context context) {
        this.context = context;

        view = LayoutInflater.from(context).inflate(R.layout.layout_custom_anim_text, null);
        linearLayout = view.findViewById(R.id.layout_container);
    }

    public View setText(String text, final Animation animation, int duration) {
        int time = 0;
        if(text != null && !text.isEmpty()) {
            char[] characters = text.toCharArray();
            for(char c : characters) {
                final TextView textView = new TextView(context);
                //遍历传入的字符串的每个字符，生成一个TextView，并设置它的动画
                textView.setText(String.valueOf(c));
                textView.setTextSize(36);
                Handler h = new Handler();
                //每隔duration时间，播放下一个TextView的动画
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.addView(textView);
                        textView.setAnimation(animation);
                    }
                }, time);

                time += duration;

            }
        }

        return view;
    }
}

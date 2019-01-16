package com.zlsk.zTool.baiduMap.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;


/**
 * Created by IceWang on 2018/11/30.
 */

public class MarkView {
    private View mark_view;
    private TextView tv_mark;
    private ImageView tv_img;

    public MarkView(Context context) {
        mark_view = LayoutInflater.from(context).inflate(R.layout.view_mark_point,null);
        tv_mark = mark_view.findViewById(R.id.tv_mark);
        tv_img = mark_view.findViewById(R.id.img_mark);
    }

    public MarkView setText(String text){
        tv_mark.setText(text);
        return this;
    }

    public MarkView setTextColor(int color){
        tv_mark.setTextColor(color);
        return this;
    }

    public MarkView setImg(int id){
        tv_img.setImageResource(id);
        return this;
    }

    public View getView(){
        return mark_view;
    }

}

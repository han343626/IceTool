package com.zlsk.zTool.adapter;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.zlsk.zTool.R;

import java.util.List;

/**
 * Created by IceWang on 2018/7/3.
 */

public class TextAdapter extends CommonAdapter<String>{
    protected int gravity = Gravity.CENTER_VERTICAL;

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public TextAdapter(Context context) {
        super(context);
    }

    public TextAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    protected void initializeView(ViewHolder holder,int position) {
        TextView content = holder.findViewById(R.id.tv_content);
        content.setText(getDataList().get(position));
        content.setGravity(gravity);
    }

    @Override
    protected int getConvertViewId() {
        return R.layout.item_list_score_content;
    }
}
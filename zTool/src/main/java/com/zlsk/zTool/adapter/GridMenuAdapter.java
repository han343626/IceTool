package com.zlsk.zTool.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.menuCmd.base.AMenuCommand;
import com.zlsk.zTool.utils.system.ResourceUtil;

import java.util.Map;

/**
 * Created by IceWang on 2018/8/31.
 */

public class GridMenuAdapter extends CommonAdapter<Map<String, Object>>{
    public GridMenuAdapter(Context context) {
        super(context);
    }

    @Override
    protected void initializeView(ViewHolder holder, int position) {
        final Map<String, Object> model = dataList.get(position);

        ImageView imageView = holder.findViewById(R.id.iv_image);
        TextView textView = holder.findViewById(R.id.tv_label);

        imageView.setImageResource(ResourceUtil.getResourceId(context,(String) model.get("icon"),"drawable"));
        textView.setText(model.get("name").toString());

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMenuCommand mc = (AMenuCommand)model.get("command");
                if(mc != null){
                    if(mc.canExecute(context, model)){
                        mc.execute(context, model);
                    }else{
                        ZToast.getInstance(context).show(context.getString(R.string.stc_tip_no_menu_auth));
                    }
                }
            }
        });
    }

    @Override
    protected int getConvertViewId() {
        return R.layout.item_image_with_text;
    }
}

package com.zlsk.zTool.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.other.TransPixelUtil;
import com.zlsk.zTool.utils.system.DeviceUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/5/9.
 */

public class PhotoGridViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> dataList = new ArrayList<>();
    private boolean isNeedAddPic = true;
    private ImageOptions options;
    private int picSize;
    private int limitCount;

    public PhotoGridViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        options = new ImageOptions.Builder().setLoadingDrawableId(R.drawable.icon_loading).setFailureDrawableId(R.drawable.icon_error_loading).build();
        //计算照片每个ITEM多大 横向间距10dp
        int screenWidth = DeviceUtil.getScreenWidth(context);
        int margin = TransPixelUtil.dip2px(context,40) * 2;
        int horizontalSpace = TransPixelUtil.dip2px(context,10) * 2;
        picSize = (screenWidth - margin - horizontalSpace) / 3;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    /**
     * 设置是否允许添加照片
     */
    public void setNeedAddPic(boolean needAddPic) {
        isNeedAddPic = needAddPic;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(dataList.size() == limitCount){
            return dataList.size();
        }
        return (dataList == null ? 1: dataList.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.custom_grid_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.width = picSize;
        params.height = picSize;
        holder.imageView.setLayoutParams(params);

        if (position == dataList.size()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.addphoto));
            holder.imageView.setVisibility((position != limitCount && isNeedAddPic) ? View.VISIBLE : View.GONE);
        } else {
            x.image().bind(holder.imageView,dataList.get(position),options);
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            imageView = view.findViewById(R.id.item_grid_image);
            imageView.setDrawingCacheEnabled(true);
        }
    }
}
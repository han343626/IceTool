package com.zlsk.zTool.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.VideoModel;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/7/4.
 */

public class VideoGridViewAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<VideoModel> dataList = new ArrayList<>();

    private boolean isNeedAddVideo = true;
    private ImageOptions options;
    private int limitCount;

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public VideoGridViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        options = new ImageOptions.Builder().setLoadingDrawableId(R.drawable.icon_loading).setFailureDrawableId(R.drawable.icon_error_loading).build();
    }

    public void setDataList(List<VideoModel> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
        startGetThumbnailPathThread();
    }

    public void setNeedAddVideo(boolean needAddVideo) {
        isNeedAddVideo = needAddVideo;
    }

    private void startGetThumbnailPathThread(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 getThumbnailPath();
             }
         }).start();
    }

    private void getThumbnailPath() {
        for (VideoModel model : dataList) {
            String thumbnailPath = BitmapUtil.getVideoThumbnailPath(model.getVideoPath());
            model.setThumbnailPath(thumbnailPath);
        }

        handler.sendMessage(new Message());
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            notifyDataSetChanged();
            return true;
        }
    });

    @Override
    public int getCount() {
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

        if (position == dataList.size()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.addvideo));
            holder.imageView.setVisibility((position != limitCount && isNeedAddVideo) ? View.VISIBLE : View.GONE);
        } else {
            x.image().bind(holder.imageView,dataList.get(position).getThumbnailPath(),options);
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            imageView = view.findViewById(R.id.item_grid_image);
        }
    }
}

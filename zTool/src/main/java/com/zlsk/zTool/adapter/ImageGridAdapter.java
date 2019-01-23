package com.zlsk.zTool.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.BitmapCacheActivity;
import com.zlsk.zTool.baseActivity.photo.SinglePhotoFullScreenActivity;
import com.zlsk.zTool.customControls.other.AvoidDoubleClickListener;
import com.zlsk.zTool.model.photo.ImageItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageGridAdapter extends BaseAdapter {
    private int limitCount = 9;

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    private TextCallback textcallback = null;
    private final String TAG = getClass().getSimpleName();
    private Activity act;
    private List<ImageItem> dataList;
    public Map<String, String> map = new HashMap<String, String>();
    private BitmapCacheActivity cache;
    private Handler mHandler;
    private int selectTotal = 0;
    private BitmapCacheActivity.ImageCallback callback = (imageView, bitmap, params) -> {
        if (imageView != null && bitmap != null) {
            String url = (String) params[0];
            if (url != null && url.equals(imageView.getTag())) {
                imageView.setImageBitmap(bitmap);
            } else {
                Log.e(TAG, "callback, bmp not match");
            }
        } else {
            Log.e(TAG, "callback, bmp null");
        }
    };

    public interface TextCallback {
        void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
        this.act = act;
        dataList = list;
        cache = new BitmapCacheActivity();
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectTotal(int selectTotal) {
        this.selectTotal = selectTotal;
    }

    private class Holder {
        private ImageView iv;
        private ImageView selected;
        private LinearLayout layout_select;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = convertView.findViewById(R.id.image);
            holder.selected = convertView.findViewById(R.id.img_select);
            holder.layout_select = convertView.findViewById(R.id.layout_select);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ImageItem item = dataList.get(position);
        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);

        if (item.isSelected) {
            holder.selected.setImageResource(R.drawable.icon_data_select);
        } else {
            holder.selected.setImageBitmap(null);
        }

        holder.layout_select.setOnClickListener(v -> {
            String path = dataList.get(position).imagePath;

            if ((selectTotal) < limitCount) {
                item.isSelected = !item.isSelected;
                if (item.isSelected) {
                    holder.selected
                            .setImageResource(R.drawable.icon_data_select);
                    selectTotal++;
                    if (textcallback != null)
                        textcallback.onListen(selectTotal);
                    map.put(path, path);

                } else {
                    holder.selected.setImageResource(-1);
                    selectTotal--;
                    if (textcallback != null)
                        textcallback.onListen(selectTotal);
                    map.remove(path);
                }
            } else if ((selectTotal) >= limitCount) {
                if (item.isSelected) {
                    item.isSelected = !item.isSelected;
                    holder.selected.setImageResource(-1);
                    selectTotal--;
                    if (textcallback != null) textcallback.onListen(selectTotal);
                    map.remove(path);

                } else {
                    Message message = Message.obtain(mHandler, 0);
                    message.sendToTarget();
                }
            }
        });
        convertView.setOnClickListener(new AvoidDoubleClickListener() {
            @Override
            public void OnClick(View view) {
                Intent intent = new Intent(act, SinglePhotoFullScreenActivity.class);
                intent.putExtra(SinglePhotoFullScreenActivity.INTENT_FIELD_IMAGE_PATH,item.imagePath);
                act.startActivity(intent);
            }
        });

        return convertView;
    }
}

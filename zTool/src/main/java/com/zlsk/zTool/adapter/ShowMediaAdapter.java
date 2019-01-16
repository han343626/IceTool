package com.zlsk.zTool.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zlsk.zTool.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by saki on 2018/3/20.
 */

public class ShowMediaAdapter extends BaseAdapter {
    private final static int ITEM_TAG_KEY = R.string.stkey_show_media_adapter_tag_key;
    private final static String TAG_ADD_IMAGE = "TAG_ADD_IMAGE";

    private boolean edit = false;
    private List<Map<String, Object>> mList = null;

    private Activity mContext = null;
    private LayoutInflater inflater = null;
    private BitmapDrawable bd = null;

    public ShowMediaAdapter(Activity context){
        super();
        mContext = context;
        inflater = LayoutInflater.from(context);
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused);
        bd = new BitmapDrawable(mContext.getResources(), bm);
    }

    @Override
    public int getCount() {
        return getData().size() + (edit ? 1 : 0);
    }

    @Override
    public Object getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        List<Map<String, Object>> d = getData();
        if(isAddButton(position)){
            if(convertView == null || !TAG_ADD_IMAGE.equals(convertView.getTag(ITEM_TAG_KEY))){
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
                convertView.setTag(ITEM_TAG_KEY, TAG_ADD_IMAGE);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            vh.ivImage.setImageDrawable(bd);
        }else{
            Map<String, Object> di = d.get(position);
            if(convertView == null || !di.equals(convertView.getTag(ITEM_TAG_KEY))){
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
                convertView.setTag(ITEM_TAG_KEY, d.get(position));
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            displayImage(vh.ivImage, di.get("url").toString());
        }
        return convertView;
    }

    public List<Map<String, Object>> getData() {
        if(mList == null){
            mList = new ArrayList<>();
        }
        return mList;
    }
    public void setData(List<Map<String, Object>> data) {
        this.mList = data;
    }

    public boolean isEdit() {
        return edit;
    }
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    public boolean isAddButton(int position){
        return edit && position == getData().size();
    }

    private void displayImage(ImageView imageView, String url){
        ImageOptions options=new ImageOptions.Builder().setLoadingDrawableId(R.drawable.icon_loading).setFailureDrawableId(R.drawable.icon_error_loading).build();
        x.image().bind(imageView, url,options);
    }

    private static class ViewHolder{
        ImageView ivImage = null;

        public ViewHolder(View view){
            ivImage = view.findViewById(R.id.i_iv_image);
        }
    }
}

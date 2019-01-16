package com.zlsk.zTool.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/6/11.
 */

public abstract class CommonAdapter<T> extends BaseAdapter{
    protected abstract void initializeView(ViewHolder holder,int position);
    protected abstract int getConvertViewId();

    protected Context context;
    protected List<T> dataList;

    public CommonAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public CommonAdapter(Context context) {
        this.context = context;
    }

    public List<T> getDataList() {
        if(dataList == null){
            dataList = new ArrayList<>();
        }
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
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
        ViewHolder holder = ViewHolder.getInstance(context, convertView, getConvertViewId());
        initializeView(holder,position);
        return holder.getConvertView();
    }

    protected static class ViewHolder {
        public View convertView;
        private SparseArray<View> views;

        private ViewHolder(View convertView) {
            this.views = new SparseArray<>();
            this.convertView = convertView;
            convertView.setTag(this);
        }

        public static ViewHolder getInstance(Context context, View convertView, int layout) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(layout, null);
                return new ViewHolder(convertView);
            }
            return (ViewHolder) convertView.getTag();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T findViewById(int id) {
            View view = views.get(id);
            if (view == null) {
                view = convertView.findViewById(id);
                views.append(id, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return convertView;
        }
    }
}

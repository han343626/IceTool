package com.zlsk.zTool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.model.photo.FileModel;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends BaseAdapter {

    private final static int ICON_FOLDER = R.drawable.ic_folder_blue;
    private final static int ICON_FILE = R.drawable.ic_file_blue;

    private final LayoutInflater mInflater;

    private List<FileModel> mData = new ArrayList<FileModel>();

    public FileListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void add(FileModel file) {
        mData.add(file);
        notifyDataSetChanged();
    }

    public void remove(FileModel file) {
        mData.remove(file);
        notifyDataSetChanged();
    }

    public void insert(FileModel file, int index) {
        mData.add(index, file);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public FileModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<FileModel> getListItems() {
        return mData;
    }

    /**
     * Set the list items without notifying on the clear. This prevents loss of
     * scroll position.
     *
     * @param data
     */
    public void setListItems(List<FileModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.file_check, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Get the file at the current position
        final FileModel file = getItem(position);
        viewHolder.tvFileInfo.setText(file.getmFile().getName());

        // If the item is not a directory, use the file icon
        int icon = file.getmFile().isDirectory() ? ICON_FOLDER : ICON_FILE;
        int visibleStatus = file.getmFile().isDirectory() ? View.GONE : View.VISIBLE;
        viewHolder.cbCheckInfo.setVisibility(visibleStatus);
        viewHolder.tvFileInfo.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        viewHolder.cbCheckInfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	file.setChecked(isChecked);
            }
        });

        if (file.isChecked()) {
            viewHolder.cbCheckInfo.setChecked(true);
        } else {
            viewHolder.cbCheckInfo.setChecked(false);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView tvFileInfo;
        private CheckBox cbCheckInfo;

        public ViewHolder(View convertView) {
            tvFileInfo = (TextView) convertView.findViewById(R.id.tv_fileinfo);
            cbCheckInfo = (CheckBox) convertView.findViewById(R.id.cb_check);
        }
    }
}
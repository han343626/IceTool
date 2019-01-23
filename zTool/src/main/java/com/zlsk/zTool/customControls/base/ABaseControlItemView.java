package com.zlsk.zTool.customControls.base;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zlsk.zTool.R;

public abstract class ABaseControlItemView {
    protected Activity context;
    protected ControlsItem mControlsItem;
    protected CustomViewInflater mCustomInflater;

    public View inflate(Activity context, CustomViewInflater customInflater, ControlsItem item) {
        this.context = context;
        this.mControlsItem = item;
        this.mCustomInflater = customInflater;

        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(getContentView(), null);
        setupTitle(item, contentView);

        setup(contentView);

        contentView.setVisibility(item.isVisible() ? View.VISIBLE : View.GONE);

        return contentView;
    }

    protected abstract void setup(View contentView);

    protected abstract int getContentView();

    protected void setupTitle(ControlsItem item, View view) {
        TextView tvTitle = view.findViewById(R.id.tv_item_title);
        if (tvTitle != null) {
            tvTitle.setText(item.getAlias());
        }
        ImageView img_required = view.findViewById(R.id.img_required);
        if(img_required != null){
            img_required.setImageResource(item.isRequired() ? R.drawable.icon_red_point : R.drawable.icon_gray_point);
        }
    }

    public ControlsItem getInspectItem() {
        return mControlsItem;
    }

    public final void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
        mCustomInflater.setPendingInspectItemView(null);
        CustomViewInflater.pendingViewInflater = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override in concrete class.
    }

    protected void startActivityForResult(Intent intent, int requestCode) {
        mCustomInflater.setPendingInspectItemView(this);
        CustomViewInflater.pendingViewInflater = mCustomInflater;
        context.startActivityForResult(intent, requestCode);
    }

}

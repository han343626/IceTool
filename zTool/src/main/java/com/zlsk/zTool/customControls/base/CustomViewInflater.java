package com.zlsk.zTool.customControls.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class CustomViewInflater {
    private Activity mCurrentActivity;

    /**
     * 调用了startActivityForResult的项，等待接收onActivityResult的返回结果
     */
    private ABaseControlItemView pendingControlItem;

    /**
     * 调用startActivityForResult的CustomViewInflater。在多个tab的情况下，用于区分不同tab下的实例。
     */
    public static CustomViewInflater pendingViewInflater;

    public CustomViewInflater(Activity activity) {
        mCurrentActivity = activity;
    }

    public View inflate(ControlsItem item) {
        ABaseControlItemView itemView = ControlsItemViewFactory.getInstance().getInspectItemView(item.getType());
        if (itemView == null) {
            return null;
        }
        return itemView.inflate(mCurrentActivity, this, item);
    }

    public void setPendingInspectItemView(ABaseControlItemView itemView) {
        this.pendingControlItem = itemView;
    }

    /**
     * 在使用的activity的onAcitivyResult中调用此方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (pendingControlItem != null) {
            pendingControlItem.handleOnActivityResult(requestCode, resultCode, data);
        }
    }
}

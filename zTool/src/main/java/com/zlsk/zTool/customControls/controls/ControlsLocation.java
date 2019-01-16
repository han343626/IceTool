package com.zlsk.zTool.customControls.controls;

import android.Manifest;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.other.LocationUtil;
import com.zlsk.zTool.utils.system.PermissionUtil;

public class ControlsLocation extends ABaseControlItemView {
    @Override
    protected int getContentView() {
        return mControlsItem.isEdit() ? R.layout.custom_form_item_geometry_select : R.layout.custom_form_item_text;
    }

    @Override
    public void setup(final View contentView) {
        PermissionUtil.build(context).
                addPermission(Manifest.permission.INTERNET).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if (mControlsItem.isEdit()) {
            final EditText etAddressValue = contentView.findViewById(R.id.et_address_value);
            etAddressValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s)){
                        return;
                    }
                    mControlsItem.setValue(s.toString());
                }
            });
            etAddressValue.setEnabled(true);
            setDefaultValue(etAddressValue);

            ImageView tvGetAddress = contentView.findViewById(R.id.tv_getCurrentLocationInfo);
            tvGetAddress.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingDialog.getInstance().show(context.getResources().getString(R.string.event_report_hint_address_loading));
                    LocationUtil.getInstance(context).getAddressDetail(new LocationUtil.onAddressDetailCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            LoadingDialog.getInstance().dismiss();
                            etAddressValue.setText(result);
                            mControlsItem.setValue(result);
                        }

                        @Override
                        public void onFailure(String result) {
                            LoadingDialog.getInstance().dismiss();
                            ZToast.getInstance().show("地址获取失败,请检查网络或者GPS");
                        }
                    });
                }
            });
        } else {
            EditText etValue = contentView.findViewById(R.id.et_item_value);
            etValue.setEnabled(mControlsItem.isEdit());
            setDefaultValue(etValue);
        }
    }

    private void setDefaultValue(EditText et){
        if (mControlsItem.getValue() != null && !mControlsItem.getValue().equals("")) {
            et.setText(mControlsItem.getValue());
        } else if (mControlsItem.getDefaultValue() != null && !mControlsItem.getDefaultValue().equals("")) {
            et.setText(mControlsItem.getDefaultValue());
        }
    }
}

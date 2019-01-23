package com.zlsk.zTool.customControls.controls;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.customControls.base.ControlsItemType;
import com.zlsk.zTool.customControls.base.ControlsItemUtil;

public class ControlsText extends ABaseControlItemView {
    private static final int TEXT_MAX_LENGTH = 80;
    private static final int LONG_TEXT_MAX_LENGTH = 525;
    private EditText etValue;

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_text;
    }

    @Override
    protected void setup(View contentView) {
        etValue = contentView.findViewById(R.id.et_item_value);
        if (mControlsItem.getType().equals(ControlsItemType.TEXT_NUM)){
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        etValue.setEnabled(mControlsItem.isEdit());
        if (mControlsItem.isEdit()) {
            boolean isTextNormal = mControlsItem.getType().equals(ControlsItemType.TEXT);
            int maxLength = isTextNormal ? TEXT_MAX_LENGTH : LONG_TEXT_MAX_LENGTH;
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            etValue.setFilters(filters);
        } else {
            etValue.setText(" ");
        }

        ControlsItemUtil.InspectTextInputType type = mControlsItem.getInspectTextInputType();
        if(type != null){
            final int decimalCount = mControlsItem.getDecimalCount();

            if(type == ControlsItemUtil.InspectTextInputType.type_int){
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            }else if(type == ControlsItemUtil.InspectTextInputType.type_float){
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etValue.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
                    String lastInputContent = dest.toString();
                    if (lastInputContent.contains(".")) {
                        int index = lastInputContent.indexOf(".");
                        if(dend - index >= decimalCount + 1){
                            return "";
                        }
                    }
                    return null;
                }});
            }
        }

        if (mControlsItem.getValue() != null && !mControlsItem.getValue().equals("")) {
            etValue.setText(mControlsItem.getValue());
            etValue.setSelection(mControlsItem.getValue().length());
        } else if (mControlsItem.getDefaultValue() != null && !mControlsItem.getDefaultValue().equals("")) {
            etValue.setText(mControlsItem.getDefaultValue());
            etValue.setSelection(mControlsItem.getDefaultValue().length());
        }

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etValue.setSelection(s.length());
                mControlsItem.setValue(s.toString());
            }
        });

    }
}

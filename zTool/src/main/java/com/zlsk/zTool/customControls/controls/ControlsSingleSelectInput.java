package com.zlsk.zTool.customControls.controls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.customControls.base.ControlsItemType;
import com.zlsk.zTool.customControls.base.SingleSelectActivity;
import com.zlsk.zTool.customControls.base.SingleSelectInputActivity;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IceWang on 2018/7/11.
 */

public class ControlsSingleSelectInput extends ABaseControlItemView {
    private TextView tv_show;
    private List<String> selectValues;
    private boolean isMulSelect = false;
    @Override
    protected void setup(View contentView) {
        initUi(contentView);
        isMulSelect = mControlsItem.getType() == ControlsItemType.MUL_SELECT_INPUT;
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_single_select;
    }

    private void initUi(final View contentView){
        tv_show = contentView.findViewById(R.id.tv_show);

        selectValues = mControlsItem.getSingleSelectValues();
        if(selectValues != null && selectValues.size() != 0){
            tv_show.setText(selectValues.get(0));
            mControlsItem.setValue(selectValues.get(0));
        }else if(mControlsItem.getSingleSelectValuesArray() != null){
            selectValues = new ArrayList<>();
            Collections.addAll(selectValues,mControlsItem.getSingleSelectValuesArray());
            if(selectValues != null && selectValues.size() != 0){
                tv_show.setText(selectValues.get(0));
                mControlsItem.setValue(selectValues.get(0));
            }
        }

        if(mControlsItem.getDefaultValue() != null && !mControlsItem.getDefaultValue().equals("")){
            tv_show.setText(mControlsItem.getDefaultValue());
        }else if(mControlsItem.getValue() != null && !mControlsItem.getValue().equals("")){
            tv_show.setText(mControlsItem.getValue());
        }

        LinearLayout layout_container = contentView.findViewById(R.id.layout_container);
        layout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mControlsItem.isEdit() && selectValues != null && selectValues.size() != 0){
                    Intent intent = new Intent(context, SingleSelectInputActivity.class);
                    intent.putExtra(SingleSelectInputActivity.INTENT_FIELD_IS_MUL_SELECT,isMulSelect);
                    intent.putStringArrayListExtra(SingleSelectInputActivity.INTENT_FIELD_DATA, (ArrayList<String>) selectValues);
                    intent.putStringArrayListExtra(SingleSelectInputActivity.INTENT_FIELD_INPUT_BOOLEAN,mControlsItem.getNeedInputItems());
                    startActivityForResult(intent,SingleSelectInputActivity.INTENT_REQUEST_CODE);
                }
            }
        });

        ImageView imageView = contentView.findViewById(R.id.iv_spn_value);
        imageView.setVisibility(mControlsItem.isEdit() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SingleSelectActivity.INTENT_REQUEST_CODE){
            if(resultCode == SingleSelectActivity.INTENT_RESULT_CODE){
                Bundle bundle = data.getExtras();
                if(bundle != null){
                    String result = bundle.getString(SingleSelectInputActivity.INTENT_FIELD_RESULT);
                    String alias = bundle.getString(SingleSelectInputActivity.INTENT_FIELD_RESULT_ALIAS);
                    if(alias != null){
                        String[] aliasArray = alias.split(",");
                        StringBuilder showText = new StringBuilder();
                        for (String item : aliasArray){
                            int index = item.indexOf("_");
                            showText.append(item.substring(index + 1)).append(",");
                        }
                        if(result != null && !result.equals(mControlsItem.getValue())){
                            tv_show.setText(showText.toString().substring(0,showText.length() - 1));
                            RequestEvent requestEvent = new RequestEvent(mControlsItem.getDropDownSelectEventId(),result);
                            EventBusUtil.post(requestEvent);
                            mControlsItem.setValue(alias);
                        }
                    }else {
                        if(result != null && !result.equals(mControlsItem.getValue())){
                            tv_show.setText(result);
                            RequestEvent requestEvent = new RequestEvent(mControlsItem.getDropDownSelectEventId(),result);
                            EventBusUtil.post(requestEvent);
                            mControlsItem.setValue(result);
                        }
                    }
                }
            }
        }
    }
}

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
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IceWang on 2018/7/3.
 */

public class ControlsSingleSelect extends ABaseControlItemView{
    private TextView tv_show;
    private List<String> selectValues;
    private boolean isMulSelect = false;
    @Override
    protected void setup(View contentView) {
        initUi(contentView);
        isMulSelect = mControlsItem.getType() == ControlsItemType.MUL_SELECT;
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_single_select;
    }

    private void initUi(final View contentView){
        tv_show = contentView.findViewById(R.id.tv_show);

        selectValues = mControlsItem.getSingleSelectValues();
        if(selectValues != null && selectValues.size() != 0){
//            tv_show.setText(selectValues.get(0));
//            mControlsItem.setValue(selectValues.get(0));
        }else if(mControlsItem.getSingleSelectValuesArray() != null){
            selectValues = new ArrayList<>();
            Collections.addAll(selectValues,mControlsItem.getSingleSelectValuesArray());
            if(selectValues != null && selectValues.size() != 0){
//                tv_show.setText(selectValues.get(0));
//                mControlsItem.setValue(selectValues.get(0));
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
                    Intent intent = new Intent(context, SingleSelectActivity.class);
                    intent.putExtra(SingleSelectActivity.INTENT_FIELD_IS_MUL_SELECT,isMulSelect);
                    intent.putStringArrayListExtra(SingleSelectActivity.INTENT_FIELD_DATA, (ArrayList<String>) selectValues);
                    startActivityForResult(intent,SingleSelectActivity.INTENT_REQUEST_CODE);
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
                    String result = bundle.getString(SingleSelectActivity.INTENT_FIELD_RESULT);
                    if(result != null && !result.equals(mControlsItem.getValue())){
                        tv_show.setText(result);
                        mControlsItem.setValue(result);
                        RequestEvent requestEvent = new RequestEvent(mControlsItem.getDropDownSelectEventId(),mControlsItem);
                        EventBusUtil.post(requestEvent);
                    }
                }
            }
        }
    }
}

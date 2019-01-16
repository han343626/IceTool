package com.zlsk.zTool.customControls.controls;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.customControls.base.SingleSelectActivity;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;
import com.zlsk.zTool.utils.list.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/10/11.
 */

public class ControlDbSearch extends ABaseControlItemView {
    private TextView tv_show;
    private List<String> selectValues = new ArrayList<>();

    @Override
    protected void setup(View contentView) {
        initUi(contentView);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_db_search;
    }

    protected void initUi(View contentView){
        contentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RequestEvent requestEvent = new RequestEvent(mControlsItem.getDbSelectDeleteId(),mControlsItem);
                EventBusUtil.post(requestEvent);
                return true;
            }
        });

        LinearLayout layout_accurate = contentView.findViewById(R.id.layout_accurate);
        View view_divider = contentView.findViewById(R.id.view_divider);

        tv_show = contentView.findViewById(R.id.tv_show);

        selectValues = mControlsItem.getSingleSelectValues();
        if(mControlsItem.getSingleSelectValuesArray() != null){
            selectValues = ListUtil.toList(mControlsItem.getSingleSelectValuesArray());
        }

        layout_accurate.setVisibility((selectValues == null || selectValues.size() == 0) ? View.GONE : View.VISIBLE);
        view_divider.setVisibility((selectValues == null || selectValues.size() == 0) ? View.GONE : View.VISIBLE);

        if(mControlsItem.getDefaultValue() != null && !mControlsItem.getDefaultValue().equals("")){
            tv_show.setText(mControlsItem.getDefaultValue());
        }else if(mControlsItem.getValue() != null && !mControlsItem.getValue().equals("")){
            tv_show.setText(mControlsItem.getValue());
        }

        layout_accurate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mControlsItem.isEdit() && selectValues != null && selectValues.size() != 0){
                    Intent intent = new Intent(context, SingleSelectActivity.class);
                    intent.putExtra(SingleSelectActivity.INTENT_FIELD_IS_MUL_SELECT,false);
                    intent.putStringArrayListExtra(SingleSelectActivity.INTENT_FIELD_DATA, (ArrayList<String>) selectValues);
                    startActivityForResult(intent,SingleSelectActivity.INTENT_REQUEST_CODE);
                }
            }
        });

        ImageView imageView = contentView.findViewById(R.id.iv_spn_value);
        imageView.setVisibility(mControlsItem.isEdit() ? View.VISIBLE : View.GONE);

        EditText etValue = contentView.findViewById(R.id.et_item_value);
        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 50) {
                    String substring = s.toString().substring(0, 50);
                    etValue.setSelection(substring.length());
                    etValue.setText(substring);
                    mControlsItem.setDbInputValue(substring);
                } else {
                    etValue.setSelection(s.length());
                    mControlsItem.setDbInputValue(s.toString());
                }
            }
        });
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
                        mControlsItem.setDbSelectValue(result);
                        RequestEvent requestEvent = new RequestEvent(mControlsItem.getDropDownSelectEventId(),mControlsItem);
                        EventBusUtil.post(requestEvent);
                    }
                }
            }
        }
    }
}

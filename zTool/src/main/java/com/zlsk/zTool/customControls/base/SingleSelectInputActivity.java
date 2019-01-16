package com.zlsk.zTool.customControls.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.baseActivity.ARefreshListBaseActivity;
import com.zlsk.zTool.dialog.InputDialog;
import com.zlsk.zTool.dialog.ZToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/7/11.
 */

public class SingleSelectInputActivity extends ARefreshListBaseActivity {
    public static final String INTENT_FIELD_INPUT_BOOLEAN = "intent_field_input_boolean";
    public static final String INTENT_FIELD_DATA = "intent_field_data";
    public static final String INTENT_FIELD_RESULT = "intent_field_result";
    public static final String INTENT_FIELD_RESULT_ALIAS = "intent_field_result_alias";
    public static final String INTENT_FIELD_IS_MUL_SELECT = "intent_field_is_mul_select";

    public static final int INTENT_REQUEST_CODE = 10085;
    public static final int INTENT_RESULT_CODE = 10086;
    public static final int INTENT_RESULT_CODE_NONE = 10087;
    protected Adapter adapter;
    protected boolean isMulSelect;
    private ArrayList<String> needInputItems;
    private InputDialog inputDialog;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_select;
    }

    @Override
    public String getTitleString() {
        return "请选择";
    }

    @Override
    public String getActionString() {
        return isMulSelect ? "确定" : null;
    }

    @Override
    public void onActionButtonClicked(View view) {
        super.onActionButtonClicked(view);
        StringBuilder aliasBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        List<SelectModel> selectModelList = adapter.getDataList();
        for (int index = 0;index < selectModelList.size();index ++){
            SelectModel model = selectModelList.get(index);
            if(model.isSelect()){
                result.append(model.getName()).append(",");
                aliasBuilder.append(model.getAlias()).append(",");
            }
        }
        if(result.length() == 0){
            back();
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(INTENT_FIELD_RESULT, result.toString().substring(0,result.length() - 1));
            bundle.putString(INTENT_FIELD_RESULT_ALIAS,aliasBuilder.toString().substring(0,aliasBuilder.length() - 1));
            setResult(INTENT_RESULT_CODE,new Intent().putExtras(bundle));
        }

        finish();
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    public void onBackButtonClicked(View view) {
        super.onBackButtonClicked(view);
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getPullToRefreshListView() {
        return R.id.refresh;
    }

    @Override
    protected BaseAdapter getRefreshListViewAdapter() {
        return adapter = new Adapter(this);
    }

    @Override
    protected AdapterView.OnItemClickListener getOnItemClickListener() {
        return null;
    }

    @Override
    protected void requestList() {

    }

    @Override
    protected boolean isPullLoadEnabled() {
        return false;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        isMulSelect = getIntent().getBooleanExtra(INTENT_FIELD_IS_MUL_SELECT,false);
        needInputItems = getIntent().getStringArrayListExtra(INTENT_FIELD_INPUT_BOOLEAN);
    }

    @Override
    protected void initUi() {
        super.initUi();
        List<String> nameList = getIntent().getStringArrayListExtra(INTENT_FIELD_DATA);
        List<SelectModel> selectModelList = new ArrayList<>();
        for(String name : nameList){
            selectModelList.add(new SelectModel(name,false));
        }
        adapter.setDataList(selectModelList);

        inputDialog = new InputDialog(InputDialog.InputType.other, context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.hideSoftInput();
                Editable editable = inputDialog.getCount();
                if(TextUtils.isEmpty(editable)){
                    ZToast.getInstance().show("请输入对应值");
                }else {
                    back(String.valueOf(inputDialog.getCount()));
                    inputDialog.dismiss();
                }
            }
        });
    }

    protected void back(){
        setResult(INTENT_RESULT_CODE_NONE);
    }

    protected void back(String value){
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_FIELD_RESULT, value);
        setResult(INTENT_RESULT_CODE,new Intent().putExtras(bundle));
        finish();
    }

    class Adapter extends CommonAdapter<SelectModel> {
        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected void initializeView(ViewHolder holder, final int position) {

            final SelectModel entity = getDataList().get(position);
            final TextView content = holder.findViewById(R.id.tv_content);
            content.setText(entity.getName());
            content.setGravity(Gravity.CENTER);
            holder.convertView.setBackgroundColor(Color.parseColor(entity.isSelect() ? "#00A7DB" : "#ffffff"));
            holder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String needInput = needInputItems.get(position);
                    if(isMulSelect){
                        if(needInput.equals("1")){
                            if(!entity.isSelect()){
                                inputDialog = new InputDialog(InputDialog.InputType.other, context, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        inputDialog.hideSoftInput();
                                        Editable editable = inputDialog.getCount();
                                        if(TextUtils.isEmpty(editable)){
                                            ZToast.getInstance().show("请输入对应值");
                                        }else {
                                            entity.setAlias((position + 1) + "_" + String.valueOf(editable));
                                            entity.setSelect(!entity.isSelect());
                                            notifyDataSetChanged();
                                            inputDialog.dismiss();
                                        }
                                    }
                                });
                                inputDialog.show();
                            }else {
                                entity.setSelect(!entity.isSelect());
                                notifyDataSetChanged();
                            }
                        }else {
                            entity.setSelect(!entity.isSelect());
                            notifyDataSetChanged();
                        }
                    }else {
                        if(needInput.equals("1")){
                            inputDialog.show();
                        }else {
                            back(adapter.getDataList().get(position).getName());
                        }
                    }
                }
            });
        }

        @Override
        protected int getConvertViewId() {
            return R.layout.item_list_score_content;
        }
    }
}


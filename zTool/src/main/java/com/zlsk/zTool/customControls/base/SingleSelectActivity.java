package com.zlsk.zTool.customControls.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.baseActivity.ARefreshListBaseActivity;
import com.zlsk.zTool.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/7/3.
 */

public class SingleSelectActivity extends ARefreshListBaseActivity {
    public static final String INTENT_FIELD_DATA = "intent_field_data";
    public static final String INTENT_FIELD_RESULT = "intent_field_result";
    public static final String INTENT_FIELD_IS_MUL_SELECT = "intent_field_is_mul_select";
    public static final int INTENT_REQUEST_CODE = 10085;
    public static final int INTENT_RESULT_CODE = 10086;
    public static final int INTENT_RESULT_CODE_NONE = 10087;
    protected Adapter adapter;
    protected boolean isMulSelect;

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
        StringBuilder result = new StringBuilder();
        List<SelectModel> selectModelList = adapter.getDataList();
        for (int index = 0;index < selectModelList.size();index ++){
            SelectModel model = selectModelList.get(index);
            if(model.isSelect()){
                result.append(model.getName()).append(",");
            }
        }
        if(result.length() == 0){
            back();
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(INTENT_FIELD_RESULT, result.toString().substring(0,result.length() - 1));
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
    }

    @Override
    protected void initUi() {
        super.initUi();
        List<String> nameList = getIntent().getStringArrayListExtra(INTENT_FIELD_DATA);
        nameList = StringUtil.sortChinese(nameList);
        List<SelectModel> selectModelList = new ArrayList<>();
        for(String name : nameList){
            selectModelList.add(new SelectModel(name,false));
        }
        adapter.setDataList(selectModelList);
    }

    protected void back(){
        setResult(INTENT_RESULT_CODE_NONE);
    }

    class Adapter extends CommonAdapter<SelectModel>{
        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected void initializeView(ViewHolder holder, final int position) {
            final SelectModel entity = getDataList().get(position);
            TextView content = holder.findViewById(R.id.tv_content);
            content.setText(entity.getName());
            content.setGravity(Gravity.CENTER);

            LinearLayout layout_container = holder.findViewById(R.id.layout_container);
            layout_container.setBackgroundColor(Color.parseColor(entity.isSelect() ? "#999999" : "#ffffff"));
            layout_container.setOnClickListener(v -> {
                if(isMulSelect){
                    entity.setSelect(!entity.isSelect());
                    notifyDataSetChanged();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString(INTENT_FIELD_RESULT, adapter.getDataList().get(position).getName());
                    setResult(INTENT_RESULT_CODE,new Intent().putExtras(bundle));
                    finish();
                }
            });
        }

        @Override
        protected int getConvertViewId() {
            return R.layout.item_list_score_content;
        }
    }
}

package com.ice.icetool.item;

import android.widget.LinearLayout;

import com.ice.icetool.R;
import com.zlsk.zTool.baiduMap.DbMapView;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.utils.CommonToolUtil;

/**
 * Created by IceWang on 2019/1/4.
 */

public class BdActivity extends ATitleBaseActivity{
    private DbMapView dbMapView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bd_map;
    }

    @Override
    public String getTitleString() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public String getActionString() {
        return null;
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        CommonToolUtil.getInstance().initBaiduMap();
    }

    @Override
    protected void initUi() {
        super.initUi();

        dbMapView = new DbMapView(context);
        LinearLayout layout = findViewById(R.id.layout_container);
        layout.addView(dbMapView.getView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbMapView.onDestroy();
    }
}

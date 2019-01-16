package com.ice.icetool.item;

import android.widget.LinearLayout;

import com.ice.icetool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.base.ControlsItem;
import com.zlsk.zTool.customControls.base.ControlsItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/12/28.
 */

public class ControlActivity extends ATitleBaseActivity{
    @Override
    public int getContentViewId() {
        return R.layout.activity_control;
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
    protected void initUi() {
        super.initUi();

        List<ControlsItem> controlsItemList = new ArrayList<>();

        //region 构造控件
        ControlsItem controlsItem = new ControlsItem();
        controlsItem.setAlias("可编辑文字框");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.TEXT);
        controlsItemList.add(controlsItem);

        controlsItem = new ControlsItem();
        controlsItem.setAlias("不可编辑文字框");
        controlsItem.setRequired(false);
        controlsItem.setEdit(false);
        controlsItem.setType(ControlsItemType.TEXT);
        controlsItemList.add(controlsItem);
        controlsItem.setDefaultValue("不可编辑");

        controlsItem = new ControlsItem();
        controlsItem.setAlias("多行文字框");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.LONGTEXT);
        controlsItemList.add(controlsItem);

        controlsItem = new ControlsItem();
        controlsItem.setAlias("单项选择");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.SINGLE_SELECT);
        controlsItemList.add(controlsItem);
        controlsItem.setSingleSelectValuesArray(new String[]{"A","b","C","d","E","f","G"});

        controlsItem = new ControlsItem();
        controlsItem.setAlias("多项选择");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.MUL_SELECT);
        controlsItemList.add(controlsItem);
        controlsItem.setSingleSelectValuesArray(new String[]{"A","b","C","d","E","f","G"});

        controlsItem = new ControlsItem();
        controlsItem.setAlias("照片选择");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.IMAGE);
        controlsItemList.add(controlsItem);

        controlsItem = new ControlsItem();
        controlsItem.setAlias("视频选择");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.VIDEO);
        controlsItemList.add(controlsItem);

        //使用百度地图api，需要注册key并且在zTool AndroidManifest.xml中配置
        controlsItem = new ControlsItem();
        controlsItem.setAlias("详细地址");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.LOCATION);
        controlsItemList.add(controlsItem);

        controlsItem = new ControlsItem();
        controlsItem.setAlias("日期选择");
        controlsItem.setRequired(true);
        controlsItem.setEdit(true);
        controlsItem.setType(ControlsItemType.DATE);
        controlsItemList.add(controlsItem);

        //endregion

        LinearLayout layout_container = findViewById(R.id.layout_container);
        for (ControlsItem item : controlsItemList){
            layout_container.addView(customViewInflater.inflate(item));
        }
    }

}

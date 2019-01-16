package com.zlsk.zTool.baseActivity;

import android.view.KeyEvent;
import android.widget.GridView;

import com.zlsk.zTool.adapter.GridMenuAdapter;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.helper.MenuHelper;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by IceWang on 2018/8/31.
 */

public abstract class AMenuBaseActivity extends ATitleBaseActivity{
    protected abstract JSONArray getMenuInfoFromServer();
    protected abstract int getGridViewId();
    private long exitTime = 0;

    List<Map<String, Object>> userMenus;

    @Override
    public boolean showBackButton() {
        return false;
    }

    @Override
    protected boolean supportSlideBack() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();

        MenuHelper menuHelper = MenuHelper.getInstance(context);
        menuHelper.setUserMenuInfo(getMenuInfoFromServer());
        userMenus = menuHelper.getAvailableMenus();
    }

    @Override
    protected void initUi() {
        super.initUi();

        GridView gvMenus = findViewById(getGridViewId());
        GridMenuAdapter gridMenuAdapter = new GridMenuAdapter(this);
        gvMenus.setAdapter(gridMenuAdapter);
        gridMenuAdapter.setDataList(userMenus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ZToast.getInstance().show("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

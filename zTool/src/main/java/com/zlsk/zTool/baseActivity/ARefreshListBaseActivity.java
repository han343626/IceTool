package com.zlsk.zTool.baseActivity;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zlsk.zTool.customControls.refreshlist.PullToRefreshBase;
import com.zlsk.zTool.customControls.refreshlist.PullToRefreshListView;
import com.zlsk.zTool.dialog.LoadingDialog;

/**
 * Created by IceWang on 2018/5/30.
 */

public abstract class ARefreshListBaseActivity extends ATitleBaseActivity{
    protected abstract int getPullToRefreshListView();
    protected abstract BaseAdapter getRefreshListViewAdapter();
    protected abstract AdapterView.OnItemClickListener getOnItemClickListener();
    protected abstract void requestList();

    protected boolean isPullLoadEnabled() {
        return true;
    }
    protected boolean isPullRefreshEnabled() {
        return true;
    }

    protected PullToRefreshListView refreshListView;
    protected ListView lvRecords;

    protected final int PAGE_SIZE = 10;
    protected int page_num = 1;
    protected boolean isRefresh = false;

    @Override
    protected void initUi() {
        super.initUi();

        initRefreshList();
    }

    protected void initRefreshList(){
        refreshListView = findViewById(getPullToRefreshListView());
        refreshListView.setPullLoadEnabled(isPullLoadEnabled());
        refreshListView.setPullRefreshEnabled(isPullRefreshEnabled());
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_num = 1;
                isRefresh = true;
                requestList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                requestList();
            }
        });

        lvRecords = refreshListView.getRefreshableView();
        lvRecords.setAdapter(getRefreshListViewAdapter());
        lvRecords.setOnItemClickListener(getOnItemClickListener());
    }

    protected void onRequestComplete(){
        LoadingDialog.getInstance().dismiss();
        if(isRefresh){
            refreshListView.onPullDownRefreshComplete();
        }else {
            refreshListView.onPullUpRefreshComplete();
        }
    }

    protected void setHasNoMoreData(){
        refreshListView.setHasMoreData(false);
    }
}

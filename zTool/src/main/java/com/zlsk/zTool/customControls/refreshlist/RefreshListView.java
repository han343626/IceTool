package com.zlsk.zTool.customControls.refreshlist;

import android.widget.ListView;

import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.list.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/12/5.
 */

public abstract class RefreshListView<T> {
    public abstract void request(int pageNum);

    private PullToRefreshListView pullToRefreshListView;
    private CommonAdapter adapter;
    private boolean isRefresh = false;
    public int pageNum = 1;

    public RefreshListView(CommonAdapter adapter, PullToRefreshListView pullToRefreshListView) {
        this.adapter = adapter;
        this.pullToRefreshListView = pullToRefreshListView;
        pullToRefreshListView.setPullLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(true);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                request(pageNum);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                pageNum ++;
                request(pageNum);
            }
        });

        ListView lvRecords = pullToRefreshListView.getRefreshableView();
        lvRecords.setAdapter(adapter);
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public void onRequestCallback(List<T> data){
        LoadingDialog.getInstance().dismiss();
        if(isRefresh){
            if(ListUtil.isEmpty(data)){
                adapter.setDataList(new ArrayList());
            }else {
                adapter.setDataList(data);
            }
            pullToRefreshListView.onPullDownRefreshComplete();
        }else {
            if(ListUtil.isEmpty(data)){
                pullToRefreshListView.setHasMoreData(true);
                ZToast.getInstance().show("没有更多的数据了");
            }else {
                List<T> exist = adapter.getDataList();
                exist.addAll(data);
                adapter.setDataList(exist);
            }
            pullToRefreshListView.onPullUpRefreshComplete();
        }
    }
}

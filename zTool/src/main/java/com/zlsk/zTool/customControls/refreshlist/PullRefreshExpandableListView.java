package com.zlsk.zTool.customControls.refreshlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ExpandableListView;

public class PullRefreshExpandableListView extends PullToRefreshBase<ExpandableListView> implements OnScrollListener {
    private ExpandableListView mListView;

    public PullRefreshExpandableListView(Context context) {
        this(context, null);
    }

    public PullRefreshExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPullLoadEnabled(false);
    }

    @Override
    protected ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
        mListView = new ExpandableListView(context, attrs);
        mListView.setOnScrollListener(this);
        return mListView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }
        if (mListView.getFirstVisiblePosition() == 0) {// 一定要加上这个
            int mostTop = (getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
            if (mostTop >= 0) {
                return true;
            }
        }
        return false;
    }
}

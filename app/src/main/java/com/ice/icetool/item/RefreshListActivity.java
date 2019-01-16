package com.ice.icetool.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ice.icetool.R;
import com.ice.icetool.model.MessageModel;
import com.zlsk.zTool.adapter.CommonAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.other.AvoidDoubleClickListener;
import com.zlsk.zTool.customControls.refreshlist.RefreshListView;
import com.zlsk.zTool.customControls.swipe.SwipeMenuLayout;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.utils.date.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2019/1/4.
 */

public class RefreshListActivity extends ATitleBaseActivity{
    private static final int PAGE_SIZE = 10;
    private RefreshListView refreshListView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_layout_refresh_list;
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
    }

    @Override
    protected void initUi() {
        super.initUi();

        //网上拷贝的控件
        refreshListView = new RefreshListView(new Adapter(context),findViewById(R.id.pullToRefreshListView)) {
            @Override
            public void request(int pageNum) {
                requestData(pageNum);
            }
        };
        LoadingDialog.getInstance().show("数据请求中");
        refreshListView.request(1);
    }

    /**
     * 模拟网络请求
     */
    private void requestData(int pageNum){
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> refreshListView.onRequestCallback(getList(pageNum)));
            refreshListView.setRefresh(pageNum == 1);
        }).start();
    }

    /**
     * 分页数据
     */
    private List<MessageModel> getList(int pageNum){
        List<MessageModel> messageModelList = new ArrayList<>();
        for (int index = 1 + (pageNum - 1) * 10; index < 1 + (pageNum - 1) * 10 + PAGE_SIZE; index++) {
            String time = TimeUtil.timeMill2Date(System.currentTimeMillis());
            time = TimeUtil.formatTimeStr("yyyy-MM-dd HH:mm:ss","HH:mm:ss",time);

            messageModelList.add(new MessageModel("title_" + index,"content_content_content_" + index,time));
        }
        return messageModelList;
    }

    /**
     * CommonAdapter 极好用的adapter
     */
    class Adapter extends CommonAdapter<MessageModel>{

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected void initializeView(ViewHolder holder, int position) {
            MessageModel messageModel = dataList.get(position);

            SwipeMenuLayout swipeMenuLayout = holder.findViewById(R.id.SwipeMenuLayout);
            TextView msg_title = holder.findViewById(R.id.msg_title);
            TextView msg_content = holder.findViewById(R.id.msg_content);
            TextView msg_time = holder.findViewById(R.id.msg_time);
            TextView msg_read_state = holder.findViewById(R.id.msg_read_state);
            TextView btn_read = holder.findViewById(R.id.btn_read);

            swipeMenuLayout.setSwipeEnable(!messageModel.read);
            msg_title.setText(messageModel.title);
            msg_content.setText(messageModel.content);
            msg_time.setText(messageModel.time);
            msg_read_state.setVisibility(messageModel.read ? View.INVISIBLE : View.VISIBLE);
            btn_read.setOnClickListener(new AvoidDoubleClickListener() {
                @Override
                public void OnClick(View view) {
                    swipeMenuLayout.smoothClose();
                    msg_read_state.setVisibility(View.INVISIBLE);
                    messageModel.read = true;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        protected int getConvertViewId() {
            return R.layout.item_list_refresh;
        }
    }
}

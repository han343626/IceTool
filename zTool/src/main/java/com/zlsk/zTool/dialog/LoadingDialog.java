package com.zlsk.zTool.dialog;

import android.app.Activity;

import com.zlsk.zTool.baseActivity.slideback.ActivityHelper;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoadingDialog {
    private static final int Loading_event_id_show = 1;
    private static final int Loading_event_id_update = 2;
    private static final int Loading_event_id_dismiss = 3;

    private static LoadingDialog instance;
    private LoadingProgressDialog dialog;

    public LoadingDialog() {
        EventBusUtil.register(this);
    }

    public static synchronized LoadingDialog getInstance() {
        if (instance == null) {
            instance = new LoadingDialog();
        }
        return instance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event) {
        if(event.getTarget() == LoadingDialog.class){
            switch (event.getEventId()){
                case Loading_event_id_show:
                    Activity activity = ActivityHelper.getInstance().getCurrentActivity();
                    dialog = new LoadingProgressDialog(activity);
                    dialog.setMessage(String.valueOf(event.getData()));
                    dialog.show();
                    break;
                case Loading_event_id_update:
                    dialog.setMessage(String.valueOf(event.getData()));
                    break;
                case Loading_event_id_dismiss:
                    dialog.dismiss();
                    break;
            }
        }
    }

    public void show(String msg){
        EventBusUtil.post(new RequestEvent(LoadingDialog.class,Loading_event_id_show,msg));
    }

    public void dismiss(){
        EventBusUtil.post(new RequestEvent(Loading_event_id_dismiss,LoadingDialog.class));
    }

    public void update(String msg){
        EventBusUtil.post(new RequestEvent(LoadingDialog.class,Loading_event_id_update,msg));
    }
}
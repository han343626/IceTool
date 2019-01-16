package com.zlsk.zTool.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by IceWang on 2018/9/12.
 */

public class ZToast {
    private static ZToast instance;
    private Toast toast;
    private TextView toastTextView;
    private Context context;
    private View view;

    public static ZToast getInstance(){
        return instance;
    }

    public static ZToast getInstance(Context context) {
        if(instance == null){
            instance = new ZToast(context);
        }
        return instance;
    }

    private ZToast(Context context) {
        EventBusUtil.register(this);
        this.context = context;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.custom_toast_view, null);
        toastTextView = view.findViewById(R.id.toast_text);
    }

    public void show(String msg){
        EventBusUtil.post(new RequestEvent(msg,ZToast.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event) {
        if(event.getTarget() == ZToast.class){
            toastTextView.setText(String.valueOf(event.getData()));
            toast = new Toast(context);
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

package com.zlsk.zTool.utils.event;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by IceWang on 2018/10/25.
 */

public class BaseEventClass {
    public BaseEventClass() {
        EventBusUtil.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event) {

    }
}

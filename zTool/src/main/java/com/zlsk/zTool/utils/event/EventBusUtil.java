package com.zlsk.zTool.utils.event;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }
}

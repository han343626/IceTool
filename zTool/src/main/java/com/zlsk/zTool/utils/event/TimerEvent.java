package com.zlsk.zTool.utils.event;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IceWang on 2018/9/27.
 */

public class TimerEvent {
    public interface OnTimerEventCallback{
        void onCallback();
    }

    private Timer timer;
    private long delay = 0;
    private long period = 500;
    private OnTimerEventCallback onTimerEventCallback;

    public TimerEvent(OnTimerEventCallback onTimerEventCallback) {
        this.onTimerEventCallback = onTimerEventCallback;
        timer = new Timer();
    }

    public TimerEvent setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public TimerEvent setPeriod(long period) {
        this.period = period;
        return this;
    }

    public TimerEvent start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onTimerEventCallback.onCallback();
            }
        },delay,period);

        return this;
    }

    public void stop(){
        if(timer != null){
            timer.cancel();
        }
    }
}

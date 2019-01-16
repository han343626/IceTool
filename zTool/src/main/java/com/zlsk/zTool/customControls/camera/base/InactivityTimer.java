package com.zlsk.zTool.customControls.camera.base;

import android.app.Activity;
import android.content.DialogInterface;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by IceWang on 2018/11/22.
 */

public final class InactivityTimer {

    private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

    private final ScheduledExecutorService inactivityTimer =
            Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private final Activity activity;
    private ScheduledFuture<?> inactivityFuture = null;

    public InactivityTimer(Activity activity) {
        this.activity = activity;
        onActivity();
    }

    public void onActivity() {
        cancel();
        inactivityFuture = inactivityTimer.schedule(new FinishListener(activity),
                INACTIVITY_DELAY_SECONDS,
                TimeUnit.SECONDS);
    }

    private void cancel() {
        if (inactivityFuture != null) {
            inactivityFuture.cancel(true);
            inactivityFuture = null;
        }
    }

    public void shutdown() {
        cancel();
        inactivityTimer.shutdown();
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }

    public final class FinishListener
            implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {

        private final Activity activityToFinish;

        public FinishListener(Activity activityToFinish) {
            this.activityToFinish = activityToFinish;
        }

        public void onCancel(DialogInterface dialogInterface) {
            run();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            run();
        }

        public void run() {
            activityToFinish.finish();
        }

    }

}



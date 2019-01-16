package com.zlsk.zTool.customControls.camera.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.google.zxing.Result;
import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.photo.MediaCacheManager;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.customControls.DoubleClickListener;
import com.zlsk.zTool.utils.camera.StartCameraUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ScannerManager {
    private enum State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }

    /**
     * 是否有预览
     */
    private boolean hasSurface;
    private boolean isFlashOn = false;
    private boolean isZoomIn = false;

    private CaptureActivityHandler captureActivityHandler;

    private Context context;
    private ScannerCallback scannerCallback;
    private SurfaceHolder surfaceHolder;
    private View decodeView;
    private View parentView;
    private View scannerLine;

    /**
     * @param context 上下文
     * @param decodeView 解码聚焦的View
     * @param parentView    承载camera的view
     * @param scannerLine   动态线条(可为null)
     * @param surfaceHolder   surfaceView
     * @param scannerCallback   扫码回调
     */
    public ScannerManager(Context context,View decodeView ,View parentView ,View scannerLine,SurfaceHolder surfaceHolder,ScannerCallback scannerCallback){
        this.context = context;
        this.decodeView = decodeView;
        this.parentView = parentView;
        this.scannerLine = scannerLine;
        this.scannerCallback = scannerCallback;
        this.surfaceHolder = surfaceHolder;

        initCameraOperate();
        initScannerLineAnimation();
        CameraManager.getInstance(context);
    }

    /**
     * 暂停
     */
    public void onPause(){
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            captureActivityHandler.removeCallbacksAndMessages(null);
            captureActivityHandler = null;
        }
        CameraManager.getInstance().closeDriver();
    }

    /**
     * 恢复
     */
    public void onResume() {
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(new SurfaceCallback());
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    /**
     *  相册照片扫码
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constant.CODE_SELECT_PHOTO_FROM_SCANNER){
            if(MediaCacheManager.imageAddress != null && MediaCacheManager.imageAddress.size() > 0){
                String path = MediaCacheManager.imageAddress.get(0);
                MediaCacheManager.imageAddress.clear();
                Result result = DecodeManager.getInstance().decode(path);
                scannerCallback.onSuccess(result);
            }
        }
    }

    /**
     * 扫码重置,可再次扫码
     */
    public void setReStart(){
        if(captureActivityHandler != null){
            captureActivityHandler.sendEmptyMessage(R.id.restart_preview);
        }
    }

    /**
     * 从本地相册选照片识别码
     */
    public void selectPicFromLocal(){
        MediaCacheManager.imageAddress.clear();
        StartCameraUtil.getPhotoFromLocal(context,1,"照片相册", Constant.CODE_SELECT_PHOTO_FROM_SCANNER);
    }

    private void initCameraOperate(){
        decodeView.setOnLongClickListener(v -> {
            isFlashOn = !isFlashOn;
            if(isFlashOn){
                CameraManager.getInstance().openLight();
            }else {
                CameraManager.getInstance().offLight();
            }
            return true;
        });
        decodeView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                isZoomIn = !isZoomIn;
                if(isZoomIn){
                    CameraManager.getInstance().zoomIn();
                }else {
                    CameraManager.getInstance().zoomOut();
                }
            }
        });
    }

    private void initScannerLineAnimation(){
        if(scannerLine == null){
            return;
        }

        TranslateAnimation down = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        down.setDuration(3000);

        TranslateAnimation up = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        up.setDuration(3000);

        down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scannerLine.startAnimation(up);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scannerLine.startAnimation(down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        scannerLine.startAnimation(down);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.getInstance().openDriver(surfaceHolder);
            Point point = CameraManager.getInstance().getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = decodeView.getWidth() * width.get() / parentView.getWidth();
            int cropHeight = decodeView.getHeight() * height.get() / parentView.getHeight();
            CameraManager.FRAME_WIDTH = cropWidth;
            CameraManager.FRAME_HEIGHT = cropHeight;
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (captureActivityHandler == null) {
            captureActivityHandler = new CaptureActivityHandler();
        }
    }

    class SurfaceCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            hasSurface = false;

        }
    }

    @SuppressLint("HandlerLeak")
    class CaptureActivityHandler extends Handler {

        DecodeThread decodeThread = null;
        private State state;

        public CaptureActivityHandler() {
            decodeThread = new DecodeThread();
            decodeThread.start();
            state = State.SUCCESS;
            CameraManager.getInstance().startPreview();
            restartPreviewAndDecode();
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                    CameraManager.getInstance().requestAutoFocus(this, R.id.auto_focus);
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode();
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS;
                scannerCallback.onSuccess((Result) message.obj);
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW;
                CameraManager.getInstance().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            }
        }

        public void quitSynchronously() {
            state = State.DONE;
            decodeThread.interrupt();
            CameraManager.getInstance().stopPreview();
            removeMessages(R.id.decode_succeeded);
            removeMessages(R.id.decode_failed);
            removeMessages(R.id.decode);
            removeMessages(R.id.auto_focus);
        }

        private void restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW;
                CameraManager.getInstance().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                CameraManager.getInstance().requestAutoFocus(this, R.id.auto_focus);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    class DecodeHandler extends Handler {
        DecodeHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == R.id.decode) {
                Result result = DecodeManager.getInstance().decode((byte[]) message.obj, message.arg1, message.arg2);
                if (result != null) {
                    Message.obtain(captureActivityHandler, R.id.decode_succeeded, result).sendToTarget();
                } else {
                    try{
                        Message.obtain(captureActivityHandler, R.id.decode_failed).sendToTarget();
                    }catch (Exception ignored){

                    }
                }
            } else if (message.what == R.id.quit) {
                Looper.myLooper().quit();
            }
        }
    }

    final class DecodeThread extends Thread {

        private final CountDownLatch handlerInitLatch;
        private Handler handler;

        DecodeThread() {
            handlerInitLatch = new CountDownLatch(1);
        }

        Handler getHandler() {
            try {
                handlerInitLatch.await();
            } catch (InterruptedException ie) {
                // continue?
            }
            return handler;
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new DecodeHandler();
            handlerInitLatch.countDown();
            Looper.loop();
        }
    }
}
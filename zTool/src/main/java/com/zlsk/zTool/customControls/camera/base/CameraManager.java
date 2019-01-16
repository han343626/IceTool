package com.zlsk.zTool.customControls.camera.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraManager {
    public static int FRAME_WIDTH = -1;
    public static int FRAME_HEIGHT = -1;
    public static int FRAME_MARGINTOP = -1;

    private static CameraManager instance;
    private CameraConfigurationManager configManager;

    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;

    private Camera camera;
    private boolean initialized;
    private boolean previewing;
    private Rect framingRect;
    private Rect framingRectInPreview;

    private int normalZoomValue = -1;

    private CameraManager(Context context) {
        configManager = new CameraConfigurationManager(context);

        previewCallback = new PreviewCallback(configManager);
        autoFocusCallback = new AutoFocusCallback();
    }

    public static synchronized CameraManager getInstance(Context context) {
        if (instance == null) {
            instance = new CameraManager(context);
        }
        return instance;
    }

    public static CameraManager getInstance() {
        return instance;
    }

    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);
            FlashlightManager.enableFlashlight();
        }
    }

    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.release();
            camera = null;
        }
    }

    public void openLight() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        }
    }

    public void offLight() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
        }
    }

    public void zoomIn(){
        if (camera != null) {
            if(normalZoomValue == -1){
                normalZoomValue = camera.getParameters().getZoom();
            }
            zoomAnimate(normalZoomValue,camera.getParameters().getMaxZoom());
        }
    }

    public void zoomOut(){
        if (camera != null) {
            zoomAnimate(camera.getParameters().getMaxZoom(),normalZoomValue);
        }
    }

    private void zoomAnimate(int start,int end){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start,end);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            int zoom = (int) animation.getAnimatedValue();
            Camera.Parameters parameter = camera.getParameters();
            parameter.setZoom(zoom);
            camera.setParameters(parameter);
        });
        valueAnimator.start();
    }

    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    public void stopPreview() {
        if (camera != null && previewing) {
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            camera.setOneShotPreviewCallback(previewCallback);
        }
    }

    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            camera.autoFocus(autoFocusCallback);
        }
    }

    private Rect getFramingRect() {
        if (camera == null) {
            return null;
        }

        try {
            Point screenResolution = configManager.getScreenResolution();
            int leftOffset = (screenResolution.x - FRAME_WIDTH) / 2;
            int topOffset;
            if (FRAME_MARGINTOP != -1) {
                topOffset = FRAME_MARGINTOP;
            } else {
                topOffset = (screenResolution.y - FRAME_HEIGHT) / 2;
            }
            framingRect = new Rect(leftOffset, topOffset, leftOffset + FRAME_WIDTH, topOffset + FRAME_HEIGHT);
            // }
            return framingRect;
        } catch (Exception e) {
            return null;
        }
    }

    private Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            //modify here
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = configManager.getPreviewFormat();
        String previewFormatString = configManager.getPreviewFormatString();
        switch (previewFormat) {
            // This is the standard Android format which all devices are REQUIRED to support.
            // In theory, it's the only one we should ever care about.
            case ImageFormat.NV21:
            case ImageFormat.NV16:
                return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                        rect.width(), rect.height());
            default:
                if ("yuv420p".equals(previewFormatString)) {
                    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                            rect.width(), rect.height());
                }else {
                    return null;
                }
        }
    }
}

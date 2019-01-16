package com.zlsk.zTool.customControls.camera.base;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * Created by IceWang on 2018/11/22.
 */

public class PreviewCallback implements Camera.PreviewCallback{
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;

    public PreviewCallback(CameraConfigurationManager configManager) {
        this.configManager = configManager;
    }

    public void setHandler(Handler previewHandler, int previewMessage) {
        this.previewHandler = previewHandler;
        this.previewMessage = previewMessage;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = configManager.getCameraResolution();
        if (previewHandler != null) {
            Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x,
                    cameraResolution.y, data);
            message.sendToTarget();
            previewHandler = null;
        }
    }
}

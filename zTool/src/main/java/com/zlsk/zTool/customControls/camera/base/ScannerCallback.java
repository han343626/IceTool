package com.zlsk.zTool.customControls.camera.base;

import com.google.zxing.Result;

/**
 * Created by IceWang on 2018/11/22.
 */

public interface ScannerCallback {
    void onSuccess(Result result);
}

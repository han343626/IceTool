package com.zlsk.zTool.utils.network;

import java.io.File;

/**
 * Created by IceWang on 2018/9/25.
 */

public interface IDownloadCallback {
    void onSuccess(File file);
    void onError(String msg);
    void onLoading(long total, long current, boolean isDownloading);
}

package com.zlsk.zTool.utils.file;

/**
 * Created by IceWang on 2018/9/18.
 */

public interface IProgress {
    void onProgress(int progress);
    void onError(String msg);
    void onDone();
}

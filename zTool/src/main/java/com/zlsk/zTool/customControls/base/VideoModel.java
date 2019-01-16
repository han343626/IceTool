package com.zlsk.zTool.customControls.base;

import java.io.Serializable;

/**
 * Created by IceWang on 2018/7/4.
 */

public class VideoModel implements Serializable{
    private String videoPath;
    private String thumbnailPath;

    public VideoModel(String videoPath, String thumbnailPath) {
        this.videoPath = videoPath;
        this.thumbnailPath = thumbnailPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}

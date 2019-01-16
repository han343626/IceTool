package com.zlsk.zTool.model.photo;

import java.io.Serializable;

/**
 * Created by IceWang on 2018/5/28.
 */

public class ReportPhotoModel implements Serializable {
    private String filename;
    private String photo_name;
    private String add_time;
    private String abs_path;

    public String getAbs_path() {
        return abs_path;
    }

    public void setAbs_path(String abs_path) {
        this.abs_path = abs_path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}

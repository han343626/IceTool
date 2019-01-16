package com.zlsk.zTool.model.other;

/**
 * Created by IceWang on 2018/6/11.
 */

public class MenuModel {
    private String content;
    private String imgName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public MenuModel(String content, String imgName) {
        this.content = content;
        this.imgName = imgName;
    }
}

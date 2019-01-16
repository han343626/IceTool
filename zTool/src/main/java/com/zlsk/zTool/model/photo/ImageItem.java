package com.zlsk.zTool.model.photo;

import java.io.Serializable;

/**
 * Created by IceWang on 2018/5/25.
 */

public class ImageItem implements Serializable{
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}

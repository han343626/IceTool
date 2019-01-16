package com.zlsk.zTool.model.photo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IceWang on 2018/5/25.
 */

public class ImageBucket implements Serializable{
    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;
}

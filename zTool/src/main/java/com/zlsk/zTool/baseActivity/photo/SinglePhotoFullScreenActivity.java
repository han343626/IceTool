package com.zlsk.zTool.baseActivity.photo;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.scaleImage.ZScaleImageView;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;

/**
 * Created by IceWang on 2019/1/23.
 */

public class SinglePhotoFullScreenActivity extends ATitleBaseActivity{
    public static final String INTENT_FIELD_IMAGE_PATH = "intent_field_image_path";

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_photo_full_screen;
    }

    @Override
    public String getTitleString() {
        return "照片";
    }

    @Override
    public String getActionString() {
        return null;
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void initUi() {
        super.initUi();

        String path = getIntent().getStringExtra(INTENT_FIELD_IMAGE_PATH);
        ZScaleImageView zScaleImageView = findViewById(R.id.zScaleImage);
        BitmapUtil.bindScaleImageSource(path,zScaleImageView);
    }
}

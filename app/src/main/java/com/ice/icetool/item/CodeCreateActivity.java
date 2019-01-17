package com.ice.icetool.item;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ice.icetool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.camera.base.CodeBuilder;
import com.zlsk.zTool.customControls.other.AvoidDoubleClickListener;
import com.zlsk.zTool.dialog.ConfirmDialog;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;
import com.zlsk.zTool.utils.file.FileUtil;

/**
 * Created by IceWang on 2019/1/17.
 */

public class CodeCreateActivity extends ATitleBaseActivity{
    private int count = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_code_create;
    }

    @Override
    public String getTitleString() {
        return "二维码/条形码生成";
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
        ImageView img_qr_code = findViewById(R.id.img_qr_code);
        ImageView img_bar_code = findViewById(R.id.img_bar_code);

        EditText et_input = findViewById(R.id.et_input);
        findViewById(R.id.layout_create).setOnClickListener(new AvoidDoubleClickListener() {
            @Override
            public void OnClick(View view) {
                if(TextUtils.isEmpty(et_input.getText())){
                    ZToast.getInstance().show("请输入二维码/条形码中的信息");
                }else {
                    count = 0;
                    String content = et_input.getText().toString();
                    LoadingDialog.getInstance().show("二维码/条形码生成中");
                    new CodeBuilder().
                            content(content).
                            codeColor(Color.parseColor("#1296db")).
                            buildQrCode((bitmap -> handle(img_qr_code,bitmap)));
                    new CodeBuilder().
                            content(content).
                            codeColor(Color.BLACK).
                            buildBarCode((bitmap -> handle(img_bar_code,bitmap)));
                }
            }
        });

        Save save = new Save();
        img_qr_code.setOnLongClickListener(save);
        img_bar_code.setOnLongClickListener(save);
    }

    public class Save implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            ImageView imageView = (ImageView) v;
            Drawable drawable = imageView.getDrawable();
            if(drawable != null){
                new ConfirmDialog(context, "是否保存", ok -> {
                    if(ok){
                        BitmapUtil.savePhotoToSD(FileUtil.getInstance().getZLSKShootImagePath() + "/code_" + System.currentTimeMillis() + ".png",BitmapUtil.transDrawableToBitmap(drawable));
                        ZToast.getInstance().show("保存成功");
                        FileUtil.getInstance().refreshImageFolders(FileUtil.getInstance().getZLSKShootImagePath());
                    }
                }).show();
            }
            return true;
        }
    }

    private void handle(ImageView imageView, Bitmap bitmap){
        count ++;
        runOnUiThread(() -> {
            if(bitmap != null){
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }else {
                imageView.setVisibility(View.GONE);
            }
        });
        if(count == 2){
            LoadingDialog.getInstance().dismiss();
        }
    }
}

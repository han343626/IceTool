package com.zlsk.zTool.customControls.camera.scanner;

import android.app.Activity;
import android.content.Intent;
import android.view.SurfaceView;
import android.view.View;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.customControls.camera.base.ScannerManager;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.other.BeepTool;

/**
 * Created by IceWang on 2018/11/22.
 */

public class CodeScannerActivity extends ATitleBaseActivity{
    private ScannerManager scannerManager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_code_sacnner;
    }

    @Override
    public String getTitleString() {
        return "扫描";
    }

    @Override
    public String getActionString() {
        return "相册";
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerManager.onResume();
    }

    @Override
    protected void initData() {
        super.initData();
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        scannerManager = new ScannerManager(context,
                findViewById(R.id.layout_identification),
                findViewById(R.id.layout_container),
                findViewById(R.id.img_scan_line),
                surfaceView.getHolder(),
                result -> {
                    BeepTool.playBeep((Activity) context, true);
                    ZToast.getInstance().show(result == null ? "解析失败" : result.getText());
                    scannerManager.setReStart();
                });
    }

    @Override
    protected void initUi() {
        super.initUi();
    }

    @Override
    public void onActionButtonClicked(View view) {
        scannerManager.selectPicFromLocal();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        scannerManager.onActivityResult(requestCode, resultCode, data);
    }
}

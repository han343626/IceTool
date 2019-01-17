package com.zlsk.zTool.customControls.controls;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.PhotoGridViewAdapter;
import com.zlsk.zTool.baseActivity.photo.MediaCacheManager;
import com.zlsk.zTool.baseActivity.photo.PhotoFullScreenActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.customControls.base.CustomViewInflater;
import com.zlsk.zTool.dialog.ActionSheetDialog;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.helper.GridViewForScrollView;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;
import com.zlsk.zTool.utils.camera.StartCameraUtil;
import com.zlsk.zTool.utils.date.TimeUtil;
import com.zlsk.zTool.utils.file.FileUtil;
import com.zlsk.zTool.utils.other.LocationUtil;
import com.zlsk.zTool.utils.string.StringUtil;
import com.zlsk.zTool.utils.system.PermissionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IceWang on 2018/5/9.
 */

public class ControlsImage extends ABaseControlItemView {
    private GridViewForScrollView gridViewImage;
    private PhotoGridViewAdapter mImageAdapter;
    /**
     * 拍照时的临时照片文件名
     */
    private String currentPhotoName;
    /**
     * 拍照时的临时照片文件路径(不包括文件名)
     */
    private String currentPhotoPath;

    /**
     * 当前item下图片路径集合
     */
    private List<String> tempPhotoList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_image_select;
    }

    @Override
    protected void setup(View contentView) {
        PermissionUtil.
                build(context).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.ACCESS_NETWORK_STATE).
                addPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS).
                checkPermission();

        currentPhotoPath = FileUtil.getInstance(null).getZLSKShootImagePath();
        initImageGridView(contentView);
    }

    /**
     * 初始化
     */
    private void initImageGridView(View contentView) {
        mImageAdapter = new PhotoGridViewAdapter(context);
        mImageAdapter.setNeedAddPic(mControlsItem.isEdit());
        mImageAdapter.setLimitCount(mControlsItem.getImageLimitCount());

        gridViewImage = contentView.findViewById(R.id.gridviewImage);
        gridViewImage.setVisibility(View.VISIBLE);
        gridViewImage.setOnItemClickListener((parent, view, position, id) -> onImageItemClick(position));

        gridViewImage.setAdapter(mImageAdapter);
        if (mControlsItem.getValue() != null && !mControlsItem.getValue().isEmpty()) {
            String[] imageStrings = mControlsItem.getValue().split(Constant.IMAGE_SPLIT_STRING);
            Collections.addAll(tempPhotoList,imageStrings);
        }
        mImageAdapter.setDataList(tempPhotoList);
    }

    /**
     * 点击照片对应事件(照片选择+照片查看)
     */
    private void onImageItemClick(int position) {
        if (position == tempPhotoList.size()) {
            if (!mControlsItem.isEdit()){
                return;
            }
            String[] menus = new String[]{"拍照","从相册中选择"};
            ActionSheetDialog.show(context, "请选择", menus, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            takePhoto();
                            ActionSheetDialog.dismiss();
                            break;
                        case 1:
                            selectPicture();
                            ActionSheetDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            //全屏查看相片
            Intent intent = new Intent(context, PhotoFullScreenActivity.class);
            intent.putExtra(Constant.INTENT_FIELD_IMAGE_PATH, mControlsItem.getValue());
            intent.putExtra(Constant.INTENT_FIELD_CAN_EDIT, mControlsItem.isEdit());
            intent.putExtra(Constant.INTENT_FIELD_CURRENT_POSITION, position);
            startActivityForResult(intent, Constant.EDIT_PHOTO);
        }
    }

    /**
     * 点击拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtil.
                    build(context).
                    addPermission(Manifest.permission.CAMERA).
                    checkPermission();
            return;
        }

        mCustomInflater.setPendingInspectItemView(ControlsImage.this);
        CustomViewInflater.pendingViewInflater = mCustomInflater;
        currentPhotoName = System.currentTimeMillis() + ".jpg";
        StartCameraUtil.launchCamera(context, Constant.CODE_TAKE_PHOTO, currentPhotoPath, currentPhotoName);
    }

    /**
     * 从相册中选择照片
     */
    private void selectPicture() {
        if (null != MediaCacheManager.imageAddress) {
            MediaCacheManager.imageAddress.clear();
        }
        for (int i = 0; i < tempPhotoList.size(); i++) {
            if (!MediaCacheManager.imageAddress.contains(tempPhotoList.get(i))) {
                MediaCacheManager.imageAddress.add(tempPhotoList.get(i));
            }
        }
        mCustomInflater.setPendingInspectItemView(ControlsImage.this);
        StartCameraUtil.getPhotoFromLocal(context,mControlsItem.getImageLimitCount(),"照片相册",Constant.SELECT_PHOTO);
    }

    /**
     * 拍照+选择+编辑
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.SELECT_PHOTO) {
            selectPhotoThread();
        } else if (requestCode == Constant.EDIT_PHOTO) {
            if(mControlsItem.isEdit()){
                editPhotoThread(data);
            }
        } else if (requestCode == Constant.CODE_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                takePhotoThread();
            }
        }
    }

    /**
     * 照相后对图片的处理线程
     */
    private void takePhotoThread(){
        LoadingDialog.getInstance().show("图片处理中");
        new Thread(() -> {
            refreshImageFolders();
            String path = currentPhotoPath + "/" + currentPhotoName;
            BitmapUtil.amendRotatePhoto(path);
            if (getInspectItem().isAddWaterMark()){
                addWatermark(path);
            }else {
                tempPhotoList.add(path);
                sendMessageToGridView();
            }
        }).start();
    }

    private void addWatermark(final String path){
        LocationUtil.getInstance(null).getAddressDetail(new LocationUtil.onAddressDetailCallBack() {
            @Override
            public void onSuccess(String result) {
                addWatermark(path,result);
            }

            @Override
            public void onFailure(String result) {
                addWatermark(path,"");
            }
        });
    }

    private void addWatermark(final String path, final String location){
        new Thread(() -> {
            String time = TimeUtil.timeMill2Date(System.currentTimeMillis());
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            bitmap = BitmapUtil.addWatermark(bitmap,time,location + "\n" + getInspectItem().getAddWaterMarkMessage());
            BitmapUtil.savePhotoToSD(path,bitmap);
            bitmap.recycle();

            tempPhotoList.add(path);
            sendMessageToGridView();
        }).start();
    }

    /**
     * 从相册中选择照片后的对应处理线程
     */
    private void selectPhotoThread(){
        LoadingDialog.getInstance().show("图片处理中");
        new Thread(() -> {
            for (String path : MediaCacheManager.imageAddress){
                if(!tempPhotoList.contains(path)){
                    tempPhotoList.add(path);
                }
            }

            sendMessageToGridView();
        }).start();
    }

    /**
     * 编辑照片(删除)后对应的处理线程
     */
    private void editPhotoThread(final Intent data){
        LoadingDialog.getInstance().show("图片处理中");
        new Thread(() -> {
            if(data != null){
                tempPhotoList = data.getStringArrayListExtra(Constant.INTENT_FIELD_BACK_TO_IMAGE);
            }
            sendMessageToGridView();
        }).start();
    }

    /**
     * 将照片路径添加到InspectItem中--->以IMAGE_SPLIT_STRING隔开
     */
    private void setImagesPathToInspect() {
        StringBuilder sbPaths = new StringBuilder();
        for (String path : tempPhotoList) {
            if (!StringUtil.isBlank(sbPaths.toString())) {
                sbPaths.append(Constant.IMAGE_SPLIT_STRING);
            }
            sbPaths.append(path);
        }
        mControlsItem.setValue(sbPaths.toString());

        mImageAdapter.setDataList(tempPhotoList);
        gridViewImage.setAdapter(mImageAdapter);
    }

    private void sendMessageToGridView(){
        Message message = new Message();
        handler.sendMessage(message);

    }

    private Handler handler = new Handler(msg -> {
        LoadingDialog.getInstance().dismiss();
        setImagesPathToInspect();
        return true;
    });

    /**
     * MediaScanner扫描,让其包含到相册中
     */
    private void refreshImageFolders() {
        new Thread(() -> MediaScannerConnection.scanFile(context, new String[]{currentPhotoPath}, null, (path, uri) -> {
        })).start();
    }
}
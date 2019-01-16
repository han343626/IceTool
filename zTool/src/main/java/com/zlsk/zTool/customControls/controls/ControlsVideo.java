package com.zlsk.zTool.customControls.controls;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.VideoGridViewAdapter;
import com.zlsk.zTool.baseActivity.photo.MediaCacheManager;
import com.zlsk.zTool.baseActivity.photo.PlayVideoActivity;
import com.zlsk.zTool.baseActivity.photo.VideoAlbumSelectActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.customControls.base.ABaseControlItemView;
import com.zlsk.zTool.customControls.base.CustomViewInflater;
import com.zlsk.zTool.customControls.base.VideoModel;
import com.zlsk.zTool.dialog.ActionSheetDialog;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.helper.GridViewForScrollView;
import com.zlsk.zTool.utils.camera.StartCameraUtil;
import com.zlsk.zTool.utils.file.FileUtil;
import com.zlsk.zTool.utils.string.StringUtil;
import com.zlsk.zTool.utils.system.PermissionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IceWang on 2018/7/4.
 */

public class ControlsVideo extends ABaseControlItemView{
    private GridViewForScrollView gridView;
    private VideoGridViewAdapter videoGridViewAdapter;
    private List<String> tempVideoList = new ArrayList<>();

    /**
     * 拍摄时的临时视频文件名
     */
    private String currentVideoName;

    /**
     * 拍摄时的临时视频文件路径(不包括文件名)
     */
    private String currentVideoPath;

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_video;
    }

    @Override
    protected void setup(View contentView) {
        PermissionUtil.
                build(context).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                checkPermission();

        currentVideoPath = FileUtil.getInstance(null).getZLSKRecordVideoPath();

        videoGridViewAdapter = new VideoGridViewAdapter(context);
        videoGridViewAdapter.setNeedAddVideo(mControlsItem.isEdit());
        videoGridViewAdapter.setLimitCount(mControlsItem.getImageLimitCount());

        gridView = contentView.findViewById(R.id.gv_video);
        gridView.setAdapter(videoGridViewAdapter);
        gridView.setVisibility(View.VISIBLE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGridViewItemClick(position);
            }
        });

        if (mControlsItem.getValue() != null && !mControlsItem.getValue().isEmpty()) {
            String[] videoStrings = mControlsItem.getValue().split(Constant.IMAGE_SPLIT_STRING);
            Collections.addAll(tempVideoList,videoStrings);
        }

        setModelListToAdapter();
    }

    private void setModelListToAdapter(){
        List<VideoModel> videoModelList = new ArrayList<>();
        for(String path : tempVideoList){
            videoModelList.add(new VideoModel(path,""));
        }
        videoGridViewAdapter.setDataList(videoModelList);
        gridView.setAdapter(videoGridViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.CODE_TAKE_VIDEO){
            if (resultCode == Activity.RESULT_OK) {
                takeVideoCallbackThread();
            }
        }else if(requestCode == Constant.CODE_SELECT_VIDEO){
            selectVideoCallbackThread();
        }else if(requestCode == Constant.CODE_EDIT_VIDEO){
            editVideoCallback(data);
        }
    }

    private void editVideoCallback(Intent data){
        if(data != null){
            String delete = data.getStringExtra(PlayVideoActivity.INTENT_KEY_DELETE_VIDEO);
            tempVideoList.remove(delete);
            MediaCacheManager.videoAddress = tempVideoList;
            setVideoPathToControls();
        }
    }

    private void selectVideoCallbackThread() {
        for (String path : MediaCacheManager.videoAddress){
            if(!tempVideoList.contains(path)){
                tempVideoList.add(path);
            }
        }
        MediaCacheManager.videoAddress = new ArrayList<>();
        MediaCacheManager.videoAddress.addAll(tempVideoList);
        setVideoPathToControls();
    }

    private void onGridViewItemClick(int position){
        if (position == tempVideoList.size()) {
            if (!mControlsItem.isEdit()){
                return;
            }
            String[] menus = new String[]{"摄像","选取"};
            ActionSheetDialog.show(context, "请选择", menus, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            takeVideo();
                            ActionSheetDialog.dismiss();
                            break;
                        case 1:
                            selectVideo();
                            ActionSheetDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.putExtra(PlayVideoActivity.INTENT_KEY_VIDEO_PATH, tempVideoList.get(position));
            intent.putExtra(PlayVideoActivity.INTENT_KEY_CAN_DELETE, mControlsItem.isEdit());
            startActivityForResult(intent,Constant.CODE_EDIT_VIDEO);
        }
    }

    private void takeVideo(){
        mCustomInflater.setPendingInspectItemView(ControlsVideo.this);
        CustomViewInflater.pendingViewInflater = mCustomInflater;
        currentVideoName = System.currentTimeMillis() + ".mp4";
        StartCameraUtil.launchCameraTakeVideo(context, Constant.CODE_TAKE_VIDEO, currentVideoPath, currentVideoName);
    }

    private void selectVideo(){
        ArrayList<String> preferredAlbumNames = FileUtil.getInstance().getPreferredAlbumNames();
        Intent intent = new Intent(context, VideoAlbumSelectActivity.class);
        intent.putExtra(Constant.INTENT_FIELD_VIDEO_ALBUM, "视频相册");
        intent.putExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT, mControlsItem.getImageLimitCount());
        intent.putStringArrayListExtra(VideoAlbumSelectActivity.INTENT_KEY_PREFERRED_ALBUM_NAMES, preferredAlbumNames);
        startActivityForResult(intent, Constant.CODE_SELECT_VIDEO);
    }

    private void takeVideoCallbackThread(){
        LoadingDialog.getInstance().show("视频处理中");
        refreshImageFolders();
        String path = currentVideoPath + "/" + currentVideoName;
        tempVideoList.add(path);
        setVideoPathToControls();
    }

    private void setVideoPathToControls(){
        StringBuilder sbPaths = new StringBuilder();
        for (String path : tempVideoList) {
            if (!StringUtil.isBlank(sbPaths.toString())) {
                sbPaths.append(Constant.IMAGE_SPLIT_STRING);
            }
            sbPaths.append(path);
        }
        mControlsItem.setValue(sbPaths.toString());
        setModelListToAdapter();
        LoadingDialog.getInstance().dismiss();
    }

    /**
     * MediaScanner扫描,让其包含到相册中
     */
    private void refreshImageFolders() {
        new Thread(new Runnable() {
            public void run() {
                MediaScannerConnection.scanFile(context, new String[]{currentVideoPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }
        }).start();
    }
}

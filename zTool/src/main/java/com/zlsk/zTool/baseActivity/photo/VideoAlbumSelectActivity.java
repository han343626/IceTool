package com.zlsk.zTool.baseActivity.photo;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.ImageBucketAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.helper.AlbumHelper;
import com.zlsk.zTool.model.photo.ImageBucket;
import com.zlsk.zTool.model.photo.ImageItem;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;
import com.zlsk.zTool.utils.file.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWang on 2018/7/4.
 */

public class VideoAlbumSelectActivity extends ATitleBaseActivity{
    public static final String INTENT_KEY_PREFERRED_ALBUM_NAMES = "INTENT_KEY_PREFERRED_ALBUM_NAMES";
    private List<ImageBucket> dataList;
    private String titleName;
    private int MAX_SELECT_PHOTO_COUNT = 9;

    @Override
    public int getContentViewId() {
        return R.layout.activity_ui_image_bucket;
    }

    @Override
    public String getTitleString() {
        return titleName;
    }

    @Override
    public String getActionString() {
        return "取消";
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    public void onActionButtonClicked(View view) {
        super.onActionButtonClicked(view);
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        titleName = getIntent().getStringExtra(Constant.INTENT_FIELD_VIDEO_ALBUM);
        MAX_SELECT_PHOTO_COUNT = getIntent().getIntExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT,9);
    }

    @Override
    protected void initUi() {
        super.initUi();
        final GridView gridView = findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ImageGridActivity.setIBtnConfirmOnClickCallback(mIOnClickBtnConfirmCallback);

                Intent intent = new Intent(VideoAlbumSelectActivity.this, ImageGridActivity.class);
                intent.putExtra(Constant.INTENT_FIELD_VIDEO_ALBUM,"选择视频");
                intent.putExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT,MAX_SELECT_PHOTO_COUNT);
                intent.putExtra(ImageGridActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
                startActivity(intent);
            }
        });


        LoadingDialog.getInstance().show("资源初始化");
        new Thread(new Runnable() {
            @Override
            public void run() {
                createVideoAlbum();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.getInstance().dismiss();
                        ImageBucketAdapter adapter = new ImageBucketAdapter(VideoAlbumSelectActivity.this, dataList);
                        gridView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private ArrayList<String> getAvailableAlbums(ArrayList<String> original){
        ArrayList<String> albums = new ArrayList<>();
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        List<ImageBucket> buckets = helper.getImagesBucketList(true);
        for (ImageBucket bucket : buckets){
            if (original.contains(bucket.bucketName)) {
                albums.add(bucket.bucketName);
            }
        }
        return albums;
    }


    private void createVideoAlbum(){
        String deviceImageTopDirectory = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";
        dataList = new ArrayList<>();
        ArrayList<String> preferredAlbumNames = getIntent().getStringArrayListExtra(INTENT_KEY_PREFERRED_ALBUM_NAMES);

        ArrayList<String> AvailableAlbums = getAvailableAlbums(preferredAlbumNames);
        //遍历文件夹下所有视频
        for (String album : AvailableAlbums){
            album = deviceImageTopDirectory + album;
            File albumFile = new File(album);

            List<File> videoList = new ArrayList<>();
            FileUtil.getInstance().getVideoFile(videoList,album);
            if(videoList.size() == 0){
                continue;
            }

            List<ImageItem> imageItemList = new ArrayList<>();
            for (File file : videoList){
                ImageItem imageItem = new ImageItem();
                imageItem.imagePath = file.getAbsolutePath();
                imageItem.thumbnailPath = BitmapUtil.getVideoThumbnailPath(imageItem.imagePath);

                imageItemList.add(imageItem);
            }

            ImageBucket imageBucket = new ImageBucket();
            imageBucket.bucketName = albumFile.getName();
            imageBucket.count = videoList.size();
            imageBucket.imageList = imageItemList;

            dataList.add(imageBucket);
        }
    }

    private ImageGridActivity.IBtnConfirmOnClickCallback mIOnClickBtnConfirmCallback = new ImageGridActivity.IBtnConfirmOnClickCallback() {
        @Override
        public void OnBtnConfirmClicked() {
            VideoAlbumSelectActivity.this.finish();
        }
    };
}

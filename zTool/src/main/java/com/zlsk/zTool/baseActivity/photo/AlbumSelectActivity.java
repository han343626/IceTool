package com.zlsk.zTool.baseActivity.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.ImageBucketAdapter;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.helper.AlbumHelper;
import com.zlsk.zTool.model.photo.ImageBucket;
import com.zlsk.zTool.utils.list.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumSelectActivity extends ATitleBaseActivity {
    /**
     * 设置允许选择的相册名称。设置后，只有这些相册才会显示，供选择。不设置的话，显示所有可用的相册。
     */
    public static final String INTENT_KEY_PREFERRED_ALBUM_NAMES = "INTENT_KEY_PREFERRED_ALBUM_NAMES";
    private String titleName;
    List<ImageBucket> dataList;
    GridView gridView;
    ImageBucketAdapter adapter;
    AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bimap;

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
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        titleName = getIntent().getStringExtra(Constant.INTENT_FIELD_VIDEO_ALBUM);
        List<String> preferredAlbumNames = getIntent().getStringArrayListExtra(INTENT_KEY_PREFERRED_ALBUM_NAMES);
        MAX_SELECT_PHOTO_COUNT = getIntent().getIntExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT,9);

        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
        List<ImageBucket> buckets = helper.getImagesBucketList(true);
        if (ListUtil.isEmpty(buckets) || ListUtil.isEmpty(preferredAlbumNames)) {
            dataList = buckets;
        } else {
            dataList = new ArrayList<>();
            for (ImageBucket bucket : buckets) {
                if (preferredAlbumNames.contains(bucket.bucketName)) {
                    dataList.add(bucket);
                }
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        gridView = findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(AlbumSelectActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ImageGridActivity.setIBtnConfirmOnClickCallback(mIOnClickBtnConfirmCallback);
                Intent intent = new Intent(AlbumSelectActivity.this, ImageGridActivity.class);
                intent.putExtra(Constant.INTENT_FIELD_VIDEO_ALBUM,"选择照片");
                intent.putExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT,MAX_SELECT_PHOTO_COUNT);
                intent.putExtra(AlbumSelectActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
                startActivity(intent);
            }
        });
    }

    private ImageGridActivity.IBtnConfirmOnClickCallback mIOnClickBtnConfirmCallback = new ImageGridActivity.IBtnConfirmOnClickCallback() {
        @Override
        public void OnBtnConfirmClicked() {
            AlbumSelectActivity.this.finish();
        }
    };
}

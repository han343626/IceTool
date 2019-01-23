package com.zlsk.zTool.baseActivity.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.customControls.scaleImage.ZScaleImageView;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhotoFullScreenActivity extends ATitleBaseActivity {
    private List<String> paths = new ArrayList<>();
    private List<ZScaleImageView> imageList;
    private boolean isEdit = true;
    private int currentPosition = 0;
    private MyAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected boolean supportSlideBack() {
        return false;
    }

    private void backToImageActivity(){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.INTENT_FIELD_BACK_TO_IMAGE, (ArrayList<String>) paths);
        setResult(10086,new Intent().putExtras(bundle));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        backToImageActivity();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackButtonClicked(View view){
        backToImageActivity();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    public String getTitleString() {
        return "照片";
    }

    @Override
    public String getActionString() {
        return isEdit ? "删除" : null;
    }

    @Override
    public void onActionButtonClicked(View view){
        //删除当前照片，刷新
        if(paths.size() == 0){
            finish();
        }else {
            paths.remove(currentPosition);
            imageList.remove(currentPosition);
            if(imageList.size() == 0){
                backToImageActivity();
            }else {
                adapter.setImageList(imageList);
                currentPosition = currentPosition == 0 ? currentPosition : currentPosition - 1;
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(currentPosition);

                updateTitle("照片(" + (currentPosition + 1) + "/" + paths.size() + ")");
            }
        }
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        imageList = new ArrayList<>();

        isEdit = getIntent().getBooleanExtra(Constant.INTENT_FIELD_CAN_EDIT,false);
        currentPosition = getIntent().getIntExtra(Constant.INTENT_FIELD_CURRENT_POSITION,0);
        String picPaths = getIntent().getStringExtra(Constant.INTENT_FIELD_IMAGE_PATH);
        String[] pathStrs = picPaths.split(Constant.IMAGE_SPLIT_STRING);
        Collections.addAll(paths,pathStrs);

        for (String path : paths){
            ZScaleImageView zScaleImageView = new ZScaleImageView(this);
            BitmapUtil.bindScaleImageSource(path,zScaleImageView);
            imageList.add(zScaleImageView);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();

        updateTitle("照片(" + (currentPosition + 1) + "/" + paths.size() + ")");

        adapter = new MyAdapter();
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        adapter.setImageList(imageList);

        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateTitle("照片(" + (currentPosition + 1) + "/" + paths.size() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyAdapter extends PagerAdapter {
        private List<ZScaleImageView> imageList;

        public void setImageList(List<ZScaleImageView> imageList) {
            this.imageList = imageList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return imageList == null ? 0 : imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZScaleImageView iv = imageList.get(position % imageList.size());
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageList.get(position % imageList.size()));
        }
    }
}

package com.zlsk.zTool.baseActivity.photo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.baseActivity.ATitleBaseActivity;


public class PlayVideoActivity extends ATitleBaseActivity {
    public static final String INTENT_KEY_VIDEO_PATH = "intent_key_video_path";
    public static final String INTENT_KEY_CAN_DELETE = "intent_key_can_delete";
    public static final String INTENT_KEY_DELETE_VIDEO = "intent_key_delete_video";

    private VideoView video_view;
    private String videoPath;
    private boolean isCanDelete = false;
    private LinearLayout layout_loading;

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_play;
    }

    @Override
    public String getTitleString() {
        return "视频播放";
    }

    @Override
    public String getActionString() {
        return isCanDelete ? "删除" : null;
    }

    @Override
    public void onActionButtonClicked(View view) {
        super.onActionButtonClicked(view);

        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_DELETE_VIDEO, videoPath);
        setResult(10086,new Intent().putExtras(bundle));
        finish();
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();

        videoPath = getIntent().getStringExtra(INTENT_KEY_VIDEO_PATH);
        isCanDelete = getIntent().getBooleanExtra(INTENT_KEY_CAN_DELETE,false);
    }

    @Override
    protected void initUi() {
        super.initUi();
        layout_loading = findViewById(R.id.layout_loading);
        ImageView img_loading = findViewById(R.id.img_loading);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_loading);
        img_loading.startAnimation(hyperspaceJumpAnimation);

        MediaController mediaController = new MediaController(this);

        video_view = findViewById(R.id.video_view);
        video_view.setVideoPath(videoPath);
        video_view.requestFocus();
        mediaController.setMediaPlayer(video_view);
        video_view.setMediaController(mediaController);

        setListen();
    }

    private void setListen(){
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                layout_loading.setVisibility(View.GONE);

                video_view.start();
            }
        });

        video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showToast("视频加载失败");
                finish();
                return true;
            }
        });
    }
}

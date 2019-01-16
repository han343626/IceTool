package com.zlsk.zTool.utils.other;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.zlsk.zTool.R;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by IceWang on 2018/11/22.
 */

public class BeepTool {

    private static final float BEEP_VOLUME = 0.50f;
    private static final int VIBRATE_DURATION = 200;
    private static boolean playBeep = false;
    private static MediaPlayer mediaPlayer;

    public static void playBeep(Activity mContext, boolean vibrate) {
        playBeep = true;
        AudioManager audioService = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            mContext.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));

            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
        if (vibrate) {
            VibrateTool.vibrateOnce(mContext, VIBRATE_DURATION);
        }
    }
}


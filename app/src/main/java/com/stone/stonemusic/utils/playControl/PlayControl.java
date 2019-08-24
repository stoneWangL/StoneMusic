package com.stone.stonemusic.utils.playControl;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/4/517:36
 * 播放控制类-通过调用MediaUtils封装的方法实现
 */
public class PlayControl {
    private static String TAG = "PlayControl";
    static Intent startServiceIntent = new Intent( MusicApplication.getContext(), MusicService.class);

    private static AudioManager mAm = (AudioManager) MusicApplication.getContext().getSystemService(Context.AUDIO_SERVICE);

    private static MyOnAudioFocusChangeListener mListener = new MyOnAudioFocusChangeListener();



    /**
     * 监听是否有其他音乐开始播放或者停止
     */
    private static class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.i(TAG, "focusChange=" + focusChange);
            switch (focusChange) {
                case -1:
                    Log.i(TAG, "测试，其他音乐播放时，返回-1");
                    PlayControl.controlPauseSameSong();
                    break;
            }
        }
    }

    /**
     * 开启服务
     */
    private static void startMusicService() {
        MusicApplication.getContext().startService(startServiceIntent);
    }

    /**
     * 暂停
     */
    public static void controlPauseSameSong() {
        MediaUtils.pause();
        AbandonAudioFocus(); //放弃焦点
    }


    /**
     * 播放-暂停-键的逻辑处理（同一首）
     */
    public static void controlBtnPlaySameSong() {
        startMusicService();

        //获取播放焦点
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(TAG, "requestAudioFocus successfully.");

            if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE) {
                MediaUtils.continuePlay();
            } else if (MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
                MediaUtils.prepare(
                        SongModel.getInstance().getChooseSongList().
                                get(MediaUtils.currentSongPosition).getFileUrl());
                MediaUtils.start();
            } else if (MediaUtils.currentState == MediaStateCode.PLAY_START ||
                    MediaUtils.currentState == MediaStateCode.PLAY_CONTINUE) {
                MediaUtils.pause();
                AbandonAudioFocus(); //放弃焦点
            }
        } else {
            Log.i(TAG, "requestAudioFocus failed.");
        }


    }

    /**
     * 播放-逻辑的处理（不是同一首）
     */
    public static void controlBtnPlayDiffSong() {
        startMusicService();

        //获取播放焦点
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(TAG, "requestAudioFocus successfully.");

            MediaUtils.prepare(
                    SongModel.getInstance().getChooseSongList().
                            get(MediaUtils.currentSongPosition).getFileUrl());
            MediaUtils.start();
        } else {
            Log.i(TAG, "requestAudioFocus failed.");
        }

    }

    /**
     * 上一曲
     */
    public static void controlBtnLast() {
        startMusicService();

        //获取播放焦点
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(TAG, "requestAudioFocus successfully.");

            MediaUtils.last();
            MediaUtils.prepare(
                    SongModel.getInstance().getChooseSongList().
                            get(MediaUtils.currentSongPosition).getFileUrl());
            MediaUtils.start();
        } else {
            Log.i(TAG, "requestAudioFocus failed.");
        }
    }

    /**
     * 下一曲
     */
    public static void controlBtnNext() {
        startMusicService();

        //获取播放焦点
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(TAG, "requestAudioFocus successfully.");

            MediaUtils.next();
            MediaUtils.prepare(
                    SongModel.getInstance().getChooseSongList().
                            get(MediaUtils.currentSongPosition).getFileUrl());
            MediaUtils.start();
        } else {
            Log.i(TAG, "requestAudioFocus failed.");
        }
    }

    public static void AbandonAudioFocus() {
        mAm.abandonAudioFocus(mListener); //放弃焦点
    }
}

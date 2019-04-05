package com.stone.stonemusic.present;

import android.content.Intent;

import com.stone.stonemusic.bean.ItemViewChoose;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;

/**
 * author : stoneWang
 * date   : 2019/4/517:36
 * 播放控制类-通过调用MediaUtils封装的方法实现
 */
public class PlayControl {
    static Intent startServiceIntent = new Intent( MusicAppUtils.getContext(), MusicService.class);

    /**
     * 开启服务
     */
    private static void startMusicService() {
//        if (ItemViewChoose.getInstance().getItemChoosePosition() == -1)
            MusicAppUtils.getContext().startService(startServiceIntent);
    }


    public static void controlBtnPlaySameSong() {
        startMusicService();

        if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE) {
            MediaUtils.continuePlay();
        } else if (MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
            MediaUtils.prepare(
                    SongModel.getInstance().getSongList().
                            get(MediaUtils.currentSongPosition).getFileUrl());
            MediaUtils.start();
        } else if (MediaUtils.currentState == MediaStateCode.PLAY_START ||
                MediaUtils.currentState == MediaStateCode.PLAY_CONTINUE) {
            MediaUtils.pause();
        }
    }

    public static void controlBtnPlayDiffSong() {
        startMusicService();

        MediaUtils.prepare(
                SongModel.getInstance().getSongList().
                        get(MediaUtils.currentSongPosition).getFileUrl());
        MediaUtils.start();
    }

    public static void controlBtnLast() {
        startMusicService();

        MediaUtils.last();
        MediaUtils.prepare(
                SongModel.getInstance().getSongList().
                        get(MediaUtils.currentSongPosition).getFileUrl());
        MediaUtils.start();
    }

    public static void controlBtnNext() {
        startMusicService();

        MediaUtils.next();
        MediaUtils.prepare(
                SongModel.getInstance().getSongList().
                        get(MediaUtils.currentSongPosition).getFileUrl());
        MediaUtils.start();
    }
}

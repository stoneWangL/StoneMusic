package com.stone.stonemusic.utils;

import android.media.MediaPlayer;

import com.stone.stonemusic.model.SongModel;

import java.io.IOException;

/*问题播放结束时没有监听，修改播放器的状态*/
public class MediaUtils {
    //当前播放歌曲postion
    public static int currentSongPosition = 0;
    //当前播放状态,初始化默认为stop
    public static int currentState = MediaStateCode.PLAY_STOP;

    private static MediaPlayer sMediaPlayer;

    public static MediaPlayer getMediaPlayer(){
        if (sMediaPlayer == null) {
            sMediaPlayer = new MediaPlayer();
        }
        return sMediaPlayer;
    }


    //准备
    public static void prepare(String path) {
        if (getMediaPlayer() != null) {
            try {
                getMediaPlayer().reset();
                getMediaPlayer().setDataSource(path);
                getMediaPlayer().prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //开始
    public static void start() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().start();
            MediaUtils.currentState = MediaStateCode.PLAY_START;
        }
    }

    //暂停
    public static void pause() {
        if (getMediaPlayer() != null && getMediaPlayer().isPlaying()) {
            getMediaPlayer().pause();
            MediaUtils.currentState = MediaStateCode.PLAY_PAUSE;
        }
    }

    //继续播放
    public static void continuePlay() {
        if (getMediaPlayer() != null && !getMediaPlayer().isPlaying()) {
            getMediaPlayer().start();
            MediaUtils.currentState = MediaStateCode.PLAY_CONTINUE;
        }
    }

    //停止播放
    public static void stop() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().stop();
            MediaUtils.currentState = MediaStateCode.PLAY_STOP;
        }
    }
    //上一曲
    public static void last(){
        MediaUtils.currentState = MediaStateCode.PLAY_START;
        if (MediaUtils.currentSongPosition == 0) {
            MediaUtils.currentSongPosition = SongModel.getInstance().getSongListSize() - 1;
        } else {
            MediaUtils.currentSongPosition--;
        }
    }
    //下一曲
    public static void next(){
        MediaUtils.currentState = MediaStateCode.PLAY_START;
        if (MediaUtils.currentSongPosition == SongModel.getInstance().getSongListSize() - 1) {
            MediaUtils.currentSongPosition = 0;
        } else {
            MediaUtils.currentSongPosition++;
        }
    }

    //释放Media Player资源
    public static void release() {
        if (sMediaPlayer != null) {
            currentState = MediaStateCode.PLAY_STOP;
            sMediaPlayer.stop();
            sMediaPlayer.reset();
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }
}

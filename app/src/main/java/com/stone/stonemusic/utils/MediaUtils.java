package com.stone.stonemusic.utils;

import android.media.MediaPlayer;

import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.present.MusicPositionObserverManager;

import java.io.IOException;
import java.util.Random;

/*问题播放结束时没有监听，修改播放器的状态*/
public class MediaUtils {
    //当前播放歌曲postion
    public static int currentSongPosition = 0;
    //当前播放状态,初始化默认为stop
    public static int currentState = MediaStateCode.PLAY_STOP;
    //记录seekBar是否改变
    public static boolean seekBarIsChanging = false;
    //当前循环播放模式，默认列表循环
    public static int currentLoopMode = MediaStateCode.LOOP_MODE_ORDER_LIST;

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

        //被观察者发生变化->PLAY_START
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_START);
    }

    //暂停
    public static void pause() {
        if (getMediaPlayer() != null && getMediaPlayer().isPlaying()) {
            getMediaPlayer().pause();
            MediaUtils.currentState = MediaStateCode.PLAY_PAUSE;
        }

        //被观察者发生变化->PLAY_PAUSE
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_PAUSE);
    }

    //继续播放
    public static void continuePlay() {
        if (getMediaPlayer() != null && !getMediaPlayer().isPlaying()) {
            getMediaPlayer().start();
            MediaUtils.currentState = MediaStateCode.PLAY_CONTINUE;
        }

        //被观察者发生变化->PLAY_CONTINUE
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_CONTINUE);
    }

    //停止播放
    public static void stop() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().stop();
            MediaUtils.currentState = MediaStateCode.PLAY_STOP;
        }

        //被观察者发生变化->PLAY_STOP
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_STOP);
    }
    //上一曲
    public static void last(){
        MediaUtils.currentState = MediaStateCode.PLAY_START;
        if (MediaUtils.currentSongPosition == 0) {
            MediaUtils.currentSongPosition = SongModel.getInstance().getSongListSize() - 1;
        } else {
            MediaUtils.currentSongPosition--;
        }

        //被观察者发生变化->播放位置发生改变
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
    }
    //下一曲
    public static void next(){
        MediaUtils.currentState = MediaStateCode.PLAY_START;

        if (MediaUtils.currentSongPosition == SongModel.getInstance().getSongListSize() - 1) {
            MediaUtils.currentSongPosition = 0;
        } else {
            switch (currentLoopMode) {
                case MediaStateCode.LOOP_MODE_ONLY_ONE:
                    /*单曲循环*/
                    break;
                case MediaStateCode.LOOP_MODE_ORDER_LIST:
                    /*循环循环*/
                    MediaUtils.currentSongPosition++;
                    break;
                case MediaStateCode.LOOP_MODE_OUT_OF_ORDER:
                    /*随机循环*/
                    Random r = new Random();
                    MediaUtils.currentSongPosition = r.nextInt(
                            SongModel.getInstance().getSongList().size() - 1);
                    break;
            }

        }

        //被观察者发生变化->播放位置发生改变
        MusicPositionObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
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

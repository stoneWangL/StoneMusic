package com.stone.stonemusic.utils.playControl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.code.PlayType;
import java.util.Random;

/*问题播放结束时没有监听，修改播放器的状态*/
public class MediaUtils implements PlayType {
    private static final String TAG = "MediaUtils";

    //当前播放歌曲postion
    public static int currentSongPosition = 0;
    //当前播放状态,初始化默认为stop
    public static int currentState = MediaStateCode.PLAY_STOP;
    //记录seekBar是否改变
    public static boolean seekBarIsChanging = false;
    //当前循环播放模式，默认列表循环
    public static int currentLoopMode = MediaStateCode.LOOP_MODE_ORDER_LIST;

    private static volatile MediaPlayer sMediaPlayer;

    public static MediaPlayer getMediaPlayer() {
        //避免不必要的同步
        if (sMediaPlayer == null) {
            //同步
            synchronized (MediaUtils.class) {
                if (sMediaPlayer == null) {
                    sMediaPlayer = new MediaPlayer();
                }
            }
        }
        return sMediaPlayer;
    }

    //设置MediaPlayer为空
    private static void setMediaPlayerNew(){
        sMediaPlayer = null;
    }

    //准备
    public static void prepare(String path) {
        if (getMediaPlayer() != null) {

            try {

                getMediaPlayer().reset();
                getMediaPlayer().setDataSource(path);
                getMediaPlayer().prepare();
            }catch (Exception e) {
                Log.e(TAG, "prepare()->catch (Exception e)");
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
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_START);
        //被观察者发生变化->播放位置发生改变
//        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
    }

    //暂停
    public static void pause() {
        if (getMediaPlayer() != null && getMediaPlayer().isPlaying()) {
            getMediaPlayer().pause();
            MediaUtils.currentState = MediaStateCode.PLAY_PAUSE;
        }

        //被观察者发生变化->PLAY_PAUSE
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_PAUSE);
    }

    //继续播放
    public static void continuePlay() {
        if (getMediaPlayer() != null && !getMediaPlayer().isPlaying()) {
            getMediaPlayer().start();
            MediaUtils.currentState = MediaStateCode.PLAY_CONTINUE;
        }

        //被观察者发生变化->PLAY_CONTINUE
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_CONTINUE);
    }

    //停止播放
    public static void stop() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().stop();
            MediaUtils.currentState = MediaStateCode.PLAY_STOP;
        }

        //被观察者发生变化->PLAY_STOP
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.PLAY_STOP);
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
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
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
                            SongModel.getInstance().getChooseSongList().size() - 1);
                    break;
            }

        }

        //被观察者发生变化->播放位置发生改变
        MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
    }

    //释放Media Player资源
    public static void release() {
        if (sMediaPlayer != null) {
            currentState = MediaStateCode.PLAY_STOP;
            sMediaPlayer.stop();
            sMediaPlayer.reset();
            sMediaPlayer.release();
            sMediaPlayer = null; //懒汉线程不安全需要
        }
    }

    /**
     * 监听是否有其他音乐开始播放或者停止
     */
    public class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    }

}

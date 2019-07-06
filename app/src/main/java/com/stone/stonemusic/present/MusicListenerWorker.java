package com.stone.stonemusic.present;

import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;

/**
 * author : stoneWang
 * date   : 2019/7/611:40
 * 音乐播放监听器
 */
public class MusicListenerWorker implements Runnable {
    private int nowMediaNum = 0, listMediaNum = 1024, resultNum = 100, listSize, outOfOrderNum;

    private MusicService musicService;

    public MusicListenerWorker(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void run() {
        /*死循环，监听音乐状态*/
        for (;;) {
            try {
                Thread.sleep(1000); /*每1000毫秒更新一次*/
                nowMediaNum = MediaUtils.getMediaPlayer().getCurrentPosition();
                listMediaNum = (int) musicService.musicList.get(MediaUtils.currentSongPosition).getDuration();
                resultNum = nowMediaNum - listMediaNum;
//                    Log.d(TAG, "播放器状态：" + MediaUtils.currentState +
//                    "//seekBar正在改变：" + MediaUtils.seekBarIsChanging +
//                    "//播放器中音乐长度：" + nowMediaNum +
//                    "//当前音乐长度：" + listMediaNum +
//                    "resultNum == " + resultNum);


                /*手离开了seekBar 而且 音乐播放完了（由于获取的数值有一定的差异，所以允许+-10bite的数值差异）*/
                if (!MediaUtils.seekBarIsChanging && (resultNum <= 1024 && resultNum >= -1024)) {
                    /*下一曲*/
                    PlayControl.controlBtnNext();
                    //使用观察者管理类通知，音乐源已改变需要更新
                    MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
                }

                //使用观察者管理类通知，seekBar位置需要更新
                MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_SEEKBAR_CHANGED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

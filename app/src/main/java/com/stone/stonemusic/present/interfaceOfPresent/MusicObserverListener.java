package com.stone.stonemusic.present.interfaceOfPresent;

/**
 * author : stoneWang
 * date   : 2019/4/38:58
 * 观察者接口
 */
public interface MusicObserverListener {

    /**
     * 传输MediaStateCode的值
     * @param content MediaStateCode
     *                MediaStateCode.MUSIC_POSITION_CHANGED 音乐播放位置改变
     *                MediaStateCode.MUSIC_SEEKBAR_CHANGED 音乐SeekBar变化
     */
    void observerUpData(int content);//刷新操作

}

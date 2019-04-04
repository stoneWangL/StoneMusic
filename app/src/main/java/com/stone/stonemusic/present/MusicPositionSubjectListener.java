package com.stone.stonemusic.present;

/**
 * author : stoneWang
 * date   : 2019/4/38:59
 * 被观察者接口
 */
public interface MusicPositionSubjectListener {

    void add(MusicPositionObserverListener observerListener);

    void notifyObserver(int content);

    void remove(MusicPositionObserverListener observerListener);

}

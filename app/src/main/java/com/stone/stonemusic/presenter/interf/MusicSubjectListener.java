package com.stone.stonemusic.presenter.interf;

/**
 * author : stoneWang
 * date   : 2019/4/38:59
 * 被观察者接口
 */
public interface MusicSubjectListener {

    void add(MusicObserverListener observerListener);

    void notifyObserver(int content);

    void remove(MusicObserverListener observerListener);

}

package com.stone.stonemusic.presenter.impl;

import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.presenter.interf.MusicSubjectListener;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author : stoneWang
 * date   : 2019/4/39:01
 * 观察者管理类
 */
public class MusicObserverManager implements MusicSubjectListener {
    private static String TAG = "MusicPositionObserverManager";
    private static MusicObserverManager observerManager = null;

    //观察者接口集合(使用CopyOnWriteArrayList而不是List是为了线程安全)
    private CopyOnWriteArrayList<MusicObserverListener> list = new CopyOnWriteArrayList<>();

    /**
     * 静态内部类单例
     * @return 观察者管理类的实例
     */
    public static MusicObserverManager getInstance() {
        return SingletonHolder.observerManager;
    }

    private static class SingletonHolder {
        private static final MusicObserverManager observerManager = new MusicObserverManager();
    }



    /**
     * 加入监听队列
     * @param observerListener 监听者对象
     */
    @Override
    public void add(MusicObserverListener observerListener) {
        list.add(observerListener);
    }

    /**
     * 通知观察者刷新数据
     * @param content 通知观察者的信号量
     */
    @Override
    public void notifyObserver(int content) {
        for (MusicObserverListener observerListener : list) {
            observerListener.observerUpData(content);
        }
    }

    /**
     * 监听队列中移除
     * @param observerListener 观察者对象
     */
    @Override
    public void remove(MusicObserverListener observerListener) {
        if (list.contains(observerListener)) {
            list.remove(observerListener);
        }
    }
}

package com.stone.stonemusic.present;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author : stoneWang
 * date   : 2019/4/39:01
 * 观察者管理类
 */
public class MusicPositionObserverManager implements MusicPositionSubjectListener {
    private static String TAG = "MusicPositionObserverManager";
    private static MusicPositionObserverManager observerManager = null;

    //观察者接口集合(使用CopyOnWriteArrayList而不是List是为了线程安全)
    private CopyOnWriteArrayList<MusicPositionObserverListener> list = new CopyOnWriteArrayList<>();

    /**
     * 静态内部类单例
     * @return 观察者管理类的实例
     */
    public static MusicPositionObserverManager getInstance() {
        return SingletonHolder.observerManager;
    }

    private static class SingletonHolder {
        private static final MusicPositionObserverManager observerManager = new MusicPositionObserverManager();
    }



    /**
     * 加入监听队列
     * @param observerListener
     */
    @Override
    public void add(MusicPositionObserverListener observerListener) {
        list.add(observerListener);
    }

    /**
     * 通知观察者刷新数据
     * @param content
     */
    @Override
    public void notifyObserver(int content) {
        for (MusicPositionObserverListener observerListener : list) {
            observerListener.observerUpData(content);
        }
    }

    /**
     * 监听队列中移除
     * @param observerListener
     */
    @Override
    public void remove(MusicPositionObserverListener observerListener) {
        if (list.contains(observerListener)) {
            list.remove(observerListener);
        }
    }
}

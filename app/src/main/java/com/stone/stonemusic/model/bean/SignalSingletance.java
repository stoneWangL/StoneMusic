package com.stone.stonemusic.model.bean;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 17:53
 * @Description: 信号量单例，线程安全，不延迟加载
 */
public class SignalSingletance {
    //歌单界面，是否正在加载更多，信号量
    private boolean canLoadMore = true;
    private static final SignalSingletance mInstance = new SignalSingletance();

    private SignalSingletance() {}

    public static SignalSingletance getInstance() {
        return mInstance;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}

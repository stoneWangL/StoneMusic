package com.stone.stonemusic.presenter.interf;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 8:52
 * @Description:
 */
public interface GeDanPresenter {
    int TYPE_INIT_OR_REFRESH = 1;
    int TYPE_LOAD_MORE = 2;

    //初始化或者刷新
    void loadDatas();

    //加载更多
    void loadMore(int offset);
}

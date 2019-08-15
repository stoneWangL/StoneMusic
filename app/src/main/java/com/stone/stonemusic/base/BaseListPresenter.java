package com.stone.stonemusic.base;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/9 22:40
 * @Description: 所有下拉刷新和上拉加载更多列表界面presenter的基类
 */
public interface BaseListPresenter {
    int TYPE_INIT_OR_REFRESH = 1;
    int TYPE_LOAD_MORE = 2;

    //初始化或者刷新
    void loadDatas(String cat);

    //加载更多
    void loadMore(int offset, String cat);

    //解绑presenter和View
    void destoryView();
}

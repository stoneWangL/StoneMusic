package com.stone.stonemusic.base;

import com.stone.stonemusic.model.PlayListBean;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/9 22:30
 * @Description: 所有下拉刷新和上拉加载更多列表界面的view的基类
 * 不知道具体类型，所以使用泛型
 */
public interface BaseView<RESPONSE> {
    /**
     * 获取数据失败
     */
    void onError(String message);

    /**
     * 初始化数据或者刷新数据成功
     */
    void loadSuccess(RESPONSE response);

    /**
     * 加载更多成功
     */
    void loadMore(RESPONSE response);
}

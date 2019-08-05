package com.stone.stonemusic.View

import com.itheima.player.model.bean.YueDanBean

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:47
 * @Description:
 */
interface YueDanView {
    /**
     * 获取数据失败
     */
    fun onError(message: String?)

    /**
     * 初始化数据或者刷新数据成功
     */
    fun loadSuccess(response: YueDanBean?)

    /**
     * 加载更多成功
     */
    fun loadMore(response: YueDanBean?)
}
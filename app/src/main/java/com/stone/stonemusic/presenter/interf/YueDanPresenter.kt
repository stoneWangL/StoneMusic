package com.stone.stonemusic.presenter.interf

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:50
 * @Description:
 */
interface YueDanPresenter {
    /**
     * 伴生对象
     * 和java不同，java在接口里面可以定义常量，但是在kotlin中如果要写，则需要写在伴生对象中
     */
    companion object{
        val TYPE_INIT_OR_REFRESH = 1 //初始化或者刷新
        val TYPE_LOAD_MORE = 2 //加载更多
    }

    fun loadDatas() //初始化或者刷新
    fun loadMore(offset: Int) //加载更多
}
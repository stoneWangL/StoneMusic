package com.stone.stonemusic.presenter.impl

import com.itheima.player.model.bean.YueDanBean
import com.stone.stonemusic.View.YueDanView
import com.stone.stonemusic.net.ResponseHandler
import com.stone.stonemusic.net.YueDanRequest
import com.stone.stonemusic.presenter.interf.YueDanPresenter

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:50
 * @Description:
 */
class YueDanPresenterImpl(var yueDanView: YueDanView): YueDanPresenter, ResponseHandler<YueDanBean> {
    override fun onError(type: Int, msg: String?) {
        yueDanView.onError(msg)
    }

    override fun onSuccess(type: Int, result: YueDanBean) {
        if (type == YueDanPresenter.TYPE_INIT_OR_REFRESH) {
            yueDanView.loadMore(result)
        }
    }

    override fun loadDatas() {
        YueDanRequest(YueDanPresenter.TYPE_INIT_OR_REFRESH, 0, this).excute()
    }

    override fun loadMore(offset: Int) {

    }
}
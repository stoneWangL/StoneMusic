package com.stone.stonemusic.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.itheima.player.model.bean.YueDanBean
import com.stone.stonemusic.R
import com.stone.stonemusic.View.YueDanView
import com.stone.stonemusic.adapter.YueDanAdapter
import com.stone.stonemusic.base.BaseFragment
import com.stone.stonemusic.presenter.impl.YueDanPresenterImpl
import org.jetbrains.anko.support.v4.find

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 22:18
 * @Description:
 */
class YueDanFragment: BaseFragment(), YueDanView {
    val adapter by lazy { YueDanAdapter() }
    val presenter by lazy { YueDanPresenterImpl(this) }

    override fun onError(message: String?) {
        myToast("加载数据失败")
    }

    override fun loadSuccess(response: YueDanBean?) {
        //刷新adapter
        adapter.upDateList(response?.playLists)
    }

    override fun loadMore(response: YueDanBean?) {

    }


    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_list, null)
    }

    override fun initListener() {
        val recycleView = find<RecyclerView>(R.id.recycle_view)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = adapter
    }

    override fun initData() {
        //加载数据
        presenter.loadDatas()
    }
}
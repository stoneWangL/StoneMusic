package com.stone.stonemusic.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stone.stonemusic.R
import com.stone.stonemusic.base.BaseFragment
import org.jetbrains.anko.support.v4.find

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 22:18
 * @Description:
 */
class YueDanFragment: BaseFragment() {
    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_list, null)
    }

    override fun initListener() {
        val recycleView = find<RecyclerView>(R.id.recycle_view)
        recycleView.layoutManager = LinearLayoutManager(context)

    }
}
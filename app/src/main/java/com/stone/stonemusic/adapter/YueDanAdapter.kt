package com.stone.stonemusic.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.itheima.player.model.bean.YueDanBean
import com.stone.stonemusic.widget.YuedanItemView
import java.util.ArrayList

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:08
 * @Description: 悦单界面适配器
 */
class YueDanAdapter: RecyclerView.Adapter<YueDanAdapter.YueDanHolder>() {
    private var list = ArrayList<YueDanBean.PlayListsBean>()
    /**
     * 更新列表
     */
    fun upDateList(list:List<YueDanBean.PlayListsBean>?) {
        list?.let {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YueDanHolder {
        return YueDanHolder(YuedanItemView(parent?.context))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: YueDanHolder?, position: Int) {

    }

    class YueDanHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {

    }
}
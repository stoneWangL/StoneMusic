package com.stone.stonemusic.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.widget.GeDanItemView;
import com.stone.stonemusic.widget.LoadMoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 9:32
 * @Description:
 */
public class GeDanAdapter extends RecyclerView.Adapter<GeDanAdapter.ViewHolder>{

    private static final String TAG = "GeDanAdapter";
    private List<PlayListBean> list = new ArrayList<>();


    /**
     * 更新列表
     */
    public void upDateList(List<PlayListBean> listBeans) {
        if (null != list){
            this.list.clear();
            this.list.addAll(listBeans);
            notifyDataSetChanged();
            Log.i(TAG, "upDateList->list.size" + list.size());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            //最后一条
            itemView= new LoadMoreView(parent.getContext());
        } else {
            //普通条目
            itemView = new GeDanItemView(parent.getContext());
        }
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //如果是最后一条 不需要进行刷新view
        if (position == list.size()) return;

        //条目数据
        PlayListBean data = list.get(position);
        //条目view
        GeDanItemView itemView = (GeDanItemView)holder.itemView;//转为HomeItemView类型
        //条目刷新
        itemView.setData(data);
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            //最后一条
            return 1;
        } else {
            //普通条目
            return 0;
        }
    }
}

package com.stone.stonemusic.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stone.stonemusic.adapter.GeDanAdapter;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.widget.GeDanItemView;
import com.stone.stonemusic.widget.LoadMoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/9 23:03
 * @Description: 所有下拉刷新和上拉加载更多列表界面adapter界面adapter基类
 */
public abstract class BaseListAdapter<ITEMBEAN, ITEMVIEW extends View> extends RecyclerView.Adapter<BaseListAdapter.BaseListHolder>{

    private static final String TAG = "GeDanAdapter";
    private List<ITEMBEAN> list = new ArrayList<>();


    /**
     * 更新列表
     */
    public void upDateList(List<ITEMBEAN> listBeans) {
        if (null != list){
            this.list.clear();
            this.list.addAll(listBeans);
            notifyDataSetChanged();
            Log.i(TAG, "upDateList->list.size" + list.size());
        }
    }

    /**
     * 加载更多
     * 只需要在集合里面进行添加就可以了
     */
    public void loadMore(List<ITEMBEAN> listBeans) {
        if (null != list){
            this.list.addAll(listBeans);
            notifyDataSetChanged();
            Log.i(TAG, "loadMore->list.size" + list.size());
        }
    }

    @Override
    public BaseListAdapter.BaseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            //最后一条
            itemView= new LoadMoreView(parent.getContext());
        } else {
            //普通条目
            itemView = getItemView(parent.getContext());
        }
        return new BaseListAdapter.BaseListHolder(itemView);


    }



    @Override
    public void onBindViewHolder(BaseListAdapter.BaseListHolder holder, int position) {
        //如果是最后一条 不需要进行刷新view
        if (position == list.size()) return;

        //条目数据
        ITEMBEAN data = list.get(position);
        //条目view
        ITEMVIEW itemView = (ITEMVIEW)holder.itemView;//转为HomeItemView类型
        //条目刷新
//        itemView.setData(data);
        refreshItemView(itemView, data);
    }



    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class BaseListHolder extends RecyclerView.ViewHolder{

        public BaseListHolder(View itemView) {
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

    /**
     * 刷新条目view
     * @param itemView
     * @param data
     */
    public abstract void refreshItemView(ITEMVIEW itemView, ITEMBEAN data);


    /**
     * 获取条目的view
     * @param context
     * @return
     */
    protected abstract ITEMVIEW getItemView(Context context);
}

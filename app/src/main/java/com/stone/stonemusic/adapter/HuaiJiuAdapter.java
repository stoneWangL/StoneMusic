package com.stone.stonemusic.adapter;

import android.content.Context;

import com.stone.stonemusic.base.BaseListAdapter;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.widget.GeDanItemView;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 18:22
 * @Description:
 */
public class HuaiJiuAdapter extends BaseListAdapter<PlayListBean, GeDanItemView> {
    @Override
    public void refreshItemView(GeDanItemView itemView, PlayListBean data) {
        itemView.setData(data);
    }

    @Override
    protected GeDanItemView getItemView(Context context) {
        return new GeDanItemView(context);
    }
}

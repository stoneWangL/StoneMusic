package com.stone.stonemusic.ui.fragment;

import com.stone.stonemusic.adapter.GeDanAdapter;
import com.stone.stonemusic.base.BaseListAdapter;
import com.stone.stonemusic.base.BaseListFragment;
import com.stone.stonemusic.base.BaseListPresenter;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.presenter.impl.GeDanPresenterImpl;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 8:39
 * @Description:
 * <List<PlayListBean>, GeDanItemBean, GeDanItemView>
 */
public class GeDanFragment extends BaseListFragment {
    private static final String TAG = "GeDanFragment";
    private GeDanAdapter adapter;
    private GeDanPresenterImpl geDanPresenter;


    @Override
    protected void initData() {
        //加载数据
        geDanPresenter.loadDatas();
    }

    @Override
    public BaseListAdapter getSpecialAdapter() {
        if (null == adapter)
            adapter = new GeDanAdapter();
        return adapter;
    }

    @Override
    public BaseListPresenter getSpecialPresenter() {
        if (null == geDanPresenter)
            geDanPresenter = new GeDanPresenterImpl(this);
        return geDanPresenter;
    }

    @Override
    public List getList(Object o) {
        List<PlayListBean> response = (List<PlayListBean>)o;
        return response;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解绑presenter
        geDanPresenter.destoryView();
    }
}

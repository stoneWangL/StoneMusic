package com.stone.stonemusic.ui.fragment;

import com.stone.stonemusic.adapter.HuaiJiuAdapter;
import com.stone.stonemusic.base.BaseListAdapter;
import com.stone.stonemusic.base.BaseListFragment;
import com.stone.stonemusic.base.BaseListPresenter;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.presenter.impl.HuaiJiuPresenterImpl;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 11:29
 * @Description:
 */
public class StyleHuaiJiuFragment extends BaseListFragment {
    private static final String TAG = "StyleHuaiJiuFragment";
    private HuaiJiuAdapter adapter;
    private HuaiJiuPresenterImpl huaiJiuPresenter;

    @Override
    protected void initData() {
        //加载数据
        huaiJiuPresenter.loadDatas();
    }

    @Override
    public List getList(Object o) {
        List<PlayListBean> response = (List<PlayListBean>)o;
        return response;
    }

    @Override
    public BaseListPresenter getSpecialPresenter() {
        if (null == huaiJiuPresenter)
            huaiJiuPresenter = new HuaiJiuPresenterImpl(this);
        return huaiJiuPresenter;
    }

    @Override
    public BaseListAdapter getSpecialAdapter() {
        if (null == adapter)
            adapter = new HuaiJiuAdapter();
        return adapter;
    }
}

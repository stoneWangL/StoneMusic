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
public abstract class GeDanFragment extends BaseListFragment {
    private static final String TAG = "GeDanFragment";
    private GeDanAdapter adapter;
    protected GeDanPresenterImpl geDanPresenter;

    /**
     * 供具体的实现Fragment，返回对应主题
     * @return 返回主题名
     */
    protected abstract String SetStyleName();


    /**
     * 实现抽象方法 PresenterLoadDatas()
     * ->geDanPresenter.loadDatas("主题名")
     */
    @Override
    protected void PresenterLoadDatas() {
        geDanPresenter.loadDatas(SetStyleName());
    }

    /**
     * 实现抽象方法 presenterLoadMore()
     * ->geDanPresenter.loadMore(偏移位, "主题名");
     */
    @Override
    protected void presenterLoadMore(int offset) {
        geDanPresenter.loadMore(offset, SetStyleName());
    }

    @Override
    protected void initData(){
        //加载数据
        PresenterLoadDatas();
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

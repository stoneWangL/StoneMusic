package com.stone.stonemusic.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.GedanView;
import com.stone.stonemusic.adapter.GeDanAdapter;
import com.stone.stonemusic.base.BaseFragment;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.presenter.impl.GeDanPresenterImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 8:39
 * @Description:
 */
public class GeDanFragment extends BaseFragment implements GedanView {
    private View thisView;
    private RecyclerView recyclerView;
    private GeDanAdapter adapter;
    private GeDanPresenterImpl geDanPresenter = new GeDanPresenterImpl(this);
    @Nullable
    @Override
    public View initView() {
        thisView = View.inflate(getContext(), R.layout.fragment_list, null);
        return thisView;
    }


    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    @Override
    protected void initListener() {
        if (null != getContext()){
            recyclerView = thisView.findViewById(R.id.recycle_view);
            adapter = new GeDanAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void initData() {
        //加载数据
        geDanPresenter.loadDatas();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void loadSuccess(List<PlayListBean> response) {
        //刷新adapter
        adapter.upDateList(response);
    }

    @Override
    public void loadMore(List<PlayListBean> response) {

    }
}

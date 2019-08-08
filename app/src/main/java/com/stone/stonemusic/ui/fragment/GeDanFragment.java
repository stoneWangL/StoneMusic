package com.stone.stonemusic.ui.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.GedanView;
import com.stone.stonemusic.adapter.GeDanAdapter;
import com.stone.stonemusic.base.BaseFragment;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.model.bean.SignalSingletance;
import com.stone.stonemusic.presenter.impl.GeDanPresenterImpl;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.ThreadUtil2;
import com.stone.stonemusic.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 8:39
 * @Description:
 */
public class GeDanFragment extends BaseFragment implements GedanView {
    private static final String TAG = "GeDanFragment";
    private View thisView;
    private RecyclerView recyclerView;
    private GeDanAdapter adapter;
    private GeDanPresenterImpl geDanPresenter = new GeDanPresenterImpl(this);
    private SwipeRefreshLayout refreshLayout;
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

            refreshLayout = thisView.findViewById(R.id.refreshLayout);
            refreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
            //刷新监听
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    geDanPresenter.loadDatas(); //加载数据
                }
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    //空闲状态
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            LinearLayoutManager manager = (LinearLayoutManager)layoutManager;
                            int lastPosition = manager.findLastVisibleItemPosition();
                            if (lastPosition == adapter.getItemCount() - 1
                                    && SignalSingletance.getInstance().isCanLoadMore()) {
                                SignalSingletance.getInstance().setCanLoadMore(false); //正在请求加载更多，设置不能请求
                                //最后一条已经显示了
                                geDanPresenter.loadMore(adapter.getItemCount() - 1);
                            }
                        }
                    }
                }
            });

        }


    }

    @Override
    protected void initData() {
        //加载数据
        geDanPresenter.loadDatas();
    }

    @Override
    public void onError(String message) {
        Log.i(TAG, "onError->加载数据失败");
        new ThreadUtil2().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getToastShort("加载数据失败");
            }
        });
        //隐藏刷新控件
        refreshLayout.setRefreshing(false);


    }

    @Override
    public void loadSuccess(List<PlayListBean> response) {
        //隐藏刷新控件
        refreshLayout.setRefreshing(false);
        //刷新adapter
        adapter.upDateList(response);
    }

    @Override
    public void loadMore(List<PlayListBean> response) {
        adapter.loadMore(response);
        SignalSingletance.getInstance().setCanLoadMore(true); //加载完毕，设置可以请求
    }
}

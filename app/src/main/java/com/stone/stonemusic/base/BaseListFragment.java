package com.stone.stonemusic.base;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.bean.SignalSingletance;
import com.stone.stonemusic.utils.ThreadUtil2;
import com.stone.stonemusic.utils.ToastUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/9 22:24
 * @Description: 所有具有下拉刷新和上拉加载更多列表界面的基类
 *
 * GedanView -> BaseView
 * GeDanAdapter -> BaseListAdapter
 * GeDanPresenterImpl -> BaseListPresenter
 */
public abstract class BaseListFragment<RESPONSE, ITEMBEAN, ITEMVIEW extends View>
        extends BaseFragment
        implements BaseView<RESPONSE> {
    private static final String TAG = "GeDanFragment";
    private View thisView;
    private RecyclerView recyclerView;
    private BaseListAdapter adapter;
    private BaseListPresenter geDanPresenter = getSpecialPresenter();
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

    //让子类去实现
    protected abstract void PresenterLoadDatas();
    //让子类去实现
    protected abstract void presenterLoadMore(int offset);

    @Override
    protected void initListener() {
        if (null != getContext()) {
            recyclerView = thisView.findViewById(R.id.recycle_view);
            adapter = getSpecialAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            refreshLayout = thisView.findViewById(R.id.refreshLayout);
            refreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
            //刷新监听
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //加载数据
//                    geDanPresenter.loadDatas();
                    PresenterLoadDatas();
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
//                                geDanPresenter.loadMore(adapter.getItemCount() - 1);
                                presenterLoadMore(adapter.getItemCount() - 1);
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onError(String message) {
        Log.i(TAG, "onError->加载数据失败");
        new ThreadUtil2().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getToastShort("加载数据失败");
                //隐藏刷新控件
                refreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void loadSuccess(RESPONSE response) {
        new ThreadUtil2().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                //隐藏刷新控件
                refreshLayout.setRefreshing(false);
            }
        });
        //刷新adapter
        adapter.upDateList(getList(response));
    }

    @Override
    public void loadMore(RESPONSE response) {
        adapter.loadMore(getList(response));
        SignalSingletance.getInstance().setCanLoadMore(true); //加载完毕，设置可以请求
    }

    /**
     * 从返回结果中获取列表数据集合
     * @param response
     * @return
     */
    public abstract List getList(RESPONSE response);

    /**
     * 获取适配器adapter
     * @param <ITEMBEAN>
     * @param <ITEMVIEW>
     * @return
     */
    public abstract <ITEMBEAN, ITEMVIEW extends View> BaseListAdapter getSpecialAdapter();

    /**
     * 获取Presenter
     * @return
     */
    public abstract BaseListPresenter getSpecialPresenter();
}

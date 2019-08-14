package com.stone.stonemusic.presenter.impl;

import android.util.Log;

import com.stone.stonemusic.base.BaseView;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.presenter.interf.HuaiJiuPresenter;
import com.stone.stonemusic.utils.ThreadUtil2;
import com.stone.stonemusic.utils.URLProviderUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 18:28
 * @Description:
 */
public class HuaiJiuPresenterImpl implements HuaiJiuPresenter {
    private static final String TAG = "GeDanPresenterImpl";
    private BaseView huaiJiuView;

    public HuaiJiuPresenterImpl(BaseView<List<PlayListBean>> huaiJiuView) {
        this.huaiJiuView = huaiJiuView;
    }

    @Override
    public void loadDatas() {
        String path = URLProviderUtils.getRecommendAll(0,20, "怀旧");
        Log.i(TAG, "loadDatas->path = " + path);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * 子线程调用
             */
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "loadDatas -> 获取数据失败");
            }

            /**
             * 子线程调用
             */
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "loadDatas -> 获取数据成功");
                String result = response.body().string();
                Log.i(TAG, "result = " + result);

                final List<PlayListBean> list = JsonToResult.getYueDanBean2FromJson(result);

                new ThreadUtil2().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        //将正确的结果回调给view层
                        if(null != huaiJiuView)
                            huaiJiuView.loadSuccess(list);
                    }
                });
            }
        });
    }

    @Override
    public void loadMore(int offset) {
        String path = URLProviderUtils.getRecommendAll(offset,20, "怀旧");
        Log.i(TAG, "loadMore->path = " + path);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * 子线程调用
             */
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                huaiJiuView.onError(e.getMessage());
            }

            /**
             * 子线程调用
             */
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "loadMore -> 获取数据成功");
                String result = response.body().string();
                Log.i(TAG, "loadMore -> result = " + result);

                final List<PlayListBean> list = JsonToResult.getYueDanBean2FromJson(result);

                new ThreadUtil2().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        //将正确的结果回调给view层
                        if(null != huaiJiuView)
                            huaiJiuView.loadMore(list);
                    }
                });
            }
        });
    }

    @Override
    public void destoryView() {

    }
}

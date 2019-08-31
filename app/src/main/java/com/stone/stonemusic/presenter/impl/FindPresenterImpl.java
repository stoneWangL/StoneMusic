package com.stone.stonemusic.presenter.impl;

import android.util.Log;
import android.view.View;

import com.stone.stonemusic.View.FindView;
import com.stone.stonemusic.View.OnLineView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ThreadPoolBean;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.presenter.interf.FindPresenter;
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
 * @CreateDate: 2019/8/31 19:07
 * @Description:
 */
public class FindPresenterImpl implements FindPresenter {
    private static String TAG = "OnLineListPresenterImpl";
    private FindView findView;

    public FindPresenterImpl(FindView findView) {
        this.findView = findView;
    }


    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFalse() {

    }


    @Override
    public void clickFindBtn(String findStr) {

        if (findStr.equals("")) {
            //反馈查询结果为空
            findView.feedBackResult(0);
        } else {
            //构建查询url
            final String QueryPath = URLProviderUtils.findByKeyWord(findStr, 1, 30, 0);
            Log.d(TAG, "QueryPath=" + QueryPath);
            //使用线程池，子线程查询
            ThreadPoolBean.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(QueryPath)
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        /**
                         * 子线程调用
                         */
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i(TAG, "搜索关键词 -> 获取数据失败");
                            findView.feedBackResult(1);
                        }

                        /**
                         * 子线程调用
                         */
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.i(TAG, "搜索关键词 -> 获取数据成功");

                            String result = response.body().string();
                            Log.i(TAG, "result = " + result);

                            final List<Music> musicList = JsonToResult.getFindResultFromJson(result);
                            if (null == musicList) {
                                //没有查到
                                findView.feedBackResult(1);
                            } else {
                                //已经查到
                                findView.feedBackResult(2);
                                findView.notifyMusicList(musicList);
                            }

                        }
                    });
                }
            });
        }
    }
}

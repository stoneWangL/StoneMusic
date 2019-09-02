package com.stone.stonemusic.presenter.impl;

import android.util.Log;

import com.stone.stonemusic.View.PlayFatherView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ThreadPoolBean;
import com.stone.stonemusic.net.DownLoadLrcFile;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.presenter.interf.PlayFatherPresenter;
import com.stone.stonemusic.utils.URLProviderUtils;
import com.stone.stonemusic.utils.code.MediaStateCode;
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
 * @CreateDate: 2019/9/2 1:43
 * @Description:
 */
public class PlayFatherPresenterImpl implements PlayFatherPresenter {
    private static String TAG = "PlayFatherPresenterImpl";
    private PlayFatherView playFatherView;

    public PlayFatherPresenterImpl(PlayFatherView playFatherView) {
        this.playFatherView = playFatherView;
    }

    @Override
    public void onLrcItemClick(Music music) {

    }


    /**
     * 从网络获取歌词
     * @return false 没有获取到， true 获取到了。
     */
    public boolean getLrcOnline(final Music song, final Music desMusic) {

        if(null != song) {
            String id = song.getMusicId();
            if (!id.equals("")) {
                final String QueryPath = URLProviderUtils.findLrc(id); //网络歌词查询URL
                Log.i(TAG, "getLrcOnline -> QueryPath = " + QueryPath);

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
                                Log.i(TAG, "查询歌词 -> 获取数据失败");
                            }

                            /**
                             * 子线程调用
                             */
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                Log.i(TAG, "查询歌词 -> 获取数据成功");
                                String result = response.body().string();
                                Log.i(TAG, "歌词result = " + result);

                                final String lrcString = JsonToResult.getOnlineLyricFromJson(result);
                                //将LrcString保存为文件
                                Boolean DownloadLrcResult = DownLoadLrcFile.getInstance().
                                        writeLrcFromStringToFile(lrcString, desMusic.getTitle(), desMusic.getArtist());
                                if (DownloadLrcResult) {
                                    //使用观察者管理类通知，音乐源已改变需要更新
                                    MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_LRC_CHANGED);
                                    playFatherView.DownloadLrcSuccess(); //让V层实现
                                } else {
                                    playFatherView.DownloadLrcFail(); //让V层实现

                                }
                            }
                        });
                    }
                });
            } else {
                Log.i(TAG, "没有musicID");
            }
        } else {
            //本地的歌曲 && 没有musicId
        }

        return false;
    }

    /**
     * 点击歌词搜索
     */
    public void clickIconLyc(String findStr, final Music music) {

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
                        playFatherView.GetLrcListFail();
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
                            playFatherView.GetLrcListFail();
                        } else {
                            //已经查到
                            playFatherView.GetLrcListSuccess(musicList);

                        }
                    }
                });
            }
        });
    }

    @Override
    public void GetLrcListFail() {
        playFatherView.GetLrcListFail();
    }

    @Override
    public void GetLrcListSuccess(List<Music> list) {
        playFatherView.GetLrcListSuccess(list);
    }
}

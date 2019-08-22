package com.stone.stonemusic.presenter.impl;


import android.os.AsyncTask;
import android.util.Log;
import com.stone.stonemusic.View.OnLineView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.presenter.interf.OnLineListPresenter;
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
 * @CreateDate: 2019/8/20 15:52
 * @Description:
 */
public class OnLineListPresenterImpl implements OnLineListPresenter {
    private static String TAG = "OnLineListPresenterImpl";
    private OnLineView onLineView;

    public OnLineListPresenterImpl(OnLineView onLineView) {
        this.onLineView = onLineView;
    }

    @Override
    public void loadSuccess() {
        onLineView.loadSuccess();
    }

    @Override
    public void loadFalse() {
        onLineView.loadFalse();
    }

    public void startTask(String id) {
        if (null == id) return;
        new GetOnLineListTask(id).execute();
    }

    private class GetOnLineListTask extends AsyncTask<Void, Integer, Integer> {
        private static final String TAG = "GetOnLineListTask";
        String id;

        public GetOnLineListTask(String id) {
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String path = URLProviderUtils.getOnLineList(id);
            Log.i(TAG, "doInBackground->path = " + path);
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
                    Log.i(TAG, "doInBackground -> 获取数据失败");
                    loadFalse();
                }

                /**
                 * 子线程调用
                 */
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i(TAG, "doInBackground -> 获取数据成功");
                    String result = response.body().string();
                    Log.i(TAG, "result = " + result);

                    final List<Music> list = JsonToResult.getOnLineListBeanFromJson(result);
                    //如果list为空，则表示解析失败，失败原因有可能是返回code非200,也有可能其他原因
                    if (null == list) {
                        loadFalse(); //提示失败
                        return; //list为空就不继续下面的事情
                    }
                    SongModel.getInstance().setSongList(list);
//                    SongModel.getInstance().setMusicType(PlayType.OnlineType);
                    loadSuccess(); //提示成功
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}

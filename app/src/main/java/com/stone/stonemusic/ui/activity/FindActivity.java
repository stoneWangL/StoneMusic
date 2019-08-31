package com.stone.stonemusic.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;
import com.stone.stonemusic.model.bean.ThreadPoolBean;
import com.stone.stonemusic.net.DownLoadLrcFile;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.utils.URLProviderUtils;
import com.stone.stonemusic.utils.lyric.LrcUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:28
 * @Description:
 */
public class FindActivity extends BaseHaveBottomBarActivity
        implements View.OnClickListener {
    private static String TAG = "FindActivity";
    private EditText editText;
    private TextView textViewResultShow;
    private ImageView ivFind;


    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    protected void initListenerOther() {
        editText = findViewById(R.id.editText_find);
        textViewResultShow = findViewById(R.id.textView_result_show);

        ivFind = findViewById(R.id.imageView_find_search);
        ivFind.setOnClickListener(this);

    }

    @Override
    protected void initDataOther() {

    }

    @Override
    protected void observerUpDataOtherPlayStartPositionChange() {

    }

    @Override
    protected void observerUpDataOtherContinueStopPause() {

    }

    @Override
    protected void onDestroyOther() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_find_search:
                clickFindBtn(); //搜索按钮被点击
            default:
                break;
        }
    }

    /**
     * 搜索按钮被点击
     */
    private void clickFindBtn() {
        //获取需要查询的字符串
        String findStr = editText.getText().toString();

        if (findStr.equals("")) {
            //反馈查询结果为空
            feedBackResult(0);
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
                            feedBackResult(1);
                        }

                        /**
                         * 子线程调用
                         */
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.i(TAG, "搜索关键词 -> 获取数据成功");
                            feedBackResult(2);
                            String result = response.body().string();
                            Log.i(TAG, "result = " + result);

                            final String findString = JsonToResult.getOnlineLyricFromJson(result);
//                            //将LrcString保存为文件
//                            DownloadLrcResult = DownLoadLrcFile.getInstance().
//                                    writeLrcFromStringToFile(lrcString, song.getTitle(), song.getArtist());
//
//                            lrcLists = LrcUtil.loadLrcFromLocalFile(song); //加载本地歌词，获取歌词list
//                            thisFragmentHandler.post(runnableUi);
                        }
                    });
                }
            });
        }

    }

    /**
     * 反馈，查询结果为空
     * @param result 0:表示查询结果为空， 1：表示查询失败， 2：查询成功
     */
    private void feedBackResult(final int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (result) {
                    case 0:
                        textViewResultShow.setText("查询结果为空");
                        break;
                    case 1:
                        textViewResultShow.setText("查询失败，请检查网络");
                        break;
                    case 2:
                        textViewResultShow.setText("");
                        break;
                }
            }
        });
    }

    /**
     * 收到UI界面更新的通知后，在此刷新UI
     */
    private Handler ThisActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

}

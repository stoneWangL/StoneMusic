package com.stone.stonemusic.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.LrcContent;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.model.bean.ThreadPoolBean;
import com.stone.stonemusic.net.JsonToResult;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.presenter.OnLrcSearchClickListener;
import com.stone.stonemusic.View.LrcView;
import com.stone.stonemusic.utils.lyric.LrcUtil;
import com.stone.stonemusic.net.DownLoadLrcFile;
import com.stone.stonemusic.utils.URLProviderUtils;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.widget.LyricView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PlayLyricFragment extends Fragment
        implements MusicObserverListener{

    private static String TAG = "PlayLyricFragment";

    private List<Music> musicList = new ArrayList<>();
    String QueryPath = ""; //查询歌词的URL
    private static LrcView playPageLrcView;
    private static LyricView lyricView;
    private static Handler thisFragmentHandler;
    private boolean DownloadLrcResult = false;
    List<LrcContent> lrcLists = null;
    private Music songCopy = null;
    private boolean hasLyric = false; //是否有歌词
    private boolean waitFirstClick = true; //还没有点击屏幕请求

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        Log.i(TAG, "回调 onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i(TAG, "回调 onCreate");
        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.i(TAG, "回调 onCreateView");
        View view = inflater.inflate(R.layout.fragment_play_lyric, container, false);

        //创建属于主线程的handler
        thisFragmentHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //有歌词
                    case 1:
                        Log.i(TAG, "handler->本地有歌词了。LrcState = " + playPageLrcView.getLrcState());
//                        playPageLrcView.setLrcLists(lrcLists);
//                        playPageLrcView.setLrcState(LRC_READ_LOC_OK);
                        lyricView.setLrcContentList(lrcLists);
                        break;
                    //没歌词
                    case 2:
                        Log.i(TAG, "handler->本地没有歌词。LrcState = "+ playPageLrcView.getLrcState());
//                        playPageLrcView.setLrcLists(null);
//                        playPageLrcView.setLrcState(LRC_READ_LOC_FAIL);
//                        lyricView.setLrcContentList(null);

                        break;
                    case 3:

                        break;
                    default:
                        break;
                }
            }
        };

        playPageLrcView = view.findViewById(R.id.playpage_lrcview);
        lyricView = view.findViewById(R.id.lyricView);
        lyricView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "开始下载歌词", Toast.LENGTH_SHORT).show();
                getLrcOnline(songCopy);
            }
        });

        init();
        return view;
    }

    private void init() {
        musicList = SongModel.getInstance().getChooseSongList(); //重新获取歌曲list
        if (null != lyricView){
            Log.i(TAG, "init()->lyricView 不为 null");

            final Music song = musicList.get(MediaUtils.currentSongPosition);
            songCopy = song;
            lrcLists = LrcUtil.loadLrcFromLocalFile(song); /*加载本地歌词，获取歌词list*/


            if (null != lrcLists && lrcLists.size() > 0) {
                //本地有歌词
                Log.i(TAG, "init()->本地有歌词");
                hasLyric = true; //有歌词
                lyricView.setSongDuration(song.getDuration());
                lyricView.setLrcContentList(lrcLists);

                thisFragmentHandler.sendEmptyMessage(1);

            } else {
                //本地没有歌词
                Log.i(TAG, "init()->本地无歌词");
                hasLyric = false; //没歌词
                thisFragmentHandler.sendEmptyMessage(2);
                //本地没有歌词，再查询网络（后期可修改为手动点击触发）
            }

            thisFragmentHandler.post(runnableUi);
        } else {
            Log.i(TAG, "init()->lyricView == null");
        }

    }


    /**
     * 从网络获取歌词
     * @return false 没有获取到， true 获取到了。
     */
    private boolean getLrcOnline(final Music song) {

        if(null != song){

            String id = song.getMusicId();
            if (!id.equals("")) {
                QueryPath = URLProviderUtils.findLrc(id); //网络歌词查询URL
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
                                DownloadLrcResult = DownLoadLrcFile.getInstance().
                                        writeLrcFromStringToFile(lrcString, song.getTitle(), song.getArtist());

                                lrcLists = LrcUtil.loadLrcFromLocalFile(song); //加载本地歌词，获取歌词list
                                thisFragmentHandler.post(runnableUi);
                            }
                        });
                    }
                });
            } else {
                Log.i(TAG, "没有musicID");
            }

        }

        return false;
    }

    /**
     * 歌词更新
     */
    Runnable runnableUi=new Runnable() {
        @Override
        public void run() {
            //更新界面
            try {
                if (null != lrcLists && lrcLists.size() > 0 && null != lyricView) {
                    Log.i(TAG, "runnableUi->lrcLists不为空，且size大于0, lyricView不为空");
                    if (DownloadLrcResult) {
                        Log.e(TAG, "歌曲下载成功，走入设置刚下载好的流程");
                        lyricView.setLrcContentList(lrcLists);
                    } else {
                        Log.e(TAG, "歌曲下载失败，DownloadLrcResult==false");
                    }
                } else {
                    Log.e(TAG, "null != lrcLists && lrcLists.size() > 0 && null != lyricView 中有一个有问题");
                }
            } catch (Exception e) {
                Log.i(TAG, "runnableUi:抛异常了");
                e.printStackTrace();
            } finally {
                DownloadLrcResult = false; //为下一次的下载，清除上次的记录
            }
        }
    };


    /**
     * MusicPosition观察者刷新数据类
     * @param content 给观察者的信号值
     */
    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.MUSIC_POSITION_CHANGED: //歌曲源改变
//                DclTimerTask.getInstance().destroyed();
//                DclTimerTask.getInstance().start();
                init();
                Log.i(TAG, "observerUpData->观察者类数据已刷新->歌曲源改变");
//                break;
//
//            case MediaStateCode.PLAY_START:
//                DclTimerTask.getInstance().destroyed();
//                DclTimerTask.getInstance().start();
//            case MediaStateCode.PLAY_CONTINUE:
//                DclTimerTask.getInstance().start();
//                break;
//
//            case MediaStateCode.PLAY_STOP:
//            case MediaStateCode.PLAY_PAUSE:
//                DclTimerTask.getInstance().destroyed();
//                break;
            default:
                if (null != lyricView){
                    lyricView.updateProgress(MediaUtils.getMediaPlayer().getCurrentPosition());
                }
                break;
        }
        Log.i(TAG, "observerUpData->观察者类数据已刷新");
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "回调 onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.i(TAG, "回调 onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i(TAG, "回调 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.i(TAG, "回调 onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i(TAG, "回调 onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.i(TAG, "回调 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i(TAG, "回调 onDestroy");
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Log.i(TAG, "回调 onDetach");
    }
}
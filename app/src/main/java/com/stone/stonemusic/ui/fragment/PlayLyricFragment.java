package com.stone.stonemusic.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.LrcContent;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.present.interfaceOfPresent.MusicObserverListener;
import com.stone.stonemusic.present.MusicObserverManager;
import com.stone.stonemusic.present.interfaceOfPresent.OnLrcSearchClickListener;
import com.stone.stonemusic.ui.View.LrcView;
import com.stone.stonemusic.utils.LrcUtil;
import com.stone.stonemusic.utils.LrcUtilOnline;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_FAIL;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_NULL;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_OK;
import static com.stone.stonemusic.data.LrcStateContants.READ_LOC_FAIL;
import static com.stone.stonemusic.data.LrcStateContants.READ_LOC_OK;

public class PlayLyricFragment extends Fragment implements OnLrcSearchClickListener
        ,MusicObserverListener ,View.OnClickListener{
    private static String TAG = "PlayLyricFragment";

    private List<Music> musicList = new ArrayList<>();

    private static LrcView playPageLrcView;
    private static Handler handler;
    private boolean DownloadLrcResult = false;
    List<LrcContent> lrcLists = null;
    private Music songCopy = null;
    private boolean hasLyric = false; //是否有歌词
    private boolean waitFirstClick = true; //还没有点击屏幕请求
    private LinearLayout linearLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "回调 onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "回调 onCreate");

        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);

    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
////                scrollY = this.getScrollY();
////                pos = (int) (this.getScrollY() / textHeight); /*当前视图顶点位置/文本高度 = 当前的歌词的下标*/
////
////                canDrawLine = true;
////                this.invalidate();
//
//                Log.i(TAG, "LrcStateView=>handleTouchLrcOK=>ACTION_DOWN && MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                /*歌词已在手指移动下，位置变化, 改变歌曲位置到手指UP后的位置*/
////                if(pos!=-1)
////                    MediaUtils.getMediaPlayer().seekTo(lrcLists.get(pos).getLrcTime());
////                canDrawLine = false;
////                pos =-1; /*手指移动状态-》没有手指移动*/
////                this.invalidate();
//                break;
//        }
//
//
//        return false;
//    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "OnClick被点击");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "回调 onCreateView");
        View view = inflater.inflate(R.layout.fragment_play_lyric, container, false);

        //创建属于主线程的handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //有歌词
                    case 1:
                        playPageLrcView.setLrcLists(lrcLists);
                        playPageLrcView.setLrcState(READ_LOC_OK);
                        break;
                    //没歌词
                    case 2:
                        playPageLrcView.setLrcState(READ_LOC_FAIL);
                        break;
                    case 3:

                        break;
                    default:
                        break;
                }
            }
        };
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_lrc_play);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "歌词Layout被点击");
            }
        });
        playPageLrcView = (LrcView) view.findViewById(R.id.playpage_lrcview);
        playPageLrcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "歌词PageLrcView被点击");
            }
        });
        init();

        view.setOnClickListener(this);

        return view;
    }

    private void init() {
        musicList = SongModel.getInstance().getSongList();
        if (null != playPageLrcView){
            playPageLrcView.setOnLrcSearchClickListener(this);
            loadLrcView();

        }

        //开启轮询监听
        //DclTimerTask.getInstance().start();
    }

    void loadLrcView() {
        final Music song = musicList.get(MediaUtils.currentSongPosition);
        songCopy = song;
        lrcLists = LrcUtil.loadLrc(song); /*加载本地歌词，获取歌词list*/

        //本地有歌词
        if (null != lrcLists && lrcLists.size() != 0) {
            hasLyric = true; //有歌词
            handler.sendEmptyMessage(1);

        }
        //本地没有歌词
        else {
            hasLyric = false; //没歌词
            handler.sendEmptyMessage(2);
            //本地没有歌词，再查询网络（后期可修改为手动点击触发）
        }

        handler.post(runnableUi);
    }

    /**
     * 歌词View回调函数
     * @param view
     */
    @Override
    public void onLrcSearchClicked(View view) {
        showLrcSearchDialog();
        getLrcOnline(songCopy);
    }

    private void showLrcSearchDialog() {
//        final Dialog dialog = new Dialog(getActivity(), R.style.lrc_dialog);
//        dialog.show();
        Toast.makeText(getActivity(), "开始下载歌词", Toast.LENGTH_SHORT).show();
//        View content = inflater.inflate(R.layout.dialog_lrc, null);
    }

    /**
     * 从网络获取歌词
     * @return false 没有获取到， true 获取到了。
     */
    private boolean getLrcOnline(final Music song) {

        if(null != song){
            new Thread(){
                public void run(){

                        //网络歌词下载地址
                        String path =  LrcUtilOnline.getInstance().getLrcURL(song.getTitle(), song.getArtist());
                        //目录+歌曲+歌手+.lrc
                        String filePath = LrcUtilOnline.getInstance().getLrcPath(song.getTitle(), song.getArtist());
                        DownloadLrcResult = LrcUtilOnline.getInstance().writeContentFromUrl(path, filePath, song.getTitle(), song.getArtist());

                    lrcLists = LrcUtil.loadLrc(song); /*加载本地歌词，获取歌词list*/
                    handler.post(runnableUi);
                }
            }.start();
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
                if (null != lrcLists && lrcLists.size() != 0 && null != playPageLrcView) {
                    if (DownloadLrcResult) {
                        Log.e(TAG, "歌曲下载成功，走入设置刚下载好的流程");
                        playPageLrcView.setLrcLists(lrcLists);
                        playPageLrcView.setLrcState(QUERY_ONLINE_OK);
                    } else {
                        Log.e(TAG, "歌曲下载成功，但是DownloadLrcResult==false");
                    }
                } else {
                    Log.e(TAG, "下载失败后，进入runnableUi分支");
                    if (null == lrcLists)
                        Log.e(TAG, "在载失败后，null == lrcLists");
                    else if (lrcLists.size() == 0) {
                        lrcLists = LrcUtil.getNullLrclist();
                        playPageLrcView.setLrcLists(lrcLists); //添加空是为了让画板能提示语
                        playPageLrcView.setLrcState(QUERY_ONLINE_NULL);
                        Log.e(TAG, "在载失败后，lrcLists.size() == 0; \n" +
                                "setLrcState = " + playPageLrcView.getLrcState());
                    }
                    else if (null == playPageLrcView)
                        Log.e(TAG, "在载失败后，null == playPageLrcView");
                    else
                        Log.e(TAG, "在载失败后，未知错误！！！");
                }
            } catch (Exception e) {
                Log.i(TAG, "runnableUi:抛异常了");
                e.printStackTrace();
            } finally {
                DownloadLrcResult = false; //为下一次的下载，清除上次的记录
            }
        }
    };




    /*更新歌词行*/
    static void updateLrcView(int currentTime) {

//        if (SlidingPanel.mTracking) return;

        int tempIndex = playPageLrcView.getIndexByLrcTime(currentTime);
        if (tempIndex != playPageLrcView.getIndex()) {
            playPageLrcView.setIndex(tempIndex);
            playPageLrcView.invalidate();
        }
    }

    /**
     * MusicPosition观察者刷新数据类
     * @param content 给观察者的信号值
     */
    @Override
    public void observerUpData(int content) {
//        switch (content) {
//            case MediaStateCode.MUSIC_POSITION_CHANGED:
//                DclTimerTask.getInstance().destroyed();
//                DclTimerTask.getInstance().start();
//                init();
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
//        }
//        Log.i(TAG, "observerUpData->观察者类数据已刷新");
    }

    /**
     * 构建Runnable对象，在runnable中更新界面
      */
    static Runnable runnableUi2=new Runnable() {
        @Override
        public void run() {
            //更新界面
            try {
                int currentTime = MediaUtils.getMediaPlayer().getCurrentPosition();
                updateLrcView(currentTime);
            } catch (Exception e) {
                Log.d(TAG, "runnableUi2:抛异常了");
                e.printStackTrace();
            }
        }
    };



    /**
     * 静态内部类
     */
//    private static class SingletonHolder {
//        private static final DclTimerTask timerTask = new DclTimerTask();
//    }

    /**
     * 内部类轮询控制器类
      */
//    public static class DclTimerTask{
//        private static String TAG = "DclTimerTask";
//        private Timer timer = null;
//
//        public static DclTimerTask getInstance() {
//            return SingletonHolder.timerTask;
//        }
//
//        public void start() {
//            //毫秒
//            long time = 1000;
//            final int[] i = {1};
//
//            if (timer == null){
//                timer = new Timer();
//            }
//
//            timer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    handler.post(runnableUi2);
//                    Log.i(TAG, "轮询第"+ i[0] + "次");
//                    i[0]++;
//
//                }
//            }, 0, time);
//        }
//
//        private void destroyed(){
//            Log.i(TAG, "轮询任务销毁");
//            if (null != timer)
//                timer.cancel();
//            timer = null;
//        }
//
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "回调 onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "回调 onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "回调 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "回调 onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "回调 onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "回调 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "回调 onDestroy");
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);
        //停止LrcUpData轮询
        //DclTimerTask.getInstance().destroyed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "回调 onDetach");
    }
}
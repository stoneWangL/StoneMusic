package com.stone.stonemusic.service;

import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.present.MusicObserverListener;
import com.stone.stonemusic.present.MusicObserverManager;
import com.stone.stonemusic.present.MusicResources;
import com.stone.stonemusic.present.PlayControl;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MyTimerTask;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MusicObserverListener{
    public static final String TAG = "MusicService";
    /*2018/12/8 stoneWang start */
    public NotificationManager mNotificationManager;
    public Notification notification;
    private RemoteViews remoteViews;
    private List<Music> musicList = new ArrayList<>();
    /*2018/12/8 stoneWang end */

    public final IBinder binder = new MyBinder();

    private Thread mLoopModeThread;
//    private ServiceTimerTask serviceTimerTask;
    private int nowMediaNum = 0, listMediaNum = 1024, resultNum = 100, listSize, outOfOrderNum;



    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"音乐服务onCreate");

        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MediaStateCode.ACTION_CLOSE);
        itFilter.addAction(MediaStateCode.ACTION_PLAY_OR_PAUSE);
        itFilter.addAction(MediaStateCode.ACTION_LAST);
        itFilter.addAction(MediaStateCode.ACTION_NEXT);
        itFilter.addAction(MediaStateCode.ACTION_LOVE);
        itFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY); //耳机拔出事件
        registerReceiver(playMusicReceiver, itFilter);

        initNotification();
        musicList = SongModel.getInstance().getSongList();

//        serviceTimerTask = new ServiceTimerTask();
//
//        //防止Service启动慢，而没有被添加到观察者队列中，而错过观察者管理类的回调，这里检查后，先关闭再开启监听任务
//        if (MediaUtils.getMediaPlayer().isPlaying()){
//            serviceTimerTask.destroyed();
//            serviceTimerTask.start(0, 1000);
//        }
        mLoopModeThread = new Thread(new LoopModeThread());
        mLoopModeThread.start(); /*启动线程*/


        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);

    }

    /**
     * PendingIntent是一种特殊的intent，设置之后并不会马上使用，而是在真正点击后只会调用。
     */
    private void setNotification(){
        Log.d(TAG, "MediaUtils.currentState == " + MediaUtils.currentState);
        if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
            /*play -》 pause*/
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_pause_black);
        }else {
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_play_black);
        }
        // 点击音乐image跳转到主界面
        Intent intentGo = new Intent(this, LocalListActivity.class);
        PendingIntent pendingIntentGo = PendingIntent.getActivity(
                this, 0, intentGo, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_album, pendingIntentGo);

        // 关闭
        Intent intentClose = new Intent(MediaStateCode.ACTION_CLOSE);
        PendingIntent pendingIntentClose = PendingIntent.getBroadcast(
                this, 0, intentClose, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_close_this, pendingIntentClose);

        // 设置上一曲
        Intent intentLast = new Intent(MediaStateCode.ACTION_LAST);
        PendingIntent pendingIntentLast = PendingIntent.getBroadcast(
                this, 0, intentLast, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_last, pendingIntentLast);

        // 设置播放&暂停
        Intent intentPlayOrPause = new Intent(MediaStateCode.ACTION_PLAY_OR_PAUSE);
        PendingIntent pendingIntentPlayOrPause =
                PendingIntent.getBroadcast(this, 0,
                        intentPlayOrPause, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause, pendingIntentPlayOrPause);

        // 下一曲
        Intent intentNext = new Intent(MediaStateCode.ACTION_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(
                this, 0, intentNext, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_next, pendingIntentNext);

        // 设置收藏
        Intent intentLove = new Intent(MediaStateCode.ACTION_LOVE);
        PendingIntent pendingIntentLove = PendingIntent.getBroadcast(
                this, 0, intentLove, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_love, pendingIntentLove);
    }

    /**
     * 初始化通知
     */
    private void initNotification() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_remote);
        initNotificationSon();
        remoteViewsHandler.sendEmptyMessage(1);
    }

    private void initNotificationSon() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MusicAppUtils.getContext());

        mBuilder.setSmallIcon(R.drawable.play_background02); // 设置顶部图标
        mBuilder.setOngoing(true);

//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = mBuilder.build();//构建通知
        setNotification();
        notification.contentView = remoteViews; // 设置下拉图标
        notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.anim_log;
//        mNotificationManager.notify(123, notification);//显示通知
        startForeground(123, notification);//启动为前台服务
    }

    public Handler remoteViewsHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Log.d(TAG, "进入MusicService --> handlerMessage start");
            Log.d(TAG, "handle position == " + msg.what);
            int position  = MediaUtils.currentSongPosition;
            Log.d(TAG, "全局播放position == " + position);
            /*专辑图片*/
            try {
                String path = MusicResources.getAlbumArt(
                        new Long(musicList.get(position).getAlbum_id()).intValue());
                Log.d(TAG,"path="+path);
                Bitmap bitmap;
                if (null == path){
                    bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.play_background02);
                }else{
                    bitmap = BitmapFactory.decodeFile(path);
                }
                remoteViews.setImageViewBitmap(R.id.notification_album, bitmap);
                bitmap = null;


                /*歌曲名称 & 歌手名*/
                remoteViews.setTextViewText(R.id.notification_title, musicList.get(position).getTitle());
                remoteViews.setTextViewText(R.id.notification_artist, musicList.get(position).getArtist());


            } catch (Exception e) {
                e.printStackTrace();
            }

            /*根据播放器状态码，设置播放暂按钮的图标*/
            Log.d(TAG, "MediaUtils.currentState == " + MediaUtils.currentState);
            if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                    MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
                remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_play_black);
            }else {
                remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_pause_black);
            }

            notification.contentView = remoteViews; // 设置下拉图标
            notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
            startForeground(123, notification);//启动为前台服务
            Log.d(TAG, "进入MusicService --> handlerMessage end");
        }
    };

    @Override
    public void observerUpData(int content) {
        Log.i(TAG, "observerUpData->观察者类数据已刷新");
        remoteViewsHandler.sendEmptyMessage(1);

//        switch (content) {
//            case MediaStateCode.PLAY_CONTINUE:
//                Log.i(TAG, "SeekBar轮询监听开始");
//                serviceTimerTask.start(0, 1000);
//                break;
//            case MediaStateCode.PLAY_START:
//            case MediaStateCode.MUSIC_POSITION_CHANGED:
//                Log.i(TAG, "SeekBar轮询监听注销");
//                serviceTimerTask.destroyed();
//                Log.i(TAG, "SeekBar轮询监听开始");
//                serviceTimerTask.start(0, 1000);
//                break;
//            case MediaStateCode.PLAY_PAUSE:
//            case MediaStateCode.PLAY_STOP:
//                Log.i(TAG, "SeekBar轮询监听注销");
//                serviceTimerTask.destroyed();
//                break;
//        }
    }

    /**
     * RemoteViews控制播放操作 && 销毁服务
     */
    private BroadcastReceiver playMusicReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (null != action && action.equals(MediaStateCode.ACTION_PLAY_OR_PAUSE)) {
                PlayControl.controlBtnPlaySameSong();
            } else if (action.equals(MediaStateCode.ACTION_LAST)) {
                PlayControl.controlBtnLast();
            } else if (action.equals(MediaStateCode.ACTION_NEXT)) {
                PlayControl.controlBtnNext();
            } else if (action.equals(MediaStateCode.ACTION_LOVE)){
                Log.d(TAG, "点击了Love");
            } else if (action.equals(MediaStateCode.ACTION_CLOSE)) {
                Log.d(TAG, "clocked Close");
                MusicAppUtils.destroyActivity("PlayActivity");
                MusicAppUtils.destroyActivity("LocalListActivity");
                stopService(new Intent(context,MusicService.class));
            } else if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                Log.i(TAG, "检测到耳机拔出");
                PlayControl.controlPauseSameSong();

            } else {
                Log.d(TAG, "未知状态229");
            }
            remoteViewsHandler.sendEmptyMessage(1);
        }

    };



    /**
     * 回调方法
     * @param intent
     * @return 返回 IBinder 对象的实例 binder , 方便Activity与Service通信
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        //获取服务
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"音乐服务onDestroy");
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);

//        serviceTimerTask = null; //seekBar轮询监听任务对象回收
        mLoopModeThread = null;

        PlayControl.AbandonAudioFocus(); //放弃播放焦点

        MediaUtils.release();
        unregisterReceiver(playMusicReceiver);
        System.exit(0);
    }

    /*seekBar进度监听线程*/
    class LoopModeThread implements Runnable {


        @Override
        public void run() {
            /*死循环，监听音乐状态*/
            for (;;) {
                try {
                    Thread.sleep(1000); /*每1000毫秒更新一次*/
                    nowMediaNum = MediaUtils.getMediaPlayer().getCurrentPosition();
                    listMediaNum = (int) musicList.get(MediaUtils.currentSongPosition).getDuration();
                    resultNum = nowMediaNum - listMediaNum;
//                    Log.d(TAG, "播放器状态：" + MediaUtils.currentState +
//                    "//seekBar正在改变：" + MediaUtils.seekBarIsChanging +
//                    "//播放器中音乐长度：" + nowMediaNum +
//                    "//当前音乐长度：" + listMediaNum +
//                    "resultNum == " + resultNum);


                    /*手离开了seekBar 而且 音乐播放完了（由于获取的数值有一定的差异，所以允许+-10bite的数值差异）*/
                    if (!MediaUtils.seekBarIsChanging && (resultNum <= 1024 && resultNum >= -1024)) {
                        /*下一曲*/
                        PlayControl.controlBtnNext();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    public class ServiceTimerTask extends MyTimerTask {
        public static final String TAG = "MusicService";

        @Override
        public void start(int delayTime, int pollTime) {
            super.start(delayTime, pollTime);
        }

        @Override
        public void doSomething() {
            super.doSomething();

            nowMediaNum = MediaUtils.getMediaPlayer().getCurrentPosition();
            listMediaNum = (int) musicList.get(MediaUtils.currentSongPosition).getDuration();
            resultNum = nowMediaNum - listMediaNum;
//            Log.d(TAG, "播放器状态：" + MediaUtils.currentState +
//            "//seekBar正在改变：" + MediaUtils.seekBarIsChanging +
//            "//播放器中音乐长度：" + nowMediaNum +
//            "//当前音乐长度：" + listMediaNum +
//            "resultNum == " + resultNum);

            /*手离开了seekBar 而且 音乐播放完了（由于获取的数值有一定的差异，所以允许+-10bite的数值差异）*/
            if (!MediaUtils.seekBarIsChanging && (resultNum <= 1024 && resultNum >= -1024)) {
                /*下一曲*/
                PlayControl.controlBtnNext();
            }

        }
    }


}

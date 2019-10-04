package com.stone.stonemusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.UI.activity.HomeActivity;
import com.stone.stonemusic.utils.LoveListSpUtil;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MusicListenerWorker;
import com.stone.stonemusic.presenter.broadcastReceiver.NotificationViewReceiver;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.playControl.PlayControl;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.MusicApplication;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MusicObserverListener{
    public static final String TAG = "MusicService";


    public Notification notification;
    public NotificationManager mNotificationManager;
    private RemoteViews remoteViews;
    public List<Music> musicList = new ArrayList<>();
    MusicListenerWorker musicListenerWorker;
    private static final int NOTIFICATION_ID = 123;

    private Thread mLoopModeThread;
    private NotificationViewReceiver notificationViewReceiver;

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
        Intent intentGo = new Intent(this, HomeActivity.class);
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

    private void initNotificationSon() {
        String id = "stoneMusic_channel";
        String name="stoneMusic";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel =
                    new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);
            Notification.Builder builder = new Notification.Builder(this , id);
            notification = builder.build(); //构建通知
            setNotification();
            notification.contentView = remoteViews; // 设置下拉图标
            notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apiSupport
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.icon = R.drawable.anim_log;

            //mNotificationManager.notify(123, notification);//显示通知
            startForeground(NOTIFICATION_ID, notification);//启动为前台服务

        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MusicApplication.getContext());
            Bitmap icBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.play_background02);
            mBuilder.setLargeIcon(icBitmap);
            mBuilder.setOngoing(true);
            mBuilder.setPriority(Notification.PRIORITY_MAX);

            notification = mBuilder.build();//构建通知
            setNotification();
            notification.contentView = remoteViews; // 设置下拉图标
            notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.icon = R.drawable.anim_log;
            //mNotificationManager.notify(123, notification);//显示通知
            startForeground(NOTIFICATION_ID, notification);//启动为前台服务
        }
    }

    public Handler remoteViewsHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Log.d(TAG, "remoteViewsHandler" + ", msg.what=" + msg.what);
            int position  = MediaUtils.currentSongPosition;
            Log.d(TAG, "remoteViewsHandler" + ", 全局播放position=" + position);
            switch (msg.what){
                //上一曲/下一曲；需要跟新专辑图和歌手名，歌曲信息
                case 1:
                    /*专辑图片，歌曲名，歌手名*/
                    RefreshAlbumPicTitleArtist(position);
                    RefreshPlayOrPauseButton();
                    break;

                //播放/暂停同一首音乐;只需要更新：播放/暂停图标
                case 2:
                    RefreshPlayOrPauseButton();
                    break;
            }
            notification.contentView = remoteViews; // 设置下拉图标
            notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apiSupport
            //mNotificationManager.notify(123, notification);//显示通知
            startForeground(NOTIFICATION_ID, notification);//启动为前台服务
            Log.d(TAG, "remoteViewsHandler end");
        }
    };

    /**
     * 更新 "播放/暂停" 图标
     */
    private void RefreshPlayOrPauseButton() {
        Log.d(TAG, "RefreshPlayOrPauseButton" + "放器状态码MediaUtils.currentState=" + MediaUtils.currentState);
        if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_play_black);
        } else {
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.drawable.ic_pause_black);
        }
    }

    /**
     * 更新 "专辑图片，歌曲名，歌手名"
     */
    private void RefreshAlbumPicTitleArtist(int position) {
        Log.i(TAG, "RefreshAlbumPicTitleArtist()");
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_remote);
        setNotification();
        try {

            /*歌曲名称 & 歌手名*/
            remoteViews.setTextViewText(R.id.notification_title, musicList.get(position).getTitle());
            remoteViews.setTextViewText(R.id.notification_artist, musicList.get(position).getArtist());


            //更新音乐专辑图
            String imagePath; //歌曲图片路径
            if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
                imagePath = musicList.get(position).getPicUrl();
            } else { //当前为播放本地歌曲状态
                imagePath = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
            }
            if (null == imagePath || imagePath.equals("")) {
                remoteViews.setImageViewResource(R.id.notification_album, R.drawable.play_background02);
            } else {
                NotificationTarget notificationTarget = new NotificationTarget(
                        this, R.id.notification_album, remoteViews, notification, NOTIFICATION_ID);
                Glide.with(this).asBitmap().load(imagePath).into(notificationTarget);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void observerUpData(int content) {
//        Log.d(TAG, "observerUpData->观察者类数据已刷新" + ", content=" + content);
        switch (content) {
            //Music源改变
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                Log.d(TAG, "observerUpData->MUSIC_POSITION_CHANGED");
                musicList = SongModel.getInstance().getChooseSongList(); //播放源改变，需要重新赋值chooseList
                remoteViewsHandler.sendEmptyMessage(1);
                break;
            //播放状态改变
            case MediaStateCode.PLAY_START:
            case MediaStateCode.PLAY_PAUSE:
            case MediaStateCode.PLAY_CONTINUE:
            case MediaStateCode.PLAY_STOP:
                remoteViewsHandler.sendEmptyMessage(2);
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"音乐服务onCreate");

        remoteViews = new RemoteViews(getPackageName(), R.layout.view_remote);
        initNotificationSon();

        remoteViewsHandler.sendEmptyMessage(1);

        musicList = SongModel.getInstance().getChooseSongList();


        //开启监听音乐播放进度的Worker
        musicListenerWorker = new MusicListenerWorker(this);
        mLoopModeThread = new Thread(musicListenerWorker);
        mLoopModeThread.start(); /*启动线程*/

        notificationViewReceiver = new NotificationViewReceiver(this);

        //初始化各种音乐列表
        initXXXList();

        //动态注册广播接收器
        RegisteredBroadcastReceiver();

        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);

    }

    /**
     * 初始化各种音乐列表
     */
    private void initXXXList() {
        //APP启动后，Service初始化过程中，从SP中取出“我喜欢的音乐”列表
        LoveListSpUtil.getLoveMusicListFromSp();
    }


    /**
     * 动态注册广播接收器
     */
    private void RegisteredBroadcastReceiver() {
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MediaStateCode.ACTION_CLOSE);
        itFilter.addAction(MediaStateCode.ACTION_PLAY_OR_PAUSE);
        itFilter.addAction(MediaStateCode.ACTION_LAST);
        itFilter.addAction(MediaStateCode.ACTION_NEXT);
        itFilter.addAction(MediaStateCode.ACTION_LOVE);
        itFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY); //耳机拔出事件
        registerReceiver(notificationViewReceiver, itFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"音乐服务onDestroy");

        //MusicApplication.getmRefWatcher().watch(this);

        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);

        mLoopModeThread = null;

        PlayControl.AbandonAudioFocus(); //放弃播放焦点

        MediaUtils.release();
        unregisterReceiver(notificationViewReceiver);
        System.exit(0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

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
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.stone.stonemusic.R;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.receiver.MusicBroadCastReceiver;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.ui.activity.PlayActivity;
import com.stone.stonemusic.utils.BroadcastUtils;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {
    public static final String TAG = "MusicService";
    /*2018/12/8 stoneWang start */
    public NotificationManager mNotificationManager;
    public Notification notification;
    private RemoteViews remoteViews;
    private List<Music> musicList = new ArrayList<>();
    /*2018/12/8 stoneWang end */

    public final IBinder binder = new MyBinder();

    private Thread mLoopModeThread;
    private int nowMediaNum = 0, listMediaNum = 1024, resultNum = 100, listSize, outOfOrderNum;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"音乐服务onCreate");

        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MediaStateCode.ACTION_IMAGE);
        itFilter.addAction(MediaStateCode.ACTION_CLOSE);
        itFilter.addAction(MediaStateCode.ACTION_PLAY_OR_PAUSE);
        itFilter.addAction(MediaStateCode.ACTION_LAST);
        itFilter.addAction(MediaStateCode.ACTION_NEXT);
        itFilter.addAction(MediaStateCode.ACTION_LOVE);


        registerReceiver(playMusicReceiver, itFilter);

        initNotification();
        musicList = SongModel.getInstance().getSongList();

        mLoopModeThread = new Thread(new LoopModeThread());
        mLoopModeThread.start(); /*启动线程*/
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
    }

    private void initNotificationSon() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MusicAppUtils.getContext());

        mBuilder.setSmallIcon(R.drawable.ic_log_white); // 设置顶部图标
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
                String path = MusicUtil.getAlbumArt(
                        new Long(musicList.get(position).getAlbum_id()).intValue());
                Log.d(TAG,"path="+path);
                Bitmap bitmap;
                if (null == path){
                    bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.anim_log);
                }else{
                    bitmap = BitmapFactory.decodeFile(path);
                }
                remoteViews.setImageViewBitmap(R.id.notification_album, bitmap);

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            Log.d(TAG,"音乐服务onStartCommand"+intent.getIntExtra("state", 0));
            int state = intent.getIntExtra("state", 0);
            switch (state) {
                case MediaStateCode.PLAY_START:
                    MediaUtils.prepare(
                            SongModel.getInstance().getSongList().
                                    get(MediaUtils.currentSongPosition).getFileUrl());
                    MediaUtils.start();
                    break;
                case MediaStateCode.PLAY_PAUSE:
                    MediaUtils.pause();
                    break;
                case MediaStateCode.PLAY_CONTINUE:
                    MediaUtils.continuePlay();
                    break;
                case MediaStateCode.PLAY_STOP:
                    MediaUtils.stop();
                    break;
                case MediaStateCode.MUSIC_POSITION_CHANGED:
                    remoteViewsHandler.sendEmptyMessage(1);
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * RemoteViews控制播放和销毁服务或者进入Activity
     */
    private BroadcastReceiver playMusicReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (action.equals(MediaStateCode.ACTION_PLAY_OR_PAUSE)) {
                if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE) {
                    MediaUtils.continuePlay();
                } else if (MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
                    MediaUtils.prepare(
                            SongModel.getInstance().getSongList().
                                    get(MediaUtils.currentSongPosition).getFileUrl());
                    MediaUtils.start();
                } else if (MediaUtils.currentState == MediaStateCode.PLAY_START ||
                        MediaUtils.currentState == MediaStateCode.PLAY_CONTINUE) {
                    MediaUtils.pause();
                }
                BroadcastUtils.sendNoticeMusicPositionChanged();
            } else if (action.equals(MediaStateCode.ACTION_LAST)) {
                MediaUtils.last();
                MediaUtils.prepare(
                        SongModel.getInstance().getSongList().
                            get(MediaUtils.currentSongPosition).getFileUrl());
                MediaUtils.start();
                BroadcastUtils.sendNoticeMusicPositionChanged();
            } else if (action.equals(MediaStateCode.ACTION_NEXT)) {
                MediaUtils.next();
                MediaUtils.prepare(
                        SongModel.getInstance().getSongList().
                                get(MediaUtils.currentSongPosition).getFileUrl());
                MediaUtils.start();
                BroadcastUtils.sendNoticeMusicPositionChanged();
            } else if (action.equals(MediaStateCode.ACTION_LOVE)){
                Log.d(TAG, "点击了Love");
            } else if (action.equals(MediaStateCode.ACTION_CLOSE)) {
                Log.d(TAG, "clocked Close");
                MusicAppUtils.destroyActivity("PlayActivity");
                MusicAppUtils.destroyActivity("LocalListActivity");
                stopService(new Intent(context,MusicService.class));
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
        MediaUtils.release();
        unregisterReceiver(playMusicReceiver);
        LocalBroadcastManager.getInstance(MusicAppUtils.getContext()).unregisterReceiver(MusicBroadCastReceiver.getInstance());
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
                    Log.d(TAG, "播放器状态：" + MediaUtils.currentState +
                    "//seekBar正在改变：" + MediaUtils.seekBarIsChanging +
                    "//播放器中音乐长度：" + nowMediaNum +
                    "//当前音乐长度：" + listMediaNum +
                    "resultNum == " + resultNum);

                    /*手离开了seekBar 而且 音乐播放完了（由于获取的数值有一定的差异，所以允许+-10bite的数值差异）*/
                    if (!MediaUtils.seekBarIsChanging && (resultNum <= 1024 && resultNum >= -1024)) {
                        /*下一曲*/
                        MediaUtils.next();
                        MediaUtils.prepare(
                                SongModel.getInstance().getSongList().
                                        get(MediaUtils.currentSongPosition).getFileUrl());
                        MediaUtils.start();
                        /*发送UI更新广播*/
                        BroadcastUtils.sendNoticeMusicPositionChanged();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

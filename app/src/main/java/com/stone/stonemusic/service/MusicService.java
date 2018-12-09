package com.stone.stonemusic.service;

import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;

public class MusicService extends Service {
    public static final String TAG = "MusicService";
    /*2018/12/8 stoneWang start */
    public NotificationManager mNotificationManager;
    private RemoteViews remoteViews;
    /*2018/12/8 stoneWang end */

    public final IBinder binder = new MyBinder();

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"音乐服务onCreate");

        initNotification();
    }

    /*2018/12/8 stoneWang start */



    /**
     * PendingIntent是一种特殊的intent，设置之后并不会马上使用，而是在真正点击后只会调用。
     */
    private void setNotification(){
//        Intent intent = new Intent(this, LocalListActivity.class);
//        // 点击跳转到主界面
//        PendingIntent intent_go = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);


//        Intent intent2 = new Intent();
//        intent2.setAction(MusicAppUtils.getContext().getResources().getString(R.string.app_name));
//        intent2.putExtra("state", MediaStateCode.PLAY_PAUSE);
//        PendingIntent intent_pause = PendingIntent.getBroadcast(this, 0, intent2,
//                PendingIntent.FLAG_UPDATE_CURRENT);// 4个参数context, requestCode, intent, flags
//        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause, intent_pause);

        // 设置收藏,测试是否能收到广播
        Intent intent3 = new Intent();
//        intent3.setAction(MusicAppUtils.getContext().getResources().getString(R.string.app_name));
        intent3.putExtra("state", MediaStateCode.TEST);
        PendingIntent intent_like = PendingIntent.getService(this, 0, intent3,
                PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_album, intent_like);
    }
    /**
     * 初始化通知
     */
    private void initNotification() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_remote);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        setNotification();

        mBuilder.setSmallIcon(R.drawable.ic_log_white); // 设置顶部图标
        mBuilder.setOngoing(true);


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final Notification notification = mBuilder.build();//构建通知
        notification.contentView = remoteViews; // 设置下拉图标
        notification.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.anim_log;



        mNotificationManager.notify(123, notification);//显示通知
        startForeground(123, notification);//启动为前台服务

        /*
        Intent intent = new Intent(this, LocalListActivity.class);
        // 点击跳转到主界面
        PendingIntent intent_go = PendingIntent.getActivity(this, 5, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);

        // 4个参数context, requestCode, intent, flags
        PendingIntent intent_close = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, intent_close);

        // 设置上一曲
        Intent prv = new Intent();
        prv.setAction(SyncStateContract.Constants.ACTION_PRV);
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, prv,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, intent_prev);

        // 设置播放
        if (Myapp.isPlay) {
            Intent playorpause = new Intent();
            playorpause.setAction(SyncStateContract.Constants.ACTION_PAUSE);
            PendingIntent intent_play = PendingIntent.getBroadcast(this, 2,
                    playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_play, intent_play);
        }
        if (!Myapp.isPlay) {
            Intent playorpause = new Intent();
            playorpause.setAction(SyncStateContract.Constants.ACTION_PLAY);
            PendingIntent intent_play = PendingIntent.getBroadcast(this, 6,
                    playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_play, intent_play);
        }

        // 下一曲
        Intent next = new Intent();
        next.setAction(SyncStateContract.Constants.ACTION_NEXT);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, intent_next);

        // 设置收藏
        PendingIntent intent_fav = PendingIntent.getBroadcast(this, 4, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_fav, intent_fav);
        */



    }

    /*
    public Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            p3Info info = (Mp3Info) msg.obj;
            Bitmap bitmap = MediaAppUtils.getArtwork(getApplicationContext(),
                    info.getId(), info.getAlbumId(), true, false);
            btm_album.setImageBitmap(bitmap);
            btm_artist.setText(info.getArtist());
            btm_title.setText(info.getTitle());

            // 播放歌曲
            btm_state
                    .setImageResource(R.drawable.player_btn_radio_pause_normal);

            // 设置通知栏的图片文字
            remoteViews = new RemoteViews(getPackageName(),
                    R.layout.view_remote);
            //remoteViews.setImageViewBitmap(R.id.widget_album, );
            remoteViews.setTextViewText(R.id.widget_title, "info.getTitle()");
            remoteViews.setTextViewText(R.id.widget_artist, "info.getArtist()");

            if (Myapp.isPlay) {
                remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_btn_pause_normal);
            }else {
                remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_btn_play_normal);
            }

            showNotification();
        };
    };
    */
    /*2018/12/8 stoneWang end */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        }

        return super.onStartCommand(intent, flags, startId);
    }

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
        MediaUtils.release();
        super.onDestroy();
        System.exit(0);
        Log.d(TAG,"音乐服务onDestroy");
    }
}

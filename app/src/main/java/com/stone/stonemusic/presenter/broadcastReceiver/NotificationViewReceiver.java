package com.stone.stonemusic.presenter.broadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.stone.stonemusic.utils.playControl.PlayControl;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/7/616:17
 * RemoteViews控制播放操作 && 销毁服务
 */
public class NotificationViewReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationViewReceiver";
    private MusicService musicService;

    public NotificationViewReceiver(MusicService service) {
        this.musicService = service;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);

        if (null != action && action.equals(MediaStateCode.ACTION_PLAY_OR_PAUSE)) {
            PlayControl.controlBtnPlaySameSong();
            musicService.remoteViewsHandler.sendEmptyMessage(2);
        } else if (null != action && action.equals(MediaStateCode.ACTION_LAST)) {
            PlayControl.controlBtnLast();
            musicService.remoteViewsHandler.sendEmptyMessage(1);
        } else if (null != action && action.equals(MediaStateCode.ACTION_NEXT)) {
            PlayControl.controlBtnNext();
            musicService.remoteViewsHandler.sendEmptyMessage(1);
        } else if (null != action && action.equals(MediaStateCode.ACTION_LOVE)){
            Log.d(TAG, "点击了Love");
            musicService.remoteViewsHandler.sendEmptyMessage(3);
        } else if (null != action && action.equals(MediaStateCode.ACTION_CLOSE)) {
            Log.d(TAG, "clocked Close");
            MusicApplication.destroyActivity("PlayActivity");
            MusicApplication.destroyActivity("LocalListActivity");
            musicService.stopService(new Intent(context,MusicService.class));
        } else if (null != action && AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            Log.i(TAG, "检测到耳机拔出");
            PlayControl.controlPauseSameSong();
        } else {
            Log.d(TAG, "未知状态229");
        }
    }
}

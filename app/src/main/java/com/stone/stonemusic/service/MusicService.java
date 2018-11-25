package com.stone.stonemusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;

public class MusicService extends Service {

    public final IBinder binder = new MyBinder();

    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int state = intent.getIntExtra("state", 0);
        switch (state) {
            case MediaStateCode.PLAY_START:
                MediaUtils.prepare(
                        SongModel.getInstance().getSongList().get(
                                MediaUtils.currentSongPosition).getFileUrl());
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
    }
}

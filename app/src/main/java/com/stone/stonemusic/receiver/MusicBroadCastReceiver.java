package com.stone.stonemusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stone.stonemusic.service.MusicService;

public class MusicBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "MusicBroadCastReceiver";

    private static MusicBroadCastReceiver sReceiver;
    private MusicBroadCastReceiver() {}

    public static MusicBroadCastReceiver getInstance(){
        if (sReceiver == null) {
            sReceiver = new MusicBroadCastReceiver();
        }
        return sReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"广播接收器onReceive");
        Log.d(TAG,"接收到的intent的值为："+ intent.getIntExtra("state", 0));
        //利用广播操作服务
        intent.setClass(context, MusicService.class);
        context.startService(intent);
    }
}

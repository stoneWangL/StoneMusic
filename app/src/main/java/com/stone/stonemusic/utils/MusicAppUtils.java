package com.stone.stonemusic.utils;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.stone.stonemusic.R;
import com.stone.stonemusic.receiver.MusicBroadCastReceiver;

public class MusicAppUtils extends Application{
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(sContext.getResources().getString(R.string.app_name));
        //动态注册广播接收器
        LocalBroadcastManager
                .getInstance(sContext)
                .registerReceiver(MusicBroadCastReceiver.getInstance(),
                        new IntentFilter(itFilter));
    }

    public static Context getContext(){
        return sContext;
    }
}

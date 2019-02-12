package com.stone.stonemusic.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.stone.stonemusic.R;
import com.stone.stonemusic.receiver.MusicBroadCastReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MusicAppUtils extends Application{
    private static Context sContext;
    private static Map<String, Activity> destroyMap = new HashMap<>(); /*用于销毁活动*/

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



    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
    public static void addDestroyActivity(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destroyMap.get(key).finish();
                }
            }
        }
    }
}

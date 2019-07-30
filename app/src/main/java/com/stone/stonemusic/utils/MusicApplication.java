package com.stone.stonemusic.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;



import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MusicApplication extends Application{
    public static final String TAG = "MusicAppUtils";
//    private static RefWatcher mRefWatcher;

    private static Context sContext;
    private static Map<String, Activity> destroyMap = new HashMap<>(); /*用于销毁活动*/

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
//        mRefWatcher = LeakCanary.install(this);
    }

    public static Context getContext(){
        return sContext;
    }

    /**
     * @return RefWatcher，用于监控、追踪应用中的对象引用
     */
//    public static RefWatcher getmRefWatcher() {
//        return null;
////        return mRefWatcher;
//    }

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

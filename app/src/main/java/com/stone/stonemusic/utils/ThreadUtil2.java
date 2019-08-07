package com.stone.stonemusic.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 11:03
 * @Description:
 */
public class ThreadUtil2 {
    private Handler handler;

    private Handler getMainHandler(){
        if (handler==null)
            return (handler = new Handler(Looper.getMainLooper()));
        else
            return handler;
    }

    public void runOnMainThread(Runnable runnable) {
        getMainHandler().post(runnable);
    }

}

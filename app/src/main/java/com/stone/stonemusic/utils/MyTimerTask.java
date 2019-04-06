package com.stone.stonemusic.utils;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author : stoneWang
 * date   : 2019/4/617:27
 *
 * 该方法为了子类能继承，无法在父类进行单例模式的封装，所以子类需要自己实现单例模式，或者如果只需要一个对象，用完指向null
 */
public class MyTimerTask {
    private String TAG = "MyTimerTask";
    private Timer timer = null;

    /**
     *
     * @param delayTime 任务延迟delayTime开始
     * @param pollTime run方法中的任务每pollTime运行一次
     */
    public void start(int delayTime, int pollTime) {
        final int[] i = {1};
        if (timer == null){
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doSomething();
                Log.i(TAG, "轮询第"+ i[0] + "次");
                i[0]++;
            }
        }, delayTime, pollTime);
    }

    /**
     * 子类实现的需要轮询的逻辑代码
     */
    public void doSomething() {}

    public void destroyed(){
        Log.i(TAG, "轮询任务销毁");
        if (null != timer)
            timer.cancel();
        timer = null;
    }
}

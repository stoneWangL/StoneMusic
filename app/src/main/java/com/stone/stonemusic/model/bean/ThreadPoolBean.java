package com.stone.stonemusic.model.bean;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/25 21:50
 * @Description: 线程池单例类
 */
public class ThreadPoolBean {
    private static ThreadPoolExecutor mInstance;

    //私有构造方法
    private ThreadPoolBean() {}

    /**
     * 共有的获取线程池对象的方法,采用DCL写法的单例,线程安全
     * @return 单例线程池对象
     */
    public static ThreadPoolExecutor getInstance() {
        //避免不必要的同步
        if (mInstance == null) {
            //同步
            synchronized (ThreadPoolBean.class) {
                //在第一次调用时初始化
                if (mInstance == null) {
                    mInstance = mInstance = new ThreadPoolExecutor(
                            3, //核心线程数
                            5, //最大线程数
                            1, //临时线程生存时间
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(100));
                }
            }
        }
        return mInstance;
    }
}

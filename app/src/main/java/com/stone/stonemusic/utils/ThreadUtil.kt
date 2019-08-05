package com.stone.stonemusic.utils

import android.os.Handler
import android.os.Looper

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 10:14
 * @Description:
 */
object ThreadUtil {
    val handler = Handler(Looper.getMainLooper())

    /**
     * 运行在主线程中
     */
    fun runOnMainThread(runnable:Runnable) {
        handler.post(runnable)
    }
}
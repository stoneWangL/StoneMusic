package com.stone.stonemusic.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*

/**
 * @Author: stoneWang
 * @CreateDate: 2019/7/29 20:52
 * @Description: 所有Activity的基类
 */
abstract class BaseActivity: AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initListener()
        initData()
    }

    /**
     * 初始化数据
     */
    open protected fun initData() {

    }

    /**
     * adapter listener
     */
    open protected fun initListener() {

    }

    /**
     * 获取布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * 线程安全的toast
     */
    protected fun myToast(msg : String) {
        runOnUiThread { toast(msg) }
    }

    /**
     * 开启Activity并且finish当前界面
     */
    inline fun <reified T : BaseActivity>startActivityAndFinish() {
        startActivity<T>()
        finish()
    }
}
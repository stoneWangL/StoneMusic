package com.stone.stonemusic.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

/**
 * @Author: stoneWang
 * @CreateDate: 2019/7/29 21:07
 * @Description: 所有fragment的基类
 */
abstract class BaseFragment : Fragment() , AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
//        debug {  }
    }

    private fun init() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView() //让子类返回显示
    }

    /**
     * 获取布局view
     */
    abstract fun initView(): View?

    /**
     * 当
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initData()
    }

    /**
     * 数据的初始化
     */
    open protected fun initData() {

    }

    /**
     * adapter listener
     */
    open protected fun initListener() {

    }

    fun myToast(msg:String) {
        context?.runOnUiThread { toast(msg) }
    }
}
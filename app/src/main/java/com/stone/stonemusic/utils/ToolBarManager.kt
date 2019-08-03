package com.stone.stonemusic.utils

import android.content.Intent
import android.support.v7.widget.Toolbar
import com.stone.stonemusic.R
import com.stone.stonemusic.ui.activity.SettingActivity


/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/1 16:33
 * @Description: 所有界面toolbar的管理类
 */
interface ToolBarManager {
    val toolbar : Toolbar
    /**
     * 初始化主界面中的toolbar
     */
//    fun initMainToolBar() {
//        toolbar.setTitle("KTMusicPlay")
//        toolbar.inflateMenu(R.menu.main)
//        //kotlin 和 java 调用特性
//        //第一种方式
//        //如果java接口中只有一个未实现的方法 可以省略接口对象 直接用{}表示未实现的方法
//        toolbar.setOnMenuItemClickListener {
//            toolbar.context.startActivity(Intent(toolbar.context, SettingActivity::class.java))
//            true
//        }
//        //第二种方式
////        toolbar.setOnMenuItemClickListener (object : Toolbar.OnMenuItemClickListener{
////            override fun onMenuItemClick(item: MenuItem?): Boolean {
////                when(item?.itemId){
////                    R.id.setting->{
//////                        Toast.makeText(toolbar.context, "点击了设置按钮", Toast.LENGTH_SHORT).show()
////                        //跳转到设置界面
////                        toolbar.context.startActivity(Intent(toolbar.context, SettingActivity::class.java))
////                    }
////                }
////                return true
////            }
////        })
//    }

    /**
     * 处理设置界面的toolbar
     */
    fun initSettingToolbar() {
        toolbar.setTitle("设置界面")
    }
}
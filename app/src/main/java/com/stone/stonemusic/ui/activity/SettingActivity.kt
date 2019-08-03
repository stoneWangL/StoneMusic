package com.stone.stonemusic.ui.activity

import android.support.v7.widget.Toolbar
import com.stone.stonemusic.R
import com.stone.stonemusic.base.BaseActivity
import com.stone.stonemusic.utils.ToolBarManager
import org.jetbrains.anko.find

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 15:29
 * @Description:
 */
class SettingActivity: BaseActivity() , ToolBarManager{

    override val toolbar by lazy {
        find<Toolbar>(R.id.toolbar)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {
        initSettingToolbar()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.stop, R.anim.left_out)
    }
}
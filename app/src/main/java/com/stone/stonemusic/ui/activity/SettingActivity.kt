package com.stone.stonemusic.ui.activity

import com.stone.stonemusic.R
import com.stone.stonemusic.base.BaseActivity

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 15:29
 * @Description:
 */
class SettingActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.stop, R.anim.left_out)
    }
}
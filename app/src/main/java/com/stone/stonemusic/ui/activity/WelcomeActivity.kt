package com.stone.stonemusic.ui.activity

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import com.stone.stonemusic.R
import com.stone.stonemusic.base.BaseActivity

/**
 * @Author: stoneWang
 * @CreateDate: 2019/7/29 21:51
 * @Description:
 */
class WelcomeActivity : BaseActivity() , ViewPropertyAnimatorListener {
    override fun onAnimationEnd(view: View?) {
        startActivityAndFinish<LocalMainActivity>()
    }

    override fun onAnimationCancel(view: View?) {

    }

    override fun onAnimationStart(view: View?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initData() {
    ViewCompat.animate(findViewById(R.id.first_music_text)).alpha(1.0f).duration = 2600
    ViewCompat.animate(findViewById(R.id.first_music_log)).
            translationY(0f).duration = 2000

    }


}
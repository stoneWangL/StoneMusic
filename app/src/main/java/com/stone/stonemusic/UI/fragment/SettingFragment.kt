package com.stone.stonemusic.UI.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stone.stonemusic.R
import com.stone.stonemusic.UI.activity.AboutActivity
import com.stone.stonemusic.UI.activity.LoveListActivity

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 15:36
 * @Description: 设置界面fragment
 */
class SettingFragment: PreferenceFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addPreferencesFromResource(R.xml.setting)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {
        val key = preference?.key
        if ("about".equals(key)) {
            //点击了关于
            activity.startActivity(Intent(activity, AboutActivity::class.java))
        } else if ("love".equals(key)) {
            //点击了关于
            activity.startActivity(Intent(activity, LoveListActivity::class.java))
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }
}
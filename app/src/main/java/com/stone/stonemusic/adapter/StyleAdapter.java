package com.stone.stonemusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.stone.stonemusic.ui.fragment.StyleFragment;
import com.stone.stonemusic.ui.fragment.StyleHuaiJiuFragment;
import com.stone.stonemusic.ui.fragment.StyleLangManFragment;
import com.stone.stonemusic.ui.fragment.StyleQingXinFragment;
import com.stone.stonemusic.ui.fragment.StyleShangGanFragment;
import com.stone.stonemusic.ui.fragment.StyleXingGanFragment;
import com.stone.stonemusic.ui.fragment.StyleZhiYuFragment;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 10:44
 * @Description:
 */
public class StyleAdapter extends FragmentPagerAdapter {
    private StyleHuaiJiuFragment styleHuaiJiuFragment = null;
    private StyleQingXinFragment styleQingXinFragment = null;
    private StyleLangManFragment styleLangManFragment = null;
    private StyleXingGanFragment styleXingGanFragment = null;
    private StyleShangGanFragment styleShangGanFragment = null;
    private StyleZhiYuFragment styleZhiYuFragment = null;
    //怀旧| 清新| 浪漫| 性感| 伤感| 治愈| 放松| 孤独| 感动| 兴奋| 快乐| 安静| 思念|
    private String[] mTitles = new String[] {
            "怀旧", "清新", "浪漫",
            "性感", "伤感", "治愈"

    };

    public StyleAdapter(FragmentManager fm) {
        super(fm);
        styleHuaiJiuFragment = new StyleHuaiJiuFragment();
        styleQingXinFragment = new StyleQingXinFragment();
        styleLangManFragment = new StyleLangManFragment();
        styleXingGanFragment = new StyleXingGanFragment();
        styleShangGanFragment = new StyleShangGanFragment();
        styleZhiYuFragment = new StyleZhiYuFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case StyleFragment.PAGE_HuaiJiu:
                fragment = styleHuaiJiuFragment;
                break;
            case StyleFragment.PAGE_QingXin:
                fragment = styleQingXinFragment;
                break;
            case StyleFragment.PAGE_LangMan:
                fragment = styleLangManFragment;
                break;
            case StyleFragment.PAGE_XingGan:
                fragment = styleXingGanFragment;
                break;
            case StyleFragment.PAGE_ShangGan:
                fragment = styleShangGanFragment;
                break;
            case StyleFragment.PAGE_ZhiYu:
                fragment = styleZhiYuFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}

package com.stone.stonemusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.stone.stonemusic.ui.fragment.StyleFangSongFragment;
import com.stone.stonemusic.ui.fragment.StyleFragment;
import com.stone.stonemusic.ui.fragment.StyleGanDongFragment;
import com.stone.stonemusic.ui.fragment.StyleGuDuFragment;
import com.stone.stonemusic.ui.fragment.StyleHuaiJiuFragment;
import com.stone.stonemusic.ui.fragment.StyleLangManFragment;
import com.stone.stonemusic.ui.fragment.StyleQingXinFragment;
import com.stone.stonemusic.ui.fragment.StyleShangGanFragment;
import com.stone.stonemusic.ui.fragment.StyleXingGanFragment;
import com.stone.stonemusic.ui.fragment.StyleZhiYuFragment;
import com.stone.stonemusic.utils.code.GeDanCode;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 10:44
 * @Description: 推荐风格下的多个fragment的页面的适配器
 */
public class StyleAdapter extends FragmentPagerAdapter {
//    private StyleHuaiJiuFragment huaiJiuFragment;
//    private StyleQingXinFragment qingXinFragment;
//    private StyleLangManFragment langManFragment;
//
//    private StyleXingGanFragment xingGanFragment;
//    private StyleShangGanFragment shangGanFragment;
//    private StyleZhiYuFragment zhiYuFragment;
//
//    private StyleFangSongFragment fangSongFragment;
    /**
     * 怀旧| 清新| 浪漫|
     * 性感| 伤感| 治愈|
     * 放松| 孤独| 感动|
     * 兴奋| 快乐| 安静|
     * 思念|
     */
    private String[] mTitles = new String[] {
            GeDanCode.HuaiJiu, GeDanCode.QingXin, GeDanCode.LangMan,
            GeDanCode.XingGan,  GeDanCode.ShangGan,  GeDanCode.ZhiYu,
            GeDanCode.FangSong,  GeDanCode.GuDu,  GeDanCode.GanDong

    };

    public StyleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case StyleFragment.PAGE_HuaiJiu:
                fragment = new StyleHuaiJiuFragment();
                break;
            case StyleFragment.PAGE_QingXin:
                fragment = new StyleQingXinFragment();
                break;
            case StyleFragment.PAGE_LangMan:
                fragment = new StyleLangManFragment();
                break;

            case StyleFragment.PAGE_XingGan:
                fragment = new StyleXingGanFragment();
                break;
            case StyleFragment.PAGE_ShangGan:
                fragment = new StyleShangGanFragment();
                break;
            case StyleFragment.PAGE_ZhiYu:
                fragment = new StyleZhiYuFragment();
                break;

            case StyleFragment.PAGE_FangSong:
                fragment = new StyleFangSongFragment();
                break;
            case StyleFragment.PAGE_GuDu:
                fragment = new StyleGuDuFragment();
                break;
            case StyleFragment.PAGE_GanDong:
                fragment = new StyleGanDongFragment();
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

package com.stone.stonemusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.stone.stonemusic.UI.fragment.LocalArtistFragment;
import com.stone.stonemusic.UI.fragment.LocalSingleSongFragment;
import com.stone.stonemusic.UI.fragment.StyleFragment;
import com.stone.stonemusic.utils.code.LocalCode;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/14 10:44
 * @Description: 本地音乐适配器
 */
public class LocalAdapter extends FragmentPagerAdapter {
//    private LocalSingleSongFragment localSingleSongFragment;
//    private LocalArtistFragment localArtistFragment;

    /**
     * 单曲| 歌手| 专辑| 文件夹
     */
    private String[] mTitles = new String[] {
            LocalCode.SingleSong, LocalCode.Singer
    };

    public LocalAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case StyleFragment.PAGE_HuaiJiu:
                fragment = new LocalSingleSongFragment();
                break;
            case StyleFragment.PAGE_QingXin:
                fragment = new LocalArtistFragment();
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
//        super.destroyItem(container, position, object);
    }
}

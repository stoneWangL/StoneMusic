package com.stone.stonemusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.stone.stonemusic.UI.activity.PlayActivity;
import com.stone.stonemusic.UI.fragment.PlayImageFragment;
import com.stone.stonemusic.UI.fragment.PlayLyricFragment;

public class PlayFragmentPagerAdapter extends FragmentPagerAdapter {
//    private final int PAGER_COUNT = 2;
    private PlayImageFragment playImageFragment = null;
    private PlayLyricFragment playLyricFragment = null;
    private String[] mTitles = new String[]{
            "*","*"};

    public PlayFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        playImageFragment = new PlayImageFragment();
        playLyricFragment = new PlayLyricFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case PlayActivity.PLAY_PAGE_IMAGE:
                fragment = playImageFragment;
                break;
            case PlayActivity.PLAY_PAGE_LYRIC:
                fragment = playLyricFragment;
                break;
        }
        return fragment;
    }

    /*ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text*/
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}

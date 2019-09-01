package com.stone.stonemusic.UI.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalAdapter;
import com.stone.stonemusic.base.BaseFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 本地音乐列表
 */
public class LocalFragment extends BaseFragment {
    private static String TAG = "LocalFragment";
    private View thisView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocalAdapter localAdapter;

    /**
     * 单曲| 歌手| 专辑|
     */
    private TabLayout.Tab
            tabSingleSong, tabArtist;

    //几个代表Fragment页面的常量
    public static final int
            PAGE_SingleSong = 0, PAGE_Artist = 1;

    @Nullable
    @Override
    public View initView() {
        thisView = View.inflate(getContext(), R.layout.fragment_local, null);
        return thisView;
    }

    @Override
    protected void initListener() {
        if (null != getActivity()) {
            tabLayout = thisView.findViewById(R.id.tabLayoutLocal);
            viewPager = thisView.findViewById(R.id.viewPagerLocal);
            /*使用适配器将ViewPager与Fragment绑定在一起*/
            localAdapter = new LocalAdapter(getActivity().getSupportFragmentManager());
            viewPager.setAdapter(localAdapter);

            //将TabLayout与ViewPager绑定在一起
            tabLayout.setupWithViewPager(viewPager);

            //指定Tab的位置
            tabSingleSong = tabLayout.getTabAt(PAGE_SingleSong); //单曲
            tabArtist = tabLayout.getTabAt(PAGE_Artist); //歌手
        } else {
            Log.i(TAG, "initListener() -> null == getActivity()");
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}

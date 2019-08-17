package com.stone.stonemusic.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.stone.stonemusic.ui.activity.LocalListActivity
import com.stone.stonemusic.ui.fragment.*
import com.stone.stonemusic.utils.code.HomeCode

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:31
 * @Description: 主界面适配器
 */
class HomeAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val PAGER_COUNT = 3
//    private var musicListFragment: LocalSingleSongFragment? = null
//    private var artistListFragment: LocalArtistFragment? = null
    private val localFragment by lazy { LocalFragment() }
    private val geDanAllFragment by lazy { GeDanAllFragment() }

    private val styleFragment by lazy { StyleFragment() }

    private val mTitles = arrayOf(
            HomeCode.LocalSong,
            HomeCode.RecommendedSong,
            HomeCode.RecommendedStyle
    )

    init {
//        musicListFragment = LocalSingleSongFragment()
//        artistListFragment = LocalArtistFragment()
//        albumListFragment =
//        folderListFragment = StyleFragment()
    }

    override fun getCount(): Int {
        return PAGER_COUNT
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            LocalListActivity.PAGE_LOCAL -> fragment = localFragment
            LocalListActivity.PAGE_GE_DAN -> fragment = geDanAllFragment
            LocalListActivity.PAGE_STYLE -> fragment = styleFragment
        }
        return fragment
    }

    /*ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text*/
    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }


}
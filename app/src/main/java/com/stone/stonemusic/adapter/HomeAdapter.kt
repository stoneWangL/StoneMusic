package com.stone.stonemusic.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.stone.stonemusic.R
import com.stone.stonemusic.ui.activity.LocalListActivity
import com.stone.stonemusic.ui.fragment.*
import com.stone.stonemusic.utils.MusicApplication

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 9:31
 * @Description: 主界面适配器
 */
class HomeAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val PAGER_COUNT = 4
    private var musicListFragment: LocalMusicListFragment0? = null
    private var artistListFragment: ArtistListFragment1? = null
    private val albumListFragment by lazy { YueDanFragment() }
    private var folderListFragment: MVFragment3? = null

    private val mTitles = arrayOf(MusicApplication.getContext().resources.getString(R.string.tab_menu_local_music), MusicApplication.getContext().resources.getString(R.string.tab_menu_local_artist), MusicApplication.getContext().resources.getString(R.string.tab_menu_yue_dan), MusicApplication.getContext().resources.getString(R.string.tab_menu_mv))

    init {
        musicListFragment = LocalMusicListFragment0()
        artistListFragment = ArtistListFragment1()
//        albumListFragment =
        folderListFragment = MVFragment3()
    }

    override fun getCount(): Int {
        return PAGER_COUNT
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            LocalListActivity.PAGE_LOCAL -> fragment = musicListFragment
            LocalListActivity.PAGE_LOCAL_Author -> fragment = artistListFragment
            LocalListActivity.PAGE_YUE_DAN -> fragment = albumListFragment
            LocalListActivity.PAGE_MV -> fragment = folderListFragment
        }
        return fragment
    }

    /*ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text*/
    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }


}
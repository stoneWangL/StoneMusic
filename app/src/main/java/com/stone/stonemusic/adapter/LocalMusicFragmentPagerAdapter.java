package com.stone.stonemusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.stone.stonemusic.R;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.ui.fragment.AlbumListFragment2;
import com.stone.stonemusic.ui.fragment.ArtistListFragment1;
import com.stone.stonemusic.ui.fragment.FolderListFragment3;
import com.stone.stonemusic.ui.fragment.MusicListFragment0;
import com.stone.stonemusic.utils.MusicApplication;

public class LocalMusicFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private MusicListFragment0 musicListFragment = null;
    private ArtistListFragment1 artistListFragment = null;
    private AlbumListFragment2 albumListFragment = null;
    private FolderListFragment3 folderListFragment = null;

    private String[] mTitles = new String[] {
            MusicApplication.getContext().getResources().getString(R.string.tab_menu_local_music),
            MusicApplication.getContext().getResources().getString(R.string.tab_menu_local_artist),
            MusicApplication.getContext().getResources().getString(R.string.tab_menu_recommend_song_sheet),
            MusicApplication.getContext().getResources().getString(R.string.tab_menu_recommend_mv)
    };

    public LocalMusicFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        musicListFragment = new MusicListFragment0();
        artistListFragment = new ArtistListFragment1();
        albumListFragment = new AlbumListFragment2();
        folderListFragment = new FolderListFragment3();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case LocalListActivity.PAGE_MUSIC:
                fragment = musicListFragment;
                break;
            case LocalListActivity.PAGE_ARTIST:
                fragment = artistListFragment;
                break;
            case LocalListActivity.PAGE_ALBUM:
                fragment = albumListFragment;
                break;
            case LocalListActivity.PAGE_FOLDER:
                fragment = folderListFragment;
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

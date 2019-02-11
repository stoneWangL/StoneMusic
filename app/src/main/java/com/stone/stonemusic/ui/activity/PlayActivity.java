package com.stone.stonemusic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.PlayFragmentPagerAdapter;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.utils.ActivityUtils;
import com.stone.stonemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    public static final String TAG = "PlayActivity";
    private List<Music> musicList = new ArrayList<>();
    private LinearLayout mlinearLayout;

    private TabLayout tabLayoutBarPlay;
    private TabLayout.Tab tabPlay;
    private TabLayout.Tab tabLyric;
    private ViewPager viewPager;
    private PlayFragmentPagerAdapter playFragmentPagerAdapter;

    /*几个代表Fragment页面的常量*/
    public static final int PLAY_PAGE_IMAGE = 0;
    public static final int PLAY_PAGE_LYRIC = 1;

    private TextView tvMusicName, tvMusicArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        init();


    }

    private void init() {
        musicList = SongModel.getInstance().getSongList();
        initColor();
        initView();
    }

    private void initColor() {
        int statusColor = ActivityUtils.initColor(this);
        mlinearLayout = (LinearLayout) findViewById(R.id.play_activity_layout);
        mlinearLayout.setBackgroundColor(getResources().getColor(statusColor));
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_play);
        playFragmentPagerAdapter = new PlayFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(playFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        tabLayoutBarPlay = (TabLayout) findViewById(R.id.tab_layout_bar_play);
        tabLayoutBarPlay.setupWithViewPager(viewPager);

        //指定Tab的位置
        tabPlay = tabLayoutBarPlay.getTabAt(PLAY_PAGE_IMAGE);
        tabLyric = tabLayoutBarPlay.getTabAt(PLAY_PAGE_LYRIC);

        int position = MediaUtils.currentSongPosition;
        tvMusicName = (TextView) findViewById(R.id.music_play_name);
        tvMusicArtist = (TextView) findViewById(R.id.music_play_artist);
        tvMusicName.setText(musicList.get(position).getTitle());
        tvMusicArtist.setText(musicList.get(position).getArtist());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    public void downBack(View view){
        onBackPressed();
    }



}

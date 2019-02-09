package com.stone.stonemusic.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalMusicFragmentPagerAdapter;
import com.stone.stonemusic.bean.ItemViewChoose;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.receiver.MusicBroadCastReceiver;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.BroadcastUtils;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class LocalListActivity extends AppCompatActivity {
    public static final String TAG = "LocalListActivity";

    private TabLayout.Tab tabMusic;
    private TabLayout.Tab tabArtist;
    private TabLayout.Tab tabAlbum;
    private TabLayout.Tab tabFolder;
    private ViewPager vpLocalMusic;
    private LocalMusicFragmentPagerAdapter musicFragmentPagerAdapter;
    private TabLayout tabLayoutBar;

    /*几个代表Fragment页面的常量*/
    public static final int PAGE_MUSIC = 0;
    public static final int PAGE_ARTIST = 1;
    public static final int PAGE_ALBUM = 2;
    public static final int PAGE_FOLDER = 3;
//    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private ImageView mIvPlay, mIvPlayNext, mIvBottomBarImage;
    private TextView mBottomBarTitle, mBottomBarArtist;
    private List<Music> musicList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setStatusBarColor(this, R.color.colorBarBottom);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_local_list);


        initViews();
        initMusicPlayImg();

        //init音乐列表
        SongModel.getInstance().setSongList(new MusicUtil().getMusic(MusicAppUtils.getContext()));
        musicList = SongModel.getInstance().getSongList();

        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MusicAppUtils.getContext().getResources().getString(R.string.app_name));
        //动态注册广播接收器
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(LocalListActivityReceiver, itFilter);
    }

    private void initViews() {

        /*使用适配器将ViewPager与Fragment绑定在一起*/
        vpLocalMusic = (ViewPager) findViewById(R.id.vp_local_music);
        musicFragmentPagerAdapter = new LocalMusicFragmentPagerAdapter(getSupportFragmentManager());
        vpLocalMusic.setAdapter(musicFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        tabLayoutBar = (TabLayout) findViewById(R.id.tab_layout_bar);
        tabLayoutBar.setupWithViewPager(vpLocalMusic);

        //指定Tab的位置
        tabMusic = tabLayoutBar.getTabAt(PAGE_MUSIC);
        tabArtist = tabLayoutBar.getTabAt(PAGE_ARTIST);
        tabAlbum = tabLayoutBar.getTabAt(PAGE_ALBUM);
        tabFolder = tabLayoutBar.getTabAt(PAGE_FOLDER);

        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mIvPlayNext = (ImageView) findViewById(R.id.iv_play_next);
        mIvBottomBarImage = (ImageView) findViewById(R.id.bottom_bar_image);
        mBottomBarTitle = (TextView) findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = (TextView) findViewById(R.id.bottom_bar_artist);
    }

    private void initMusicPlayImg() {
        Log.d(TAG, "113行 状态码 == " + MediaUtils.currentState);
        if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
            mIvPlay.setImageResource(R.drawable.ic_play_black);
        }else {
            mIvPlay.setImageResource(R.drawable.ic_pause_black);
        }
    }

    //播放键控制
    public void play(View view){
        Log.d(TAG, "此时的状态=="+MediaUtils.currentState);
        switch (MediaUtils.currentState) {
            case MediaStateCode.PLAY_START:
            case MediaStateCode.PLAY_CONTINUE:
                BroadcastUtils.sendPauseMusicBroadcast();
                break;
            case MediaStateCode.PLAY_PAUSE:
                BroadcastUtils.sendContinueMusicBroadcast();
                break;
            case MediaStateCode.PLAY_STOP:
                BroadcastUtils.sendPlayMusicBroadcast();
                break;
        }
    }

    //播放键控制
    public void playNext(View view){
        MediaUtils.next();
        MediaUtils.prepare(
                SongModel.getInstance().getSongList().
                        get(MediaUtils.currentSongPosition).getFileUrl());
        MediaUtils.start();

        BroadcastUtils.sendNoticeMusicPositionChanged();
    }

    /*收到UI界面更新的通知后，在此刷新UI*/
    private Handler LocalListActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = MediaUtils.currentSongPosition;

            mBottomBarTitle.setText(musicList.get(position).getTitle());
            mBottomBarArtist.setText(musicList.get(position).getArtist());

            String path = MusicUtil.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
            Log.d(TAG,"path="+path);
            if (null == path){
                mIvBottomBarImage.setImageResource(R.drawable.ic_log);
            }else{
                Glide.with(MusicAppUtils.getContext()).load(path).into(mIvBottomBarImage);
            }

            initMusicPlayImg();
        }
    };


    private BroadcastReceiver LocalListActivityReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            int state = intent.getIntExtra("state", 0);
            Log.d(TAG, "180行 action = " + action + "||其中 state == " + state + ";;");
            if (state == MediaStateCode.MUSIC_POSITION_CHANGED) {
                Log.d(TAG, "182行 action = " + action + "||其中 state == " + state + ";;");
                LocalListActivityHandler.sendEmptyMessage(1);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        LocalBroadcastManager.getInstance(
                MusicAppUtils.getContext()).unregisterReceiver(
                LocalListActivityReceiver);
    }

    /**
     * 设置状态栏颜色
     * @param activity
     * @param statusColor
     */
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        /*取消状态栏透明*/
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        /*添加Flag把状态栏设为可绘制模式*/
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        /*设置状态栏颜色*/
        window.setStatusBarColor(MusicAppUtils.getContext().getResources().getColor(statusColor));
        /*设置系统状态栏处于可见状态 | 文字颜色及图标为深色*/
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        /*让view不根据系统窗口来调整自己的布局*/
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }




    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        return super.onKeyDown(keyCode, event);
////        if (KeyEvent.KEYCODE_HOME == keyCode) {
////            //点击Home键所执行的代码
////        }
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            if ((System.currentTimeMillis()) > 2000) {
//
//            }
//
//        }
//        return super.onKeyDown(keyCode, event); // 不会回到 home 页面
//    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        ToastUtils.getToastShort("返回键坏了");
//    }

}

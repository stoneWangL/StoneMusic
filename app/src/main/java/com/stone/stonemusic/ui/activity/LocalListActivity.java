package com.stone.stonemusic.ui.activity;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalMusicFragmentPagerAdapter;
import com.stone.stonemusic.utils.MusicAppUtils;

public class LocalListActivity extends AppCompatActivity{
    public static final String TAG = "MainActivity";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, R.color.colorBarBottom);
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_local_list);


        initViews();
    }

    private void initViews() {

        /*使用适配器将ViewPager与Fragment绑定在一起*/
        vpLocalMusic = findViewById(R.id.vp_local_music);
        musicFragmentPagerAdapter = new LocalMusicFragmentPagerAdapter(getSupportFragmentManager());
        vpLocalMusic.setAdapter(musicFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        tabLayoutBar = findViewById(R.id.tab_layout_bar);
        tabLayoutBar.setupWithViewPager(vpLocalMusic);

        //指定Tab的位置
        tabMusic = tabLayoutBar.getTabAt(PAGE_MUSIC);
        tabArtist = tabLayoutBar.getTabAt(PAGE_ARTIST);
        tabAlbum = tabLayoutBar.getTabAt(PAGE_ALBUM);
        tabFolder = tabLayoutBar.getTabAt(PAGE_FOLDER);

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
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
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

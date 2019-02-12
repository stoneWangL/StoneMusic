package com.stone.stonemusic.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.PlayFragmentPagerAdapter;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.ui.View.CircleView;
import com.stone.stonemusic.utils.ActivityUtils;
import com.stone.stonemusic.utils.BroadcastUtils;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;
import com.stone.stonemusic.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "PlayActivity";
    private List<Music> musicList = new ArrayList<>();
    private LinearLayout mLinearLayout;

    private TabLayout tabLayoutBarPlay;
    private TabLayout.Tab tabPlay;
    private TabLayout.Tab tabLyric;
    private ViewPager viewPager;
    private PlayFragmentPagerAdapter playFragmentPagerAdapter;

    /*几个代表Fragment页面的常量*/
    public static final int PLAY_PAGE_IMAGE = 0;
    public static final int PLAY_PAGE_LYRIC = 1;

    private TextView tvMusicName, tvMusicArtist, tvCurrentTime, tvTotalTime;
    private SeekBar mSeekBar;
    private boolean seekBarIsChanging = false;/*记录seekBar是否改变*/
    private Thread seekBarThread;
    private CircleView cvLast, cvPlayOrPause, cvNext;
    private ImageView ivLast, ivPlayOrPause, ivNext;

    /*辅助回调的set方法,供fragment调用*/
    private PlayActivity.CallPlaysFragment mCallPlaysFragment;
    public void setCallPlaysFragment(PlayActivity.CallPlaysFragment fragment){
        this.mCallPlaysFragment = fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        init();

        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MusicAppUtils.getContext().getResources().getString(R.string.app_name));
        //动态注册广播接收器
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(PlayActivityReceiver, itFilter);

    }

    private void init() {
        musicList = SongModel.getInstance().getSongList();
        MusicAppUtils.addDestroyActivity(this, TAG); /*添加到待销毁的队列*/
        initView();
    }

    private void initView() {
        /*整个layout*/
        mLinearLayout = (LinearLayout) findViewById(R.id.play_activity_layout);

        /*Viewpager和tabLayout*/
        viewPager = (ViewPager) findViewById(R.id.vp_play);
        playFragmentPagerAdapter = new PlayFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(playFragmentPagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        tabLayoutBarPlay = (TabLayout) findViewById(R.id.tab_layout_bar_play);
        tabLayoutBarPlay.setupWithViewPager(viewPager);
        //指定Tab的位置
        tabPlay = tabLayoutBarPlay.getTabAt(PLAY_PAGE_IMAGE);
        tabLyric = tabLayoutBarPlay.getTabAt(PLAY_PAGE_LYRIC);

        /*标题 歌手*/
        tvMusicName = (TextView) findViewById(R.id.music_play_name);
        tvMusicArtist = (TextView) findViewById(R.id.music_play_artist);
        /*当前时长 进度条 全部时长*/
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        mSeekBar = (SeekBar) findViewById(R.id.musicSeekBar);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        /*上一曲 播放暂停 下一曲*/
        cvLast = (CircleView) findViewById(R.id.circle_play_last);
        ivLast = (ImageView) findViewById(R.id.iv_play_last);
        cvPlayOrPause = (CircleView) findViewById(R.id.circle_play_play_or_pause);
        ivPlayOrPause = (ImageView) findViewById(R.id.iv_play_play_or_pause);
        cvNext = (CircleView) findViewById(R.id.circle_play_next);
        ivNext = (ImageView) findViewById(R.id.iv_play_next);

        cvLast.setOnClickListener(this);
        cvPlayOrPause.setOnClickListener(this);
        cvNext.setOnClickListener(this);

        initControlPlayUI(); /*需要跟新的模块的设置*/

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /*进度发生改变时会触发*/
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d(TAG,"进度发生改变,时间为="+durationTime(progress));
                tvCurrentTime.setText(OtherUtils.durationTime(progress));
            }

            /*按住SeekBar时会触发*/
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"按住SeekBar");
                seekBarIsChanging = true; /*seekBar改变*/
            }

            /*放开SeekBar时触发*/
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"放开SeekBar");
                seekBarIsChanging = false; /*seekBar停止改变*/
                /*将media进度设置为当前seekBar的进度*/
                MediaUtils.getMediaPlayer().seekTo(seekBar.getProgress());

                seekBarThread = new Thread(new PlayActivity.SeekBarThread());
                seekBarThread.start(); /*启动线程*/
            }
        });
    }

    /*seekBar进度监听线程*/
    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (!seekBarIsChanging && MediaUtils.getMediaPlayer().isPlaying()) {
                /*将SeekBar位置设置到当前播放位置*/
                mSeekBar.setProgress(MediaUtils.getMediaPlayer().getCurrentPosition());
                try {
                    Thread.sleep(500); /*每100毫秒更新一次位置*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!seekBarIsChanging && !MediaUtils.getMediaPlayer().isPlaying()){
//                mMusicInfoUtil.setIsPlay(false);//此时音乐停止播放了
            }
        }
    }

    /*跟新 带有控制的View的UI（eg:上一曲 下一曲 播放暂停 是否喜欢 循环模式图标）*/
    private void initControlPlayUI() {
        /*整个layout*/
        int statusColor = ActivityUtils.initColor(this);
        mLinearLayout.setBackgroundColor(getResources().getColor(statusColor));

        /*标题 歌手*/
        int position = MediaUtils.currentSongPosition;
        tvMusicName.setText(musicList.get(position).getTitle());
        tvMusicArtist.setText(musicList.get(position).getArtist());

        /*当前时长 seekBar 全部时长*/
        tvCurrentTime.setText(OtherUtils.durationTime(0));
        mSeekBar.setProgress(0);
        mSeekBar.setMax((int)musicList.get(position).getDuration());
        mSeekBar.setProgress(MediaUtils.getMediaPlayer().getCurrentPosition());
        if (MediaUtils.getMediaPlayer().isPlaying()){
            seekBarThread = new Thread(new PlayActivity.SeekBarThread());
            seekBarThread.start(); /*启动seekBar监听线程*/
        }
        tvTotalTime.setText(OtherUtils.durationTime((int)musicList.get(position).getDuration()));

        /*上一曲 播放暂停 下一曲*/
        if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
            ivPlayOrPause.setImageResource(R.drawable.ic_play_white);
        }else {
            ivPlayOrPause.setImageResource(R.drawable.ic_pause_white);
        }



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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_play_last:
                Log.d(TAG, "上一曲");
                MediaUtils.last();
                MediaUtils.prepare(
                        SongModel.getInstance().getSongList().
                                get(MediaUtils.currentSongPosition).getFileUrl());
                MediaUtils.start();
                break;
            case R.id.circle_play_play_or_pause:
                Log.d(TAG, "播放暂停");
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
                break;
            case R.id.circle_play_next:
                Log.d(TAG, "下一曲");
                MediaUtils.next();
                MediaUtils.prepare(
                        SongModel.getInstance().getSongList().
                                get(MediaUtils.currentSongPosition).getFileUrl());
                MediaUtils.start();
                break;
        }
        BroadcastUtils.sendNoticeMusicPositionChanged();
    }

    /*收到UI界面更新的通知后，在此刷新UI*/
    private Handler PlayActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            initControlPlayUI();

//            String path = MusicUtil.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
////            Log.d(TAG,"path="+path);
//            if (null == path){
//                mIvBottomBarImage.setImageResource(R.drawable.ic_log);
//            }else{
//                Glide.with(MusicAppUtils.getContext()).load(path).into(mIvBottomBarImage);
//            }

        }
    };
    /*定义回调接口*/
    public interface CallPlaysFragment{
        void ChangeUI();
    }

    private BroadcastReceiver PlayActivityReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            int state = intent.getIntExtra("state", 0);
            if (state == MediaStateCode.MUSIC_POSITION_CHANGED) {
                Log.d(TAG, "182行 action = " + action + "||其中 state == " + state + ";;");
                PlayActivityHandler.sendEmptyMessage(1);

                /*调用回调方法ChangeUI，调用后Fragment重写的回调方法会被自动执行，从而在Fragment回调方法中通知handler更新UI*/
                if (null != mCallPlaysFragment) {
                    mCallPlaysFragment.ChangeUI();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*注销广播接收器*/
        LocalBroadcastManager.getInstance(
                MusicAppUtils.getContext()).unregisterReceiver(
                PlayActivityReceiver);
    }
}

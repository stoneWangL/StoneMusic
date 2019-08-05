package com.stone.stonemusic.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.PlayFragmentPagerAdapter;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.presenter.MusicObserverListener;
import com.stone.stonemusic.presenter.MusicObserverManager;
import com.stone.stonemusic.presenter.PlayControl;
import com.stone.stonemusic.View.CircleView;
import com.stone.stonemusic.View.ActivityView;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.OtherUtils;
import com.stone.stonemusic.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class PlayActivity extends AppCompatActivity implements View.OnClickListener
    ,MusicObserverListener{
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
    private CircleView cvLast, cvPlayOrPause, cvNext;
    private ImageView ivMode, ivLast, ivPlayOrPause, ivNext;

    /*辅助回调的set方法,供fragment调用*/
    private CallBackInterface mCallBackInterface;
    public void setCallBackInterface(CallBackInterface mCallBackInterface){
        this.mCallBackInterface = mCallBackInterface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);
        init();

    }

    private void init() {
        musicList = SongModel.getInstance().getSongList();
        MusicApplication.addDestroyActivity(this, TAG); /*添加到待销毁的队列*/
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
        MediaUtils.seekBarIsChanging = false; /*刚进入时，初始化是false*/
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);

        /*当前播放模式*/
        ivMode = (ImageView) findViewById(R.id.play_order_mode);

        /*上一曲 播放暂停 下一曲*/
        cvLast = (CircleView) findViewById(R.id.circle_play_last);
        ivLast = (ImageView) findViewById(R.id.iv_play_last);
        cvPlayOrPause = (CircleView) findViewById(R.id.circle_play_play_or_pause);
        ivPlayOrPause = (ImageView) findViewById(R.id.iv_play_play_or_pause);
        cvNext = (CircleView) findViewById(R.id.circle_play_next);
        ivNext = (ImageView) findViewById(R.id.iv_play_next);

        ivMode.setOnClickListener(this);
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
                MediaUtils.seekBarIsChanging = true; /*seekBar改变*/
            }

            /*放开SeekBar时触发*/
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"放开SeekBar");
                MediaUtils.seekBarIsChanging = false; /*seekBar停止改变*/
                /*将media进度设置为当前seekBar的进度*/
                MediaUtils.getMediaPlayer().seekTo(seekBar.getProgress());

            }
        });
    }

    /*更新 带有控制的View的UI（eg:上一曲 下一曲 播放暂停 是否喜欢 循环模式图标）*/
    private void initControlPlayUI() {
        /*整个layout*/
        int statusColor = ActivityView.initColor(this);
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
        tvTotalTime.setText(OtherUtils.durationTime((int)musicList.get(position).getDuration()));

        /*播放模式*/
        switch (MediaUtils.currentLoopMode) {
            case MediaStateCode.LOOP_MODE_ONLY_ONE:
                ivMode.setImageResource(R.drawable.ic_play_loop_one_white_24dp);
                break;
            case MediaStateCode.LOOP_MODE_ORDER_LIST:
                ivMode.setImageResource(R.drawable.ic_play_in_order_white_24dp);
                break;
            case MediaStateCode.LOOP_MODE_OUT_OF_ORDER:
                ivMode.setImageResource(R.drawable.ic_play_out_of_order_white_24dp);
                break;
        }

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
//        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
        overridePendingTransition(R.anim.stop,R.anim.push_up_out);
    }

    public void downBack(View view){
        onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_order_mode:
                /*切换播放模式*/
                if (MediaUtils.currentLoopMode == MediaStateCode.LOOP_MODE_ORDER_LIST) {
                    MediaUtils.currentLoopMode = MediaStateCode.LOOP_MODE_OUT_OF_ORDER;
                    ToastUtils.getToastShort(this.getString(R.string.play_mode_out_of_order));
                } else if (MediaUtils.currentLoopMode == MediaStateCode.LOOP_MODE_OUT_OF_ORDER) {
                    MediaUtils.currentLoopMode = MediaStateCode.LOOP_MODE_ONLY_ONE;
                    ToastUtils.getToastShort(this.getString(R.string.play_mode_only_one));
                } else {
                    MediaUtils.currentLoopMode = MediaStateCode.LOOP_MODE_ORDER_LIST;
                    ToastUtils.getToastShort(this.getString(R.string.play_mode_order_list));
                }
                PlayActivityHandler.sendEmptyMessage(1);
                break;
            case R.id.circle_play_last:
                /*上一曲*/
                PlayControl.controlBtnLast();
                break;
            case R.id.circle_play_play_or_pause:
                /*播放暂停*/
                PlayControl.controlBtnPlaySameSong();
                break;
            case R.id.circle_play_next:
                /*下一曲*/
                PlayControl.controlBtnNext();
                break;
        }
    }

    /*收到UI界面更新的通知后，在此刷新UI*/
    private Handler PlayActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    /*将SeekBar位置设置到当前播放位置*/
                    mSeekBar.setProgress(MediaUtils.getMediaPlayer().getCurrentPosition());
                    break;
                case 1:
                    initControlPlayUI();
                    break;
            }

        }
    };
    /*定义回调接口*/
    public interface CallBackInterface{
        void ChangeUI();
    }

    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                PlayActivityHandler.sendEmptyMessage(1);
                /*调用回调方法ChangeUI，调用后Fragment重写的回调方法会被自动执行，从而在Fragment回调方法中通知handler更新UI*/
                if (null != mCallBackInterface) {
                    mCallBackInterface.ChangeUI();
                }
                break;

            case MediaStateCode.PLAY_CONTINUE:
            case MediaStateCode.PLAY_STOP:
            case MediaStateCode.PLAY_PAUSE:
                PlayActivityHandler.sendEmptyMessage(1);
                break;
            case MediaStateCode.MUSIC_SEEKBAR_CHANGED:
                PlayActivityHandler.sendEmptyMessage(0);
                break;

            default:
                Log.i(TAG, "observerUpData->观察者类数据已刷新");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCallBackInterface = null; //释放引用
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);
    }

}

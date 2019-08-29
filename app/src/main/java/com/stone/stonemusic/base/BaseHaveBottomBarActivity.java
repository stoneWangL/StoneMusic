package com.stone.stonemusic.base;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.JumpToOtherWhere;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.playControl.PlayControl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:32
 * @Description:
 */
public abstract class BaseHaveBottomBarActivity extends BaseActivity
        implements MusicObserverListener, JumpToOtherView {
    private static String TAG = "FindActivity";
    protected CardView bottomCardViewLayout;
    protected LinearLayout bottomLinearLayout, layoutBottom;
    protected ImageView mIvPlay, mIvPlayNext, mIvBottomBarImage;
    protected TextView mBottomBarTitle, mBottomBarArtist;
    protected List<Music> chooseMusicList = new ArrayList<>(); //当前选择的 歌曲列表
    protected JumpToOtherWhere jumpToOtherWhere;


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void initListener() {
        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);
        //初始化跳转类
        jumpToOtherWhere = new JumpToOtherWhere(this);

        bottomCardViewLayout = findViewById(R.id.cardView_bottom_bar_layout);
        bottomLinearLayout = findViewById(R.id.bottom_bar_layout);
        layoutBottom = findViewById(R.id.layout_bottom);

        mIvPlay = findViewById(R.id.iv_bottom_play);
        mIvPlayNext = findViewById(R.id.iv_bottom_play_next);
        mIvBottomBarImage = findViewById(R.id.bottom_bar_image);
        mBottomBarTitle = findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = findViewById(R.id.bottom_bar_artist);

        initListenerOther(); //其他需要初始化注册的方法
    }

    /**
     * 子类实现
     * 其他需要初始化注册的方法
     */
    protected abstract void initListenerOther();

    @Override
    protected void initData() {
        //设置底部播放栏参数
        initMusicPlayImg();
        initDataOther(); //其他需要初始化数据的方法
    }

    /**
     * 子类实现
     * 其他需要初始化数据的方法
     */
    protected abstract void initDataOther();

    ////设置底部播放栏参数
    public void initMusicPlayImg() {
        try {
            chooseMusicList = SongModel.getInstance().getChooseSongList(); //初始化选中歌曲列表
            if (null != chooseMusicList) {
                int position = MediaUtils.currentSongPosition;

                mBottomBarTitle.setText(chooseMusicList.get(position).getTitle());
                mBottomBarArtist.setText(chooseMusicList.get(position).getArtist());

                String imagePath; //歌曲图片路径
                if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
                    imagePath = chooseMusicList.get(position).getPicUrl();
                } else { //当前为播放本地歌曲状态
                    imagePath = MusicResources.getAlbumArt(new Long(chooseMusicList.get(position).getAlbum_id()).intValue());
                }
                if (null == imagePath || imagePath.equals("")) {
                    mIvBottomBarImage.setImageResource(R.drawable.play_background02);
                } else {
                    Glide.with(MusicApplication.getContext()).load(imagePath).into(mIvBottomBarImage);
                }
                if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                        MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
                    mIvPlay.setImageResource(R.drawable.ic_play_black);
                } else {
                    mIvPlay.setImageResource(R.drawable.ic_pause_black);
                }
//                listAdapter.notifyDataSetChanged();
                bottomLinearLayout.setVisibility(View.VISIBLE);
            } else {
                layoutBottom.setVisibility(View.GONE);
                bottomLinearLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.PLAY_START:
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                Log.i(TAG, "MUSIC_POSITION_CHANGED");
                ThisBaseActivityHandler.sendEmptyMessage(1);
                //子类实现，其他需要在observerUpData()回调中实现的,关于 PLAY_START，MUSIC_POSITION_CHANGED 的方法
                observerUpDataOtherPlayStartPositionChange();
                break;

            case MediaStateCode.PLAY_CONTINUE:
            case MediaStateCode.PLAY_STOP:
            case MediaStateCode.PLAY_PAUSE:
                ThisBaseActivityHandler.sendEmptyMessage(1);
                //子类实现，其他需要在observerUpData()回调中实现的,关于 PLAY_CONTINUE，PLAY_STOP，PLAY_PAUSE 的方法
                observerUpDataOtherContinueStopPause();
                break;
        }
    }

    /**
     * 子类实现，其他需要在observerUpData()回调中实现的,关于 PLAY_START，MUSIC_POSITION_CHANGED 的方法
     */
    protected abstract void observerUpDataOtherPlayStartPositionChange();

    /**
     * 子类实现，其他需要在observerUpData()回调中实现的,关于 PLAY_CONTINUE，PLAY_STOP，PLAY_PAUSE 的方法
     */
    protected abstract void observerUpDataOtherContinueStopPause();

    /**
     * 收到UI界面更新的通知后，在此刷新UI
     */
    private Handler ThisBaseActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            initMusicPlayImg();
        }
    };

    /**
     * bottomBar的播放按钮点击事件
     * @param view
     */
    public void play(View view) {
        if (chooseMusicList.size() > 0) {
            PlayControl.controlBtnPlaySameSong();
        }
    }

    /**
     * bottomBar的下一曲按钮点击事件
     * @param view
     */
    public void playNext(View view) {
        if (chooseMusicList.size() > 0) {
            PlayControl.controlBtnNext();
        }
    }

    /**
     * bottomBar的下一曲按钮点击事件
     * @param view
     */
    public void bottomBarClick(View view) {
        //跳转到播放界面
        jumpToOtherWhere.GoToPlayActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);

        onDestroyOther(); //子类实现，其他需要在onDestroy()回调中实现的方法
    }

    /**
     * 子类实现，其他需要在onDestroy()回调中实现的方法
     */
    protected abstract void onDestroyOther();
}

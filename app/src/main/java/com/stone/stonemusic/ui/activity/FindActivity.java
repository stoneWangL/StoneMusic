package com.stone.stonemusic.ui.activity;

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
import com.stone.stonemusic.base.BaseActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.MusicResources;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:28
 * @Description:
 */
public class FindActivity extends BaseActivity
        implements MusicObserverListener, View.OnClickListener {
    private CardView bottomCardViewLayout;
    private LinearLayout bottomLinearLayout, layoutBottom;
    private ImageView mIvPlay, mIvPlayNext, mIvBottomBarImage;
    private TextView mBottomBarTitle, mBottomBarArtist;
    private List<Music> chooseMusicList = new ArrayList<>(); //当前选择的 歌曲列表

    private static String TAG = "FindActivity";

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    protected void initListener() {
        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);
        bottomCardViewLayout = findViewById(R.id.cardView_bottom_bar_layout);
        bottomLinearLayout = findViewById(R.id.bottom_bar_layout);
        bottomLinearLayout.setOnClickListener(this);
        layoutBottom = findViewById(R.id.layout_bottom);

        mIvPlay = findViewById(R.id.iv_bottom_play);
        mIvPlayNext = findViewById(R.id.iv_bottom_play_next);
        mIvBottomBarImage = findViewById(R.id.bottom_bar_image);
        mBottomBarTitle = findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = findViewById(R.id.bottom_bar_artist);
    }

    @Override
    protected void initData() {
        //设置底部播放栏参数
        initMusicPlayImg();
    }

    ////设置底部播放栏参数
    public void initMusicPlayImg() {
        try {
            chooseMusicList = SongModel.getInstance().getChooseSongList(); //初始化选中歌曲列表
            if (null != chooseMusicList) {
                int position = MediaUtils.currentSongPosition;
//                Log.i(TAG, "initMusicPlayImg() -> MediaUtils.currentSongPosition = " + MediaUtils.currentSongPosition);

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


    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.PLAY_START:
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                Log.i(TAG, "MUSIC_POSITION_CHANGED");
                ThisActivityHandler.sendEmptyMessage(1);
                break;

            case MediaStateCode.PLAY_CONTINUE:
            case MediaStateCode.PLAY_STOP:
            case MediaStateCode.PLAY_PAUSE:
                ThisActivityHandler.sendEmptyMessage(1);
                break;
        }
    }

    /*
     * 收到UI界面更新的通知后，在此刷新UI
     * */
    private Handler ThisActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            initMusicPlayImg();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);
    }

    @Override
    public void onClick(View v) {

    }
}

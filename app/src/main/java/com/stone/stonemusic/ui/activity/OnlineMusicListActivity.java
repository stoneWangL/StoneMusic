package com.stone.stonemusic.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.View.OnLineView;
import com.stone.stonemusic.adapter.OnlineMusicListAdapter;
import com.stone.stonemusic.base.BaseActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.JumpToOtherWhere;
import com.stone.stonemusic.presenter.impl.OnLineListPresenterImpl;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.ToastUtils;
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
 * @CreateDate: 2019/8/18 21:38
 * @Description:
 */
public class OnlineMusicListActivity extends BaseActivity
        implements OnLineView, View.OnClickListener, JumpToOtherView {
    private static final String TAG = "OnlineMusicListActivity";
    PlayListBean data;
    ImageView imageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    public ProgressDialog mDialog;
    OnLineListPresenterImpl onLineListPresenter;
    private OnlineMusicListAdapter listAdapter;
    private List<Music> chooseMusicList = new ArrayList<>(); //当前选择的 歌曲列表
    private List<Music> onLineMusicList = new ArrayList<>(); //网络歌曲列表
    private LinearLayout bottomLayout, playLayout, playNextLayout;
    private ImageView mIvPlay, mIvPlayNext, mIvBottomBarImage;
    private TextView mBottomBarTitle, mBottomBarArtist;
    private JumpToOtherWhere jumpToOtherWhere;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online_music_list;
    }

    @Override
    protected void initListener() {
        //自定义toolbar
        toolbar = findViewById(R.id.toolbar_land_show);
        setSupportActionBar(toolbar);
        //设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_land_show);
        imageView = findViewById(R.id.imageViewTheme);
        recyclerView = findViewById(R.id.recycleView_onlineMusicList);
        //给rv设置线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //为滑动顺畅的设置
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //底部播放栏
        bottomLayout = findViewById(R.id.bottom_bar_layout);
        bottomLayout.setOnClickListener(this);
        playLayout = findViewById(R.id.play_layout);
        playNextLayout = findViewById(R.id.play_next_layout);
        mIvPlay = findViewById(R.id.iv_play);
        mIvPlayNext = findViewById(R.id.iv_play_next);
        mIvBottomBarImage = findViewById(R.id.bottom_bar_image);
        mBottomBarTitle = findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = findViewById(R.id.bottom_bar_artist);

        //初始化跳转类
        jumpToOtherWhere = new JumpToOtherWhere(this);

        //P层实例化
        onLineListPresenter = new OnLineListPresenterImpl(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_bar_layout: //点击底部歌曲信息显示栏，跳转到播放界面
                //跳转到播放界面
                jumpToOtherWhere.GoToPlayActivity();
                break;
            case R.id.play_layout: //播放键控制
                Log.i(TAG, "此时的状态=="+MediaUtils.currentState);
                if (chooseMusicList.size() > 0) {
                    PlayControl.controlBtnPlaySameSong();
                }
                break;
            case R.id.play_next_layout: //播放键控制
                Log.i(TAG, "此时的状态=="+MediaUtils.currentState);
                if (chooseMusicList.size() > 0) {
                    PlayControl.controlBtnPlaySameSong();
                }
                break;
        }
    }

    @Override
    protected void initData() {
        //通过Intent获取传过来的内容,来填充
        Intent intent = getIntent();
        data = intent.getParcelableExtra("obj");
        //设置title
        if (data.getName() != null)
            collapsingToolbar.setTitle(data.getName());
        //设置titleImage
        if (data.getCoverImgUrl() != null)
            Glide.with(this).load(data.getCoverImgUrl()).into(imageView);
        if(data.getId() != null) {
            showDialog();
            onLineListPresenter.startTask(data.getId()); //开启查询任务

        }

        //设置底部播放栏参数
        initMusicPlayImg();

    }

    ////设置底部播放栏参数
    public void initMusicPlayImg() {

        try {
            chooseMusicList = SongModel.getInstance().getChooseSongList(); //初始化选中歌曲列表
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    /**
     * 顶部item的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回按钮
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadSuccess() {
        onLineMusicList = SongModel.getInstance().getOnLineSongList(); //初始化 网络歌曲列表
        //        Log.i(TAG, "onLineMusicList.size=" + onLineMusicList.size());
        //给list设置数据 -> 网络歌曲list
        listAdapter = new OnlineMusicListAdapter(onLineMusicList, this,R.layout.item_music);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.cancel();
                ToastUtils.getToastShort("获取列表成功");
                recyclerView.setAdapter(listAdapter);
            }
        });

    }

    @Override
    public void loadFalse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.cancel();
                ToastUtils.getToastShort("获取列表失败");
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        //本地item被点击，说明当前需要播放的歌曲需要切换到在线
        SongModel.getInstance().setMusicType(PlayType.OnlineType); //将播放类型切换为OnlineType
        //设置当前播放的歌曲类型为在线歌曲
        SongModel.getInstance().setChooseSongList(SongModel.getInstance().getOnLineSongList());

        Log.i(TAG, "onItemClick->position == " + position + "chooseSongList.size()=" + SongModel.getInstance().getChooseSongList().size());
        int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition(); //获取上一个选中的位置
        MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
        Log.i(TAG, "歌曲路径=" + onLineMusicList.get(position).getFileUrl());
        if (position == lastPosition && null != jumpToOtherWhere) //点击的是正在播放的歌曲
            jumpToOtherWhere.GoToPlayActivity(); //调用父类方法，跳转到播放Activity
        else { //点击的是新的歌曲
            PlayControl.controlBtnPlayDiffSong();
            mBottomBarTitle.setText(onLineMusicList.get(position).getTitle()); //更新音乐名
            mBottomBarArtist.setText(onLineMusicList.get(position).getArtist()); //更新音乐作者
            //更新音乐专辑图
            String imagePath; //歌曲图片路径
            if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
                imagePath = onLineMusicList.get(position).getPicUrl();
            } else { //当前为播放本地歌曲状态
                imagePath = MusicResources.getAlbumArt(new Long(onLineMusicList.get(position).getAlbum_id()).intValue());
            }
            if (null == imagePath || imagePath.equals("")) {
                mIvBottomBarImage.setImageResource(R.drawable.play_background02);
            } else {
                Glide.with(MusicApplication.getContext()).load(imagePath).into(mIvBottomBarImage);
            }

            //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
            ItemViewChoose.getInstance().setItemChoosePosition(position); //设置当前选中的位置
            listAdapter.notifyDataSetChanged();
        }


    }


    /**
     *显示Dialog
     */
    private void showDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在加载列表...");
        mDialog.show();
    }


}

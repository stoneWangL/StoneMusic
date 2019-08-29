package com.stone.stonemusic.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.HomeAdapter;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.InitMusicModelTask;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.presenter.impl.JumpToOtherWhere;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.utils.playControl.PlayControl;
import com.stone.stonemusic.View.ActivityView;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        MusicObserverListener, View.OnClickListener, JumpToOtherView{
    public static final String TAG = "HomeActivity";

    public ProgressDialog mDialog;

    private TabLayout.Tab tabLocal;
    private TabLayout.Tab tabGeDan;
    private TabLayout.Tab tabStyle;

    private ViewPager viewPager;
    private HomeAdapter homeAdapter;
    private TabLayout tabLayoutBar;

    /*几个代表Fragment页面的常量*/
    public static final int PAGE_LOCAL = 0;
    public static final int PAGE_GE_DAN = 1;
    public static final int PAGE_STYLE = 2;

    private LinearLayout bottomLinearLayout;
    CardView cardView;
    private ImageView mIvPlay, mIvPlayNext, mIvBottomBarImage;
    private TextView mBottomBarTitle, mBottomBarArtist;
    public List<Music> musicList = new ArrayList<>();

    private JumpToOtherWhere jumpToOtherWhere;
    private InitMusicModelTask initMusicModelTask;

    /*辅助回调的set方法*/
    private CallBackInterface mCallBackInterface;
    public void setCallBackInterface(CallBackInterface myCallBackInterface){
        this.mCallBackInterface = myCallBackInterface;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_local_list);

        //设置顶部状态栏颜色
        ActivityView.setStatusBarColor(this, R.color.colorStoneThemeShallow, true);

        //添加当前活动到待销毁队列
        MusicApplication.addDestroyActivity(this, TAG);

        //添加进观察者队列
        MusicObserverManager.getInstance().add(this);

        //初始化跳转类
        jumpToOtherWhere = new JumpToOtherWhere(this);
        //初始化初始化MusicModel类(AsyncTask)
        initMusicModelTask = new InitMusicModelTask(this);

        //展示Dialog
        showDialog();
        //执行任务在其中初始化歌手列表，初始结束后，结束Dialog
        initMusicModelTask.execute();
    }

    /**
     *显示Dialog
     */
    private void showDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在加载音乐文件");
        mDialog.show();
    }

    public void initViews() {
        cardView = findViewById(R.id.cardView_bottom_bar_layout);
        bottomLinearLayout = findViewById(R.id.bottom_bar_layout);
        bottomLinearLayout.setOnClickListener(this);

        /*使用适配器将ViewPager与Fragment绑定在一起*/
        viewPager = findViewById(R.id.vp_local_music);

        homeAdapter = new HomeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeAdapter);

        //将TabLayout与ViewPager绑定在一起
        tabLayoutBar = findViewById(R.id.tab_layout_bar);
        tabLayoutBar.setupWithViewPager(viewPager);

        //指定Tab的位置
        tabLocal = tabLayoutBar.getTabAt(PAGE_LOCAL);
        tabGeDan= tabLayoutBar.getTabAt(PAGE_GE_DAN);
        tabStyle = tabLayoutBar.getTabAt(PAGE_STYLE);

        mIvPlay = findViewById(R.id.iv_bottom_play);
        mIvPlayNext = findViewById(R.id.iv_bottom_play_next);
        mIvBottomBarImage = findViewById(R.id.bottom_bar_image);
        mBottomBarTitle = findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = findViewById(R.id.bottom_bar_artist);
    }

    public void initMusicPlayImg() {

        try {
            musicList = SongModel.getInstance().getChooseSongList();
            if (null != musicList) {
                int position = MediaUtils.currentSongPosition;

                mBottomBarTitle.setText(musicList.get(position).getTitle());
                mBottomBarArtist.setText(musicList.get(position).getArtist());

                //歌曲图片
                String imagePath; //歌曲图片路径
                if (musicList.get(position).getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
                    imagePath = musicList.get(position).getPicUrl();
                } else { //当前为播放本地歌曲状态
                    imagePath = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
                }
                //            Log.d(TAG,"imagePath="+imagePath);
                if (null == imagePath || imagePath.equals("")) {
                    mIvBottomBarImage.setImageResource(R.drawable.play_background02);
                } else {
                    Glide.with(MusicApplication.getContext()).load(imagePath).into(mIvBottomBarImage);
                }
                //播放控制按钮
                if (MediaUtils.currentState == MediaStateCode.PLAY_PAUSE ||
                        MediaUtils.currentState == MediaStateCode.PLAY_STOP) {
                    mIvPlay.setImageResource(R.drawable.ic_play_black);
                } else {
                    mIvPlay.setImageResource(R.drawable.ic_pause_black);
                }
                cardView.setVisibility(View.VISIBLE);
            } else {
                //list为空，底部控制栏隐藏
                cardView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    /*
    * 收到UI界面更新的通知后，在此刷新UI
    * */
    private Handler LocalListActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            initMusicPlayImg();
        }
    };


    //播放键控制
    public void play(View view) {
//        Log.i(TAG, "此时的状态=="+MediaUtils.currentState);
        if (musicList.size() > 0) {
            PlayControl.controlBtnPlaySameSong();
        }

    }

    //播放键控制
    public void playNext(View view) {
        if (musicList.size() > 0) {
            PlayControl.controlBtnNext();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击底部歌曲信息显示栏，跳转到播放界面
            case R.id.bottom_bar_layout:
                if (musicList.size() > 0) {
                    jumpToOtherWhere.GoToPlayActivity();
                }
                break;

        }
    }

    //点击进入左侧设置界面
    public void clickLeftTabItem(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in,R.anim.stop);
    }

    /**
     * 点击进入查找界面
     * @param view
     */
    public void clickRightFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.left_in,R.anim.stop);
    }

    /*定义回调接口*/
    public interface CallBackInterface {
        void ChangeUI();
    }

    //观察者更新数据方法
    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.MUSIC_INIT_FINISHED:
                Log.e(TAG, "Artist更新完毕");
                break;
            case MediaStateCode.PLAY_START:
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                LocalListActivityHandler.sendEmptyMessage(1);
                /*调用回调方法ChangeUI，调用后MusicListFragment重写的回调方法会被自动执行，从而在MusicListFragment回调方法中通知handler更新UI*/
                if (null != mCallBackInterface) {
                    mCallBackInterface.ChangeUI();
                }
                break;

            case MediaStateCode.PLAY_CONTINUE:
            case MediaStateCode.PLAY_STOP:
            case MediaStateCode.PLAY_PAUSE:
                LocalListActivityHandler.sendEmptyMessage(1);
                break;
        }
//        Log.i(TAG, "observerUpData->观察者类数据已刷新");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从观察者队列中移除
        MusicObserverManager.getInstance().remove(this);
        //回调接口引用指空
        mCallBackInterface = null;
    }

}

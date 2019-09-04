package com.stone.stonemusic.UI.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.View.PlayFatherView;
import com.stone.stonemusic.adapter.ChooseListAdapter;
import com.stone.stonemusic.adapter.LrcListAdapter;
import com.stone.stonemusic.adapter.PlayFragmentPagerAdapter;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.PlayFatherPresenterImpl;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.playControl.PlayControl;
import com.stone.stonemusic.View.CircleView;
import com.stone.stonemusic.View.ActivityView;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.OtherUtils;
import com.stone.stonemusic.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;


public class PlayActivity extends AppCompatActivity
        implements View.OnClickListener, MusicObserverListener, PlayFatherView {
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
    private ImageView ivMode, ivLast, ivPlayOrPause, ivNext, ivChooseList;
    private ImageView iconLyc;

    ImageView imageViewPopupPic;
    TextView popupTitle ,popupArtist;
    PopupWindow mPopWindow;
    PlayFatherPresenterImpl playFatherPresenter;
    List<Music> PopupList;
    Music PopupMusic;

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
        playFatherPresenter = new PlayFatherPresenterImpl(this); //P层初始化
        init();

    }

    private void init() {
        musicList = SongModel.getInstance().getChooseSongList();
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
        ivChooseList = findViewById(R.id.play_order_list);

        /*上一曲 播放暂停 下一曲*/
        cvLast = (CircleView) findViewById(R.id.circle_play_last);
        ivLast = (ImageView) findViewById(R.id.iv_play_last);
        cvPlayOrPause = (CircleView) findViewById(R.id.circle_play_play_or_pause);
        ivPlayOrPause = (ImageView) findViewById(R.id.iv_play_play_or_pause);
        cvNext = (CircleView) findViewById(R.id.circle_play_next);
        ivNext = (ImageView) findViewById(R.id.iv_play_next);

        ivMode.setOnClickListener(this);
        ivChooseList.setOnClickListener(this);
        cvLast.setOnClickListener(this);
        cvPlayOrPause.setOnClickListener(this);
        cvNext.setOnClickListener(this);

        //喜欢，歌词搜索，分享，设置
        iconLyc = findViewById(R.id.play_lyc);
        iconLyc.setOnClickListener(this);


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
        mSeekBar.setMax(musicList.get(position).getDuration());
        mSeekBar.setProgress(MediaUtils.getMediaPlayer().getCurrentPosition());
        tvTotalTime.setText(OtherUtils.durationTime(musicList.get(position).getDuration()));

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
                //使用观察者管理类通知，音乐源已改变需要更新
//                MusicObserverManager.getInstance().notifyObserver(MediaStateCode.MUSIC_POSITION_CHANGED);
                break;
            case R.id.play_order_list:
                //显示当前播放列表
                showChooseListPopupWindow();
                break;
            case R.id.play_lyc:
                Log.d(TAG, "点击歌词图标");
                if (null != musicList){
                    final Music music = musicList.get(MediaUtils.currentSongPosition);
                    String findStr = "" + music.getTitle() + "-" + music.getArtist();
                    playFatherPresenter.clickIconLyc(findStr, music);
                }
                break;
        }
    }





    @Override
    public void onLrcItemClick(final Music song) {
        AlertDialog dialog = new AlertDialog.Builder(PlayActivity.this)
                .setTitle("选择")
                .setMessage("确定选择"+song.getTitle()+ "-" + song.getArtist()+".lrc 这个歌词么?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //下载歌词
                        dialog.dismiss();
                        Music desMusic = musicList.get(MediaUtils.currentSongPosition);
                        playFatherPresenter.getLrcOnline(song, desMusic);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void GetLrcListFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getToastShort("歌曲列表获取失败");
            }
        });
    }




    public void DownloadLrcSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != mPopWindow)
                    mPopWindow.dismiss();//让PopupWindow消失
            }
        });
    }

    @Override
    public void DownloadLrcFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getToastShort("歌曲下载失败");
            }
        });
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
                case 2:
                    if (null != PopupList){
                        showPopupWindow(PopupList);
                    }
                    break;
            }

        }
    };

    @Override
    public void GetLrcListSuccess(List<Music> list) {
        PopupList = list;
        PlayActivityHandler.sendEmptyMessage(2); //showPopupWindow

    }

    /**
     * 显示当前播放列表list
     */
    private void showChooseListPopupWindow() {
        if (null == musicList) return;
        final Music desSong = musicList.get(MediaUtils.currentSongPosition); //当前播放歌曲
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup, null);
//        ActionBar.LayoutParams.WRAP_CONTENT //自适应尺寸
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        imageViewPopupPic = contentView.findViewById(R.id.iv_popup_pic);
        popupTitle = contentView.findViewById(R.id.tv_popup_title);
        popupArtist = contentView.findViewById(R.id.tv_popup_artist);

        popupTitle.setText("歌曲名：" + desSong.getTitle()); //设置歌名
        popupArtist.setText(desSong.getArtist()); //设置歌手
        String imagePath; //歌曲图片路径
        if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
            imagePath = desSong.getPicUrl();
        } else { //当前为播放本地歌曲状态
            imagePath = MusicResources.getAlbumArt(new Long(desSong.getAlbum_id()).intValue());
        }
        if (null == imagePath || imagePath.equals("")) {
            imageViewPopupPic.setImageResource(R.drawable.play_background02);
        } else {
            Glide.with(this).load(imagePath).into(imageViewPopupPic);
        }

        //设置各个控件的点击响应
        final ListView listView = contentView.findViewById(R.id.listView_popup);
        final ChooseListAdapter adapter = new ChooseListAdapter(this, R.layout.item_music_2, musicList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                List<Music> lastMusicList = SongModel.getInstance().getChooseSongList();

                //设置当前播放的歌曲类型为本地歌曲
//                SongModel.getInstance().setChooseSongList(SongModel.getInstance().getLocalSongList());

                Log.d(TAG, "showChooseListPopupWindow()->位置："+position+"; 歌名："+musicList.get(position).getTitle());

                MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
                int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition();
                //点击的是正在播放的歌曲
//                && lastMusicList.get(position).getMusicId().equals(
//                        SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId())
                if (position == lastPosition) {
                    //收回popup
//                    jumpToOtherWhere.GoToPlayActivity(); //调用父类方法，跳转到播放Activity
                }
                //点击的不是当前播放的歌曲
                else {
                    PlayControl.controlBtnPlayDiffSong(); //播放音乐
                    popupTitle.setText("歌曲名：" + musicList.get(position).getTitle()); //更新音乐名
                    popupArtist.setText(musicList.get(position).getArtist()); //更新音乐作者
                    //更新音乐专辑图
                    String imagePath; //歌曲图片路径
                    if (musicList.get(position).getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
                        imagePath = musicList.get(position).getPicUrl();
                    } else { //当前为播放本地歌曲状态
                        imagePath = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
                    }
                    if (null == imagePath || imagePath.equals("")) {
                        imageViewPopupPic.setImageResource(R.drawable.play_background02);
                    } else {
                        Glide.with(getApplication()).load(imagePath).into(imageViewPopupPic);
                    }

                    //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
                    ItemViewChoose.getInstance().setItemChoosePosition(position);
                }

                adapter.notifyDataSetChanged(); //更新adapter
            }
        });
        //是否具有获取焦点的能力
        mPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_play, null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * @param list 查询的可能有相同歌词的 List<Music> List
     */
    private void showPopupWindow(List<Music> list) {
        final Music desSong = musicList.get(MediaUtils.currentSongPosition); //当前播放歌曲
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup, null);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, 1000, true);
        mPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        imageViewPopupPic = contentView.findViewById(R.id.iv_popup_pic);
        popupTitle = contentView.findViewById(R.id.tv_popup_title);
        popupArtist = contentView.findViewById(R.id.tv_popup_artist);

        popupTitle.setText("歌曲名：" + desSong.getTitle()); //设置歌名
        popupArtist.setText(desSong.getArtist()); //设置歌手
        String imagePath; //歌曲图片路径
        if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
            imagePath = desSong.getPicUrl();
        } else { //当前为播放本地歌曲状态
            imagePath = MusicResources.getAlbumArt(new Long(desSong.getAlbum_id()).intValue());
        }
        if (null == imagePath || imagePath.equals("")) {
            imageViewPopupPic.setImageResource(R.drawable.play_background02);
        } else {
            Glide.with(this).load(imagePath).into(imageViewPopupPic);
        }

        //设置各个控件的点击响应
        final ListView listView = contentView.findViewById(R.id.listView_popup);
        LrcListAdapter adapter = new LrcListAdapter(this, R.layout.item_music_2, list, this);
        listView.setAdapter(adapter);
        //是否具有获取焦点的能力
        mPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_play, null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }


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
                Log.i(TAG, "observerUpData->接收消息未判断");
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

package com.stone.stonemusic.UI.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalMusicAdapter;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.presenter.impl.JumpToOtherWhere;
import com.stone.stonemusic.UI.activity.HomeActivity;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.PlayControl;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.playControl.MusicResources;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LocalSingleSongFragment extends Fragment implements
        HomeActivity.CallBackInterface, JumpToOtherView {
    public static final String TAG = "MusicListFragment";
    private ListView listView;
    private List<Music> musicList = new ArrayList<>();
    private static LocalMusicAdapter adapter;

    private TextView mNoMusic;
    private TextView mBottomBarTitle;
    private TextView mBottomBarArtist;
    private ImageView mIvPlay;
    private ImageView mIvBottomBarImage;
    private HomeActivity fatherActivity = null;
    private JumpToOtherWhere jumpToOtherWhere;

    public LocalSingleSongFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fatherActivity = ((HomeActivity)context);
        fatherActivity.setCallBackInterface(this);
        jumpToOtherWhere = new JumpToOtherWhere(fatherActivity);
    }
    @Override
    public void ChangeUI() {
        Log.d(TAG, "这里是Fragment的ChangeUI()，这里被回调了");
        LocalListActivityHandler.sendEmptyMessage(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        mNoMusic = (TextView) view.findViewById(R.id.tv_no_music);
        listView = (ListView) view.findViewById(R.id.lv_music_list);

        mBottomBarTitle = (TextView) getActivity().findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = (TextView) getActivity().findViewById(R.id.bottom_bar_artist);
        mIvPlay = (ImageView) getActivity().findViewById(R.id.iv_bottom_play);
        mIvBottomBarImage = (ImageView) getActivity().findViewById(R.id.bottom_bar_image);

        readMusic();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Music> lastMusicList = SongModel.getInstance().getChooseSongList();
                //本地item被点击，说明当前需要播放的歌曲需要切换到本地
                SongModel.getInstance().setMusicType(PlayType.LocalType); //将播放类型切换为LocalType
                //设置当前播放的歌曲类型为本地歌曲
                SongModel.getInstance().setChooseSongList(SongModel.getInstance().getLocalSongList());

                Log.d(TAG, "位置："+position+"; 歌名："+musicList.get(position).getTitle());
                adapter.notifyDataSetChanged(); //更新adapter
                MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
                int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition();
                //点击的是正在播放的歌曲
                if (position == lastPosition && null != jumpToOtherWhere
                        && lastMusicList.get(position).getMusicId().equals(
                        SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId()))
                    jumpToOtherWhere.GoToPlayActivity(); //调用父类方法，跳转到播放Activity
                //点击的不是当前播放的歌曲
                else {
                    PlayControl.controlBtnPlayDiffSong(); //播放音乐
                    mBottomBarTitle.setText(musicList.get(position).getTitle()); //更新音乐名
                    mBottomBarArtist.setText(musicList.get(position).getArtist()); //更新音乐作者
                    //更新音乐专辑图
                    String path = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
                    Log.d(TAG,"path="+path);
                    if (null == path){
                        mIvBottomBarImage.setImageResource(R.drawable.ic_log);
                    }else{
                        Glide.with(MusicApplication.getContext()).load(path).into(mIvBottomBarImage);
                    }

                    //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
                    ItemViewChoose.getInstance().setItemChoosePosition(position);
                }
            }
        });
        return view;
    }

    private void readMusic(){
        try{
            musicList = SongModel.getInstance().getLocalSongList();

            if (null != musicList && musicList.size() > 0){
                mNoMusic.setVisibility(GONE);
                adapter = new LocalMusicAdapter(MusicApplication.getContext(),R.layout.item_music,musicList);
                listView.setAdapter(adapter);
            }
            else
                mNoMusic.setVisibility(VISIBLE);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static Handler LocalListActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Handler 收到位置更新的通知");
            int position = MediaUtils.currentSongPosition;

            //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
            ItemViewChoose.getInstance().setItemChoosePosition(position);
            adapter.notifyDataSetChanged();
//            listView.smoothScrollToPosition(position); //滑动到当前位置
//            listView.setSelection(position);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        /*通过MusicBroadcastReceiver发送的intent，更新UI*/
        int state = getActivity().getIntent().getIntExtra("state", 0);
        Log.d(TAG, "onResume 02 state == " + state);
        switch (state) {
            case MediaStateCode.PLAY_START:
                break;
            case MediaStateCode.PLAY_PAUSE:
                break;
            case MediaStateCode.PLAY_CONTINUE:
                break;
            case MediaStateCode.PLAY_STOP:
                break;
            case MediaStateCode.MUSIC_POSITION_CHANGED:
                LocalListActivityHandler.sendEmptyMessage(1);
                Log.d(TAG, "onResume==" + "收到位置更新的通知");
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}

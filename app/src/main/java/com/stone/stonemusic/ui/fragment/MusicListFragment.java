package com.stone.stonemusic.ui.fragment;


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
import com.stone.stonemusic.bean.ItemViewChoose;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.present.PlayControl;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicListFragment extends Fragment implements LocalListActivity.CallBackInterface {
    public static final String TAG = "MusicListFragment";
    private ListView listView;
    private List<Music> musicList = new ArrayList<>();
    private LocalMusicAdapter adapter;

    private TextView mBottomBarTitle;
    private TextView mBottomBarArtist;
    private ImageView mIvPlay;
    private ImageView mIvBottomBarImage;
    private LocalListActivity fatherActivity = null;

    public MusicListFragment() {}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fatherActivity = ((LocalListActivity)context);
        fatherActivity.setCallBackInterface(this);
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

        listView = (ListView) view.findViewById(R.id.lv_music_list);

        mBottomBarTitle = (TextView) getActivity().findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = (TextView) getActivity().findViewById(R.id.bottom_bar_artist);
        mIvPlay = (ImageView) getActivity().findViewById(R.id.iv_play);
        mIvBottomBarImage = (ImageView) getActivity().findViewById(R.id.bottom_bar_image);

        readMusic();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "位置："+position+"; 歌名："+musicList.get(position).getTitle());
                adapter.notifyDataSetChanged(); //更新adapter
                MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
                int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition();
                //点击的是正在播放的歌曲
                if (position == lastPosition && null != fatherActivity)
                    fatherActivity.GoToPlayActivity(view); //调用父类方法，跳转到播放Activity
                //点击的不是当前播放的歌曲
                else{
                    PlayControl.controlBtnPlayDiffSong();
                    mBottomBarTitle.setText(musicList.get(position).getTitle()); //更新音乐名
                    mBottomBarArtist.setText(musicList.get(position).getArtist()); //更新音乐作者
                    //更新音乐专辑图
                    String path = MusicUtil.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
                    Log.d(TAG,"path="+path);
                    if (null == path){
                        mIvBottomBarImage.setImageResource(R.drawable.ic_log);
                    }else{
                        Glide.with(MusicAppUtils.getContext()).load(path).into(mIvBottomBarImage);
                    }

                    //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
                    ItemViewChoose.getInstance().setItemChoosePosition(position);
                }

                /*发送广播，告知，音乐播放位置已改变*/
//                BroadcastUtils.sendNoticeMusicPositionChanged();

            }
        });
        return view;
    }

    private void readMusic(){
        try{
            musicList = SongModel.getInstance().getSongList();
            adapter = new LocalMusicAdapter(MusicAppUtils.getContext(),R.layout.item_music,musicList);
            listView.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private Handler LocalListActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Handler 收到位置更新的通知");
            int position = MediaUtils.currentSongPosition;
            //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
            ItemViewChoose.getInstance().setItemChoosePosition(position);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(position);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        /*通过MusicBroadcastReceiver发送的intent，更新UI*/
        int state = getActivity().getIntent().getIntExtra("state", 0);
        Log.d(TAG, "onPause 02 state == " + state);
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
                Log.d(TAG, "收到位置更新的通知");
                break;
        }
        Log.d(TAG, "onPause 03");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}

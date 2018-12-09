package com.stone.stonemusic.ui.fragment;


import android.os.Bundle;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalMusicAdapter;
import com.stone.stonemusic.bean.ItemViewChoose;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.BroadcastUtils;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicListFragment extends Fragment {
    private ListView listView;
    private List<Music> musicList = new ArrayList<>();
    private LocalMusicAdapter adapter;

    private TextView mBottomBarTitle;
    private TextView mBottomBarArtist;
    private ImageView mIvPlay;
    private ImageView mIvBottomBarImage;

    public MusicListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                Log.d("stone1126", "位置："+position+"; 歌名："+musicList.get(position).getTitle());
                mBottomBarTitle.setText(musicList.get(position).getTitle());
                mBottomBarArtist.setText(musicList.get(position).getArtist());


                String path = MusicUtil.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
                Log.d("stone1201","path="+path);
                if (null == path){
                    mIvBottomBarImage.setImageResource(R.drawable.ic_log);
                }else{
                    Glide.with(MusicAppUtils.getContext()).load(path).into(mIvBottomBarImage);

                    //背景图部分
                }

                MediaUtils.currentSongPosition = position;//设置播放音乐的id
                BroadcastUtils.sendPlayMusicBroadcast();
                if (mIvPlay.getTag().equals(true)){
                    mIvPlay.setImageResource(R.drawable.ic_pause_black);
                    mIvPlay.setTag(false);
                }
                //设置选中的item的位置,然后更新adapter
                ItemViewChoose.getInstance().setItemChoosePosition(position);
                adapter.notifyDataSetChanged();

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




}

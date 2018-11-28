package com.stone.stonemusic.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalMusicAdapter;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.service.MusicService;
import com.stone.stonemusic.utils.BroadcastUtils;
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



    public MusicListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        listView = view.findViewById(R.id.lv_music_list);

        mBottomBarTitle = getActivity().findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = getActivity().findViewById(R.id.bottom_bar_artist);

        readMusic();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stone1126", "位置："+position+"; 歌名："+musicList.get(position).getTitle());
                mBottomBarTitle.setText(musicList.get(position).getTitle());
                mBottomBarArtist.setText(musicList.get(position).getArtist());
                BroadcastUtils.sendPlayMusicBroadcast();
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

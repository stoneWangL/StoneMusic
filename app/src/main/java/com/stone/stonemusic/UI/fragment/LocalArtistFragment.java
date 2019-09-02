package com.stone.stonemusic.UI.fragment;


import android.content.Context;
import android.content.Intent;
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

import com.stone.stonemusic.R;
import com.stone.stonemusic.UI.activity.LocalArtistListActivity;
import com.stone.stonemusic.adapter.LocalArtistAdapter;
import com.stone.stonemusic.model.ArtistModel;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.MusicApplication;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class LocalArtistFragment extends Fragment
        implements MusicObserverListener {
    public static final String TAG = "ArtistListFragment";
    private ListView listView;
    private List<ArtistModel> modelArrayList = new ArrayList<>();
    private List<Music> artistMusicList = new ArrayList<>(); //专辑歌曲二级list
    private static LocalArtistAdapter adapter;

    private TextView mNoMusic;
    private TextView mBottomBarTitle;
    private TextView mBottomBarArtist;
    private ImageView mIvPlay;
    private ImageView mIvBottomBarImage;
//    private HomeActivity fatherActivity = null;

    public LocalArtistFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        mNoMusic = view.findViewById(R.id.tv_no_music);
        listView = view.findViewById(R.id.artist_list);
        mBottomBarTitle = getActivity().findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = getActivity().findViewById(R.id.bottom_bar_artist);
        mIvPlay = getActivity().findViewById(R.id.iv_bottom_play);
        mIvBottomBarImage = getActivity().findViewById(R.id.bottom_bar_image);

        readMusic();

        return view;
    }

    private void readMusic() {
        try{
            modelArrayList = MusicResources.artistModelList;
            if (null != modelArrayList && modelArrayList.size() > 0)
                mNoMusic.setVisibility(GONE);
            else
                mNoMusic.setVisibility(VISIBLE);

            adapter = new LocalArtistAdapter(MusicApplication.getContext(), R.layout.item_artist, modelArrayList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "点击位置=" + position + " 歌手名=" + modelArrayList.get(position).getArtist());
                    artistMusicList = MusicResources.getSameArtistMusicList(
                            modelArrayList.get(position).getArtist());
                    if (null != artistMusicList) {
                        SongModel.getInstance().setmLocalArtistSongList(artistMusicList);
                        Intent intent = new Intent(getActivity(), LocalArtistListActivity.class);
                        intent.putExtra("artist", modelArrayList.get(position).getArtist());
                        startActivity(intent);
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void observerUpData(int content) {
        switch (content) {
            case MediaStateCode.MUSIC_INIT_FINISHED:
                Log.e(TAG, "Artist更新完毕");
                ArtistItemFragmentHandler.sendEmptyMessage(1);
                break;
        }
        Log.i(TAG, "observerUpData->观察者类数据已刷新");
    }

    private static Handler ArtistItemFragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Handler 收到位置更新的通知");
            adapter.notifyDataSetChanged();
        }
    };
}

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalArtistAdapter;
import com.stone.stonemusic.model.ArtistModel;
import com.stone.stonemusic.present.MusicObserverListener;
import com.stone.stonemusic.present.MusicResources;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MediaStateCode;
import com.stone.stonemusic.utils.MusicApplication;

import java.util.ArrayList;
import java.util.List;


public class ArtistListFragment extends Fragment implements
        MusicObserverListener {
    public static final String TAG = "ArtistListFragment";
    private ListView listView;
    private List<ArtistModel> modelArrayList = new ArrayList<>();
    private LocalArtistAdapter adapter;

    private TextView mBottomBarTitle;
    private TextView mBottomBarArtist;
    private ImageView mIvPlay;
    private ImageView mIvBottomBarImage;
    private LocalListActivity fatherActivity = null;

    public ArtistListFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_artist_list, container, false);
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        listView = (ListView) view.findViewById(R.id.artist_list);
        mBottomBarTitle = (TextView) getActivity().findViewById(R.id.bottom_bar_title);
        mBottomBarArtist = (TextView) getActivity().findViewById(R.id.bottom_bar_artist);
        mIvPlay = (ImageView) getActivity().findViewById(R.id.iv_play);
        mIvBottomBarImage = (ImageView) getActivity().findViewById(R.id.bottom_bar_image);

        readMusic();

        return view;
    }

    private void readMusic() {
        try{
            modelArrayList = MusicResources.artistModelList;
            adapter = new LocalArtistAdapter(MusicApplication.getContext(), R.layout.item_artist, modelArrayList);
            listView.setAdapter(adapter);
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

    private Handler ArtistItemFragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Handler 收到位置更新的通知");
            adapter.notifyDataSetChanged();
        }
    };
}

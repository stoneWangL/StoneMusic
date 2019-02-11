package com.stone.stonemusic.ui.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.bean.Music;
import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.ui.View.CircleImageView;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.MusicAppUtils;
import com.stone.stonemusic.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayImageFragment extends Fragment {
    private CircleImageView CIVAlbum;
    private List<Music> musicList = new ArrayList<>();
    public PlayImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_image, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        musicList = SongModel.getInstance().getSongList();
        initView(view);
    }
    private void initView(View view){

        /*设置专辑图*/
        CIVAlbum = (CircleImageView) view.findViewById(R.id.play_music_album_image);
        int position = MediaUtils.currentSongPosition;
        String path = MusicUtil.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
        if (null == path){
            CIVAlbum.setImageResource(R.drawable.play_background02);
        }else{
            CIVAlbum.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
}

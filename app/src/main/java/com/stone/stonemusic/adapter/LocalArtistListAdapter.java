package com.stone.stonemusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.PlayFatherView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 21:17
 * @Description:
 */
public class LocalArtistListAdapter extends ArrayAdapter<Music> {
    public static final String TAG = "LocalArtistListAdapter";
    private int resourceId;
//    private PlayFatherView lrcListView;

    public LocalArtistListAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view;
        LocalArtistListAdapter.ViewHold viewHold;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new LocalArtistListAdapter.ViewHold();
            viewHold.textViewNum = view.findViewById(R.id.textViewNum);
            viewHold.ItemPlayOrPause = view.findViewById(R.id.item_playOrPause);
            viewHold.musicName = view.findViewById(R.id.music_name);
            viewHold.musicArtist = view.findViewById(R.id.music_artist);
            viewHold.ItemSet = view.findViewById(R.id.iv_item_set);
            view.setTag(viewHold);

        }else{
            view = convertView;
            viewHold = (LocalArtistListAdapter.ViewHold) view.getTag();
        }

        viewHold.textViewNum.setText(""+(position+1));
        viewHold.ItemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position == " + position);
            }
        });

        //根据是否选中，显示对应position的item是否播放
        if (SongModel.getInstance().getMusicType() == PlayType.LocalArtistType
                && music.getId() == SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getId()) {
            viewHold.ItemPlayOrPause.setVisibility(View.VISIBLE);
        } else {
            viewHold.ItemPlayOrPause.setVisibility(View.GONE);
        }

        viewHold.musicName.setText(music.getTitle());
        viewHold.musicArtist.setText(music.getArtist());
        return view;
    }

    class ViewHold{
        TextView textViewNum;
        ImageView ItemPlayOrPause;
        TextView musicName;
        TextView musicArtist;
        ImageView ItemSet;

    }
}

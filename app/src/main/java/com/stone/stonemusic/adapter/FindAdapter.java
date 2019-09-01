package com.stone.stonemusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.stone.stonemusic.View.FindView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/31 20:44
 * @Description:
 */
public class FindAdapter extends ArrayAdapter<Music> {
    public static final String TAG = "FindAdapter";
    private int resourceId;
    List<Music> musicList;
    FindView findView;


    public FindAdapter(@NonNull Context context, int textViewResourceId, List<Music> list, FindView findView) {
        super(context, textViewResourceId, list);
        resourceId = textViewResourceId;
        musicList = list;
        this.findView = findView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //        Log.d(TAG, "当前位置-》" + position);
        Music music = getItem(position);
        View view;
        FindAdapter.ViewHold viewHold;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new FindAdapter.ViewHold();
            viewHold.textViewNum = (TextView) view.findViewById(R.id.textViewNum);
            viewHold.ItemPlayOrPause = (ImageView) view.findViewById(R.id.item_playOrPause);
            viewHold.musicName = (TextView) view.findViewById(R.id.music_name);
            viewHold.musicArtist = (TextView) view.findViewById(R.id.music_artist);
            viewHold.ItemSet = (ImageView) view.findViewById(R.id.iv_item_set);
            view.setTag(viewHold);

        }else{
            view = convertView;
            viewHold = (FindAdapter.ViewHold) view.getTag();
        }

        viewHold.textViewNum.setText(""+(position+1));
        viewHold.ItemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position == " + position);
                findView.clickItemSet(position); //让View层去处理
            }
        });

        //根据是否选中，显示对应position的item是否播放
        if (SongModel.getInstance().getMusicType() == PlayType.OnlineType
                && musicList.get(position).getMusicId().equals(
                SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId())){
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

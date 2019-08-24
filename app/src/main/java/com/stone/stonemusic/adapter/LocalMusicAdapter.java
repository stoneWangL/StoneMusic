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
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;

import java.util.List;


public class LocalMusicAdapter extends ArrayAdapter<Music> {
    public static final String TAG = "LocalMusicAdapter";
    private int resourceId;
    private Bitmap pic;


    public LocalMusicAdapter(Context context, int textViewResourceId, List<Music> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        Log.d(TAG, "当前位置-》" + position);
        Music music = getItem(position);
        View view;
        ViewHold viewHold;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new ViewHold();
            viewHold.textViewNum = (TextView) view.findViewById(R.id.textViewNum);
            viewHold.ItemPlayOrPause = (ImageView) view.findViewById(R.id.item_playOrPause);
            viewHold.musicName = (TextView) view.findViewById(R.id.music_name);
            viewHold.musicArtist = (TextView) view.findViewById(R.id.music_artist);
            viewHold.ItemSet = (ImageView) view.findViewById(R.id.iv_item_set);
            view.setTag(viewHold);

        }else{
            view = convertView;
            viewHold = (ViewHold) view.getTag();
        }

        viewHold.textViewNum.setText(""+(position+1));
        viewHold.ItemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position == " + position);
            }
        });

//        pic = GetMusic.getMusicBitemp(getContext(),music.getId(),music.getAlbum_id(),0);
//        viewHold.listMusicImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        viewHold.listMusicImage.setImageBitmap(pic);
//        viewHold.listMusicImage.setBackgroundResource(R.drawable.list_message3);
//        Glide.with(MusicAppUtils.getContext()).load(R.drawable.list_message3).into(viewHold.listMusicImage);

        //根据是否选中，显示对应position的item是否播放
        if (SongModel.getInstance().getMusicType() == PlayType.LocalType && ItemViewChoose.getInstance().getItemChoosePosition() == position){
            viewHold.ItemPlayOrPause.setVisibility(View.VISIBLE);
        } else {
            viewHold.ItemPlayOrPause.setVisibility(View.GONE);
        }

        viewHold.musicName.setText(music.getTitle());
        viewHold.musicArtist.setText(music.getArtist());
        return view;
    }

    class ViewHold{
//        ImageView listMusicImage;
        TextView textViewNum;
        ImageView ItemPlayOrPause;
        TextView musicName;
        TextView musicArtist;
        ImageView ItemSet;

    }



}

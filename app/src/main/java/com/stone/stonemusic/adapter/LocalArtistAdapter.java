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

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.ArtistModel;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.utils.MusicAppUtils;

import java.util.List;

/**
 * author : stoneWang
 * date   : 2019/4/89:06
 */
public class LocalArtistAdapter extends ArrayAdapter<ArtistModel> {
    public static final String TAG = "LocalArtistAdapter";
    private int resourceId;
    private Bitmap pic;

    public LocalArtistAdapter(@NonNull Context context, int resource, List<ArtistModel> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        Log.d(TAG, "当前位置-》" + position);
        ArtistModel artistModel = getItem(position);
        View view;
        ViewHold viewHold;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new ViewHold();

            viewHold.listArtistImage = (ImageView) view.findViewById(R.id.item_artist_image_of_artist);
            viewHold.musicArtist = (TextView) view.findViewById(R.id.item_artist_name_of_artist);
            viewHold.musicNum = (TextView) view.findViewById(R.id.item_artist_num_of_music);
            viewHold.ItemSet = (ImageView) view.findViewById(R.id.item_artist_set);
            view.setTag(viewHold);

        } else {
            view = convertView;
            viewHold = (ViewHold) view.getTag();
        }

        viewHold.ItemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "position == " + position);
            }
        });

//        pic = GetMusic.getMusicBitemp(getContext(),music.getId(),music.getAlbum_id(),0);
//        viewHold.listMusicImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        viewHold.listMusicImage.setImageBitmap(pic);
//        viewHold.listMusicImage.setBackgroundResource(R.drawable.list_message3);
//        Glide.with(MusicAppUtils.getContext()).load(R.drawable.list_message3).into(viewHold.listMusicImage);
        String path = null;
        path = artistModel.getPath();
//            Log.d(TAG,"path="+path);
        if (null == path || path.equals("")){
            Glide.with(MusicAppUtils.getContext()).load(R.drawable.ic_def_img_artist_24dp).into(viewHold.listArtistImage);
//            viewHold.listArtistImage.setImageResource(R.drawable.ic_def_img_artist_24dp);
        }else{
            Glide.with(MusicAppUtils.getContext()).load(path).into(viewHold.listArtistImage);
        }

        viewHold.musicArtist.setText(artistModel.getArtist());
        viewHold.musicNum.setText(artistModel.getNum() + "首");

        return view;
    }

    class ViewHold{
        ImageView listArtistImage;
        TextView musicArtist;
        TextView musicNum;
        ImageView ItemSet;

    }
}

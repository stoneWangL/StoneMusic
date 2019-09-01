package com.stone.stonemusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.LrcListView;
import com.stone.stonemusic.model.Music;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/1 21:04
 * @Description:
 */
public class LrcListAdapter extends ArrayAdapter<Music> {

    public static final String TAG = "LrcListAdapter";
    private int resourceId;
    private LrcListView lrcListView;


    public LrcListAdapter(@NonNull Context context, int resourceId, @NonNull List<Music> objects, LrcListView lrcListView) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.lrcListView = lrcListView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Log.d(TAG, "当前位置-》" + position);
        final Music music = getItem(position);
        View view;
        LrcListAdapter.ViewHold viewHold;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new LrcListAdapter.ViewHold();
            viewHold.textViewNum = view.findViewById(R.id.tv_item_num);
            viewHold.titleAndArtist = view.findViewById(R.id.tv_item_title_artist);
            view.setTag(viewHold);
        }else{
            view = convertView;
            viewHold = (LrcListAdapter.ViewHold) view.getTag();
        }
        viewHold.textViewNum.setText(""+(position+1));
        viewHold.titleAndArtist.setText(music.getTitle() + "-" + music.getArtist() + ".lrc");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lrcListView.onLrcItemClick(music);
            }
        });
        return view;
    }

    class ViewHold{
        TextView textViewNum;
        TextView titleAndArtist;
    }

}

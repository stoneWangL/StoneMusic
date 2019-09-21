package com.stone.stonemusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/1 21:04
 * @Description:
 */
public class ChooseListAdapter extends ArrayAdapter<Music> {

    public static final String TAG = "LrcListAdapter";
    private int resourceId;
//    private PlayFatherView lrcListView;


    /**
     * 构造函数
     * @param context
     * @param resourceId
     * @param objects
     */
    public ChooseListAdapter(@NonNull Context context, int resourceId, @NonNull List<Music> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
//        this.lrcListView = lrcListView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Log.d(TAG, "当前位置-》" + position);
        final Music music = getItem(position);
        View view;
        ChooseListAdapter.ViewHold viewHold;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHold = new ChooseListAdapter.ViewHold();
            viewHold.textViewNum = view.findViewById(R.id.tv_item_num);
            viewHold.ItemPlayOrPause = view.findViewById(R.id.iv_item2_playOrPause);
            viewHold.titleAndArtist = view.findViewById(R.id.tv_item_title_artist);
            view.setTag(viewHold);
        }else{
            view = convertView;
            viewHold = (ChooseListAdapter.ViewHold) view.getTag();
        }
        viewHold.textViewNum.setText(""+(position+1));
        //根据是否选中，显示对应position的item是否播放
        if (ItemViewChoose.getInstance().getItemChoosePosition() == position
                && music.getId() ==
                SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getId()
                && music.getMusicId().equals(
                SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId())
                ) {
            viewHold.ItemPlayOrPause.setVisibility(View.VISIBLE);
        } else {
            viewHold.ItemPlayOrPause.setVisibility(View.GONE);
        }
        viewHold.titleAndArtist.setText(music.getTitle() + "-" + music.getArtist() + ".mp3");
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                lrcListView.onLrcItemClick(music);
//            }
//        });
        return view;
    }

    class ViewHold{
        TextView textViewNum;
        ImageView ItemPlayOrPause;
        TextView titleAndArtist;
    }

}

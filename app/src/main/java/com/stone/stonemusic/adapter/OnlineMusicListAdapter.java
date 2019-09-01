package com.stone.stonemusic.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.OnLineView;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;


import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/20 17:59
 * @Description:
 */
public class OnlineMusicListAdapter extends RecyclerView.Adapter<OnlineMusicListAdapter.ViewHolder> {
    public static final String TAG = "OnlineMusicListAdapter";
    private int resourceId;

    private List<Music> list; //数据集合
    private OnLineView onLineView; //上下文

    public OnlineMusicListAdapter(List<Music> list, OnLineView onLineView, int resourceId) {
        this.list = list;
        this.onLineView = onLineView;
        this.resourceId = resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        //点击回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLineView.onItemClick(v, position);
            }
        });
        //将数据设置到item上
        holder.textViewNum.setText("" + (position+1));
        //根据是否选中，显示对应position的item是否播放
//        Log.i(TAG, "ItemChoosePosition="+ ItemViewChoose.getInstance().getItemChoosePosition() + ";position="+ position);
        if (SongModel.getInstance().getMusicType() == PlayType.OnlineType
                && ItemViewChoose.getInstance().getItemChoosePosition() == position
                && list.get(position).getMusicId().equals(
                        SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getMusicId())){
            holder.ItemPlayOrPause.setVisibility(View.VISIBLE);
        } else {
            holder.ItemPlayOrPause.setVisibility(View.GONE);
        }
        Music music = list.get(position);
        holder.musicName.setText(music.getTitle());
        holder.musicArtist.setText(music.getArtist());


        //item中的设置按钮点击事件
        holder.ItemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ItemSet->position == " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView textViewNum;
        ImageView ItemPlayOrPause;
        TextView musicName;
        TextView musicArtist;
        ImageView ItemSet;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textViewNum = (TextView) itemView.findViewById(R.id.textViewNum);
            ItemPlayOrPause = (ImageView) itemView.findViewById(R.id.item_playOrPause);
            musicName = (TextView) itemView.findViewById(R.id.music_name);
            musicArtist = (TextView) itemView.findViewById(R.id.music_artist);
            ItemSet = (ImageView) itemView.findViewById(R.id.iv_item_set);
        }
    }
}

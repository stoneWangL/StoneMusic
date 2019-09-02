package com.stone.stonemusic.UI.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.adapter.LocalArtistListAdapter;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.ItemViewChoose;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.PlayControl;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 17:24
 * @Description: 本地歌手歌曲列表
 */
public class LocalArtistListActivity extends BaseHaveBottomBarActivity
        implements View.OnClickListener{
    public static final String TAG = "LocalArtistListActivity";
    private TextView artistTextView;
    private LocalArtistListAdapter artistListAdapter;
    private ListView listView;
    private List<Music> musicList;
    private ImageView ivFind;

    @Override
    public int getLayoutId() {
        return R.layout.activity_local_artist_list;
    }

    @Override
    protected void initListenerOther() {
        ivFind = findViewById(R.id.imageView_localArtistList_find_back);
        ivFind.setOnClickListener(this);
        artistTextView = findViewById(R.id.tv_localArtistList_artist);
        listView= findViewById(R.id.listView_localArtistList);
    }

    @Override
    protected void initDataOther() {
        Intent intent = getIntent();
        String artistStr = intent.getStringExtra("artist");
        artistTextView.setText(artistStr);

        musicList = SongModel.getInstance().getmLocalArtistSongList();
        if (null != musicList) {
            artistListAdapter = new LocalArtistListAdapter(this, R.layout.item_music, musicList);
            listView.setAdapter(artistListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //上一个播放列表
                    List<Music> lastMusicList = SongModel.getInstance().getChooseSongList();

                    //设置当前播放的歌曲类型 -> 本地歌手列表
                    SongModel.getInstance().setChooseSongList(SongModel.getInstance().getmLocalArtistSongList());

                    Log.d(TAG, "位置："+position+"; 歌名："+musicList.get(position).getTitle());

                    MediaUtils.currentSongPosition = position; //设置当前播放位置全局position
                    int lastPosition = ItemViewChoose.getInstance().getItemChoosePosition(); //获取上一个选择的position
                    //点击的是正在播放的歌曲
                    if (position == lastPosition //点前点击的position是否等于当前正在播放列表的position
                            && SongModel.getInstance().getMusicType() == PlayType.LocalArtistType //当前播放类型是否等于LocalArtistType
                            && lastMusicList.get(position).getId() ==
                            SongModel.getInstance().getChooseSongList().get(MediaUtils.currentSongPosition).getId()
                    )
                        jumpToOtherWhere.GoToPlayActivity(); //调用父类方法，跳转到播放Activity
                    //点击的不是当前播放的歌曲
                    else {
                        PlayControl.controlBtnPlayDiffSong(); //播放音乐
                        //设置选中的item的位置,这里的position设置与ListView中当前播放位置的标识有关
                        ItemViewChoose.getInstance().setItemChoosePosition(position);
                        artistListAdapter.notifyDataSetChanged(); //更新adapter
                    }
                    //item被点击，当前播放的歌曲列表 -> 本地歌手
                    SongModel.getInstance().setMusicType(PlayType.LocalArtistType); //将播放类型切换为LocalArtistType
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_localArtistList_find_back:
                finish();
                overridePendingTransition(R.anim.stop, R.anim.right_out);
                break;
            default:
                break;
        }
    }

    @Override
    protected void observerUpDataOtherPlayStartPositionChange() {
        artistListAdapter.notifyDataSetChanged(); //更新adapter
    }

    @Override
    protected void observerUpDataOtherContinueStopPause() {

    }

    @Override
    protected void onDestroyOther() {

    }


}

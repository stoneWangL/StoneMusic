package com.stone.stonemusic.ui.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.View.CircleImageView;
import com.stone.stonemusic.ui.activity.PlayActivity;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.MusicResources;

import java.util.ArrayList;
import java.util.List;

public class PlayImageFragment extends Fragment implements PlayActivity.CallBackInterface{
    public static final String TAG = "PlayImageFragment";
    private CircleImageView CIVAlbum;
    private List<Music> musicList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((PlayActivity)context).setCallBackInterface(this);
    }

    @Override
    public void ChangeUI() {
        Log.i(TAG, "这里是PlayImageFragment的ChangeUI()，这里被回调了");
        PlayImageFragmentHandler.sendEmptyMessage(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_image, container, false);


        init(view);

        return view;
    }

    private void init(View view) {
        musicList = SongModel.getInstance().getChooseSongList();
        CIVAlbum = (CircleImageView) view.findViewById(R.id.play_music_album_image);
        initView();
    }
    private void initView(){
        /*设置专辑图*/
        int position = MediaUtils.currentSongPosition;
//        String path = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
//        if (null == path){
//            CIVAlbum.setImageResource(R.drawable.play_background02);
//        }else{
//            CIVAlbum.setImageBitmap(BitmapFactory.decodeFile(path));
//        }

        String imagePath; //歌曲图片路径
        if (SongModel.getInstance().getMusicType() == PlayType.OnlineType) { //当前为播放在线歌曲状态
            imagePath = musicList.get(position).getPicUrl();
        } else { //当前为播放本地歌曲状态
            imagePath = MusicResources.getAlbumArt(new Long(musicList.get(position).getAlbum_id()).intValue());
        }
        if (null == imagePath || imagePath.equals("")) {
            CIVAlbum.setImageResource(R.drawable.play_background02);
        } else {

            Glide.with(MusicApplication.getContext()).load(imagePath).into(CIVAlbum);
        }
    }

    private Handler PlayImageFragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "Handler 收到位置更新的通知");
            initView();
        }
    };
}

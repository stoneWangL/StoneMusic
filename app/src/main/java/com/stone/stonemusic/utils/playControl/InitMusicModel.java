package com.stone.stonemusic.utils.playControl;

import android.os.AsyncTask;

import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.ui.activity.HomeActivity;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/7/522:37
 */
public class InitMusicModel extends AsyncTask<String, Integer, String> {

    private HomeActivity view;

    public InitMusicModel(HomeActivity activity){
        this.view = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            view.musicList = new MusicResources().getMusic(MusicApplication.getContext());
            SongModel.getInstance().setmLocalSongList(view.musicList); //保存本地音乐列表
            MusicResources.initArtistMode(); //初始化歌手列表
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        view.initViews();
        view.initMusicPlayImg();
        view.mDialog.cancel();
    }

}
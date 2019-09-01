package com.stone.stonemusic.utils.playControl;

import android.os.AsyncTask;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.UI.activity.HomeActivity;
import com.stone.stonemusic.utils.MusicApplication;

import java.util.List;

/**
 * author : stoneWang
 * date   : 2019/7/522:37
 */
public class InitMusicModelTask extends AsyncTask<String, Integer, String> {

    private HomeActivity view;
    private List<Music> musicList;

    public InitMusicModelTask(HomeActivity activity){
        this.view = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            musicList = new MusicResources().getMusic(MusicApplication.getContext()); //初始化本地歌曲列表
            SongModel.getInstance().setLocalSongList(musicList); //保存本地音乐列表

            MusicResources.initArtistMode(); //初始化本地歌手列表
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
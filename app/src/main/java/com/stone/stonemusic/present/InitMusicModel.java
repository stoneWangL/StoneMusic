package com.stone.stonemusic.present;

import android.os.AsyncTask;

import com.stone.stonemusic.model.SongModel;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/7/522:37
 */
public class InitMusicModel extends AsyncTask<String, Integer, String> {

    private LocalListActivity view;

    public InitMusicModel(LocalListActivity activity){
        this.view = activity;
    }

    @Override
    protected void onPreExecute() {
//            text.setText("加载中");
        // 执行前显示提示
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            view.musicList = new MusicResources().getMusic(MusicApplication.getContext());
            SongModel.getInstance().setSongList(view.musicList);
            MusicResources.initArtistMode(); //初始化歌手列表
        }catch (Exception e) {
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
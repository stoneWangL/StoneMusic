package com.stone.stonemusic.model;

import java.util.List;

public class SongModel {
    private static SongModel sSongModel=null;
    private List<Music> mSongList;
    private SongModel() {}

    //单例,获取实例
    public static SongModel getInstance() {
        if (sSongModel == null) {
            sSongModel = new SongModel();
        }
        return sSongModel;
    }

    public void setSongList(List<Music> songList) {
        mSongList = songList;
    }

    public List<Music> getSongList() {
        return mSongList;
    }

    public int getSongListSize() {
        return mSongList.size();
    }
}

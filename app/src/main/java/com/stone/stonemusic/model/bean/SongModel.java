package com.stone.stonemusic.model.bean;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.utils.code.PlayType;

import java.util.List;

public class SongModel implements PlayType {
    private int musicType = PlayType.LocalType; //默认为本地,不设置的话就是本地音乐
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

    public int getMusicType() {
        return musicType;
    }

    public void setMusicType(int musicType) {
        this.musicType = musicType;
    }
}

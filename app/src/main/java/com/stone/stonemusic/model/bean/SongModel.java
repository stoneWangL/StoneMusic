package com.stone.stonemusic.model.bean;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.utils.code.PlayType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 歌单列表单例
 */
public class SongModel implements PlayType {
    private int musicType = PlayType.LocalType; //默认为本地,不设置的话就是本地音乐
    private static SongModel sSongModel=null;

    private List<Music> mChooseSongList; //歌曲列表 -> 当前播放的
    private List<Music> mLocalSongList; //歌曲列表 -> 本地歌曲列表
    private HashMap<String, ArrayList<Music>> artistMap; //歌曲列表的hashMap -> 本地歌手歌曲列表de 父列表
    private List<Music> mLocalArtistSongList; //歌曲列表 -> 本地歌手歌曲列表
    private List<Music> mOnLineSongList; //歌曲列表 -> 在线歌曲列表
    private List<Music> mFindSongList; //歌曲列表 -> 查找歌曲列表
    private List<Music> loveSongList; //歌曲列表 -> 喜欢歌曲列表

    private SongModel() {}

    //单例,获取实例
    public static SongModel getInstance() {
        if (sSongModel == null) {
            sSongModel = new SongModel();
        }
        return sSongModel;
    }

    public List<Music> getmFindSongList() {
        return mFindSongList;
    }

    public void setmFindSongList(List<Music> mFindSongList) {
        this.mFindSongList = mFindSongList;
    }

    public void setChooseSongList(List<Music> chooseSongList) {
        mChooseSongList = chooseSongList;
    }

    public List<Music> getChooseSongList() {
        return mChooseSongList;
    }

    public List<Music> getLocalSongList() {
        return mLocalSongList;
    }

    public void setLocalSongList(List<Music> mLocalSongList) {
        this.mLocalSongList = mLocalSongList;
    }

    public HashMap<String, ArrayList<Music>> getArtistMap() {
        return artistMap;
    }

    public void setArtistMap(HashMap<String, ArrayList<Music>> artistMap) {
        this.artistMap = artistMap;
    }

    public List<Music> getmLocalArtistSongList() {
        return mLocalArtistSongList;
    }

    public void setmLocalArtistSongList(List<Music> mLocalArtistSongList) {
        this.mLocalArtistSongList = mLocalArtistSongList;
    }

    public List<Music> getOnLineSongList() {
        return mOnLineSongList;
    }

    public void setOnLineSongList(List<Music> mOnLineSongList) {
        this.mOnLineSongList = mOnLineSongList;
    }

    public List<Music> getLoveSongList() {
        return loveSongList;
    }

    public void setLoveSongList(List<Music> loveSongList) {
        this.loveSongList = loveSongList;
    }






    public int getMusicType() {
        return musicType;
    }

    public void setMusicType(int musicType) {
        this.musicType = musicType;
    }

    //获取 当前播放列表 的size()
    public int getSongListSize() {
        return mChooseSongList.size();
    }
}

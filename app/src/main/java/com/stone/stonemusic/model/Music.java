package com.stone.stonemusic.model;

import com.stone.stonemusic.utils.code.PlayType;

import java.io.Serializable;

public class Music implements Serializable, PlayType {
    private int musicType;
    private long id;
    private String musicId;
    private long album_id;
    private String title;
    private String artist;
    private long size;//music的音乐长度
    private long duration;
    private String album;
    private String fileUrl; //文件路径
    private int isMusic;
    private String picUrl; //在线歌曲图片

    public int getMusicType() {
        return musicType;
    }

    public void setMusicType(int musicType) {
        this.musicType = musicType;
    }

    public String getMusicId() {
        return musicId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize(int position) {
        return getIsMusic();
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getSize() {
        return size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

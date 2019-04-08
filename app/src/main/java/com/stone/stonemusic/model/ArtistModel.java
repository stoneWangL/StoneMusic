package com.stone.stonemusic.model;

/**
 * author : stoneWang
 * date   : 2019/4/815:26
 * 列表界面，艺术家/歌手一栏，一级模型
 */
public class ArtistModel {
    private String artist; //艺术家名称
    private int num; //同一艺术家的歌曲数
    private String path; //艺术家图片地址

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.stone.stonemusic.model;

/**
 * 一行歌词的Model
 * 包括歌词的内容和歌词的时间的set和get方法
 */

public class LrcContent {
    private String lrcStr; /*歌词内容*/
    private int lrcTime; /*歌词开始时间*/

    public String getLrcStr() {
        return lrcStr;
    }
    public void setLrcStr(String lrcStr) {
        this.lrcStr = lrcStr;
    }

    public int getLrcTime() {
        return lrcTime;
    }
    public void setLrcTime(int lrcTime) {
        this.lrcTime = lrcTime;
    }
}

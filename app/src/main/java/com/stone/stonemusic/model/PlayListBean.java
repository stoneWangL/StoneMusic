package com.stone.stonemusic.model;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/6 21:32
 * @Description: PlayList的模型
 */
public class PlayListBean {
    String name;
    String id;
    String coverImgUrl;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

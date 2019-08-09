package com.stone.stonemusic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/6 20:42
 * @Description:
 */
public class GeDanBean {
    List<PlayListsBean> playLists = new ArrayList<>();
    int code;

    public List<PlayListsBean> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(List<PlayListsBean> playLists) {
        this.playLists = playLists;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class PlayListsBean{
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
}

package com.stone.stonemusic.model;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/20 11:50
 * @Description:
 */
public class OnLineListBean {
    String name;
    String id;
    String ar_name;
    String al_picUrl;

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getAl_picUrl() {
        return al_picUrl;
    }

    public void setAl_picUrl(String al_picUrl) {
        this.al_picUrl = al_picUrl;
    }

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
}

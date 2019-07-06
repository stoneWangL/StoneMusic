package com.stone.stonemusic.bean;

/**
 * author : stoneWang
 * date   : 2019/7/623:46
 */
public class Beauty {

    /**
     * 名字
     */
    private String name;
    /**
     * 图片id
     */
    private int imageId;

    public Beauty(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}

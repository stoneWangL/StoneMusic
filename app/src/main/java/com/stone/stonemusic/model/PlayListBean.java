package com.stone.stonemusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PublicKey;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/6 21:32
 * @Description: PlayList的模型
 */
public class PlayListBean implements Parcelable {
    String name;
    String id;
    String coverImgUrl;
    String description;
    public PlayListBean(){}

    public PlayListBean(Parcel in) {
        name = in.readString();
        id = in.readString();
        coverImgUrl = in.readString();
        description = in.readString();
    }

    public static final Creator<PlayListBean> CREATOR = new Creator<PlayListBean>() {
        @Override
        public PlayListBean createFromParcel(Parcel in) {
            return new PlayListBean(in);
        }

        @Override
        public PlayListBean[] newArray(int size) {
            return new PlayListBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(coverImgUrl);
        dest.writeString(description);
    }
}

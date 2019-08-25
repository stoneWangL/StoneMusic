package com.stone.stonemusic.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stone.stonemusic.ui.activity.FirstActivity;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/7/423:10
 */
public class GetPermission {
    private FirstActivity view;

    public GetPermission(FirstActivity firstActivity) {
        this.view = firstActivity;
    }

    /**
     * 动态获取文件的读权限
     */
    public void initPermission() {
        if (ContextCompat.checkSelfPermission(MusicApplication.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    view,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        } else {
            view.jumpToOtherWhere.jumpToLocalListActivity();
        }
    }




}
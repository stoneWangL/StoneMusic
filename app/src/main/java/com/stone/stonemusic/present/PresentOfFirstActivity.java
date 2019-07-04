package com.stone.stonemusic.present;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stone.stonemusic.ui.activity.FirstActivity;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.utils.MusicApplication;

/**
 * author : stoneWang
 * date   : 2019/7/423:10
 */
public class PresentOfFirstActivity {
    private FirstActivity view;

    public PresentOfFirstActivity(FirstActivity firstActivity) {
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
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1
            );
        } else {
            jumpToLocalListActivity();
        }
    }

    /**
     * 延时几2.65秒，跳转至LocalListActivity
     */
    public void jumpToLocalListActivity(){
        //延迟一段时间后跳转到另一个界面
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent2 = new Intent (view,LocalListActivity.class);
                view.startActivity(intent2);//跳转界面
                view.finish();//关闭此界面
            }
        }, 2650);
    }

    /**
     * 延迟2秒，退出当前Activity
     */
    public void exitActivityTwoSecondLater(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.finish();
            }
        }, 2000);
    }


}
package com.stone.stonemusic.utils;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.stone.stonemusic.R;

public class ActivityUtils {

    /**
     * 设置状态栏颜色
     * @param activity
     * @param statusColor
     */
    public static void setStatusBarColor(Activity activity, int statusColor, boolean systemUiImageColorBlack) {
        Window window = activity.getWindow();
        /*取消状态栏透明*/
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        /*添加Flag把状态栏设为可绘制模式*/
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        /*设置状态栏颜色*/
        window.setStatusBarColor(MusicAppUtils.getContext().getResources().getColor(statusColor));
        /*设置系统状态栏处于可见状态 | 文字颜色及图标为深色*/
        if (systemUiImageColorBlack) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        /*让view不根据系统窗口来调整自己的布局*/
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static int initColor(Activity activity) {
        int num = MediaUtils.currentSongPosition % 10;
        int statusColor;
        switch (num) {
            case 0:
                statusColor = R.color.color0;
                break;
            case 1:
                statusColor = R.color.color1;
                break;
            case 2:
                statusColor = R.color.color2;
                break;
            case 3:
                statusColor = R.color.color3;
                break;
            case 4:
                statusColor = R.color.color4;
                break;
            case 5:
                statusColor = R.color.color5;
                break;
            case 6:
                statusColor = R.color.color6;
                break;
            case 7:
                statusColor = R.color.color7;
                break;
            case 8:
                statusColor = R.color.color8;
                break;
            case 9:
                statusColor = R.color.color9;
                break;
            default:
                statusColor = R.color.red;
        }
        ActivityUtils.setStatusBarColor(activity, statusColor, false);
        return statusColor;
    }
}

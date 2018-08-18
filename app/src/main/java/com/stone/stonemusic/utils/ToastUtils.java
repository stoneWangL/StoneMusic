package com.stone.stonemusic.utils;

import android.widget.Toast;

public class ToastUtils {

    public static void getToastShort(String str){
        Toast.makeText(MusicAppUtils.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void getToastLong(String str){
        Toast.makeText(MusicAppUtils.getContext(), str, Toast.LENGTH_LONG).show();
    }
}

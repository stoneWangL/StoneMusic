package com.stone.stonemusic.utils;

import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;

    public static void getToastShort(String str){
//        Toast.makeText(MusicAppUtils.getContext(), str, Toast.LENGTH_SHORT).show();
        mToast = Toast.makeText(MusicAppUtils.getContext(), null, Toast.LENGTH_SHORT);
        mToast.setText(str);
        mToast.show();
    }

    public static void getToastLong(String str){
//        Toast.makeText(MusicAppUtils.getContext(), str, Toast.LENGTH_LONG).show();
        mToast = Toast.makeText(MusicAppUtils.getContext(), null, Toast.LENGTH_LONG);
        mToast.setText(str);
        mToast.show();
    }
}

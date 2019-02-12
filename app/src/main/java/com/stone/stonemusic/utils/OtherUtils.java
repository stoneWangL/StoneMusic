package com.stone.stonemusic.utils;

public class OtherUtils {

    /*根据时长格式化称时间文本*/
    public static String durationTime(int duration) {
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        return (min < 10 ? "0" + min : min + "") + ":" + (sec < 10 ? "0" + sec : sec + "");
    }
}

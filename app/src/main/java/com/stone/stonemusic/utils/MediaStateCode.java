package com.stone.stonemusic.utils;

public class MediaStateCode {
    public static final int MUSIC_POSITION_NO_CHANGED = 0x0000;
    public static final int PLAY_START = 0x0001;
    public static final int PLAY_PAUSE = 0x0002;
    public static final int PLAY_CONTINUE = 0x0003;
    public static final int PLAY_STOP = 0x0004;
    public static final int PLAY_LAST = 0x0005;
    public static final int PLAY_NEXT = 0x0006;
    public static final int MUSIC_POSITION_CHANGED = 0x0010; /*通知-》播放位置改变*/

    public static final int LOOP_MODE_ONLY_ONE = 0x0011; /*单曲循环*/
    public static final int LOOP_MODE_ORDER_LIST = 0x0012; /*列表循环*/
    public static final int LOOP_MODE_OUT_OF_ORDER = 0x0013; /*随机循环*/


    public static String ACTION_CLOSE = "Stone.Music.Action.Close";
    public static String ACTION_LAST = "Stone.Music.Action.Last";
    public static String ACTION_PLAY_OR_PAUSE = "Stone.Music.Action.PlayOrPause";
    public static String ACTION_NEXT = "Stone.Music.Action.Next";
    public static String ACTION_LOVE = "Stone.Music.Action.Love";

    public static String ACTION_CURRENT_UPDATE = "Stone.Music.Action.LrcCurrentUpdate";


}

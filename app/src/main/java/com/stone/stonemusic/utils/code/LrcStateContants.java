package com.stone.stonemusic.utils.code;

/*歌词界面状态常量*/
public interface LrcStateContants {
    public static final int LRC_INIT = -1; //歌词初始状态
    public static final int LRC_READ_LOC_FAIL = 0; //本地没有歌词
    public static final int LRC_READ_LOC_OK = 1; //本地有歌词
    public static final int LRC_QUERY_ONLINE_ING = 2; //正在联网查找
    public static final int LRC_QUERY_ONLINE_OK = 3; //联网查找成功
    public static final int LRC_QUERY_ONLINE_NULL = 4; //网络无歌词
    public static final int LRC_QUERY_ONLINE_FAIL = 5; //联网查找失败
}

package com.stone.stonemusic.utils;


/**
 * 接口
 */
public class URLProviderUtils {
    private static final String serverUrl = "http://122.152.248.24:3000";

    //网友推荐歌单-分类 http://144.34.228.215:3000/top/playlist/catlist?limit=2&offset=0&cat=怀旧
    public static String getRecommendAll(int offset, int size, String cat) {
        String url = serverUrl + "/top/playlist/catlist?limit="
                + size
                + "&offset=" + offset
                + "&cat=" + cat;
        return url;
    }

    //http://144.34.228.215:3000/playlist/detail?id=2900722591
    public static String getOnLineList(String id) {
        String url = serverUrl + "/playlist/detail?id="
                + id;
        return  url;
    }

    //歌手推荐
    public static String getArtistAll(int offset, int size, int cat) {
        String url = serverUrl + "/artist/list?limit="
                + size
                + "&offset=" + offset
                + "&cat=" + cat;
        return url;
    }

    //单曲获取 https://music.163.com/song/media/outer/url?id=id.mp3
    public static String getSingleSong(String id) {
        String url = "https://music.163.com/song/media/outer/url?id="
                + id + ".mp3";
        return url;
    }

    //查看单曲是否可以播放
    public static String checkMusic(String id) {
        String url = serverUrl + "/check/music?id="
                + id;
        return url;
    }

    //根据歌曲id查询歌词
    public static String findLrc(String id) {
        String url = serverUrl + "/lyric?id="
                + id;
        return url;
    }


    /**
     * 关键字搜索
     * http://144.34.228.215:3000/search/multimatch?keywords=&type=1&limit=30&offset=0
     * @param keyWord 关键词
     * @param type 搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频
     * @param limit 返回数量 , 默认为 30
     * @param offset 偏移数量，用于分页
     * @return String 类型的JSON数据
     */
    public static String findByKeyWord(String keyWord, int type, int limit, int offset) {
        String url = serverUrl + "/search?keywords="
                + keyWord
                + "&type=" + type
                + "&limit=" + limit
                + "&offset=" + offset;
        return url;
    }
}


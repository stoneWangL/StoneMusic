package com.stone.stonemusic.utils;

import android.util.Log;

public class URLProviderUtils {

    /**
     * 获取首页的url
     *
     * @param offset 数据偏移量
     * @param size   返回数据的条目个数
     * @return url
     */
    public static String getHomeUrl(int offset, int size) {
        String url = "http://mapi.yinyuetai.com/suggestions/front_page.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&offset=" + offset
                + "&size=" + size
                + "&v=4&rn=640*540";
        Log.i("Main_url", url);
        return url;
    }

    public static String getMVareaUrl() {
        String url = "http://mapi.yinyuetai.com/video/get_mv_areas.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}";
        return url;
    }

    public static String getMVListUrl(String area, int offset, int size) {
        String url = "http://mapi.yinyuetai.com/video/list.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&area=" + area
                + "&offset=" + offset
                + "&size=" + size;
        return url;
    }

    public static String getYueDanUrl(int offset, int size) {
        String url = "http://mapi.yinyuetai.com/playlist/list.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&offset=" + offset
                + "&size=" + size;
        return url;
    }

    //网友推荐歌单-分类 http://144.34.228.215:3000/top/playlist/catlist?limit=2&offset=0&cat=怀旧
    public static String getRecommendAll(int offset, int size, String cat) {
        String url = "http://144.34.228.215:3000/top/playlist/catlist?limit="
                + size
                + "&offset=" + offset
                + "&cat=" + cat;
        return url;
    }

    //http://144.34.228.215:3000/playlist/detail?id=2900722591
    public static String getOnLineList(String id) {
        String url = "http://144.34.228.215:3000/playlist/detail?id="
                + id;
        return  url;
    }

    //歌手推荐
    public static String getArtistAll(int offset, int size, int cat) {
        String url = "http://144.34.228.215:3000/artist/list?limit="
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
        String url = "http://144.34.228.215:3000/check/music?id="
                + id;
        return url;
    }

    //根据歌曲id查询歌词
    public static String findLrc(String id) {
        String url = "http://144.34.228.215:3000/lyric?id="
                + id;
        return url;
    }

    /**
     * <p>获取音乐节目列表</p>
     *
     * @param artistIds
     * @param offset
     * @param size
     * @return
     */
    public static String getYinYueProgramList(String artistIds, int offset, int size) {
        String url = "http://mapi.yinyuetai.com/playlist/show.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&offset=" + offset
                + "&size=" + size
                + "&artistIds=" + artistIds;
        return url;
    }

    /**
     * 获取V榜地址
     *
     * @return
     */
    public static String getVChartAreasUrl() {
        String url = "http://mapi.yinyuetai.com/vchart/get_vchart_areas.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}";
        return url;
    }

    /**
     * 获取V榜的周期
     *
     * @return
     */
    public static String getVChartPeriodUrl(String area) {
        String url = "http://mapi.yinyuetai.com/vchart/period.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&area=" + area;
        return url;
    }

    /**
     * 获取V榜列表
     *
     * @param area
     * @param dateCode
     * @return
     */
    public static String getVChartListUrl(String area, int dateCode) {
        String url = "http://mapi.yinyuetai.com/vchart/show.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&area=" + area
                + "&datecode=" + dateCode;
        return url;
    }

    /**
     * 获取相关MV
     *
     * @param id
     * @return
     */
    public static String getRelativeVideoListUrl(int id) {
        String url = "http://mapi.yinyuetai.com/video/show.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&relatedVideos=true"
                + "&id=" + id;
        return url;
    }

    /**
     * 通过id 获取某人的悦单
     *
     * @param id
     * @return
     */
    public static String getPeopleYueDanList(int id) {
        String url = "http://mapi.yinyuetai.com/playlist/show.json?deviceinfo="
                + "{\"aid\":\"10201036\",\"os\":\"Android\","
                + "\"ov\":" + "\"" + getSystemVersion() + "\"" + ","
                + "\"rn\":\"480*800\","
                + "\"dn\":" + "\"" + getPhoneModel() + "\"" + ","
                + "\"cr\":\"46000\","
                + "\"as\":"
                + "\"WIFI\","
                + "\"uid\":"
                + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
                + "\"clid\":110025000}"
                + "&id=" + id;
        return url;
    }

    private static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    private static String getPhoneModel() {
        return android.os.Build.MODEL;
    }
}


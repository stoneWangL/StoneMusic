package com.stone.stonemusic.utils;

import android.util.Log;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.data.LrcStateContants;
import com.stone.stonemusic.model.LrcContent;
import com.stone.stonemusic.present.LrcComparator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LrcUtil implements LrcStateContants{
    public static final String TAG = "LrcUtil";
    public static final String lrcRootPath = android.os.Environment
            .getExternalStorageDirectory().toString()
            + "/StoneMusic/Lyrics/";

    private List<LrcContent> lrclists;

    public LrcUtil() {
        super();
        lrclists = new ArrayList<LrcContent>();
        lrclists.clear();
    }

    public static List<LrcContent> loadLrc(Music music) {
        List<LrcContent> lrclists = new ArrayList<LrcContent>();
        String path = music.getFileUrl(); /*音乐地址*/

        // 得到歌词文件路径，通过截取歌词地址最后一个.前的字符串，+.lrc
        String lrcPathString = path.substring(0, path.lastIndexOf("."))
                + ".lrc";
        // 获得最后一个/的位置
        int index = lrcPathString.lastIndexOf("/");

        String parentPath;
        String lrcName;
        // if(index!=-1){
        parentPath = lrcPathString.substring(0, index); /*歌词父路径地址*/
        lrcName = lrcPathString.substring(index); /*歌词名,eg:hello.lrc*/
        // }
        File file = new File(lrcPathString);

        // StoneMusic/Lyrics
        if (!file.exists()) {
            file = new File(getLrcPath(
                    music.getTitle(), music.getArtist()));
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        // 匹配Lyrics
        if (!file.exists()) {
            file = new File(parentPath + "/../" + "Lyrics/" + lrcName );
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        // 匹配lyric
        if (!file.exists()) {
            file = new File(parentPath + "/../" + "lyric/" + lrcName);
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        // 匹配Lyric
        if (!file.exists()) {
            file = new File(parentPath + "/../" + "Lyric/" + lrcName);
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        // 匹配lyrics
        if (!file.exists()) {
            file = new File(parentPath + "/../" + "lyrics/" + lrcName);
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        // 匹配华为音乐Musiclrc
        if (!file.exists()) {
            file = new File(parentPath + "/Musiclrc/" + lrcName);
        }
//        Log.i(TAG, file.getAbsolutePath().toString());

        if (file.exists()) {
            Log.e(TAG, "本地已搜索到歌词文件！！！\n本地歌词文件路径为" + file.getAbsolutePath().toString());
            try {
                FileInputStream fin = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fin, "utf-8");
                BufferedReader br = new BufferedReader(isr);

                String s;
                /*一行一行读取*/
                while ((s = br.readLine()) != null) {
                    Log.i(TAG, s); //打印每一行歌词
                    handleOneLine(s, lrclists);
                }
                // 按时间排序
                Collections.sort(lrclists, new LrcComparator());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "loadLrc->本地目录没有搜索到歌词文件！！！");
            return null;
        }


        return lrclists;
    }

    public static List<LrcContent> getNullLrclist() {
        List<LrcContent> lrcList = new ArrayList<LrcContent>();
        try {
            String s = "空";
            handleOneLine(s, lrcList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lrcList;
    }


    /**
     * 根据歌名和艺术家生产的系统默认歌词文件位置路径
     *
     * @param title
     * @param artist
     * @return
     */
    public static String getLrcPath(String title, String artist) {
        return lrcRootPath + title + "-" + artist + ".lrc";
    }

    static void handleOneLine(String line, List<LrcContent> lrclists) {
        String s = line.replace("[", ""); // 去掉左边括号
        String lrcData[] = s.split("]");

        // 这句是歌词
        if (lrcData[0].matches("^\\d{2}:\\d{2}.\\d+$")) {
            int len = lrcData.length;
            int end = lrcData[len - 1].matches("^\\d{2}:\\d{2}.\\d+$") ? len
                    : len - 1;

            for (int i = 0; i < end; i++) {
                LrcContent lrcContent = new LrcContent();
                int lrcTime = TimeUtil.getLrcMillTime(lrcData[i]);
                lrcContent.setLrcTime(lrcTime);
                if (lrcData.length == end)
                    lrcContent.setLrcStr(""); // 空白行
                else
                    lrcContent.setLrcStr(lrcData[len - 1]);

                lrclists.add(lrcContent);
            }

        }
    }

}

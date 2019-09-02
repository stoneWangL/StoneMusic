package com.stone.stonemusic.utils.lyric;

import android.util.Log;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.utils.code.LrcStateContants;
import com.stone.stonemusic.model.LrcContent;
import com.stone.stonemusic.presenter.LrcComparator;
import com.stone.stonemusic.utils.code.PlayType;

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

//    private List<LrcContent> lrclists;

    /**
     * public的构造方法
     */
    public LrcUtil() {
        super();
//        lrclists = new ArrayList<LrcContent>();
//        lrclists.clear();
    }

    /**
     * 根据歌名和艺术家生产的系统默认歌词文件位置路径
     * @param title
     * @param artist
     * @return 返回歌曲的路径
     */
    public static String getLrcPath(String title, String artist) {
        return lrcRootPath + title + "-" + artist + ".lrc";
    }

    /**
     * 根据传入的Music对象，查看本地是否有对象的歌词文件，如果有则返回对应的 List<LrcContent> 对象
     * @param music Music对象
     * @return null:本地没有歌词文件 ； List<LrcContent>:歌词文件，返回对应的 List<LrcContent> 对象
     */
    public static List<LrcContent> loadLrcFromLocalFile(Music music) {
        List<LrcContent> lrcContentList = new ArrayList<>();
        String lrcPathString;
        File file;
        if (music.getMusicType() == PlayType.LocalType){
            String path = music.getFileUrl(); //音乐地址
            // 得到歌词文件路径，通过截取歌词地址最后一个.前的字符串，+.lrc
            lrcPathString = path.substring(0, path.lastIndexOf(".")) + ".lrc";

            // 获得最后一个/的位置
            int index = lrcPathString.lastIndexOf("/");

            String parentPath;
            String lrcName;

            parentPath = lrcPathString.substring(0, index); /*歌词父路径地址*/
            lrcName = lrcPathString.substring(index); /*歌词名,eg:hello.lrc*/

            file = new File(lrcPathString);

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

        } else { //网络歌曲,只需要在stoneWang中查找
            lrcPathString = getLrcPath(music.getTitle(), music.getArtist());
            file = new File(lrcPathString);
            Log.i(TAG, "网络歌词路径是：" + file.getAbsolutePath());
        }

        if (file.exists()) {
            Log.e(TAG, "本地已搜索到歌词文件！！！\n本地歌词文件路径为" + file.getAbsolutePath());
            try {
                FileInputStream fin = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fin, "utf-8");
                BufferedReader br = new BufferedReader(isr);

                String oneLineString;
                /*一行一行读取*/
                while ((oneLineString = br.readLine()) != null) {
//                    Log.i(TAG, oneLineString); //打印每一行歌词
                    handleOneLine(oneLineString, lrcContentList);
                }
                // 按时间排序
                Collections.sort(lrcContentList, new LrcComparator());
                return lrcContentList; //没问题，返回list
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "loadLrcFromLocalFile->本地目录没有搜索到歌词文件！！！");
        return null;
    }

    /**
     * 将传入的String类型的字符串歌词，数据设置入LrcContent对象，然后add 到其list中，返回这个list
     * @param line 传入的是单行歌词String 例如：[00:12.570]难以忘记初次见你
     * @param lrcLists List<LrcContent>
     */
    static void handleOneLine(String line, List<LrcContent> lrcLists) {
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

                lrcLists.add(lrcContent);
            }

        }
    }

}

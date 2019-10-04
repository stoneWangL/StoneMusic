package com.stone.stonemusic.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/6 20:15
 * @Description:
 */
public class LoveListSpUtil {
    private static List<Music> musicList;

    /**
     * 添加歌曲到LoveList和Sp中
     * @param music music
     */
    public static synchronized void writeMusicToListAndSp(Music music) {
        //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        SharedPreferences sp = MusicApplication.getContext().
                getSharedPreferences("SP_LoveMusic_List", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //取出musicList
        musicList = SongModel.getInstance().getLoveSongList();
        musicList.add(music); //添加元素
        //list转化为json
        String loveListJsonStr = gson.toJson(musicList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SP_LoveMusic_List_DATA", loveListJsonStr); //存入json串
        editor.commit(); //提交
    }

    /**
     * 删除歌曲从LoveList和Sp中
     * @param music music
     */
    public static synchronized void deleteMusicFromListAndSp(Music music) {
        //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        SharedPreferences sp = MusicApplication.getContext().
                getSharedPreferences("SP_LoveMusic_List", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //取出musicList
        musicList = SongModel.getInstance().getLoveSongList();
        musicList.remove(music); //添加元素
        //list转化为json
        String loveListJsonStr = gson.toJson(musicList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SP_LoveMusic_List_DATA", loveListJsonStr); //存入json串
        editor.commit(); //提交
    }

    /**
     * 初始化操作
     * 从sp中读出保存的音乐列表，并存入list集合中
     * @return
     */
    public static synchronized List<Music> getLoveMusicListFromSp() {
        //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        SharedPreferences sp = MusicApplication.getContext().
                getSharedPreferences("SP_LoveMusic_List", Activity.MODE_PRIVATE);
        //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        String loveListJsonStr = sp.getString("SP_LoveMusic_List_DATA","");
        //防空判断
        if(loveListJsonStr!="") {
            Gson gson = new Gson();
            //将json字符串转换成List集合
            musicList = gson.fromJson(loveListJsonStr, new TypeToken<List<Music>>() {}.getType());
            SongModel.getInstance().setLoveSongList(musicList);
        }
        return SongModel.getInstance().getLoveSongList();
    }


}

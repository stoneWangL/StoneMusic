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
     * 添加歌曲到LoveList中并保存到Sp中
     * @param music 喜欢的music对象
     */
    public static void writeListToSp(Music music) {
        //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        SharedPreferences sp = MusicApplication.getContext().
                getSharedPreferences("SP_LoveMusic_List", Activity.MODE_PRIVATE);
        //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        String peopleListJson = sp.getString("SP_LoveMusic_List_DATA","");
        //防空判断
        if(peopleListJson!="") {
            Gson gson = new Gson();
            //将json字符串转换成List集合
            musicList = gson.fromJson(peopleListJson, new TypeToken<List<Music>>() {}.getType());
            musicList.add(music);
            SongModel.getInstance().setLoveSongList(musicList);
        }
    }

    /**
     * 返回LoveList
     * @return
     */
    public static List<Music> getLoveMusicListFromSp() {
        return SongModel.getInstance().getLoveSongList();
    }
}

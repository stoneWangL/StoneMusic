package com.stone.stonemusic.net;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.OnLineListBean;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.utils.code.PlayType;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/6 20:52
 * @Description: 将Json数据转化为Bean
 */
public class JsonToResult implements PlayType {
    private static final String TAG = "JsonToResult";

    /**
     *
     * @param jsonData 从接口返回的json字符串
     * @return null：代表获取数据表失败， YueDanBean2类型数据，表示返回数据成功
     */
    public static List<PlayListBean> getYueDanBean2FromJson(String jsonData){

        if (!TextUtils.isEmpty(jsonData)) { //返回数据非空
            try{

                JSONObject allNews = new JSONObject(jsonData);
                int code = allNews.getInt("code");

                while(200 == code){
                    String playLists = allNews.getString("playlists"); //playList数组的字符串
                    JSONArray playListsArray = new JSONArray(playLists);

                    List<PlayListBean> listBeans = new ArrayList<>();
                    for (int i = 0; i < playListsArray.length(); i++) { //遍历playList数组,添加进对象
                        JSONObject playListObject = playListsArray.getJSONObject(i);
                        PlayListBean playListBean = getPlayListBeanInstance(
                                playListObject.getString("name"),
                                playListObject.getString("id"),
                                playListObject.getString("coverImgUrl"),
                                playListObject.getString("description"));
                        listBeans.add(playListBean);
                    }
                    return listBeans;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private static PlayListBean getPlayListBeanInstance(
            String name,
            String id,
            String coverImgUrl,
            String description) {
        PlayListBean playListBean = new PlayListBean();
        playListBean.setName(name);
        playListBean.setId(id);
        playListBean.setCoverImgUrl(coverImgUrl);
        playListBean.setDescription(description);
        return playListBean;
    }

    /**
     *
     * @param jsonData 从接口返回的json字符串
     * @return null：代表获取数据表失败， Music类型数据，表示返回数据成功
     */
    public static List<Music> getOnLineListBeanFromJson(String jsonData){
        if (!TextUtils.isEmpty(jsonData)) { //返回数据非空
            try{

                JSONObject allNews = new JSONObject(jsonData);
                int code = allNews.getInt("code");

                while(200 == code){
                    String playLists = allNews.getString("playlist"); //playList的字符串
                    JSONObject playListsObj = new JSONObject(playLists);
                    String tracks = playListsObj.getString("tracks"); //tracks数组的字符串

                    JSONArray tracksArray = new JSONArray(tracks);


                    List<Music> listBeans = new ArrayList<>();
                    for (int i = 0; i < tracksArray.length(); i++) { //遍历playList数组,添加进对象
                        JSONObject OnLineListObject = tracksArray.getJSONObject(i);
                        //ar
                        JSONArray OnLineListArrayAr = new JSONArray(OnLineListObject.getString("ar"));
                        JSONObject OnLineListObjectAr0 = OnLineListArrayAr.getJSONObject(0);
                        String ar_name = OnLineListObjectAr0.getString("name");
                        //al
                        JSONObject OnLineListArrayAl = new JSONObject(OnLineListObject.getString("al"));
                        String al_picUrl = OnLineListArrayAl.getString("picUrl");
                        Music music = getOnLineMusicInstance(
                                OnLineListObject.getString("name"),
                                OnLineListObject.getString("id"),
                                ar_name,
                                al_picUrl
                        );
                        listBeans.add(music);
                    }
                    Log.i(TAG, "listBeans.size=" + listBeans.size());
                    return listBeans;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Music getOnLineMusicInstance(
            String name,
            String id,
            String ar_name,
            String al_picUrl) {
        Music music = new Music();
        music.setMusicType(PlayType.OnlineType); //设置歌曲类型->在线歌曲
        music.setTitle(name);
        music.setMusicId(id);
        music.setArtist(ar_name);
        music.setPicUrl(al_picUrl);
        return music;
    }

}

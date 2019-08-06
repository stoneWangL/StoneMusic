package com.stone.stonemusic.net;

import android.text.TextUtils;
import android.util.Log;

import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.model.YueDanBean2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/6 20:52
 * @Description: 将Json数据转化为Bean
 */
public class JsonToResult {
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


//                for (int i=0; i<allNews.length(); i++){
//
//                    JSONObject NewsObject = allNews.getJSONObject(i);
//
//                    int newsid = NewsObject.getInt("id");
//                    String title = NewsObject.getString("title");
//                    String date = NewsObject.getString("date");
//                    String category = NewsObject.getString("category");
//                    String author_name = NewsObject.getString("author_name");
//                    String url = NewsObject.getString("url");
//                    String thumbnail_pic_s = NewsObject.getString("thumbnail_pic_s");
//
//                    GuojiNews News = new GuojiNews();
//
//                    News.setNewsid(newsid);
//                    News.setTitle(title);
//                    News.setDate(date);
//                    News.setCategory(category);
//                    News.setAuthor_name(author_name);
//                    News.setUrl(url);
//                    News.setThumbnail_pic_s(thumbnail_pic_s);
//                    News.save();
//                }
//                return true;
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
}

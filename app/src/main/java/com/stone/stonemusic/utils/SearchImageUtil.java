package com.stone.stonemusic.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : stoneWang
 * date   : 2019/4/811:26
 * 传入搜索的关键字，返回对应图片的网络地址
 */
public class SearchImageUtil {
    private static String TAG = "SearchImageUtil";
    private static String resultUrl;
    private static String url;
    private static OkHttpClient client;
    private static Request request;
    private static Response response;
    private static String responseData;

    private static String getUrl(String word, int num) {
        return "http://image.so.com/j?q=" + word + "&src=srp&correct=" + word + "&sn=" + num + "&pn=" + num;
    }

    /**
     * 耗时操作，请调用者在工作线程使用
     * @param searchWord
     * @return
     */
    public static String getImageUrl(String searchWord) {
        try {
            url = getUrl(searchWord, 1); //获取查询URL

            client = new OkHttpClient();
            request = new Request.Builder()
                    .url(url)
                    .build();

            response = client.newCall(request).execute();
            responseData = response.body().string();

            if (!TextUtils.isEmpty(responseData)) {

                JSONObject AllData = new JSONObject(responseData);
                JSONArray lrcArray = AllData.getJSONArray("list");

                Log.d(TAG, "stone JSONArray大小 ==" + lrcArray.length());

                for (int i=0; i<lrcArray.length(); i++){
                    JSONObject oneLrcObject = lrcArray.getJSONObject(i);
                    resultUrl = oneLrcObject.getString("img");
                    Log.i(TAG, "图片地址是：" + resultUrl);
                    return resultUrl; /*只返回一个*/
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

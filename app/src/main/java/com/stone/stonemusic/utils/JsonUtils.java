package com.stone.stonemusic.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * author : stoneWang
 * date   : 2019/3/1122:52
 */
public class JsonUtils {
    private static String TAG = "JsonUtils";
    public static void parseJsonComment(String jsonData){

        if (!TextUtils.isEmpty(jsonData)) {
            try {
                JSONObject AllData = new JSONObject(jsonData);
                JSONArray lrcArray = AllData.getJSONArray("result");
                Log.d(TAG, "stone JSONArray大小 ==" + lrcArray.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

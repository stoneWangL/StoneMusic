package com.stone.stonemusic.net

import android.util.Log
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import com.google.gson.GsonBuilder



/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 10:12
 * @Description:
 */
open class MRequest<RESPONSE>(val type: Int, val url:String, val handler: ResponseHandler<RESPONSE>) {
    val TAG = "MRequest"

    /**
     * 解析网络请求的一个结果
     */
    fun parseResult(result: String?): RESPONSE {
        Log.i(TAG, "parseResult->result=$result")
//        JsonToResult.getYueDanBean2FromJson(result)
        val gson = Gson()
////        val gson = GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create()
//        //获取泛型类型
        val type = (this.javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val list = gson.fromJson<RESPONSE>(result, type)
        return list
    }
//    var gson = GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create()
//    var json: User = gson.fromJson(jsonStr, User::class.java)

    /**
     * 发送网络请求
     */
    fun excute() {
        NetManager.manager.sendRequest(this)
    }
}
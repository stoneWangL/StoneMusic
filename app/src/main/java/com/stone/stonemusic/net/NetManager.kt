package com.stone.stonemusic.net

import android.util.Log
import com.stone.stonemusic.utils.ThreadUtil
import okhttp3.*
import java.io.IOException

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 10:11
 * @Description:
 */
class NetManager private constructor(){
    val TAG = "NetManager"
    val client by lazy { OkHttpClient() }
    companion object{
        val manager by lazy { NetManager() }
    }

    /**
     * 发送网络请求
     */
    fun <RESPONSE>sendRequest(req: MRequest<RESPONSE>) {
//        val path = URLProviderUtils.getHomeUrl(0,20) //MRequest中就有url地址，所以不需要这么写
//        val client = OkHttpClient()
        Log.i(TAG, "sendRequest->req.url =${req.url}")
        val request = Request.Builder()
                .url(req.url)
                .get() //请求方式
                .build()


        client.newCall(request).enqueue(object : Callback {
            /**
             * 子线程调用
             */
            override fun onFailure(call: Call, e: IOException) {
                ThreadUtil.runOnMainThread(object : Runnable{
                    override fun run() {
                        //隐藏刷新控件
//                            refreshLayout.isRefreshing = false
                        //回调到view层处理
//                        homeView.onError(e.message)
                        req.handler.onError(req.type, e.message)
                    }
                })
//                    myToast("获取数据失败")

            }

            /**
             * 子线程调用
             */
            override fun onResponse(call: Call, response: Response) {
//                    myToast("获取数据成功")
                val result = response.body?.string()
                val parseResult = req.parseResult(result)
//                val gson = Gson()
//                val list = gson.fromJson<List<HomeItemBean>>(result,object : TypeToken<List<HomeItemBean>>(){}.type)

                //刷新列表
                ThreadUtil.runOnMainThread(object : Runnable{
                    override fun run() {
                        //隐藏刷新控件
//                            refreshLayout.isRefreshing = false
                        //刷新列表
//                            adapter.updateList(list)
                        //将正确的结果回调给view层
//                        homeView.loadSuccess(list)
                        req.handler.onSuccess(req.type, parseResult)

                    }

                })


            }

        })
    }
}
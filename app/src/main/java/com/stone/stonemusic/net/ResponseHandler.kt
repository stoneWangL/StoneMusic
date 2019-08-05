package com.stone.stonemusic.net

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 10:10
 * @Description: 请求的回调
 */
interface ResponseHandler<RESPONSE> {
    /**
     * 成功的回调
     */
    fun onError(type:Int, msg: String?)

    /**
     * 失败的回调
     * 泛型：RESPONSE
     */
    fun onSuccess(type: Int, result: RESPONSE)
}
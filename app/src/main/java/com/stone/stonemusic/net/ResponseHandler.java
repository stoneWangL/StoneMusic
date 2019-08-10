package com.stone.stonemusic.net;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/10 22:26
 * @Description: 请求的回调
 * 声明泛型RESPONSE
 */
public interface ResponseHandler<RESPONSE> {

    /**
     * 失败的回调
     */
    void onError(int type, String message);

    /**
     * 成功的回调
     * 泛型：RESPONSE
     */
    void onSuccess(int type, RESPONSE result);

}

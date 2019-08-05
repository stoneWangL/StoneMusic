package com.stone.stonemusic.net

import com.itheima.player.model.bean.YueDanBean
import com.stone.stonemusic.utils.URLProviderUtils

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/5 10:01
 * @Description: 悦单界面网络请求request
 */
class YueDanRequest(type:Int, offset:Int, handler:ResponseHandler<YueDanBean>)
    : MRequest<YueDanBean>(type, URLProviderUtils.getWangYouPushAll(offset, 10), handler) {
}
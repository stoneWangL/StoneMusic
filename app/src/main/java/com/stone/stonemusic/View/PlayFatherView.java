package com.stone.stonemusic.View;

import com.stone.stonemusic.model.Music;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 0:44
 * @Description:
 */
public interface PlayFatherView {
    void onLrcItemClick(Music music);

    void GetLrcListFail();

    //让V层实现，去显示可能相同的歌词 歌单list
    void GetLrcListSuccess(List<Music> list);

    void DownloadLrcSuccess();

    void DownloadLrcFail();
}

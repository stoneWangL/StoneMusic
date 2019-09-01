package com.stone.stonemusic.presenter.interf;

import com.stone.stonemusic.model.Music;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 1:42
 * @Description:
 */
public interface PlayFatherPresenter {
    void onLrcItemClick(Music music);

    void GetLrcListFail();

    void GetLrcListSuccess(List<Music> list);
}

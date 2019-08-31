package com.stone.stonemusic.View;

import android.view.View;

import com.stone.stonemusic.model.Music;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/31 19:08
 * @Description: FindActivity的View接口
 */
public interface FindView {
    void loadSuccess();

    void loadFalse();


    void clickFindBtn();

    void feedBackResult(final int result);

    void notifyMusicList(List<Music> musicList);
}

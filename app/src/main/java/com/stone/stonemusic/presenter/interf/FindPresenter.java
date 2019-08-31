package com.stone.stonemusic.presenter.interf;

import android.view.View;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/31 19:07
 * @Description:
 */
public interface FindPresenter {
    void loadSuccess();

    void loadFalse();

    void clickFindBtn(String findStr);
}

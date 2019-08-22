package com.stone.stonemusic.View;

import android.view.View;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/20 15:57
 * @Description:
 */
public interface OnLineView {
    void loadSuccess();

    void loadFalse();

    void onItemClick(View v, int position);
}

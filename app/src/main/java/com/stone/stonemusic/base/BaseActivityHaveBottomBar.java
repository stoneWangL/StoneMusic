package com.stone.stonemusic.base;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:32
 * @Description:
 */
public class BaseActivityHaveBottomBar extends BaseActivity {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}

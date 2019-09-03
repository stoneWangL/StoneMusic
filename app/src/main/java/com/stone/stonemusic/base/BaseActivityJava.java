package com.stone.stonemusic.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stone.stonemusic.R;
import com.stone.stonemusic.View.ActivityView;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 22:57
 * @Description: 所有Activity的基类 java版
 */
abstract public class BaseActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置顶部状态栏颜色
        ActivityView.setStatusBarColor(this, R.color.colorStoneThemeShallow, true);

        setContentView(getLayoutId());
        initListener();
        initData();
    }

    /**
     * 获取布局id
     */
    public abstract int getLayoutId();

    /**
     * adapter listener
     */
    protected abstract void initListener();


    /**
     * 初始化数据
     */
    protected abstract void initData();

}

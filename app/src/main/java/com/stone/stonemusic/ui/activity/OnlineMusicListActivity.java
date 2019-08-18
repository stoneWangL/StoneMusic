package com.stone.stonemusic.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.stone.stonemusic.R;
import com.stone.stonemusic.base.BaseActivity;
import com.stone.stonemusic.model.PlayListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/18 21:38
 * @Description:
 */
public class OnlineMusicListActivity extends BaseActivity {
    List<PlayListBean> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online_music_list;
    }

    @Override
    protected void initListener() {
        //设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initData() {
        //通过Intent获取传过来的内容,来填充
        Intent intent = getIntent();
//        list = (List<PlayListBean>)intent.getExtras("obj");//武器名
//        @Override
//        public List getList(Object o) {
//            List<PlayListBean> response = (List<PlayListBean>)o;
//            return response;
//        }
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}

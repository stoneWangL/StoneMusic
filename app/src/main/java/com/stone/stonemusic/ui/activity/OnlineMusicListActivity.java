package com.stone.stonemusic.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    PlayListBean data;
    ImageView imageView;
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
        imageView = findViewById(R.id.imageViewTheme);

    }

    @Override
    protected void initData() {
        //通过Intent获取传过来的内容,来填充
        Intent intent = getIntent();
        data = getIntent().getParcelableExtra("obj");

        if (data.getCoverImgUrl() != null)
            Glide.with(this).load(data.getCoverImgUrl()).into(imageView);
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}

package com.stone.stonemusic.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.View.OnLineView;
import com.stone.stonemusic.adapter.OnlineMusicListAdapter;
import com.stone.stonemusic.base.BaseActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.PlayListBean;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.OnLineListPresenterImpl;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.ToastUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/18 21:38
 * @Description:
 */
public class OnlineMusicListActivity extends BaseActivity implements OnLineView {
    private static final String TAG = "OnlineMusicListActivity";
    PlayListBean data;
    ImageView imageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    private ListView listView;
    public ProgressDialog mDialog;
    OnLineListPresenterImpl onLineListPresenter;
    private OnlineMusicListAdapter listAdapter;
    private List<Music> musicList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_online_music_list;
    }

    @Override
    protected void initListener() {
        //自定义toolbar
        toolbar = findViewById(R.id.toolbar_land_show);
        setSupportActionBar(toolbar);
        //设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_land_show);
        imageView = findViewById(R.id.imageViewTheme);
        listView = findViewById(R.id.listView_onlineMusicList);
        onLineListPresenter = new OnLineListPresenterImpl(this);

    }

    @Override
    protected void initData() {
        //通过Intent获取传过来的内容,来填充
        Intent intent = getIntent();
        data = intent.getParcelableExtra("obj");
        //设置title
        if (data.getName() != null)
            collapsingToolbar.setTitle(data.getName());
        //设置titleImage
        if (data.getCoverImgUrl() != null)
            Glide.with(this).load(data.getCoverImgUrl()).into(imageView);
        if(data.getId() != null) {
            showDialog();
            onLineListPresenter.startTask(data.getId()); //开启查询任务

        }

    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    /**
     * 顶部item的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回按钮
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.cancel();
                ToastUtils.getToastShort("获取列表成功");
                musicList = SongModel.getInstance().getSongList();
                Log.i(TAG, "musicList.size=" + musicList.size());
                listAdapter = new OnlineMusicListAdapter(MusicApplication.getContext(),R.layout.item_music,musicList);
                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void loadFalse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.cancel();
                ToastUtils.getToastShort("获取列表失败");
            }
        });
    }



    /**
     *显示Dialog
     */
    private void showDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在加载列表...");
        mDialog.show();
    }
}

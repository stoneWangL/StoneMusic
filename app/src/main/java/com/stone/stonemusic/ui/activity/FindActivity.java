package com.stone.stonemusic.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.base.BaseActivity;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;
import com.stone.stonemusic.model.Music;
import com.stone.stonemusic.model.bean.SongModel;
import com.stone.stonemusic.presenter.impl.JumpToOtherWhere;
import com.stone.stonemusic.presenter.impl.MusicObserverManager;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.presenter.interf.MusicObserverListener;
import com.stone.stonemusic.utils.MusicApplication;
import com.stone.stonemusic.utils.code.MediaStateCode;
import com.stone.stonemusic.utils.code.PlayType;
import com.stone.stonemusic.utils.playControl.MediaUtils;
import com.stone.stonemusic.utils.playControl.MusicResources;
import com.stone.stonemusic.utils.playControl.PlayControl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/28 17:28
 * @Description:
 */
public class FindActivity extends BaseHaveBottomBarActivity {
    private static String TAG = "FindActivity";


    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    protected void initListenerOther() {

    }

    @Override
    protected void initDataOther() {

    }

    @Override
    protected void observerUpDataOtherPlayStartPositionChange() {

    }

    @Override
    protected void observerUpDataOtherContinueStopPause() {

    }

    @Override
    protected void onDestroyOther() {

    }


}

package com.stone.stonemusic.UI.activity;

import android.view.View;

import com.stone.stonemusic.R;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/6 20:07
 * @Description:
 */
public class LoveListActivity extends BaseHaveBottomBarActivity
        implements View.OnClickListener {
    public static final String TAG = "LoveListActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_love;
    }

    @Override
    public void onClick(View v) {

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

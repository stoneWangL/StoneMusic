package com.stone.stonemusic.UI.activity;

import android.view.View;
import android.widget.TextView;

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
    private TextView tvGeneralTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_love;
    }

    /**
     * 顶部导航，back键逻辑
     * @param view
     */
    public void backClick(View view) {
        finish();
        overridePendingTransition(R.anim.stop, R.anim.right_out);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initListenerOther() {
        tvGeneralTitle = findViewById(R.id.tv_general_title);
    }

    @Override
    protected void initDataOther() {
        tvGeneralTitle.setText("我喜欢的音乐");
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

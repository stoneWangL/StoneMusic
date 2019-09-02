package com.stone.stonemusic.UI.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.base.BaseHaveBottomBarActivity;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/9/2 17:24
 * @Description:
 */
public class LocalArtistListActivity extends BaseHaveBottomBarActivity {
    public static final String TAG = "LocalArtistListActivity";
    private TextView artistTextView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_local_artist_list;
    }

    @Override
    protected void initListenerOther() {
        artistTextView = findViewById(R.id.tv_localArtistList_artist);
    }

    @Override
    protected void initDataOther() {
        Intent intent = getIntent();
        String artistStr = intent.getStringExtra("artist");
        artistTextView.setText(artistStr);
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

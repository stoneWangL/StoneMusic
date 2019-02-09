package com.stone.stonemusic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.utils.ActivityUtils;

public class PlayActivity extends AppCompatActivity {
    public static final String TAG = "PlayActivity";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initColor();


    }

    private void initColor() {
        int statusColor = ActivityUtils.initColor(this);
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mToolbar.setBackgroundColor(getResources().getColor(statusColor));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }
}

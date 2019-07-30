package com.stone.stonemusic.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stone.stonemusic.R;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/7/29 23:19
 * @Description:
 */
public class Welcome extends BaseNoBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}

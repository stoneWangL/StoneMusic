package com.stone.stonemusic.present;

import android.app.Activity;
import android.content.Intent;

import com.stone.stonemusic.R;
import com.stone.stonemusic.ui.activity.PlayActivity;

/**
 * author : stoneWang
 * date   : 2019/7/521:08
 */
public class JumpToOtherWhere {

    private JumpToOtherView jumpToOtherView;

    public JumpToOtherWhere(JumpToOtherView view) {
        jumpToOtherView = view;
    }

    /*跳转到PlayActivity*/
    public void GoToPlayActivity(){
        Activity context = (Activity)jumpToOtherView;
        Intent intent = new Intent(context, PlayActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }
}

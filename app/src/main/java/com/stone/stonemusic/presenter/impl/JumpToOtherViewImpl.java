package com.stone.stonemusic.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.stone.stonemusic.R;
import com.stone.stonemusic.presenter.interf.JumpToOtherView;
import com.stone.stonemusic.UI.activity.HomeActivity;
import com.stone.stonemusic.UI.activity.PlayActivity;

/**
 * author : stoneWang
 * date   : 2019/7/521:08
 */
public class JumpToOtherViewImpl {

    private JumpToOtherView jumpToOtherView;
    private Activity context;

    public JumpToOtherViewImpl(JumpToOtherView activity) {
        jumpToOtherView = activity;
        context = (Activity)jumpToOtherView;
    }

    /*跳转到PlayActivity*/
    public void GoToPlayActivity(){
        Intent intent = new Intent(context, PlayActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
        context.overridePendingTransition(R.anim.push_up_in,R.anim.stop);
    }

    /**
     * 延时几2.65秒，跳转至LocalListActivity
     */
    public void jumpToLocalListActivity(){

        //延迟一段时间后跳转到另一个界面
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent2 = new Intent (context, HomeActivity.class);
                context.startActivity(intent2);//跳转界面
                context.finish();//关闭此界面
            }
        }, 2650);
    }

    /**
     * 延迟2秒，退出当前Activity
     */
    public void exitActivityTwoSecondLater(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        }, 2000);
    }
}

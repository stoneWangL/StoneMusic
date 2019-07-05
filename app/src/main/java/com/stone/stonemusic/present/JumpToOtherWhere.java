package com.stone.stonemusic.present;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.stone.stonemusic.R;
import com.stone.stonemusic.present.interfaceOfPresent.JumpToOtherView;
import com.stone.stonemusic.ui.activity.LocalListActivity;
import com.stone.stonemusic.ui.activity.PlayActivity;

/**
 * author : stoneWang
 * date   : 2019/7/521:08
 */
public class JumpToOtherWhere {

    private JumpToOtherView jumpToOtherView;
    private Activity context;

    public JumpToOtherWhere(JumpToOtherView activity) {
        jumpToOtherView = activity;
        context = (Activity)jumpToOtherView;
    }

    /*跳转到PlayActivity*/
    public void GoToPlayActivity(){
        Intent intent = new Intent(context, PlayActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    /**
     * 延时几2.65秒，跳转至LocalListActivity
     */
    public void jumpToLocalListActivity(){

        //延迟一段时间后跳转到另一个界面
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent2 = new Intent (context,LocalListActivity.class);
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

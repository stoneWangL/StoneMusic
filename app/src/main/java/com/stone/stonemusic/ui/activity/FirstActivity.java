package com.stone.stonemusic.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;

public class FirstActivity extends BaseNoBarActivity {
    private TextView tv = null;
    private ImageView iv = null;
    private Animation animation1 = null, animation2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        tv = findViewById(R.id.first_music_text);
        iv = findViewById(R.id.first_music_log);

        initAnim();

        initJump();
    }

    /**
     * 初始化动画
     */
    private void initAnim() {

        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        tv.startAnimation(animation1);

        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        iv.startAnimation(animation2);

    }

    /**
     * 延时几秒，跳转至MainActivity
     */
    private void initJump(){
        //延迟一段时间后跳转到另一个界面
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent2 = new Intent (FirstActivity.this,MainActivity.class);
                startActivity(intent2);//跳转界面
                FirstActivity.this.finish();//关闭此界面
            }
        }, 2600);
    }
}

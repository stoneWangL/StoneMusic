package com.stone.stonemusic.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.present.PresentOfFirstActivity;
import com.stone.stonemusic.utils.ToastUtils;

public class FirstActivity extends BaseNoBarActivity {
    private static String TAG = "FirstActivity";
    private TextView tv = null;
    private ImageView iv = null;
    private Animation animation1 = null, animation2 = null;
    private PresentOfFirstActivity present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        tv = (TextView) findViewById(R.id.first_music_text);
        iv = (ImageView) findViewById(R.id.first_music_log);

        initAnim();
        initPermissions();
    }



    /**
     * 动态获取存储读权限
     */
    private void initPermissions() {
        present = new PresentOfFirstActivity(this);
        present.initPermission();
    }

    /**
     * 根据用户是否同意APP具有存储读权限，跳转功能页面或者退出。
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    present.jumpToLocalListActivity();
                }else{
                    /*没有得到许可，退出*/
                    ToastUtils.getToastShort("未许可权限，软件将自动退出");
                    present.exitActivityTwoSecondLater();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}

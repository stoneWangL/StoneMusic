package com.stone.stonemusic.ui.activity;

import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.stonemusic.R;
import com.stone.stonemusic.presenter.JumpToOtherWhere;
import com.stone.stonemusic.presenter.GetPermission;
import com.stone.stonemusic.presenter.JumpToOtherView;
import com.stone.stonemusic.utils.ToastUtils;

public class FirstActivity extends BaseNoBarActivity implements JumpToOtherView{
    private static String TAG = "FirstActivity";
    private TextView tv = null;
    private ImageView iv = null;
    private Animation animation1 = null, animation2 = null;
    private GetPermission getPermission;
    public JumpToOtherWhere jumpToOtherWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getPermission = new GetPermission(this);
        jumpToOtherWhere = new JumpToOtherWhere(this);

        tv = (TextView) findViewById(R.id.first_music_text);
        iv = (ImageView) findViewById(R.id.first_music_log);

        initAnim();
        initPermissions();
    }




    /**
     * 动态获取存储读权限
     */
    private void initPermissions() {
        getPermission.initPermission();
    }

    /**
     * 根据用户是否同意APP具有存储读权限，跳转功能页面或者退出。
     * @param requestCode 返回值
     * @param permissions permissions数组
     * @param grantResults 结果数组
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    jumpToOtherWhere.jumpToLocalListActivity();
                }else{
                    /*没有得到许可，退出*/
                    ToastUtils.getToastShort("未许可权限，软件将自动退出");
                    jumpToOtherWhere.exitActivityTwoSecondLater();
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "alpha", 0f, 1f);
        animator.setDuration(2600);
        animator.start();

        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        iv.startAnimation(animation2);
    }
}

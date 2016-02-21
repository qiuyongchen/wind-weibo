package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.qiuyongchen.windweibo.R;

/**
 * @author qiuyongchen
 */
public class LogoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        ImageView imageView = (ImageView) this.findViewById(R.id.img_logo);

        // 利用动画实现图像透明度的渐变
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            // 动画结束后就跳转到登录页面
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(intent);
                // 结束本Activity
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.setAnimation(alphaAnimation);


    }
}

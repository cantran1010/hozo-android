package vn.tonish.hozo.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.TransitionScreen;

import static vn.tonish.hozo.common.Constants.SPLASH_TIME;

/**
 * Created by Can Tran on 4/11/17.
 */

public class SplashActivity extends BaseActivity {
    private ImageView imgLogo;
    private Animation animation;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        animation = AnimationUtils.loadAnimation(this, R.anim.local_matching_effect);
    }

    @Override
    protected void initData() {
        imgLogo.setAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (UserManager.checkLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class), TransitionScreen.FADE_IN);
                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class),TransitionScreen.FADE_IN);
              }
           }
        }, SPLASH_TIME);
    }

    @Override
    protected void resumeData() {

    }
}

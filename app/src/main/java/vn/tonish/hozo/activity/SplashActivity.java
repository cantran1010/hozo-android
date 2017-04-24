package vn.tonish.hozo.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.UserManager;

import static vn.tonish.hozo.common.Constants.SPLASH_TIME;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {
    private LinearLayout llLogo;
    private Animation animation;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        llLogo = (LinearLayout) findViewById(R.id.ll_logo);
        animation = AnimationUtils.loadAnimation(this, R.anim.local_matching_effect);
    }

    @Override
    protected void initData() {
        llLogo.setAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserManager.checkLogin(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginScreen.class));
                }
            }
        }, SPLASH_TIME);
    }

    @Override
    protected void resumeData() {

    }
}

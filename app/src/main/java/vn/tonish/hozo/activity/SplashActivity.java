package vn.tonish.hozo.activity;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.UserManager;

import static vn.tonish.hozo.common.Constants.SPLASH_TIME;

/**
 * Created by Can Tran on 4/11/17.
 */

public class SplashActivity extends BaseActivity {
    private ImageView imgLogo;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        imgLogo = (ImageView) findViewById(R.id.img_logo);
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserManager.checkLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
            }
        }, SPLASH_TIME);
        finish();
    }

    @Override
    protected void resumeData() {

    }
}

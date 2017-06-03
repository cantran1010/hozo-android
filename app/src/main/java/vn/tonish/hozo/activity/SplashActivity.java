package vn.tonish.hozo.activity;

import android.os.Handler;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.UserManager;

import static vn.tonish.hozo.common.Constants.SPLASH_TIME;

/**
 * Created by CanTran on 4/11/17.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserManager.checkLogin())
                    startActivity(MainActivity.class);
                else
                    startActivity(HomeActivity.class);

                finish();
            }
        }, SPLASH_TIME);

    }

    @Override
    protected void resumeData() {

    }
}

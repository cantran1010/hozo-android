package vn.tonish.hozo.activity;

import android.os.Handler;
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
                if (UserManager.checkLogin())
                    startActivity(MainActivity.class, TransitionScreen.FADE_IN);
                else
                    startActivity(HomeActivity.class, TransitionScreen.FADE_IN);

                finish();
            }
        }, SPLASH_TIME);

    }

    @Override
    protected void resumeData() {

    }
}

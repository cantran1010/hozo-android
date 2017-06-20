package vn.tonish.hozo.activity;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.LoginFragment;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by CanTran on 5/9/17.
 */

public class HomeActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activty_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

//        if (getIntent().hasExtra(Constants.LOGOUT_EXTRA))
//            if (getIntent().getBooleanExtra(Constants.LOGOUT_EXTRA, true))
                Utils.cancelAllNotification(this);

        openFragment(R.id.layout_container, LoginFragment.class, false, TransitionScreen.FADE_IN);
    }

    @Override
    protected void resumeData() {

    }

}

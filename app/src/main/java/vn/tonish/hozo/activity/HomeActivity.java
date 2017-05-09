package vn.tonish.hozo.activity;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.LoginFragment;

/**
 * Created by tonish1 on 5/9/17.
 */

public class HomeActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.home_activty;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, LoginFragment.class, false,true);

    }

    @Override
    protected void resumeData() {

    }
}

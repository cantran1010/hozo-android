package vn.tonish.hozo.activity.other;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;

/**
 * Created by huyquynh on 4/12/17.
 */

public class ProfileActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        setBackButton();
        setTitleHeader(getString(R.string.profile_tv_header));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }
}

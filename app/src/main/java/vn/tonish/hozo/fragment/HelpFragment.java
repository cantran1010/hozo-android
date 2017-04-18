package vn.tonish.hozo.fragment;

import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.ProfileActivity;

/**
 * Created by Admin on 4/4/2017.
 */

public class HelpFragment extends BaseFragment {
    @Override
    protected int getLayout() {
        return R.layout.setting_fragment;
    }

    @Override
    protected void initView() {
        findViewById(R.id.tvProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ProfileActivity.class);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }
}

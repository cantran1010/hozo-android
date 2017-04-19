package vn.tonish.hozo.fragment;

import android.view.View;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.ProfileActivity;
import vn.tonish.hozo.activity.other.ShareActivity;

/**
 * Created by Admin on 4/4/2017.
 */

public class HelpFragment extends BaseFragment implements View.OnClickListener {
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
        findViewById(R.id.tvShare).setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvShare:
                startActivity(ShareActivity.class);
                break;
        }
    }
}

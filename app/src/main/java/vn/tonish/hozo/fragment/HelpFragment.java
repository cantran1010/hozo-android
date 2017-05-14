package vn.tonish.hozo.fragment;

import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.AboutActivity;
import vn.tonish.hozo.activity.other.ConditionActivity;
import vn.tonish.hozo.activity.other.HistoryActivity;
import vn.tonish.hozo.activity.other.InfoActivity;
import vn.tonish.hozo.activity.other.NADActivity;
import vn.tonish.hozo.activity.other.PaymentActivity;
import vn.tonish.hozo.activity.other.ProfileActivity;
import vn.tonish.hozo.activity.other.ShareActivity;
import vn.tonish.hozo.utils.TransitionScreen;

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
                startActivity(ProfileActivity.class, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_history).setOnClickListener(this);
        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_payment).setOnClickListener(this);
        findViewById(R.id.tv_condition).setOnClickListener(this);
        findViewById(R.id.tv_nda).setOnClickListener(this);
        findViewById(R.id.tv_nda).setOnClickListener(this);
        findViewById(R.id.tv_info).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);

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
            case R.id.tv_share:
                startActivity(ShareActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_history:
                startActivity(HistoryActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_payment:
                startActivity(PaymentActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_condition:
                startActivity(ConditionActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_nda:
                startActivity(NADActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_info:
                startActivity(InfoActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_about:
                startActivity(AboutActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}

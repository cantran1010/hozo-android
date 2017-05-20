package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.GeneralInfoActivity;
import vn.tonish.hozo.activity.other.ProfileActivity;
import vn.tonish.hozo.activity.other.ShareActivity;
import vn.tonish.hozo.common.Constants;
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
        findViewById(R.id.layout_profile).setOnClickListener(this);
        findViewById(R.id.layout_share).setOnClickListener(this);
//        findViewById(R.id.tv_history).setOnClickListener(this);
//        findViewById(R.id.tv_payment).setOnClickListener(this);
        findViewById(R.id.layout_condition).setOnClickListener(this);
        findViewById(R.id.layout_nda).setOnClickListener(this);
        findViewById(R.id.layout_nda).setOnClickListener(this);
        findViewById(R.id.layout_info).setOnClickListener(this);
        findViewById(R.id.layout_about).setOnClickListener(this);
        findViewById(R.id.layout_share).setOnClickListener(this);


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

            case R.id.layout_profile:
                startActivity(ProfileActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.layout_share:
                startActivity(ShareActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

//            case R.id.tv_history:
//                startActivity(HistoryActivity.class, TransitionScreen.RIGHT_TO_LEFT);
//                break;
//
//            case R.id.tv_payment:
//                startActivity(PaymentActivity.class, TransitionScreen.RIGHT_TO_LEFT);
//                break;

            case R.id.layout_condition:
                openGeneralInfoActivity(getString(R.string.other_condition), "http://vnexpress.net/");
                break;

            case R.id.layout_nda:
                openGeneralInfoActivity(getString(R.string.other_nad), "http://vnexpress.net/");
                break;

            case R.id.layout_info:
                openGeneralInfoActivity(getString(R.string.other_info), "http://vnexpress.net/");
                break;

            case R.id.layout_about:
                openGeneralInfoActivity(getString(R.string.other_about), "http://vnexpress.net/");
                break;

        }
    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(getActivity(), GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }


}

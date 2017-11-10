package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.GeneralInfoActivity;
import vn.tonish.hozo.activity.payment.PaymentHistoryActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.activity.SupportActivity;
import vn.tonish.hozo.activity.TaskAlertsActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/17/17.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private TextViewHozo appVersion;

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }


    @Override
    protected void initView() {
        findViewById(R.id.layout_profile).setOnClickListener(this);
        findViewById(R.id.layout_new_notification).setOnClickListener(this);
        findViewById(R.id.layout_payment).setOnClickListener(this);
        findViewById(R.id.layout_condition).setOnClickListener(this);
        findViewById(R.id.layout_nda).setOnClickListener(this);
        findViewById(R.id.layout_share).setOnClickListener(this);
        findViewById(R.id.layout_about).setOnClickListener(this);
        findViewById(R.id.layout_support).setOnClickListener(this);
        appVersion = (TextViewHozo) findViewById(R.id.app_version);
    }


    @Override
    protected void initData() {
        PackageInfo pInfo = null;
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String vcode = getContext().getString(R.string.app_name) + " " + (pInfo != null ? pInfo.versionName : null) + " (" + String.valueOf(pInfo != null ? pInfo.versionCode : 0) + ")" + getContext().getString(R.string.hozo_tonish);
        appVersion.setText(vcode);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.layout_profile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, UserManager.getMyUser().getId());
                intent.putExtra(Constants.IS_MY_USER, UserManager.getMyUser().isMyUser());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.layout_share:
                doShareApp();
                break;

//            case R.id.tv_history:
//                startActivity(HistoryActivity.class, TransitionScreen.RIGHT_TO_LEFT);
//               break;
//
            case R.id.layout_payment:
                startActivity(PaymentHistoryActivity.class, TransitionScreen.RIGHT_TO_LEFT);
//                Toast.makeText(getActivity(), "Hiện tại chức năng này đang trong quá trình xây dựng!!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.layout_condition:
                openGeneralInfoActivity(getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
                break;

            case R.id.layout_nda:
                openGeneralInfoActivity(getString(R.string.other_nad), "http://hozo.vn/chinh-sach-bao-mat/?ref=app");
                break;

//            case R.id.layout_info:
//                openGeneralInfoActivity(getString(R.string.other_info), "http://hozo.vn/?ref=app");
//                break;

            case R.id.layout_about:
                openGeneralInfoActivity(getString(R.string.other_about), "http://hozo.vn/gioi-thieu/?ref=app");
                break;

            case R.id.layout_new_notification:
                startActivity(TaskAlertsActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.layout_support:
                startActivity(SupportActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }

    private void doShareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String content = getString(R.string.share_app_content);
            content = content + "http://hyperurl.co/hozo" + " \n";
            i.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(i, getString(R.string.share_app_title)));
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showLongToast(getContext(), getString(R.string.share_app_error), true, false);
        }

    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(getActivity(), GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }


}

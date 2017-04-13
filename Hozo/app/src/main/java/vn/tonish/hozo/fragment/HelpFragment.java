package vn.tonish.hozo.fragment;

import android.view.View;
import android.widget.Toast;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.AboutActivity;
import vn.tonish.hozo.activity.other.ConditionActivity;
import vn.tonish.hozo.activity.other.HistoryActivity;
import vn.tonish.hozo.activity.other.InfoActivity;
import vn.tonish.hozo.activity.other.NADActivity;
import vn.tonish.hozo.activity.other.PaymentActivity;
import vn.tonish.hozo.activity.other.ProfileActivity;

/**
 * Created by Admin on 4/4/2017.
 */

public class HelpFragment extends BaseFragment implements View.OnClickListener {

    private View tvProfile, tvHistory, tvPayment, tvCondition, tvNDA, tvInfo, tvShare, tvAbout;

    private int[] arr_id = {R.id.tvProfile, R.id.tvHistory, R.id.tvPayment, R.id.tvCondition, R.id.tvNDA, R.id.tvInfo, R.id.tvShare, R.id.tvAbout};


    @Override
    protected int getLayout() {
        return R.layout.setting_fragment;
    }


    @Override
    protected void initView() {
        for (int i = 0; i < arr_id.length; i++) {
            init(arr_id[i]);
        }
    }

    public void init(int id) {
        findViewById(id).setOnClickListener(this);
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

            case R.id.tvProfile:
                startActivity(ProfileActivity.class);
                break;

            case R.id.tvHistory:
                startActivity(HistoryActivity.class);
                break;

            case R.id.tvPayment:
                startActivity(PaymentActivity.class);
                break;

            case R.id.tvCondition:
                startActivity(ConditionActivity.class);
                break;

            case R.id.tvNDA:
                startActivity(NADActivity.class);
                break;

            case R.id.tvInfo:
                startActivity(InfoActivity.class);
                break;

            case R.id.tvShare:
                // intent share
                Toast.makeText(getActivity(), "SHARE APP TO GET CREDITS", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tvAbout:
                startActivity(AboutActivity.class);
                break;
        }
    }
}

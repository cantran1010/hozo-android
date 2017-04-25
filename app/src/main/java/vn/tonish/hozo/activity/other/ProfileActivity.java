package vn.tonish.hozo.activity.other;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.FeedBackFragment;
import vn.tonish.hozo.model.FeedBack;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.DialogUtils;

/**
 * Created by huyquynh on 4/12/17.
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private Context context;


    // info for user's profile
    private ImageView img_avatar;
    private TextView tv_name;
    private TextView tv_birthday;
    private TextView tv_address;
    private TextView tv_phone;
    private TextView btnLogOut;

    // pager for tab feedback
    private ViewPager viewPager;

    // tab1 and tab 2 in viewpager
    private TextView tab_1, tab_2;

    // adapter for feedback pager
    private FeedBackPagerAdapter feedBackPagerAdapter;

    // data for feedback tab1 _ tab2 ;
    private ArrayList<FeedBack> tab1Data;
    private ArrayList<FeedBack> tab2Data;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        context = ProfileActivity.this;
        setBackButton();
        setTitleHeader(getString(R.string.profile_tv_header));
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        btnLogOut = (TextView) findViewById(R.id.btn_logout);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        viewPager = (ViewPager) findViewById(R.id.pagers);
        tab_1 = (TextView) findViewById(R.id.tab_1);
        tab_2 = (TextView) findViewById(R.id.tab_2);
        tab_2 = (TextView) findViewById(R.id.tab_2);

        tab1Data = new ArrayList<>();
        tab2Data = new ArrayList<>();

        feedBackPagerAdapter = new FeedBackPagerAdapter(getSupportFragmentManager(), tab1Data, tab2Data);
        viewPager.setAdapter(feedBackPagerAdapter);


    }

    @Override
    protected void initData() {
        btnLogOut.setOnClickListener(this);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                DialogUtils.showConfirmAndCancelAlertDialog(this, getString(R.string.msg_logOut), new DialogUtils.ConfirmDialogOkCancelListener() {
                    @Override
                    public void onOkClick() {
                        logOut();
                    }

                    @Override
                    public void onCancelClick() {

                    }

                });
                break;
        }

    }

    public void logOut() {
        NetworkUtils.getRequestVolleyFormData(true, true, true, this, NetworkConfig.API_LOGOUT, new HashMap<String, String>(), new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                try {
                    if (jsonResponse.getInt("code") == 0) {
                        UserManager.deleteAll();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (jsonResponse.getInt("code") == 1) {
                        Toast.makeText(context, " Account is not exist", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError() {

            }
        });


    }


    public class FeedBackPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<FeedBack> tab1Data;
        private ArrayList<FeedBack> tab2Data;

        public FeedBackPagerAdapter(FragmentManager fm, ArrayList<FeedBack> tab1Data, ArrayList<FeedBack> tab2Data) {
            super(fm);
            this.tab1Data = tab1Data;
            this.tab2Data = tab2Data;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FeedBackFragment.instance(tab1Data);
                case 1:
                    return FeedBackFragment.instance(tab2Data);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

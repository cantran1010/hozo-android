package vn.tonish.hozo.activity.other;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.EditProfileActivity;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.RealmDbHelper;
import vn.tonish.hozo.fragment.FeedBackFragment;
import vn.tonish.hozo.model.FeedBack;

import vn.tonish.hozo.model.User;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.FeedBackFragment;

import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by huy_quynh on 4/12/17.
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener, NetworkUtils.NetworkListener {
    private Context context;

    private ImageView img_avatar;
    private TextView tv_name;
    private TextView tv_birthday;
    private TextView tv_address;
    private TextView tv_phone;
    private TextView btnLogOut;

    // pager for tab feedback
    protected ViewPager viewPager;

    // tab1 and tab 2 in viewpager
    protected TextView tab_1, tab_2;

    // adapter for feedback pager
    protected FeedBackPagerAdapter feedBackPagerAdapter;

    // data for feedback tab1 _ tab2 ;
    protected ArrayList<FeedBack> tab1Data;
    protected ArrayList<FeedBack> tab2Data;


    // User data;
    private User user;


    private TextView btn_right;

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

        btn_right = (TextView) findViewById(R.id.btnRight);
        btn_right.setText("EDIT");
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                if (user == null) {
                    Toast.makeText(ProfileActivity.this, getString(R.string.network_error_msg), Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra(Constants.DATA, user);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        btnLogOut.setOnClickListener(this);
        int userID = UserManager.getUserLogin(context).getId();
        NetworkUtils.getRequestVolley(true, true, true, ProfileActivity.this, NetworkConfig.API_GET_PROFILE + userID, new JSONObject(), this);
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

        UserEntity userEntity = UserManager.getUserLogin(this);
        LogUtils.d("", "user logout " + userEntity.toString());

        NetworkUtils.getRequestVolleyFormData(true, true, true, this, NetworkConfig.API_LOGOUT, new HashMap<String, String>(), new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                Log.e("ABC", jsonResponse.toString());
                try {
                    if (jsonResponse.getInt(Constants.CODE) == 0) {
                        UserManager.deleteAll();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (jsonResponse.getInt("code") == 1) {
                        Toast.makeText(context, "Account is not exist", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSuccess(JSONObject jsonResponse) {
        if (jsonResponse.toString() != null) {
            try {
                int code = jsonResponse.getInt(Constants.CODE);
                String message = jsonResponse.getString(Constants.MESSAGE);
                if (code == Constants.REQUEST_SUCCESSFUL) {
                    JSONObject data = jsonResponse.getJSONObject(Constants.DATA);
                    JSONObject user = data.getJSONObject(Constants.USER);
                    this.user = new Gson().fromJson(user.toString(), User.class);
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError() {


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
package vn.tonish.hozo.activity.other;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.EditProfileActivity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.workerReviewFragment;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by Can Tran on 4/11/17.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback, imgEdit;
    private TextView btnAddVerify;
    private FrameLayout btnLogOut;
    private FrameLayout layoutContainer;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_edit:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }

    private void logOut() {
        UserEntity userEntity = UserManager.getUserLogin(this);
        LogUtils.d("", "user logout " + userEntity.toString());
    }

    @Override
    protected void initView() {

        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        btnAddVerify = (TextView) findViewById(R.id.tv_add_verify);
        btnLogOut = (FrameLayout) findViewById(R.id.btn_logout);
        layoutContainer = (FrameLayout) findViewById(R.id.layout_container);
    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnAddVerify.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        openFragment(R.id.layout_container, workerReviewFragment.class, false,true);
//        NetworkUtils.getRequestVolleyFormData(true, true, true, this, NetworkConfig.API_LOGOUT, new HashMap<String, String>(), new NetworkUtils.NetworkListener() {
//            @Override
//            public void onSuccess(JSONObject jsonResponse) {
//                Log.e("ABC", jsonResponse.toString());
//                try {
//                    if (jsonResponse.getInt(Constants.CODE) == 0) {
//                        UserManager.deleteAll();
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                    } else if (jsonResponse.getInt("code") == 1) {
//                        Toast.makeText(context, "Account is not exist", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });

    }

    @Override
    protected void resumeData() {

    }
}

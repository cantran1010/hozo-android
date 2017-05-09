package vn.tonish.hozo.activity.other;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.EditProfileActivity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.PosterReviewFragment;
import vn.tonish.hozo.fragment.workerReviewFragment;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by Can Tran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback, imgEdit;
    private TextView btnAddVerify;
    private FrameLayout btnLogOut;
    private TextView btnWorker, btnPoster;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        btnAddVerify = (TextView) findViewById(R.id.tv_add_verify);
        btnLogOut = (FrameLayout) findViewById(R.id.btn_logout);
        btnWorker = (TextView) findViewById(R.id.btn_worker);
        btnPoster = (TextView) findViewById(R.id.btn_poster);
    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnAddVerify.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnPoster.setOnClickListener(this);
        btnWorker.setOnClickListener(this);
        openFragment(R.id.layout_container, workerReviewFragment.class, false, true);

    }

    @Override
    protected void resumeData() {

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
            case R.id.btn_logout:
                logOut();
                break;
            case R.id.btn_poster:
                btnPoster.setTextColor(ContextCompat.getColor(this, R.color.white));
                setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_selected));
                btnWorker.setTextColor(ContextCompat.getColor(this, R.color.black));
                setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_press));
                openFragment(R.id.layout_container, workerReviewFragment.class, false, true);
                break;
            case R.id.btn_worker:
                btnWorker.setTextColor(ContextCompat.getColor(this, R.color.white));
                setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_selected));
                btnPoster.setTextColor(ContextCompat.getColor(this, R.color.black));
                setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_press));
                openFragment(R.id.layout_container, PosterReviewFragment.class, false, false);
                break;
        }
    }

    private void logOut() {
        UserEntity userEntity = UserManager.getUserLogin(this);
        LogUtils.d("", "user logout " + userEntity.toString());

    }
}

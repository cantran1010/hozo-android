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
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by Can Tran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback, imgEdit, avatar;
    private TextView btnAddVerify, btnWorker, btnPoster, btnMoreReview;
    private FrameLayout btnLogOut;
    private TextViewHozo tvName, tvDateOfBirth, tvAddress, tvMobile;
    private ImageView imgVerifyMobile, imgVerifyFacebook, imgverifyMail, imgVerifyCard, imgVerifyBank;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        avatar = (ImageView) findViewById(R.id.img_avatar);
        btnAddVerify = (TextView) findViewById(R.id.tv_add_verify);
        btnLogOut = (FrameLayout) findViewById(R.id.btn_logout);
        btnWorker = (TextViewHozo) findViewById(R.id.btn_worker);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvDateOfBirth = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvMobile = (TextViewHozo) findViewById(R.id.tv_phone);
        imgVerifyMobile = (ImageView) findViewById(R.id.img_verify_mobile);
        imgVerifyFacebook = (ImageView) findViewById(R.id.img_verify_facebook);
        imgverifyMail = (ImageView) findViewById(R.id.img_verify_email);
        imgVerifyCard = (ImageView) findViewById(R.id.img_verify_card);
        imgVerifyBank = (ImageView) findViewById(R.id.img_verify_bank);
    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnAddVerify.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnPoster.setOnClickListener(this);
        btnWorker.setOnClickListener(this);

        //set cache data
        setUserInfoFromCache();

        //get data from server
        updateUserInfoFromServer();

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

    private void updateUserInfoFromServer() {


    }

    private void setUserInfoFromCache() {
        UserEntity userEntity = new UserEntity();
        userEntity = UserManager.getUserLogin(this);
        Utils.displayImageAvatar(this, avatar, userEntity.getAvatar());
        tvName.setText(userEntity.getFullName());
        tvDateOfBirth.setText(userEntity.getDateOfBirth());
        tvMobile.setText(userEntity.getPhoneNumber());
        tvAddress.setText(userEntity.getAddress());
        setVerify(userEntity.getVerified(), imgVerifyMobile, imgVerifyFacebook, imgverifyMail, imgVerifyCard, imgVerifyBank);
    }

    private void setVerify(int verify, ImageView imgPhone, ImageView imgFacebook, ImageView imgMail, ImageView imgCard, ImageView imgBank) {
        if ((verify & 1) == 0) {
            imgPhone.setAlpha(0.2f);
        }
        if ((verify & 2) == 0) {
            imgFacebook.setAlpha(0.2f);
        }
        if ((verify & 4) == 0) {
            imgMail.setAlpha(0.2f);
        }
        if ((verify & 8) == 0) {
            imgCard.setAlpha(0.2f);
        }

        if ((verify & 16) == 0) {
            imgBank.setAlpha(0.2f);
        }

    }

}


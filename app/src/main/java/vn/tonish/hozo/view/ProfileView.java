package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by tonish1 on 5/9/17.
 */

public class ProfileView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDateOfBirth, tvAddress, tvMobile, tvGender, btnAddVerify;
    private ImageView imgVerifyMobile, imgVerifyFacebook, imgverifyMail, imgVerifyCard, imgVerifyBank;
    private LinearLayout layoutInfor;

    public ProfileView(Context context) {
        super(context);
        initView();
    }

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.profile_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        layoutInfor = (LinearLayout) findViewById(R.id.layout_infor);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        tvDateOfBirth = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvMobile = (TextViewHozo) findViewById(R.id.tv_phone);
        tvGender = (TextViewHozo) findViewById(R.id.tv_gender);
        imgVerifyMobile = (ImageView) findViewById(R.id.img_verify_mobile);
        imgVerifyFacebook = (ImageView) findViewById(R.id.img_verify_facebook);
        imgverifyMail = (ImageView) findViewById(R.id.img_verify_email);
        imgVerifyCard = (ImageView) findViewById(R.id.img_verify_card);
        imgVerifyBank = (ImageView) findViewById(R.id.img_verify_bank);
        btnAddVerify = (TextViewHozo) findViewById(R.id.tv_add_verify);
    }

    public void updateData(boolean isAccount, String urlAvatar, String fullName, String birthday, String gender, String address, String mobile, int verified) {
        Utils.displayImageAvatar(getContext(), imgAvatar, urlAvatar);
        tvName.setText(fullName);
        tvDateOfBirth.setText(birthday);
        setVerify(verified, imgVerifyMobile, imgVerifyFacebook, imgverifyMail, imgVerifyCard, imgVerifyBank);
        if (isAccount) {
            layoutInfor.setVisibility(View.VISIBLE);
            tvAddress.setText(address);
            tvGender.setText(gender);
            tvMobile.setText(mobile);
            btnAddVerify.setVisibility(View.VISIBLE);
            btnAddVerify.setEnabled(true);
        } else {
            layoutInfor.setVisibility(View.GONE);
            btnAddVerify.setVisibility(View.GONE);
            btnAddVerify.setEnabled(false);
        }


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

    @Override
    public void onClick(View v) {
    }
}


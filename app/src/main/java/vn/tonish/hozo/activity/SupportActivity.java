package vn.tonish.hozo.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.support.SupportMailFragment;
import vn.tonish.hozo.fragment.support.SupportPhoneFragment;
import vn.tonish.hozo.fragment.support.SupportRateFragment;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 11/1/17.
 */

public class SupportActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SupportActivity.class.getSimpleName();
    private TextViewHozo tvPhone, tvMail, tvRate;
    private ImageView imgPhone, imgMail, imgRate;
    private int tab = 1;

    @Override
    protected int getLayout() {
        return R.layout.support_activity;
    }

    @Override
    protected void initView() {
        LinearLayout phoneLayout = (LinearLayout) findViewById(R.id.support_phone_layout);
        LinearLayout mailLayout = (LinearLayout) findViewById(R.id.support_mail_layout);
        LinearLayout rateLayout = (LinearLayout) findViewById(R.id.support_rate_layout);

        phoneLayout.setOnClickListener(this);
        mailLayout.setOnClickListener(this);
        rateLayout.setOnClickListener(this);

        tvPhone = (TextViewHozo) findViewById(R.id.tv_support_phone);
        tvMail = (TextViewHozo) findViewById(R.id.tv_support_mail);
        tvRate = (TextViewHozo) findViewById(R.id.tv_support_rate);

        imgPhone = (ImageView) findViewById(R.id.img_support_phone);
        imgMail = (ImageView) findViewById(R.id.img_support_mail);
        imgRate = (ImageView) findViewById(R.id.img_support_rate);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, SupportPhoneFragment.class, new Bundle(), false, TransitionScreen.FADE_IN);
    }

    @Override
    protected void resumeData() {

    }

    private void updateTabUi(int position) {
        Utils.setViewBackground(imgPhone, ContextCompat.getDrawable(this, R.drawable.circle_support_off));
        Utils.setViewBackground(imgMail, ContextCompat.getDrawable(this, R.drawable.circle_support_off));
        Utils.setViewBackground(imgRate, ContextCompat.getDrawable(this, R.drawable.circle_support_off));

        tvPhone.setTextColor(ContextCompat.getColor(this, R.color.tv_gray_new));
        tvMail.setTextColor(ContextCompat.getColor(this, R.color.tv_gray_new));
        tvRate.setTextColor(ContextCompat.getColor(this, R.color.tv_gray_new));

        switch (position) {

            case 1:
                Utils.setViewBackground(imgPhone, ContextCompat.getDrawable(this, R.drawable.circle_support_on));
                tvPhone.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;

            case 2:
                Utils.setViewBackground(imgMail, ContextCompat.getDrawable(this, R.drawable.circle_support_on));
                tvMail.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;

            case 3:
                Utils.setViewBackground(imgRate, ContextCompat.getDrawable(this, R.drawable.circle_support_on));
                tvRate.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.support_phone_layout:

                if (tab == 1) break;

                if (tab > 1)
                    openFragment(R.id.layout_container, SupportPhoneFragment.class, new Bundle(), false, TransitionScreen.LEFT_TO_RIGHT);
                else
                    openFragment(R.id.layout_container, SupportPhoneFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);

                updateTabUi(1);
                tab = 1;
                break;

            case R.id.support_mail_layout:

                if (tab == 2) break;

                if (tab > 2)
                    openFragment(R.id.layout_container, SupportMailFragment.class, new Bundle(), false, TransitionScreen.LEFT_TO_RIGHT);
                else
                    openFragment(R.id.layout_container, SupportMailFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);

                updateTabUi(2);
                tab = 2;
                break;

            case R.id.support_rate_layout:
                if (tab == 3) break;

                if (tab > 3)
                    openFragment(R.id.layout_container, SupportRateFragment.class, new Bundle(), false, TransitionScreen.LEFT_TO_RIGHT);
                else
                    openFragment(R.id.layout_container, SupportRateFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);

                updateTabUi(3);
                tab = 3;
                break;

            case R.id.img_back:
                finish();
                break;

        }
    }
}

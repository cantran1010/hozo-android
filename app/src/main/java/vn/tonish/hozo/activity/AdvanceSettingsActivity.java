package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.PriceSettingActivity;
import vn.tonish.hozo.activity.setting.TaskTypeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgBack;
    private TextViewHozo tvWorkType, tvPrice, tvLocation, tvTime, tvGender, tvAge;
    private SwitchCompat swNotification;
    private ButtonHozo btnReset;


    @Override
    protected int getLayout() {
        return R.layout.activity_advance_settings;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvLocation = (TextViewHozo) findViewById(R.id.tv_location);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvGender = (TextViewHozo) findViewById(R.id.tv_gender);
        tvAge = (TextViewHozo) findViewById(R.id.tv_age);
        swNotification = (SwitchCompat) findViewById(R.id.sw_notification);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);


        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tab_type).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tv_location).setOnClickListener(this);
        findViewById(R.id.tv_time).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tab_age).setOnClickListener(this);
        findViewById(R.id.tab_notification).setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_type:
//                startActivityForResult(new Intent(this, TaskTypeActivity.class), Constants.REQUEST_CODE_TASK_TYPE, TransitionScreen.LEFT_TO_RIGHT);
                startActivity(new Intent(this, TaskTypeActivity.class), TransitionScreen.LEFT_TO_RIGHT);
                break;
            case R.id.tab_price:
              startActivityForResult(new Intent(this, PriceSettingActivity.class), Constants.REQUEST_CODE_SETTING_PRICE_, TransitionScreen.LEFT_TO_RIGHT);
                break;
            case R.id.tv_location:

                break;
            case R.id.tv_time:
                break;

            case R.id.tab_gender:
                break;

            case R.id.tab_age:
                break;

            case R.id.tab_notification:
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


        }
    }
}

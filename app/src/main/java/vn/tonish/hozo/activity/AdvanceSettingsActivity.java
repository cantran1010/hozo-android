package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.PriceSettingActivity;
import vn.tonish.hozo.activity.setting.TaskTypeActivity;
import vn.tonish.hozo.activity.setting.TimeSettingActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.AddvanceSetting;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = AdvanceSettingsActivity.class.getSimpleName();
    private ImageView imgBack;
    private TextViewHozo tvWorkType, tvPrice, tvLocation, tvTime, tvGender, tvAge;
    private SwitchCompat swNotification;
    private ButtonHozo btnReset;
    private AddvanceSetting addvanceSetting;


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
        addvanceSetting = new AddvanceSetting();


    }

    @Override
    protected void initData() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tab_type).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tab_location).setOnClickListener(this);
        findViewById(R.id.tab_time).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tab_age).setOnClickListener(this);
        findViewById(R.id.tab_notification).setOnClickListener(this);


    }

    @Override
    protected void resumeData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_type:
                startActivityForResult(new Intent(this, TaskTypeActivity.class), Constants.REQUEST_CODE_TASK_TYPE, TransitionScreen.DOWN_TO_UP);
                break;
            case R.id.tab_price:
                startActivityForResult(new Intent(this, PriceSettingActivity.class), Constants.REQUEST_CODE_SETTING_PRICE, TransitionScreen.DOWN_TO_UP);
                break;
            case R.id.tab_location:

                break;
            case R.id.tab_time:
                startActivityForResult(new Intent(this, TimeSettingActivity.class), Constants.REQUEST_CODE_SETTING_PRICE, TransitionScreen.LEFT_TO_RIGHT);
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
        addvanceSetting.setUserId(UserManager.getUserLogin().getId());
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_TASK_TYPE && data != null) {
            LogUtils.d(TAG, "onActivityResult show Data " + data.getExtras().getIntegerArrayList(Constants.EXTRA_CATEGORY_ID));
            addvanceSetting.setCategoryIds(data.getExtras().getIntegerArrayList(Constants.EXTRA_CATEGORY_ID));
            LogUtils.d(TAG, "onActivityResult show categoryId " + addvanceSetting.toString());

        }
        if (requestCode == Constants.REQUEST_CODE_SETTING_PRICE && data != null) {
            String minPrice = data.getStringExtra(Constants.EXTRA_MIN_PRICE);
            String maxPrice = data.getStringExtra(Constants.EXTRA_MAX_PRICE);
            addvanceSetting.setMinWorkerRate(minPrice);
            addvanceSetting.setMaxWorkerRate(maxPrice);
            LogUtils.d(TAG, "onActivityResult show price " + addvanceSetting.toString());
        }
    }
}

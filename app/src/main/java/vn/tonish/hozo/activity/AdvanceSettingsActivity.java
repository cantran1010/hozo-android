package vn.tonish.hozo.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.AgeSettingActivity;
import vn.tonish.hozo.activity.setting.FeeActivity;
import vn.tonish.hozo.activity.setting.TimeSettingActivity;

/**
 * Created by huyquynh on 4/18/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_save;
    private TextView tv_fee;


    @Override
    protected int getLayout() {
        return R.layout.activity_advance_settings;
    }

    @Override
    protected void initView() {
        setBackButton();
        setTitleHeader(getString(R.string.advance_setting_title));
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdvanceSettingsActivity.this, "Just Demo!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tv_fee = (TextView) findViewById(R.id.tv_fee_text);
        String titleFee = getString(R.string.tv_fee);
        String currency = getString(R.string.vnd);
        Spanned text = Html.fromHtml(titleFee + "<i>" + currency + "</i>");
        tv_fee.setText(text);

        findViewById(R.id.tv_type).setOnClickListener(this);
        findViewById(R.id.tv_fee).setOnClickListener(this);
        findViewById(R.id.tv_location).setOnClickListener(this);
        findViewById(R.id.tv_time).setOnClickListener(this);
        findViewById(R.id.tv_sex).setOnClickListener(this);
        findViewById(R.id.tv_age).setOnClickListener(this);
        findViewById(R.id.tv_notification).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    private final int OPEN_TIME_SETTING = 111;

    private final int OPEN_AGE_SETTING = 222;

    private final int OPEN_FEE_SETTING = 322;

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tv_type:

                break;
            case R.id.tv_fee:
                startActivityForResult(new Intent(getApplicationContext(), FeeActivity.class), OPEN_FEE_SETTING);
                break;
            case R.id.tv_location:

                break;
            case R.id.tv_time:
                startActivityForResult(new Intent(getApplicationContext(), TimeSettingActivity.class), OPEN_TIME_SETTING);
                break;

            case R.id.tv_sex:
                break;

            case R.id.tv_age:
                startActivityForResult(new Intent(getApplicationContext(), AgeSettingActivity.class), OPEN_AGE_SETTING);
                break;

            case R.id.tv_notification:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_TIME_SETTING:
                break;
        }
    }
}

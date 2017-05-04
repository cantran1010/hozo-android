package vn.tonish.hozo.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import vn.tonish.hozo.view.TextViewHozo;
import android.widget.Toast;

import vn.tonish.hozo.R;

/**
 * Created by huyquynh on 4/18/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayout() {
        return R.layout.activity_advance_settings;
    }

    @Override
    protected void initView() {
        setBackButtonHozo();
        setTitleHeader(getString(R.string.advance_setting_title));

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdvanceSettingsActivity.this, "Just Demo!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        TextViewHozo tv_fee = (TextViewHozo) findViewById(R.id.tv_fee_text);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tv_type:

                break;
            case R.id.tv_fee:
                break;
            case R.id.tv_location:

                break;
            case R.id.tv_time:
                break;

            case R.id.tv_sex:
                break;

            case R.id.tv_age:
                break;

            case R.id.tv_notification:
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

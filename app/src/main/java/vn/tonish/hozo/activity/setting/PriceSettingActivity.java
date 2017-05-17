package vn.tonish.hozo.activity.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by CanTran on 5/16/17.
 */


public class PriceSettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PriceSettingActivity.class.getSimpleName();
    private ImageView imgBack;
    private EdittextHozo edtMinPrice, edtMaxPrice;
    private ButtonHozo btnReset, btnSave;

    @Override
    protected int getLayout() {
        return R.layout.activity_price_setting;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        edtMinPrice = (EdittextHozo) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EdittextHozo) findViewById(R.id.edt_max_price);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        edtMinPrice.addTextChangedListener(new NumberTextWatcher(edtMinPrice));
        edtMaxPrice.addTextChangedListener(new NumberTextWatcher(edtMaxPrice));

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
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void save() {
        if (edtMinPrice.getText().toString().trim().isEmpty()) {
            edtMinPrice.setError(getString(R.string.erro_emply_price));
        } else if (edtMaxPrice.getText().toString().trim().isEmpty()) {
            edtMaxPrice.setError(getString(R.string.erro_emply_price));
        } else {
            long minPrice = Long.valueOf(edtMinPrice.getText().toString().replace(".", ""));
            long maxPrice = Long.valueOf(edtMaxPrice.getText().toString().replace(".", ""));
            if (maxPrice < minPrice) {
                edtMaxPrice.setError(getString(R.string.erro_price));
            } else {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_MIN_PRICE, minPrice + "");
                intent.putExtra(Constants.EXTRA_MAX_PRICE, maxPrice + "");
                setResult(Constants.REQUEST_CODE_SETTING_PRICE, intent);
                finish();

            }
        }


    }

    private void reset() {
        edtMinPrice.setText("");
        edtMaxPrice.setText("");

    }
}

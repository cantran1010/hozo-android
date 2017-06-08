package vn.tonish.hozo.activity.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 28/05/2017.
 */

public class CostActivity extends BaseActivity implements View.OnClickListener {
    private EdittextHozo edtMinPrice, edtMaxPrice;
    private int minCost;
    private int maxCost;

    @Override
    protected int getLayout() {
        return R.layout.activity_cost;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        TextViewHozo tvReset = (TextViewHozo) findViewById(R.id.tv_reset);
        edtMinPrice = (EdittextHozo) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EdittextHozo) findViewById(R.id.edt_max_price);
        ButtonHozo btnSave = (ButtonHozo) findViewById(R.id.btn_done);

        imgBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        minCost = intent.getExtras().getInt(Constants.EXTRA_MIN_PRICE);
        maxCost = intent.getExtras().getInt(Constants.EXTRA_MAX_PRICE);
        edtMinPrice.addTextChangedListener(new NumberTextWatcher(edtMinPrice));
        edtMaxPrice.addTextChangedListener(new NumberTextWatcher(edtMaxPrice));
        setDataForView();
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                save();
                break;
            case R.id.tv_reset:
                reset();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void reset() {
        minCost = 10000;
        maxCost = 100000000;
        setDataForView();

    }

    private void setDataForView() {
        if (minCost == 0)
            edtMinPrice.setText(String.valueOf((int) SettingManager.getSettingEntiny().getMinWorkerRate()));
        else
            edtMinPrice.setText(String.valueOf(minCost));
        if (maxCost == 0)
            edtMaxPrice.setText(String.valueOf((int) SettingManager.getSettingEntiny().getMaxWorkerRate()));
        else
            edtMaxPrice.setText(String.valueOf(maxCost));

    }

    private void save() {
        String strMin = edtMinPrice.getText().toString().trim();
        String strMax = edtMaxPrice.getText().toString().trim();
        if (strMin.isEmpty()) {
            edtMinPrice.setError(getString(R.string.erro_emply_price));
        } else if (strMax.isEmpty()) {
            edtMaxPrice.setError(getString(R.string.erro_emply_price));
        } else {
            int minPrice = Integer.parseInt(edtMinPrice.getText().toString().replace(".", ""));
            int maxPrice = Integer.valueOf(edtMaxPrice.getText().toString().replace(".", ""));
            if (minPrice < 10000) {
                edtMinPrice.setError(getString(R.string.erro_emply_price));
            } else if (maxPrice > 100000000)
                edtMaxPrice.setError(getString(R.string.erro_emply_price));
            else if (maxPrice < minPrice)
                Utils.showLongToast(this, getString(R.string.erro_price), true, false);
            else {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_MIN_PRICE, minPrice);
                intent.putExtra(Constants.EXTRA_MAX_PRICE, maxPrice);
                setResult(Constants.RESULT_CODE_COST, intent);
                finish();
            }
        }
    }

}

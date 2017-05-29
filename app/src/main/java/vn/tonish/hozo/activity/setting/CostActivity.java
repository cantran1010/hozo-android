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
 * Created by Can Tran on 28/05/2017.
 */

public class CostActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgBack;
    private EdittextHozo edtMinPrice, edtMaxPrice;
    private TextViewHozo tvReset;
    private ButtonHozo btnSave;
    private int minCost;
    private int maxCost;

    @Override
    protected int getLayout() {
        return R.layout.activity_cost;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvReset = (TextViewHozo) findViewById(R.id.tv_reset);
        edtMinPrice = (EdittextHozo) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EdittextHozo) findViewById(R.id.edt_max_price);
        btnSave = (ButtonHozo) findViewById(R.id.btn_done);

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
        if (minCost == 0)
            edtMinPrice.setText((int) SettingManager.getSettingEntiny().getMinWorkerRate()+"");
        else
            edtMinPrice.setText(minCost+"");
        if (maxCost == 0)
            edtMaxPrice.setText((int) SettingManager.getSettingEntiny().getMaxWorkerRate()+"");
        else
            edtMaxPrice.setText(maxCost+"");


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
        edtMinPrice.setText(minCost);
        edtMaxPrice.setText(maxCost);

    }

    private void save() {
        String strMin = edtMinPrice.getText().toString().trim();
        String strMax = edtMaxPrice.getText().toString().trim();
        long minPrice = Long.valueOf(edtMinPrice.getText().toString().replace(".", ""));
        long maxPrice = Long.valueOf(edtMaxPrice.getText().toString().replace(".", ""));
        if (strMin.isEmpty() || minPrice < 10000) {
            edtMinPrice.setError(getString(R.string.erro_emply_price));
        } else if (strMax.isEmpty() || maxPrice > 100000000) {
            edtMaxPrice.setError(getString(R.string.erro_emply_price));
        } else if (edtMaxPrice.getText().toString().trim().isEmpty()) {
            edtMaxPrice.setError(getString(R.string.erro_emply_price));
        } else {
            if (maxPrice < minPrice) {
                Utils.showLongToast(this, getString(R.string.erro_price));
                return;
            } else {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_MIN_PRICE, minPrice);
                intent.putExtra(Constants.EXTRA_MAX_PRICE, maxPrice);
                setResult(Constants.RESULT_CODE_COST, intent);
                finish();
            }
        }
    }

}

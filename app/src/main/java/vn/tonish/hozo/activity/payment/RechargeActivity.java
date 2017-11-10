package vn.tonish.hozo.activity.payment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;
    private ButtonHozo btnRecharge;
    private Spinner spType;
    private EdittextHozo edtMoney;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnRecharge = (ButtonHozo) findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(this);

        spType = (Spinner) findViewById(R.id.sp_type);

        edtMoney = (EdittextHozo) findViewById(R.id.edt_money);
    }

    @Override
    protected void initData() {
        edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private String getLongEdittext(EdittextHozo edittextHozo) {
        return edittextHozo.getText().toString().replace(",", "").replace(".", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.btn_recharge:

                switch (spType.getSelectedItemPosition()){

                    case 0:
                        startActivity(PromotionCodeActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                    case 1:

                        break;

                }

                break;

        }
    }

}

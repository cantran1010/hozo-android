package vn.tonish.hozo.activity.setting;

import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;

/**
 * Created by MAC2015 on 4/22/17.
 */

public class FeeActivity extends BaseActivity {


    EdittextHozo et_max, et_min;

    // ButtonHozo in the right of header
    TextViewHozo btn_right;

    @Override
    protected int getLayout() {
        return R.layout.activity_fee;
    }


    @Override
    protected void initView() {
        setBackButtonHozo();
        et_max = (EdittextHozo) findViewById(R.id.et_max);
        et_min = (EdittextHozo) findViewById(R.id.et_min);

        btn_right = (TextViewHozo) findViewById(R.id.btnRight);
        btn_right.setText(getString(R.string.btn_reset));

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void resumeData() {

    }
}

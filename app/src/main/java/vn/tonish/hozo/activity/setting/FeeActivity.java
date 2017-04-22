package vn.tonish.hozo.activity.setting;

import android.widget.EditText;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;

/**
 * Created by MAC2015 on 4/22/17.
 */

public class FeeActivity extends BaseActivity {


    EditText et_max, et_min;

    // button in the right of header
    TextView btn_right;

    @Override
    protected int getLayout() {
        return R.layout.activity_fee;
    }


    @Override
    protected void initView() {
        setBackButton();
        et_max = (EditText) findViewById(R.id.et_max);
        et_min = (EditText) findViewById(R.id.et_min);

        btn_right = (TextView) findViewById(R.id.btnRight);
        btn_right.setText(getString(R.string.btn_reset));

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void resumeData() {

    }
}

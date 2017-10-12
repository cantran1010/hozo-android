package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;
    private ButtonHozo btnRecharge;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnRecharge = findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.btn_recharge:
                Toast.makeText(RechargeActivity.this, "Hiện tại chức năng này đang trong quá trình xây dựng!!!", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}

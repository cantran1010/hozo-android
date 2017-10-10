package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
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

        }
    }

}

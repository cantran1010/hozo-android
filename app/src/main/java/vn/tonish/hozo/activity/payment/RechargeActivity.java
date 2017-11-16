package vn.tonish.hozo.activity.payment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;
    private LinearLayout promotionLayout, atmLayout;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        promotionLayout = (LinearLayout) findViewById(R.id.promotion_layout);
        promotionLayout.setOnClickListener(this);

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

            case R.id.promotion_layout:
                startActivity(PromotionCodeActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;


        }
    }

}

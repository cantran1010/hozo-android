package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 10/30/17.
 */

public class PromotionCodeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PromotionCodeActivity.class.getSimpleName();
    private ImageView imgBack;
    private ButtonHozo btnPromotion;
    private LinearLayout progressLayout;

    @Override
    protected int getLayout() {
        return R.layout.promotion_code_activity;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnPromotion = findViewById(R.id.btn_promotion);
        btnPromotion.setOnClickListener(this);

        progressLayout = findViewById(R.id.progress_layout);
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

            case R.id.progress_layout:

                break;

        }
    }
}

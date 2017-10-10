package vn.tonish.hozo.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 10/10/17.
 */

public class PaymentHistoryActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PaymentHistoryActivity.class.getSimpleName();
    private ImageView imgBack;
    private TextViewHozo tvMore;
    private ButtonHozo btnPayment;
    private RecyclerView rcvPayment;

    @Override
    protected int getLayout() {
        return R.layout.payment_history_activity;
    }

    @Override
    protected void initView() {
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        tvMore = findViewById(R.id.tv_more);
        tvMore.setOnClickListener(this);

        btnPayment = findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(this);

        rcvPayment = findViewById(R.id.rcv_payment_history);
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

            case R.id.tv_more:

                break;

            case R.id.btn_payment:
                startActivity(RechargeActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}

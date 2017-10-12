package vn.tonish.hozo.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PaymentAdapter;
import vn.tonish.hozo.model.Payment;
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
    private TextViewHozo tvMyWallet, tvCountHistory;
    private PaymentAdapter paymentAdapter;
    private ArrayList<Payment> payments = new ArrayList<>();

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

        tvMyWallet = findViewById(R.id.tv_my_wallet);
        tvCountHistory = findViewById(R.id.tv_count_history);

        rcvPayment = findViewById(R.id.rcv_payment_history);
    }

    @Override
    protected void initData() {
        tvMyWallet.setText(getString(R.string.my_wallet, "0"));
        tvCountHistory.setText(getString(R.string.history_title, "0"));

        refreshList();
    }

    @Override
    protected void resumeData() {

    }

    private void refreshList() {

//        for (int i = 0; i < 10; i++) {
//            Payment payment = new Payment(i, true, 2000, "09:20 20/09/2017", "Nạp tiền vào tài khoản hozo");
//            payments.add(payment);
//        }
//
//        paymentAdapter = new PaymentAdapter(payments);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvPayment.setLayoutManager(linearLayoutManager);
//        rcvPayment.setAdapter(paymentAdapter);

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

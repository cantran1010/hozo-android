package vn.tonish.hozo.activity.payment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.PaymentAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TransactionResponse;
import vn.tonish.hozo.rest.responseRes.WalletResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
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
    private ArrayList<TransactionResponse> payments = new ArrayList<>();
    private String since;

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

        setUpList();

        getWalletInfo();
        getTransactions();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpList() {
        paymentAdapter = new PaymentAdapter(payments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPayment.setLayoutManager(linearLayoutManager);
        rcvPayment.setAdapter(paymentAdapter);
    }

    private void getTransactions() {
        final Map<String, String> params = new HashMap<>();

        params.put("limit", "20");
        if (since != null) params.put("since", since);

        ApiClient.getApiService().getTransactionsHistory(UserManager.getUserToken(), params).enqueue(new Callback<List<TransactionResponse>>() {
            @Override
            public void onResponse(Call<List<TransactionResponse>> call, Response<List<TransactionResponse>> response) {
                LogUtils.d(TAG, "getTransactions code : " + response.code());
                LogUtils.d(TAG, "getTransactions response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    payments.addAll(response.body());
                    paymentAdapter.notifyDataSetChanged();
                } else {
                    DialogUtils.showRetryDialog(PaymentHistoryActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTransactions();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TransactionResponse>> call, Throwable t) {
                DialogUtils.showRetryDialog(PaymentHistoryActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getTransactions();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    private void getWalletInfo() {
        ApiClient.getApiService().getWalletInfo(UserManager.getUserToken()).enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                LogUtils.d(TAG, "getWalletInfo code : " + response.code());
                LogUtils.d(TAG, "getWalletInfo response : " + response.body());


                if (response.code() == Constants.HTTP_CODE_OK) {
                    WalletResponse walletResponse = response.body();
                    tvMyWallet.setText(getString(R.string.my_wallet, Utils.formatNumber(walletResponse.getBalance())));
                } else {
                    DialogUtils.showRetryDialog(PaymentHistoryActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getWalletInfo();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                LogUtils.e(TAG, "getWalletInfo onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(PaymentHistoryActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getWalletInfo();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
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

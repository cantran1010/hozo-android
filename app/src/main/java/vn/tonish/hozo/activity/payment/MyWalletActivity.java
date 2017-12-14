package vn.tonish.hozo.activity.payment;

import android.content.Intent;
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
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
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

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MyWalletActivity.class.getSimpleName();
    private TextViewHozo tvMore;
    private RecyclerView rcvPayment;
    private TextViewHozo tvMyWallet, tvCountHistory, tvNoHistory;
    private PaymentAdapter paymentAdapter;
    private ArrayList<TransactionResponse> payments = new ArrayList<>();
    private String since;

    @Override
    protected int getLayout() {
        return R.layout.my_wallet_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        tvMore = (TextViewHozo) findViewById(R.id.tv_more);
        tvMore.setOnClickListener(this);

        ButtonHozo btnPayment = (ButtonHozo) findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(this);

        tvMyWallet = (TextViewHozo) findViewById(R.id.tv_my_wallet);
        tvCountHistory = (TextViewHozo) findViewById(R.id.tv_count_history);

        rcvPayment = (RecyclerView) findViewById(R.id.rcv_payment_history);
        tvNoHistory = (TextViewHozo) findViewById(R.id.tv_no_history);
    }

    @Override
    protected void initData() {
        setUpList();
    }

    @Override
    protected void resumeData() {
        payments.clear();
        getWalletInfo();
        getTransactions();
    }

    private void setUpList() {
        paymentAdapter = new PaymentAdapter(this, payments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPayment.setLayoutManager(linearLayoutManager);
        rcvPayment.setAdapter(paymentAdapter);

        paymentAdapter.setOnItemClickLister(new PaymentAdapter.OnItemClickLister() {
            @Override
            public void onClick(int position) {

                if (payments.get(position).getTaskId() != 0) {
                    Intent intent = new Intent(MyWalletActivity.this, DetailTaskActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, payments.get(position).getTaskId());
                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                }

            }
        });

    }

    private void getTransactions() {
        final Map<String, String> params = new HashMap<>();

        params.put("limit", "10");
        if (since != null) params.put("since", since);

        ApiClient.getApiService().getTransactionsHistory(UserManager.getUserToken(), params).enqueue(new Callback<List<TransactionResponse>>() {
            @Override
            public void onResponse(Call<List<TransactionResponse>> call, Response<List<TransactionResponse>> response) {
                LogUtils.d(TAG, "getTransactions code : " + response.code());
                LogUtils.d(TAG, "getTransactions response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    payments.addAll(response.body());
                    paymentAdapter.notifyDataSetChanged();
                    paymentAdapter.stopLoadMore();

                    if (payments.size() == 0)
                        tvNoHistory.setVisibility(View.VISIBLE);
                    else
                        tvNoHistory.setVisibility(View.GONE);

                    if (payments.size() < 10)
                        tvMore.setVisibility(View.GONE);
                    else
                        tvMore.setVisibility(View.VISIBLE);

                } else {
                    DialogUtils.showRetryDialog(MyWalletActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(MyWalletActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

                    tvMyWallet.setText(Utils.formatNumber(walletResponse.getBalance()));
                    tvCountHistory.setText(getString(R.string.history_payment_count, walletResponse.getTotalTransactions()));

                } else {
                    DialogUtils.showRetryDialog(MyWalletActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(MyWalletActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                startActivity(PaymentHistoryActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_payment:
                Intent intent = new Intent(MyWalletActivity.this, RechargeActivity.class);
                startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}

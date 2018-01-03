package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
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
    //    private RecyclerView rcvPayment;
    private TextViewHozo tvMyWalletAccount, tvMyWalletMoney, tvCountHistoryAccount, tvHistoryMoney, tvNoHistory;
    //    private PaymentAdapter paymentAdapter;
    private ArrayList<TransactionResponse> payments = new ArrayList<>();
    private String since;
    private WalletResponse walletResponse;
    private ImageView imgWalletAccount, imgWalletMoney;

    @Override
    protected int getLayout() {
        return R.layout.my_wallet_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

//        tvMore = (TextViewHozo) findViewById(R.id.tv_more);
//        tvMore.setOnClickListener(this);

        ButtonHozo btnPayment = (ButtonHozo) findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(this);

        ButtonHozo btnExchange = (ButtonHozo) findViewById(R.id.btn_exchange);
        btnExchange.setOnClickListener(this);

        ButtonHozo btnHistoryAccount = (ButtonHozo) findViewById(R.id.btn_history_account);
        btnHistoryAccount.setOnClickListener(this);

        ButtonHozo btnHistoryMoney = (ButtonHozo) findViewById(R.id.btn_history_money);
        btnHistoryMoney.setOnClickListener(this);

        ButtonHozo btnWithdraw = (ButtonHozo) findViewById(R.id.btn_withdraw_money);
        btnWithdraw.setOnClickListener(this);

        tvMyWalletAccount = (TextViewHozo) findViewById(R.id.tv_my_wallet_account);
        tvMyWalletMoney = (TextViewHozo) findViewById(R.id.tv_my_wallet_money);
//        tvCountHistory = (TextViewHozo) findViewById(R.id.tv_count_history);

//        rcvPayment = (RecyclerView) findViewById(R.id.rcv_payment_history);
//        tvNoHistory = (TextViewHozo) findViewById(R.id.tv_no_history);

        imgWalletAccount = (ImageView) findViewById(R.id.img_wallet_account_info);
        imgWalletAccount.setOnClickListener(this);

        imgWalletMoney = (ImageView) findViewById(R.id.img_wallet_account_money);
        imgWalletMoney.setOnClickListener(this);

    }

    @Override
    protected void initData() {
//        setUpList();
    }

    @Override
    protected void resumeData() {
        payments.clear();
        getWalletInfo();
//        getTransactions();
    }

//    private void setUpList() {
//        paymentAdapter = new PaymentAdapter(this, payments);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvPayment.setLayoutManager(linearLayoutManager);
//        rcvPayment.setAdapter(paymentAdapter);
//
//        paymentAdapter.setOnItemClickLister(new PaymentAdapter.OnItemClickLister() {
//            @Override
//            public void onClick(int position) {
//
//                if (payments.get(position).getTaskId() != 0) {
//                    Intent intent = new Intent(MyWalletActivity.this, DetailTaskActivity.class);
//                    intent.putExtra(Constants.TASK_ID_EXTRA, payments.get(position).getTaskId());
//                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
//                }
//
//            }
//        });
//
//    }

    private void getWalletInfo() {
        ApiClient.getApiService().getWalletInfo(UserManager.getUserToken()).enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                LogUtils.d(TAG, "getWalletInfo code : " + response.code());
                LogUtils.d(TAG, "getWalletInfo response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    walletResponse = response.body();
                    tvMyWalletAccount.setText(Utils.formatNumber(walletResponse.getBalanceAccount()));
                    tvMyWalletMoney.setText(Utils.formatNumber(walletResponse.getBalanceCash()));
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

            case R.id.btn_history_account:
                Intent intentHistoryAccount = new Intent(MyWalletActivity.this, PaymentHistoryActivity.class);
                intentHistoryAccount.putExtra(Constants.WALLET_TYPE_EXTRA, 1);
                intentHistoryAccount.putExtra(Constants.WALLET_COUNT_HISTORY_EXTRA, walletResponse.getTotalAccountTransactions());
                startActivity(intentHistoryAccount, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_history_money:
                Intent intentHistoryMoney = new Intent(MyWalletActivity.this, PaymentHistoryActivity.class);
                intentHistoryMoney.putExtra(Constants.WALLET_TYPE_EXTRA, 2);
                intentHistoryMoney.putExtra(Constants.WALLET_COUNT_HISTORY_EXTRA, walletResponse.getTotalCashTransactions());
                startActivity(intentHistoryMoney, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_payment:
                Intent intent = new Intent(MyWalletActivity.this, RechargeActivity.class);
                startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_exchange:
                startActivity(ExchangeActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_withdraw_money:
                startActivity(WithdrawalActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_wallet_account_info:
                Intent intent1 = new Intent(MyWalletActivity.this, WalletInfoActivity.class);
                intent1.putExtra(Constants.WALLET_TYPE_EXTRA, 1);
                startActivity(intent1, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_wallet_account_money:
                Intent intent2 = new Intent(MyWalletActivity.this, WalletInfoActivity.class);
                intent2.putExtra(Constants.WALLET_TYPE_EXTRA, 2);
                startActivity(intent2, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}

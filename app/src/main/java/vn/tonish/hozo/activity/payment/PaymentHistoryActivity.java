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
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 11/16/17.
 */

public class PaymentHistoryActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PaymentHistoryActivity.class.getSimpleName();
    private RecyclerView rcvPayment;
    private PaymentAdapter paymentAdapter;
    private ArrayList<TransactionResponse> payments = new ArrayList<>();
    private boolean isLoadingMoreFromServer = true;
    private String since;
    private static final int LIMIT = 20;
    private String type = "";
    private TextViewHozo tvCount;

    @Override
    protected int getLayout() {
        return R.layout.payment_history_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        tvCount = (TextViewHozo) findViewById(R.id.tv_count);

        rcvPayment = (RecyclerView) findViewById(R.id.rcv_payment_history);
    }

    @Override
    protected void initData() {
        if (getIntent().getIntExtra(Constants.WALLET_TYPE_EXTRA, 1) == 1) {
            type = "account";
        } else if (getIntent().getIntExtra(Constants.WALLET_TYPE_EXTRA, 1) == 2) {
            type = "cash";
        }

        tvCount.setText(getString(R.string.count_history, getIntent().getIntExtra(Constants.WALLET_COUNT_HISTORY_EXTRA, 0)));
    }

    @Override
    protected void resumeData() {
        setUpList();
    }

    private void setUpList() {
        paymentAdapter = new PaymentAdapter(this, payments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPayment.setLayoutManager(linearLayoutManager);
        rcvPayment.setAdapter(paymentAdapter);

        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
                if (isLoadingMoreFromServer) getTransactions();
            }
        };
        rcvPayment.addOnScrollListener(endlessRecyclerViewScrollListener);

        paymentAdapter.setOnItemClickLister(new PaymentAdapter.OnItemClickLister() {
            @Override
            public void onClick(int position) {

                if (payments.get(position).getTaskId() != 0) {
                    Intent intent = new Intent(PaymentHistoryActivity.this, DetailTaskActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, payments.get(position).getTaskId());
                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                }

            }
        });

    }

    private void getTransactions() {
        final Map<String, String> params = new HashMap<>();

        params.put("wallet", type);
        params.put("limit", LIMIT + "");
        if (since != null) params.put("since", since);

        LogUtils.d(TAG, "getTransactions params : " + params);

        ApiClient.getApiService().getTransactionsHistory(UserManager.getUserToken(), params).enqueue(new Callback<List<TransactionResponse>>() {
            @Override
            public void onResponse(Call<List<TransactionResponse>> call, Response<List<TransactionResponse>> response) {
                LogUtils.d(TAG, "getTransactions code : " + response.code());
                LogUtils.d(TAG, "getTransactions response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    payments.addAll(response.body());

                    if (response.body().size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        paymentAdapter.stopLoadMore();
                    }
                    paymentAdapter.notifyDataSetChanged();

                    if (response.body().size() > 0)
                        since = response.body().get(response.body().size() - 1).getCreatedAt();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

        }
    }
}

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
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;

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

    @Override
    protected int getLayout() {
        return R.layout.payment_history_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        rcvPayment = (RecyclerView) findViewById(R.id.rcv_payment_history);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {
        setUpList();
    }

    private void setUpList() {
        paymentAdapter = new PaymentAdapter(this,payments);
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

    }

    private void getTransactions() {
        final Map<String, String> params = new HashMap<>();

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

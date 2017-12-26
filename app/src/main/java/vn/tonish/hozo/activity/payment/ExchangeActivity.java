package vn.tonish.hozo.activity.payment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.ExchangeAdapter;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.AssignerExchangeResponse;
import vn.tonish.hozo.rest.responseRes.TaskExchangeResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 12/25/17.
 */

public class ExchangeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ExchangeActivity.class.getSimpleName();
    private Spinner spTask;
    private RecyclerView rcvAssigner;
    private ArrayList<TaskExchangeResponse> taskExchangeResponses = new ArrayList<>();
    private ArrayList<AssignerExchangeResponse> assignerExchangeResponses = new ArrayList<>();
    private ExchangeAdapter exchangeAdapter;
    private ButtonHozo btnExchange;

    @Override
    protected int getLayout() {
        return R.layout.exchange_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        spTask = (Spinner) findViewById(R.id.sp_task);
        rcvAssigner = (RecyclerView) findViewById(R.id.rcv_assigner);

        btnExchange = (ButtonHozo) findViewById(R.id.btn_exchange);
        btnExchange.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        setUpExchangeList();
        getTransferableTask();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpExchangeList() {
        exchangeAdapter = new ExchangeAdapter(assignerExchangeResponses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssigner.setLayoutManager(linearLayoutManager);
        rcvAssigner.setAdapter(exchangeAdapter);
    }

    private void getTransferableTask() {
        ApiClient.getApiService().getTransferableTask(UserManager.getUserToken()).enqueue(new Callback<List<TaskExchangeResponse>>() {
            @Override
            public void onResponse(Call<List<TaskExchangeResponse>> call, Response<List<TaskExchangeResponse>> response) {
                LogUtils.d(TAG, "getTransferableTask code : " + response.code());
                LogUtils.d(TAG, "getTransferableTask response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskExchangeResponses.addAll(response.body());

                    List<String> list = new ArrayList<String>();
                    for (TaskExchangeResponse taskExchangeResponse : response.body()) {
                        list.add(taskExchangeResponse.getTitle());
                    }

                    HozoSpinnerAdapter dataAdapter = new HozoSpinnerAdapter(ExchangeActivity.this, list);
                    spTask.setAdapter(dataAdapter);

                    spTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, final int posititon, long id) {
                            assignerExchangeResponses.clear();
                            assignerExchangeResponses.addAll(taskExchangeResponses.get(posititon).getAssignees());
                            exchangeAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } else {
                    DialogUtils.showRetryDialog(ExchangeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTransferableTask();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TaskExchangeResponse>> call, Throwable t) {
                DialogUtils.showRetryDialog(ExchangeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getTransferableTask();
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

            case R.id.btn_exchange:
                LogUtils.d(TAG, "assignerExchangeResponses : " + assignerExchangeResponses.toString());
                break;

        }
    }
}

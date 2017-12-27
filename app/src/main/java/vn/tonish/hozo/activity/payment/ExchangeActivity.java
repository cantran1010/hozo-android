package vn.tonish.hozo.activity.payment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.ExchangeAdapter;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.AssignerExchangeResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskExchangeResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

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
    private TextViewHozo tvNoData;
    private LinearLayout mainLayout;

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

        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
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

                    if (response.body().size() == 0) {
                        mainLayout.setVisibility(View.GONE);
                        btnExchange.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        mainLayout.setVisibility(View.VISIBLE);
                        btnExchange.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    }

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

    private void doExchange() {
        boolean isExchange = false;

        for (int i = 0; i < assignerExchangeResponses.size(); i++) {
            if (assignerExchangeResponses.get(i).isChecked()) {
                isExchange = true;
                break;
            }
        }

        if (!isExchange) {
            Utils.showLongToast(this, getString(R.string.non_user_exchange), true, false);
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < assignerExchangeResponses.size(); i++) {

            if (assignerExchangeResponses.get(i).isChecked()) {
                AssignerExchangeResponse assignerExchangeResponse = assignerExchangeResponses.get(i);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", assignerExchangeResponse.getId());
                    jsonObject.put("amount", assignerExchangeResponse.getPrice());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        LogUtils.d(TAG, "doExchange data jsonArray : " + jsonArray.toString());
        LogUtils.d(TAG, "doExchange taskId : " + taskExchangeResponses.get(spTask.getSelectedItemPosition()).getId());

        ProgressDialogUtils.showProgressDialog(this);

//        JSONObject jsonRequest = null;
//        try {
//            jsonRequest = new JSONObject(jsonArray.toString());
//            LogUtils.d(TAG, "doExchange data request : " + jsonRequest.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        try {
//            jsonRequest.put("provider",jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonArray.toString());

        ApiClient.getApiService().transfer(UserManager.getUserToken(), taskExchangeResponses.get(spTask.getSelectedItemPosition()).getId(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                LogUtils.d(TAG, "doExchange onResponse : " + response.body());
                LogUtils.d(TAG, "doExchange code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(ExchangeActivity.this, getString(R.string.transfer_success), false, false);
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "doExchange error : " + error.toString());

                    if (error.status().equals("no_task")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_no_task), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("no_permission")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_no_permission), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("transfer_not_allowed")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_transfer_not_allowed), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("np_user")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_np_user), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("transfer_duplicated")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_transfer_duplicated), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("not_enough_balance")) {
                        DialogUtils.showOkDialog(ExchangeActivity.this, getString(R.string.error), getString(R.string.transfer_error_not_enough_balance), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }

                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LogUtils.e(TAG, "doExchange onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(ExchangeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doExchange();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
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
                doExchange();
                LogUtils.d(TAG, "assignerExchangeResponses : " + assignerExchangeResponses.toString());
                break;

        }
    }
}

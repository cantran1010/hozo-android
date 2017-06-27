package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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
import vn.tonish.hozo.adapter.TaskAlertsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.TaskAlert;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by CanTran on 6/21/17.
 */

public class TaskAlertsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = TaskAlertsActivity.class.getSimpleName();
    private ArrayList<TaskAlert> taskAlerts;
    private TaskAlertsAdapter taskAlertsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_task_alerts;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        ButtonHozo btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        imgBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_alert);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAlerts = new ArrayList<>();
        taskAlertsAdapter = new TaskAlertsAdapter(taskAlerts);
        mRecyclerView.setAdapter(taskAlertsAdapter);
    }

    @Override
    protected void initData() {
        if (!(CategoryManager.getAllCategories() == null || CategoryManager.getAllCategories().size() == 0))
            for (CategoryEntity categoryEntity : CategoryManager.getAllCategories()
                    ) {
                TaskAlert taskAlert = new TaskAlert();
                taskAlert.setId(categoryEntity.getId());
                taskAlert.setName(categoryEntity.getName());
                taskAlert.setSelected(false);
                taskAlerts.add(taskAlert);
                getSettingAlertId();
            }


    }

    @Override
    protected void resumeData() {

    }


    private void getSettingAlertId() {
        ProgressDialogUtils.showHozoProgressDialog(this);
        Call<List<Integer>> call = ApiClient.getApiService().getSettingAlert(UserManager.getUserToken());
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                LogUtils.d(TAG, "getSettingAlertId code : " + response.code());
                LogUtils.d(TAG, "getSettingAlertId body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (response.body().size() > 0) {
                        for (Integer in : response.body()
                                ) {
                            for (TaskAlert alert : taskAlerts
                                    ) {
                                if (alert.getId() == in) {
                                    alert.setSelected(true);

                                }
                            }

                        }
                    }
                    taskAlertsAdapter.notifyDataSetChanged();

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskAlertsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getSettingAlertId();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskAlertsActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(TaskAlertsActivity.this, error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "getSettingAlertId , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(TaskAlertsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getSettingAlertId();
                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        });
    }

    private void postSettingAlertId() {
        ProgressDialogUtils.showHozoProgressDialog(this);
        LogUtils.d(TAG, "postSettingAlertId data request : " + getIdSetting());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), getIdSetting());
        Call<Void> call = ApiClient.getApiService().postSettingAlert(UserManager.getUserToken(), body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "postSettingAlertId code : " + response.code());
                LogUtils.d(TAG, "postSettingAlertId body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskAlertsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            postSettingAlertId();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskAlertsActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(TaskAlertsActivity.this, error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "getSettingAlertId , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(TaskAlertsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        postSettingAlertId();
                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        });
    }

    private String getIdSetting() {
        final JSONObject jsonRequest = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (TaskAlert taskAlert : taskAlerts
                ) {
            if (taskAlert.isSelected()) {
                jsonArray.put(taskAlert.getId());

            }
            try {
                jsonRequest.put("category_ids", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonRequest.toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
            case R.id.btn_save:
                postSettingAlertId();
        }
    }


}

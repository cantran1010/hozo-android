package vn.tonish.hozo.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskFinishActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PostATaskFinishActivity.class.getSimpleName();
    protected ButtonHozo btnDone;
    private EdittextHozo edtBudget, edtNumberWorker;
    private TextViewHozo tvTotal;
    private TaskResponse work;
    private ImageView imgBack;
    private Category category;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_finish_activity;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        edtBudget = (EdittextHozo) findViewById(R.id.edt_budget);
        edtNumberWorker = (EdittextHozo) findViewById(R.id.edt_number_worker);
        tvTotal = (TextViewHozo) findViewById(R.id.tv_total);

        btnDone = (ButtonHozo) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        work = (TaskResponse) getIntent().getSerializableExtra(Constants.EXTRA_TASK);
        category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);
        edtBudget.addTextChangedListener(new NumberTextWatcher(edtBudget));
//        edtNumberWorker.addTextChangedListener(new NumberTextWatcher(edtNumberWorker));

        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPayment();
            }
        });

        edtNumberWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPayment();
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private void updateTotalPayment() {

        try {
            if (edtBudget.getText().toString().equals("") || edtNumberWorker.getText().toString().equals("")) {
                tvTotal.setText("");
            } else {
                Long bBudget = Long.valueOf(edtBudget.getText().toString().replace(".", ""));
                Long bNumberWorker = Long.valueOf(edtNumberWorker.getText().toString().replace(".", ""));
                Long total = bBudget * bNumberWorker;
                tvTotal.setText(Utils.formatNumber(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            if (edtBudget.getText().toString().equals("") || edtNumberWorker.getText().toString().equals("")) {
//                tvTotal.setText("");
//            } else {
//                BigInteger bBudget = new BigInteger(edtBudget.getText().toString());
//                BigInteger bNumberWorker = new BigInteger(edtNumberWorker.getText().toString());
//                BigInteger total = bBudget.multiply(bNumberWorker);
//                tvTotal.setText(total + "");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.btn_done:
                doDone();
                break;

        }
    }

    private void doDone() {

        if (edtBudget.getText().toString().equals("0") || edtBudget.getText().toString().equals("")) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.post_a_task_budget_error));
            return;
        } else if (edtNumberWorker.getText().toString().equals("0") || edtNumberWorker.getText().toString().equals("")) {
            edtNumberWorker.requestFocus();
            edtNumberWorker.setError(getString(R.string.post_a_task_number_worker_error));
            return;
        }

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("category_id", category.getId());
            jsonRequest.put("title", work.getTitle());
            jsonRequest.put("description", work.getDescription());
//            jsonRequest.put("start_time", DateTimeUtils.getTimeIso8601(work.getDate(), work.getStartTime()));
//            jsonRequest.put("end_time", DateTimeUtils.getTimeIso8601(work.getDate(), work.getEndTime()));
            jsonRequest.put("start_time", work.getStartTime());
            jsonRequest.put("end_time", work.getEndTime());

            if (work.getGender() != null)
                jsonRequest.put("gender", work.getGender());
            jsonRequest.put("min_age", work.getMinAge());
            jsonRequest.put("max_age", work.getMaxAge());
            jsonRequest.put("latitude", work.getLatitude());
            jsonRequest.put("longitude", work.getLongitude());
            jsonRequest.put("city", work.getCity());
            jsonRequest.put("district", work.getDistrict());
            jsonRequest.put("address", work.getAddress());
            jsonRequest.put("worker_rate", Integer.valueOf(edtBudget.getText().toString().replace(".", "")));
            jsonRequest.put("worker_count", Integer.valueOf(edtNumberWorker.getText().toString().replace(".", "")));

            if (work.getAttachmentsId() != null && work.getAttachmentsId().length > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < work.getAttachmentsId().length; i++)
                    jsonArray.put(work.getAttachmentsId()[i]);
                jsonRequest.put("attachments", jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "createNewTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().createNewTask(UserManager.getUserToken(), body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                LogUtils.d(TAG, "createNewTask code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
//                    DialogUtils.showOkDialog(PostATaskFinishActivity.this, getString(R.string.post_a_task_complete_title), getString(R.string.post_a_task_complete), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//                            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
//                            finish();
//                        }
//                    });
                    Utils.showLongToast(PostATaskFinishActivity.this, getString(R.string.post_a_task_complete));
                    setResult(Constants.POST_A_TASK_RESPONSE_CODE);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(PostATaskFinishActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDone();
                        }
                    });
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    DialogUtils.showOkDialog(PostATaskFinishActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "createNewTask onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(PostATaskFinishActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doDone();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }
}

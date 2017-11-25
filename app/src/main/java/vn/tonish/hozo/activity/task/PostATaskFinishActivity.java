package vn.tonish.hozo.activity.task;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.CustomArrayAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.formatNumber;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskFinishActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PostATaskFinishActivity.class.getSimpleName();
    private EdittextHozo edtNumberWorker;
    private AutoCompleteTextView edtBudget;
    private TextViewHozo tvTotal;
    private TaskResponse taskResponse;
    private String budgetBefore;
    private static final int MAX_BUGDET = 10000000;
    private static final int MIN_BUGDET = 10000;
    private ArrayList<String> vnds = new ArrayList<>();
    private CustomArrayAdapter adapter;
    private static final int MAX_WORKER = 50;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_a_task_finish;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        edtBudget = (AutoCompleteTextView) findViewById(R.id.edt_budget);
        edtNumberWorker = (EdittextHozo) findViewById(R.id.edt_number_worker);
        tvTotal = (TextViewHozo) findViewById(R.id.tv_total);
        ButtonHozo btnDone = (ButtonHozo) findViewById(R.id.btn_done);
        edtBudget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
            }

        });
        btnDone.setOnClickListener(this);
        adapter = new CustomArrayAdapter(this, vnds);
        edtBudget.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        edtBudget.setThreshold(0);
    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.EXTRA_TASK);
        edtBudget.addTextChangedListener(new NumberTextWatcher(edtBudget));

        if (taskResponse.getWorkerCount() != 0 && taskResponse.getWorkerRate() != 0) {
            edtNumberWorker.setText(String.valueOf(taskResponse.getWorkerCount()));
            edtBudget.setText(String.valueOf(taskResponse.getWorkerRate()));
            updateTotalPayment();
        }

        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter = null;
                vnds.clear();
                if (!edtBudget.getText().toString().equals("") && Long.valueOf(getLongAutoCompleteTextView(edtBudget)) <= MAX_BUGDET)
                    edtBudget.setError(null);

                budgetBefore = edtBudget.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    edtBudget.setThreshold(edtBudget.getText().length());
                    formatMoney(Long.parseLong(edtBudget.getText().toString().trim().replace(",", "").replace(".", "")));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtBudget.getText().toString().equals(""))
                    if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
                        edtBudget.setText(budgetBefore);
                        edtBudget.setError(getString(R.string.max_budget_error));
                        edtBudget.setSelection(edtBudget.getText().toString().length());
                    } else {
                        updateTotalPayment();
                    }
            }
        });

        edtNumberWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!edtNumberWorker.getText().toString().equals("") && Integer.valueOf(edtNumberWorker.getText().toString()) <= MAX_WORKER)
                    edtNumberWorker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtNumberWorker.getText().toString().equals("")) {
                    if (Integer.valueOf(edtNumberWorker.getText().toString()) > MAX_WORKER) {
                        edtNumberWorker.setText(edtNumberWorker.getText().toString().substring(0, edtNumberWorker.getText().toString().length() - 1));
                        edtNumberWorker.setError(getString(R.string.max_number_worker_error, MAX_WORKER));
                        edtNumberWorker.setSelection(edtNumberWorker.getText().toString().length());
                    } else {
                        edtNumberWorker.setError(null);
                        updateTotalPayment();
                    }
                }
            }
        });
    }

    private String getLongEdittext(EdittextHozo edittextHozo) {
        return edittextHozo.getText().toString().replace(",", "").replace(".", "");
    }

    private String getLongAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "");
    }

    @Override
    protected void resumeData() {

    }

    private void updateTotalPayment() {

        try {
            if (edtBudget.getText().toString().equals("") || edtNumberWorker.getText().toString().equals("")) {
                tvTotal.setText("");
            } else {
                Long bBudget = Long.valueOf(getLongAutoCompleteTextView(edtBudget));
                Long bNumberWorker = Long.valueOf(getLongEdittext(edtNumberWorker));
                Long total = bBudget * bNumberWorker;
                tvTotal.setText(formatNumber(total));
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

    private void formatMoney(long mn) {
        if (mn * 10 >= MIN_BUGDET && mn * 10 <= MAX_BUGDET && mn * 10 % 1000 == 0)
            vnds.add(String.valueOf(formatNumber(mn * 10)));
        if (mn * 100 >= MIN_BUGDET && mn * 100 <= MAX_BUGDET && mn * 100 % 1000 == 0)
            vnds.add(String.valueOf(formatNumber(mn * 100)));
        if (mn * 1000 >= MIN_BUGDET && mn * 1000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 1000)));
        if (mn * 10000 >= MIN_BUGDET && mn * 10000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 10000)));
        if (mn * 100000 >= MIN_BUGDET && mn * 100000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 100000)));
        if (mn * 1000000 >= MIN_BUGDET && mn * 1000000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 1000000)));
        if (mn * 10000000 >= MIN_BUGDET && mn * 10000000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 10000000)));

        adapter = new CustomArrayAdapter(this, vnds);
        edtBudget.setAdapter(adapter);

//        adapter.getFilter().filter(formatNumber(Integer.parseInt(textView.getText().toString().trim())), null);
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
        } else if (Integer.valueOf(edtNumberWorker.getText().toString()) > MAX_WORKER) {
            edtNumberWorker.setError(getString(R.string.max_number_worker_error, MAX_WORKER));
            return;
        } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) < MIN_BUGDET) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.min_budget_error));
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("category_id", taskResponse.getCategoryId());
            jsonRequest.put("title", taskResponse.getTitle());
            jsonRequest.put("description", taskResponse.getDescription());
            jsonRequest.put("start_time", taskResponse.getStartTime());
            jsonRequest.put("end_time", taskResponse.getEndTime());

            if (taskResponse.getGender() != null)
                jsonRequest.put("gender", taskResponse.getGender());
            jsonRequest.put("min_age", taskResponse.getMinAge());
            jsonRequest.put("max_age", taskResponse.getMaxAge());
            jsonRequest.put("latitude", taskResponse.getLatitude());
            jsonRequest.put("longitude", taskResponse.getLongitude());
            jsonRequest.put("city", taskResponse.getCity());
            jsonRequest.put("district", taskResponse.getDistrict());
            jsonRequest.put("address", taskResponse.getAddress());
            jsonRequest.put("worker_rate", Integer.valueOf(getLongAutoCompleteTextView(edtBudget)));
            jsonRequest.put("worker_count", Integer.valueOf(getLongEdittext(edtNumberWorker)));

            if (taskResponse.getAttachmentsId() != null && taskResponse.getAttachmentsId().length > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < taskResponse.getAttachmentsId().length; i++)
                    jsonArray.put(taskResponse.getAttachmentsId()[i]);
                jsonRequest.put("attachments", jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "createNewTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "request body create task : " + body.toString());

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
                    Utils.showLongToast(PostATaskFinishActivity.this, getString(R.string.post_a_task_complete), false, false);
                    setResult(Constants.POST_A_TASK_RESPONSE_CODE);
                    finish();
                    FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(PostATaskFinishActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDone();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(PostATaskFinishActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    DialogUtils.showOkDialog(PostATaskFinishActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });

                }
                ProgressDialogUtils.dismissProgressDialog();
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
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }
}
